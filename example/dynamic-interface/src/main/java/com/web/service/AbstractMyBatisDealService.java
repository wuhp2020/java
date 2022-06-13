package com.web.service;

import com.google.gson.Gson;
import com.web.vo.InterfaceResVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @ Author : wuheping
 * @ Date   : 2022/6/1
 * @ Desc   : 描述
 */
@Service
@Slf4j
public abstract class AbstractMyBatisDealService {

    @Autowired
    public InterfaceService interfaceService;

    @Autowired
    public SqlSessionFactory sqlSessionFactory;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private TransactionDefinition transactionDefinition;

    @Autowired
    public Gson gson;

    public abstract String method();
    public abstract String xml(InterfaceResVO resVO);
    public abstract Object invoke(HttpServletRequest request) throws Exception;

    protected Object commonInvoke(String xml, Map<String,Object> paramMap, String invokeMethod) throws Exception {
        TransactionStatus transactionStatus = platformTransactionManager.getTransaction(transactionDefinition);
        try {
            InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
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
            Method method = clazz.getMethod(invokeMethod, Map.class);
            // 这里就是通过类对象从configuration中获取对应的Mapper
            Object testMapper = sqlSessionXML.getMapper(clazz);

            Object result = method.invoke(testMapper, paramMap);
            log.info("执行结果: " + gson.toJson(result));
            platformTransactionManager.commit(transactionStatus);
            return result;
        } catch (Exception e) {
            platformTransactionManager.rollback(transactionStatus);
            log.error("保存异常: {}", e);
            throw new RuntimeException("保存异常");
        }
    }

}
