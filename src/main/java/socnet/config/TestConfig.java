package socnet.config;

import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;


@Profile("test")
@Configuration
@ComponentScan("socnet")
@Import(JpaConfig.class)
public class TestConfig {
    @Bean
    DataSource dataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        
        return builder.setName("jdbc:h2:mem:socnet_test;IGNORECASE=TRUE;")
                .setType(EmbeddedDatabaseType.H2)
                .addScripts("testdb.sql")
                .build();
    }
    
    @Bean
    JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.H2);
        vendorAdapter.setShowSql(false);
        
        return vendorAdapter;
    }
}
