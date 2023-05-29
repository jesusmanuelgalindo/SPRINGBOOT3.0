package gob.jmas.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "comercialEntityManagerFactory",
        transactionManagerRef = "comercialTransactionManager",
        basePackages = { "gob.jmas.repository.comercial" }
)
public class ComercialDatabaseConfig {

    @Autowired
    private Environment environment;
    @Bean
    @ConfigurationProperties("comercial.datasource")
    public DataSourceProperties comercialDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource comercialDataSource() {
        return comercialDataSourceProperties().initializeDataSourceBuilder().build();
    }
    @Bean(name = "comercialEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean comercialEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(comercialDataSource())
                .packages("gob.jmas.model.comercial")
                .persistenceUnit("comercial")
                .properties(hibernateProperties())
                .build();
    }
    private Map<String, Object> hibernateProperties() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", environment.getProperty("comercial.jpa.hibernate.ddl-auto"));
        return properties;
    }

    @Bean
    public PlatformTransactionManager comercialTransactionManager(
            final @Qualifier("comercialEntityManagerFactory") LocalContainerEntityManagerFactoryBean comercialEntityManagerFactory) {
        return new JpaTransactionManager(comercialEntityManagerFactory.getObject());
    }
}