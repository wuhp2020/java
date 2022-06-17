package com.spring.config;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import java.util.Collection;

/**
 * @ Author     : wuhp
 * @ Date       : 2022/1/24
 * @ Description: 描述
 */
public class PreciseShardingAlgorithmConfig implements PreciseShardingAlgorithm<Long> {

    @Override
    public String doSharding(Collection<String> dataSourceNames, PreciseShardingValue<Long> preciseShardingValue) {
        String result = "";
        Long id = preciseShardingValue.getValue();
        if (id >=0L && id < 10000000L) {
            for (String dataSourceName : dataSourceNames) {
                if (dataSourceName.endsWith("0")) {
                    result = dataSourceName;
                }
            }
        } else if ( id >=10000000L && id < 20000000L) {
            for (String dataSourceName : dataSourceNames) {
                if (dataSourceName.endsWith("1")) {
                    result = dataSourceName;
                }
            }
        } else {
            throw new UnsupportedOperationException();
        }
        return result;
    }
}
