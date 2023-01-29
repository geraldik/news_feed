package com.gmail.geraldik.newsfeed.config;

import com.gmail.geraldik.newsfeed.pesristence.tables.daos.ItemDao;
import lombok.RequiredArgsConstructor;
import org.jooq.SQLDialect;
import org.jooq.impl.*;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;

@SpringBootConfiguration
@RequiredArgsConstructor
public class JoogConfig {

    private final DataSource dataSource;

    @Bean
    public DataSourceConnectionProvider connectionProvider() {
        return new DataSourceConnectionProvider(
                new TransactionAwareDataSourceProxy(dataSource));
    }

    public DefaultConfiguration configuration() {
        DefaultConfiguration jooqConfiguration = new DefaultConfiguration();
        jooqConfiguration.set(connectionProvider());
        jooqConfiguration.set(SQLDialect.POSTGRES);
        jooqConfiguration
                .set(new DefaultExecuteListenerProvider(new DefaultExecuteListener()));

        return jooqConfiguration;
    }

    @Bean
    public DefaultDSLContext dsl() {
        return new DefaultDSLContext(configuration());
    }

    @Bean
    ItemDao getItemDao() {
        return new ItemDao(configuration());
    }
}
