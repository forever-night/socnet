package socnet.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;


@Configuration
@Import({WebConfig.class, DataConfig.class, WebSecurityConfig.class})
@ComponentScan("socnet")
@EnableTransactionManagement
public class AppConfig {
    @Autowired
    DataSource dataSource;

    @Bean
    JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.POSTGRESQL);
        vendorAdapter.setDatabasePlatform("org.hibernate.dialect.PostgreSQL94Dialect");
        vendorAdapter.setShowSql(false);

        return vendorAdapter;
    }

    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.format_sql", "true");
        properties.setProperty("hibernate.hbm2ddl.auto", "validate");

        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setJpaVendorAdapter(jpaVendorAdapter());
        emf.setPackagesToScan("socnet");
        emf.setJpaProperties(properties);

        return emf;
    }

    @Bean
    JpaTransactionManager jpaTransactionManager() {
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setDataSource(dataSource);
        tm.setEntityManagerFactory(entityManagerFactory().getObject());

        return tm;
    }
}
