package generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Set;

@Slf4j
public class MyBatisPlusGenerator {

    // 生成代码路径
    // 作者
    // 数据库地址
    // 数据库用户
    // 数据库密码
    private static String dbUrl = "jdbc:mysql://192.168.221.129:3306/a";
    private static String dbUsername = "root";
    private static String dbPassword = "123";
    private static String author = "wuheping";
    private static String outputDir = "/Users/wuheping/Desktop/aa/src/main/java";


    public static void main(String[] args) {
        // 1.配置生成包的路径
        PackageConfig pc = new PackageConfig();
        pc.setParent("com");
        pc.setEntity("api.entity");
        pc.setMapper("provider.mapper");
        pc.setService("provider.service");
        pc.setServiceImpl("provider.service.impl");
        pc.setController("provider.controller");
        pc.setXml("provider.mapper");
        String voPackage = "api.vo";

        // 构建一个代码生成器对象
        AutoGenerator mpg = new AutoGenerator();
        // 2.全局配置
        GlobalConfig gc = new GlobalConfig();
        // 设置代码生成路径
        gc.setOutputDir(outputDir);
        // 生成的类的注释中作者信息
        gc.setAuthor(author);
        // 生成后是否打开文件夹
        gc.setOpen(false);
        // 是否覆盖
        gc.setFileOverride(true);
        // 生成service类的后缀
        gc.setServiceName("%sService");
        // 主键生成策略 和数据库id生成策略一致
        gc.setIdType(IdType.INPUT);
        // 设置日期类型
        gc.setDateType(DateType.ONLY_DATE);
        // 是否生成Swagger
        gc.setSwagger2(true);
        // 生成entity类的后缀
        gc.setEntityName("%sEntity");
        mpg.setGlobalConfig(gc);

        // 3.设置数据源
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(dbUrl);
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername(dbUsername);
        dsc.setPassword(dbPassword);
        dsc.setDbType(DbType.MYSQL);
        mpg.setDataSource(dsc);

        // 加载包信息
        mpg.setPackageInfo(pc);

        // 4.策略配置
        StrategyConfig strategy = new StrategyConfig();
        // 设置要映射的表名 不配置默认处理全部表
        // strategy.setInclude("sys_record_flow_node");
        // 表名中下划线转驼峰命名
        strategy.setNaming(NamingStrategy.underline_to_camel);
        // 表中字段如果有下划线，转驼峰命名
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);//自动生成Lombok
        strategy.setRestControllerStyle(true);//开启 RestFul 风格
        strategy.setControllerMappingHyphenStyle(true);
        // 对表中的字段 设置逻辑删除 生成的dao层代码会添加@TableLogic
        // strategy.setLogicDeleteFieldName("delete_flag");

        // 5.自动填充 (表中如果有创建时间、修改时间话, 可以使用自动填充)
        TableFill createTime = new TableFill("created_date", FieldFill.INSERT);
        TableFill updateTime = new TableFill("modified_date", FieldFill.INSERT_UPDATE);
        ArrayList<TableFill> tableFills = new ArrayList<>();
        tableFills.add(createTime);
        tableFills.add(updateTime);
        strategy.setTableFillList(tableFills);
        // 乐观锁配置
        strategy.setVersionFieldName("version");
        mpg.setStrategy(strategy);

        // 6.配置实体类模板
        TemplateConfig templateConfig = new TemplateConfig();
        // 如果setXxxxx(null) 不会生成Xxxx实体类相关代码
        // 因此如果只生成dao层代码
        // 可以在这里控制
        templateConfig.setController("/templates/controller.java");
        templateConfig.setMapper("/templates/mapper.java");
        templateConfig.setService("/templates/service.java");
        templateConfig.setServiceImpl("/templates/serviceImpl.java");
        templateConfig.setXml("/templates/mapper.xml");
        templateConfig.setEntity("/templates/entity.java");
        mpg.setTemplate(templateConfig);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());

        // 7.执行代码生成操作
        mpg.execute();

        // 8.生成vo
        createVO(pc.getParent(), strategy.getInclude(), voPackage, "FindPageReqVO");
        createVO(pc.getParent(), strategy.getInclude(), voPackage, "FindListReqVO");
        createVO(pc.getParent(), strategy.getInclude(), voPackage, "CreateReqVO");
        createVO(pc.getParent(), strategy.getInclude(), voPackage, "UpdateReqVO");
        createVO(pc.getParent(), strategy.getInclude(), voPackage, "CreateOrUpdateReqVO");
        createVO(pc.getParent(), strategy.getInclude(), voPackage, "IdReqVO");
        createVO(pc.getParent(), strategy.getInclude(), voPackage, "CodeReqVO");
        createVO(pc.getParent(), strategy.getInclude(), voPackage, "ReqVO");
        createVO(pc.getParent(), strategy.getInclude(), voPackage, "ResVO");
    }

    /**
     * 生成vo类
     * @param parentPackage
     * @param voPackage
     * @param voName
     */
    public static void createVO(String parentPackage, Set<String> tableNames, String voPackage, String voName) {
        // 1.配置生成包的路径
        PackageConfig pc = new PackageConfig();
        pc.setParent(parentPackage);
        pc.setEntity(voPackage);

        // 构建一个代码生成器对象
        AutoGenerator mpg = new AutoGenerator();
        // 2.全局配置
        GlobalConfig gc = new GlobalConfig();
        // 设置代码生成路径
        gc.setOutputDir(outputDir);
        // 生成的类的注释中作者信息
        gc.setAuthor(author);
        // 生成后是否打开文件夹
        gc.setOpen(false);
        // 是否覆盖
        gc.setFileOverride(true);
        // 设置日期类型
        gc.setDateType(DateType.ONLY_DATE);
        // 是否生成Swagger
        gc.setSwagger2(true);
        // 生成entity类的后缀
        gc.setEntityName("%s"+ voName);
        mpg.setGlobalConfig(gc);

        // 3.设置数据源
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(dbUrl);
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername(dbUsername);
        dsc.setPassword(dbPassword);
        dsc.setDbType(DbType.MYSQL);
        mpg.setDataSource(dsc);

        // 加载包信息
        mpg.setPackageInfo(pc);

        // 4.策略配置
        StrategyConfig strategy = new StrategyConfig();
        if (!CollectionUtils.isEmpty(tableNames)) {
            String[] include = new String[tableNames.size()];
            tableNames.toArray(include);
            strategy.setInclude(include);
        }
        // strategy.setInclude("sys_record_flow_node");
        // 表名中下划线转驼峰命名
        strategy.setNaming(NamingStrategy.underline_to_camel);
        // 表中字段如果有下划线，转驼峰命名
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        //自动生成Lombok
        strategy.setEntityLombokModel(true);
        mpg.setStrategy(strategy);

        // 6.配置vo类模板
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setController(null);
        templateConfig.setMapper(null);
        templateConfig.setService(null);
        templateConfig.setServiceImpl(null);
        templateConfig.setXml(null);
        templateConfig.setEntity("/templates/vo.java");
        mpg.setTemplate(templateConfig);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());

        // 7.执行代码生成操作
        mpg.execute();
    }
}
