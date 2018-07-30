package com.zhenhui.demo.sparklers.uic.config;

import com.zhenhui.demo.sparklers.uic.domain.executor.PostExecutionThread;
import com.zhenhui.demo.sparklers.uic.domain.executor.ThreadExecutor;
import com.zhenhui.demo.sparklers.uic.utils.ExceptionTranslator;
import io.reactivex.schedulers.Schedulers;
import jdk.nashorn.internal.runtime.options.Option;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jooq.SpringTransactionProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.sql.DataSource;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Configuration
public class AppConfig {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private DataSourceTransactionManager transactionManager;

    @Bean
    @Primary
    public DSLContext dsl() {

        TransactionAwareDataSourceProxy proxy = new TransactionAwareDataSourceProxy(dataSource);
        org.jooq.Configuration configuration = new DefaultConfiguration()
                .set(new DataSourceConnectionProvider(proxy))
                .set(new SpringTransactionProvider(transactionManager))
                .set(new ExceptionTranslator())
                .set(SQLDialect.MYSQL);

        return DSL.using(configuration);
    }

    @Bean
    public ThreadExecutor threadExecutor() {

        final Executor executor = new ThreadPoolExecutor(64
            , 128
            , 60
            , TimeUnit.SECONDS
            , new LinkedBlockingDeque<>(128));

        return command -> executor.execute(command);
    }

    @Bean
    public PostExecutionThread postExecutionThread() {
        return () -> Schedulers.from(threadExecutor());
    }

}
