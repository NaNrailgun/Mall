package com.nanrailgun.springbootstartersnowflake.config;

import com.nanrailgun.springbootstartersnowflake.beans.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({IdWorker.class})
@EnableConfigurationProperties(IdWorkerProperties.class)
public class IdWorkerAutoConfiguration {

    @Autowired
    IdWorkerProperties idWorkerProperties;

    @Bean
    public IdWorker idWorker(){
        return new IdWorker(idWorkerProperties.getWorkerId(), idWorkerProperties.getDataCenterId(), idWorkerProperties.getSequence());
    }
}
