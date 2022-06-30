package generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;

public class MyBatisPlusGenerator {
    public static void main(String[] args) {
        // 构建一个代码生成器对象
        AutoGenerator mpg = new AutoGenerator();
        // 1.全局配置
        GlobalConfig gc = new GlobalConfig();
        // 当前项目路径
        String proPath = "/Users/mac/Desktop/aa";
        // 设置代码生成路径
        gc.setOutputDir(proPath + "/src/main/java");
        // 生成的类的注释中作者信息
        gc.setAuthor("wuheping");
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

        // 2.设置数据源
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://192.168.221.129:3306/test");
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123");
        dsc.setDbType(DbType.MYSQL);
        mpg.setDataSource(dsc);

        // 3.配置生成包的路径
        PackageConfig pc = new PackageConfig();
        // 设置模块存放位置
        pc.setParent("com.web");
        // 设置该模块包的路径
        pc.setEntity("entity");
        pc.setMapper("mapper");
        pc.setService("service");
        pc.setServiceImpl("service.impl");
        pc.setController("controller");
        pc.setXml("mapper");
        mpg.setPackageInfo(pc);

        // 4.策略配置
        StrategyConfig strategy = new StrategyConfig();
        // 设置要映射的表名 不配置默认处理全部表
        // strategy.setInclude("user");
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
        TableFill createTime = new TableFill("create_time", FieldFill.INSERT);
        TableFill updateTime = new TableFill("update_time", FieldFill.INSERT_UPDATE);
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
        // templateConfig.setController(null);
        // templateConfig.setMapper(null);
        // templateConfig.setService(null);
        // templateConfig.setServiceImpl(null);
        // templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 7.执行代码生成操作
        mpg.execute();
    }
}
