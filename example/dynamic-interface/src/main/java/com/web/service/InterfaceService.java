package com.web.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.web.convert.MasterDataMapper;
import com.web.entity.InterfaceEntity;
import com.web.entity.InterfaceParamEntity;
import com.web.mapper.InterfaceMapper;
import com.web.mapper.InterfaceParamMapper;
import com.web.mapper.ModelTableMapper;
import com.web.vo.InterfaceParamVO;
import com.web.vo.InterfaceVO;
import com.web.vo.ModelTableColumnVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
public class InterfaceService {

    @Autowired
    private List<IMyBatisXmlService> myBatisXmlServices;

    @Autowired
    private InterfaceMapper interfaceMapper;

    @Autowired
    private InterfaceParamMapper interfaceParamMapper;

    @Autowired
    private ModelTableMapper modelTableMapper;

    @Autowired
    private ModelService modelService;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    public static Map<String, InterfaceEntity> interfaceEntityMap = Maps.newConcurrentMap();

    @Transactional(rollbackFor = Exception.class)
    public void create(InterfaceVO createInterfaceVO) {
        IMyBatisXmlService myBatisFileService = myBatisXmlServices.stream()
                .filter(iMyBatisFileService -> iMyBatisFileService.method()
                        .equals(createInterfaceVO.getMethod()))
                .findFirst().get();

        // 接口主信息
        String xml = myBatisFileService.xml(createInterfaceVO);
        InterfaceEntity interfaceEntity = MasterDataMapper.INSTANCES.interfaceVO2Entity(createInterfaceVO);
        Long id = SnowflakeIdWorker.getId();
        interfaceEntity.setId(id);
        interfaceEntity.setXml(xml);
        interfaceMapper.insert(interfaceEntity);

        // 缓存
        interfaceEntityMap.put(createInterfaceVO.getUrl(), interfaceEntity);

        // body
        ModelTableColumnVO body = createInterfaceVO.getBody();
        if (body != null) {
            InterfaceParamEntity interfaceParamBody = new InterfaceParamEntity();
            interfaceParamBody.setId(SnowflakeIdWorker.getId());
            interfaceParamBody.setType("body");
            interfaceParamBody.setModelId(modelTableMapper.selectById(body.getTableId()).getModelId());
            interfaceParamBody.setTableId(body.getTableId());
            interfaceParamMapper.insert(interfaceParamBody);
            this.childrenInterfaceParam(id, "body", body.getChildren());
        }

        // header
        List<InterfaceParamVO> header = createInterfaceVO.getHeader();
        Optional.ofNullable(header).orElse(Collections.emptyList()).stream().forEach(headerParam -> {
            InterfaceParamEntity interfaceParamHeader = new InterfaceParamEntity();
            interfaceParamHeader.setId(SnowflakeIdWorker.getId());
            interfaceParamHeader.setInterfaceId(id);
            interfaceParamHeader.setType("header");
            interfaceParamHeader.setName(headerParam.getName());
            interfaceParamHeader.setType(headerParam.getType());
            interfaceParamMapper.insert(interfaceParamHeader);
        });

        // params
        List<InterfaceParamVO> params = createInterfaceVO.getParams();
        Optional.ofNullable(params).orElse(Collections.emptyList()).stream().forEach(paramVO -> {
            InterfaceParamEntity interfaceParamHeader = new InterfaceParamEntity();
            interfaceParamHeader.setId(SnowflakeIdWorker.getId());
            interfaceParamHeader.setInterfaceId(id);
            interfaceParamHeader.setType("param");
            interfaceParamHeader.setName(paramVO.getName());
            interfaceParamHeader.setType(paramVO.getType());
            interfaceParamMapper.insert(interfaceParamHeader);
        });

        // result
        ModelTableColumnVO result = createInterfaceVO.getResult();
        if (result != null) {
            InterfaceParamEntity interfaceParamResult = new InterfaceParamEntity();
            interfaceParamResult.setId(SnowflakeIdWorker.getId());
            interfaceParamResult.setType("result");
            interfaceParamResult.setModelId(modelTableMapper.selectById(result.getTableId()).getModelId());
            interfaceParamResult.setTableId(result.getTableId());
            interfaceParamResult.setInterfaceId(id);
            interfaceParamMapper.insert(interfaceParamResult);
            this.childrenInterfaceParam(id, "result", result.getChildren());
        }
    }

    private void childrenInterfaceParam(Long interfaceId, String type, List<ModelTableColumnVO> children) {
        for (ModelTableColumnVO tableColumnVO: children) {
            if ("Object".equals(tableColumnVO.getType())) {
                InterfaceParamEntity interfaceParam = new InterfaceParamEntity();
                interfaceParam.setId(SnowflakeIdWorker.getId());
                interfaceParam.setType(type);
                interfaceParam.setModelId(modelTableMapper.selectById(tableColumnVO.getTableId()).getModelId());
                interfaceParam.setTableId(tableColumnVO.getTableId());
                interfaceParam.setInterfaceId(interfaceId);
                interfaceParamMapper.insert(interfaceParam);
                this.childrenInterfaceParam(interfaceId, type, tableColumnVO.getChildren());
            }
        }
    }

    public InterfaceVO getById(Long id) {
        InterfaceEntity interfaceEntity = interfaceMapper.selectById(id);
        InterfaceVO interfaceVO = MasterDataMapper.INSTANCES.interfaceEntity2VO(interfaceEntity);
        Map<String, List<InterfaceParamEntity>> maps = this.findInterfaceParamByInterfaceId(id);

        interfaceVO.setParams(MasterDataMapper.INSTANCES.interfaceParamEntitys2VOs(maps.get("param")));
        interfaceVO.setParams(MasterDataMapper.INSTANCES.interfaceParamEntitys2VOs(maps.get("header")));
        // result
        List<InterfaceParamEntity> interfaceParamEntityResult = maps.get("result");
        List<Long> resultTableIds = Optional.ofNullable(interfaceParamEntityResult)
                .orElse(Collections.emptyList()).stream()
                .map(InterfaceParamEntity::getTableId).collect(Collectors.toList());
        interfaceVO.setResult(modelService.createTree(resultTableIds, null));

        // body
        List<InterfaceParamEntity> interfaceParamEntityBody = maps.get("body");
        List<Long> bodyTableIds = Optional.ofNullable(interfaceParamEntityBody)
                .orElse(Collections.emptyList()).stream()
                .map(InterfaceParamEntity::getTableId).collect(Collectors.toList());
        interfaceVO.setBody(modelService.createTree(bodyTableIds, null));

        return interfaceVO;
    }

    public Object invoke(HttpServletRequest request) throws Exception {
        // 测试一个
        InterfaceEntity interfaceEntity = interfaceEntityMap.get(request.getRequestURI());
        InputStream inputStream = new ByteArrayInputStream(interfaceEntity.getXml().getBytes());
        // 不能使用原有的config对象加载, 否则下次就不会重复加载导致传入的SQL语句不能切换
        // 也可以在这里指定数据源, 从对应的数据源做查询动作
        Configuration baseConfig = sqlSessionFactory.getConfiguration();
        Configuration configuration = new Configuration(baseConfig.getEnvironment());
        String resource = "resource";
        ErrorContext.instance().resource(resource);
        XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration,resource ,configuration.getSqlFragments());
        mapperParser.parse();
        SqlSession sqlSessionXML = new DefaultSqlSessionFactory(configuration).openSession();
        // 加载我们生成的Mapper类
        MyBatisFileClassLoader myBatisFileClassLoader = new MyBatisFileClassLoader();
        Class<?> clazz = myBatisFileClassLoader.defineClass("com.BasicMapper");
        // 将生成的类对象加载到configuration中
        configuration.addMapper(clazz);
        Method query = clazz.getMethod("select", Map.class);
        // 这里就是通过类对象从configuration中获取对应的Mapper
        Object testMapper = sqlSessionXML.getMapper(clazz);

        Map<String,Object> param = Maps.newHashMap();
        List<InterfaceParamEntity> interfaceParamEntities = this.findInterfaceParamByInterfaceId(interfaceEntity.getId()).get("param");
        Optional.ofNullable(interfaceParamEntities)
                .orElse(Collections.emptyList())
                .stream()
                .forEach(interfaceParamEntity -> {
                    String paramValue = request.getParameter(interfaceParamEntity.getName());
                    param.put(interfaceParamEntity.getName(), paramValue);
                });
        Object result = query.invoke(testMapper, param);
        log.info("执行结果: " + new Gson().toJson(result));

        return result;
    }

    private Map<String, List<InterfaceParamEntity>> findInterfaceParamByInterfaceId(Long interfaceId) {
        Wrapper wrapper = Wrappers.<InterfaceParamEntity>query().lambda().eq(InterfaceParamEntity::getInterfaceId, interfaceId);
        List<InterfaceParamEntity> interfaceParamEntities = interfaceParamMapper.selectList(wrapper);
        Map<String, List<InterfaceParamEntity>> maps = Optional.ofNullable(interfaceParamEntities)
                .orElse(Collections.emptyList())
                .stream()
                .collect(Collectors.groupingBy(InterfaceParamEntity::getType));

        return maps;
    }

    public void initInterface() {
        Wrapper wrapper = Wrappers.<InterfaceEntity>query().lambda();
        List<InterfaceEntity> interfaceEntities = interfaceMapper.selectList(wrapper);
        Optional.ofNullable(interfaceEntities).orElse(Collections.emptyList())
                .stream().forEach(interfaceEntity -> {
                    interfaceEntityMap.put(interfaceEntity.getUrl(), interfaceEntity);
                });

    }
}
