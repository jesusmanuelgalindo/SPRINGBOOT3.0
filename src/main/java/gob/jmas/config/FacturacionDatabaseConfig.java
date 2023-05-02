package gob.jmas.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
        entityManagerFactoryRef = "facturacionEntityManagerFactory",
        transactionManagerRef = "facturacionTransactionManager",
        basePackages = { "gob.jmas.repository.facturacion" }
)
public class FacturacionDatabaseConfig {

    @Autowired
    private Environment environment;
    @Bean
    @Primary
    @ConfigurationProperties("facturacion.datasource")
    public DataSourceProperties facturacionDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource facturacionDataSource() {
        return facturacionDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Primary
    @Bean(name = "facturacionEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean facturacionEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(facturacionDataSource())
                .packages("gob.jmas.model.facturacion")
                .persistenceUnit("facturacion")
                .properties(hibernateProperties())
                .build();
    }

    private Map<String, Object> hibernateProperties() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", environment.getProperty("facturacion.jpa.hibernate.ddl-auto"));
        return properties;
    }
    @Primary
    @Bean
    public PlatformTransactionManager facturacionTransactionManager(
            final @Qualifier("facturacionEntityManagerFactory") LocalContainerEntityManagerFactoryBean facturacionEntityManagerFactory) {
        return new JpaTransactionManager(facturacionEntityManagerFactory.getObject());
    }
}