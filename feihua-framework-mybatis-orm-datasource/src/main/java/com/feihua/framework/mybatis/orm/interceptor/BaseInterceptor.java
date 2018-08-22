package com.feihua.framework.mybatis.orm.interceptor;


import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.Invocation;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;


public class BaseInterceptor {
    private static Logger logger = org.slf4j.LoggerFactory.getLogger(BaseInterceptor.class);
    /**
     * 产生新的MappedStatement对象
     * @param ms
     * @return
     */
    protected MappedStatement buildNewMappedStatement(final MappedStatement ms, final BoundSql sourceBoundSql, final String newSql, boolean isSingleObject) {

        SqlSource sqlSource = new SqlSource() {
            private BoundSql sqlBound = new BoundSql(ms.getConfiguration(), newSql, sourceBoundSql.getParameterMappings(), sourceBoundSql.getParameterObject()) {
                {
                    for (ParameterMapping mapping : sourceBoundSql.getParameterMappings()) {
                        String prop = mapping.getProperty();
                        if (sourceBoundSql.hasAdditionalParameter(prop)) {
                            this.setAdditionalParameter(prop, sourceBoundSql.getAdditionalParameter(prop));
                        }
                    }
                }
            };

            @Override
            public BoundSql getBoundSql(Object parameterObject) {
                return sqlBound;
            }
        };
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), sqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null) {
            for (String keyProperty : ms.getKeyProperties()) {
                builder.keyProperty(keyProperty);
            }
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());

        if (isSingleObject) {
            List<ResultMap> resultMaps = new ArrayList<ResultMap>();
            ResultMap.Builder inlineResultMapBuilder = new ResultMap.Builder(
                    ms.getConfiguration(),
                    ms.getId() + "-Inline",
                    Integer.class,
                    new ArrayList<ResultMapping>(),
                    null);
            resultMaps.add(inlineResultMapBuilder.build());
            builder.resultMaps(resultMaps);
        } else {
            builder.resultMaps(ms.getResultMaps());
        }
        builder.cache(ms.getCache());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }

    protected Object excuteMappedStatement(Invocation invocation, MappedStatement newMs) {
        final Object[] invocatArgs = invocation.getArgs();
        final MappedStatement invocMs = (MappedStatement) invocatArgs[0];
        try {
            invocatArgs[0] = newMs;
            return invocation.proceed();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            invocatArgs[0] = invocMs;
        }
        return null;
    }
}
