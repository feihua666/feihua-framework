package com.feihua.framework.base.generator;

import org.apache.ibatis.io.Resources;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangwei
 * Created at 2017/6/12 16:25
 */
public class GeneratorMain {


    public static void main(String[] args) throws Exception {
        // 信息缓存
        List<String> warnings = new ArrayList<String>();
        // 覆盖已有的重名文件
        boolean overwrite = true;
        // 准备 配置文件
        Reader reader = Resources.getResourceAsReader("generatorConfig.xml");
        // 1.创建 配置解析器
        ConfigurationParser parser = new ConfigurationParser(warnings);
        // 2.获取 配置信息
        Configuration config = parser.parseConfiguration(reader);
        // 3.创建 默认命令解释调回器
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        // 4.创建 mybatis的生成器
        MybatisGenerator myBatisGenerator = new MybatisGenerator(config, callback, warnings);
        // 5.执行，关闭生成器
        myBatisGenerator.generate(null);
    }
}
