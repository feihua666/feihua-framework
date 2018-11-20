package com.feihua.framework.cms;

import freemarker.template.DefaultObjectWrapper;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.Version;

public class DefaultObjectWrapperBuilderFactory {
	
	private DefaultObjectWrapperBuilderFactory() {} 
	
	private static final Version v = new Version(2, 3, 28);
	private static DefaultObjectWrapperBuilder defaultObjectWrapperBuilder = null;

    //静态工厂方法   
    public static DefaultObjectWrapperBuilder getInstance() {
        if (defaultObjectWrapperBuilder == null) {
            defaultObjectWrapperBuilder =  new DefaultObjectWrapperBuilder(v);
        }
        return defaultObjectWrapperBuilder;  
    }
    
    public static DefaultObjectWrapper getDefaultObjectWrapper() {
        return getInstance().build();
    }
    
}
