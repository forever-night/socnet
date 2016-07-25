package socnet.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;


@Configuration
@ComponentScan("socnet")
@Import({JpaConfig.class})
public class TestConfig {
    @Bean
    DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/socnet");
        dataSource.setUsername("user");
        dataSource.setPassword("user");
        dataSource.setDriverClassName("org.postgresql.Driver");
        
        return dataSource;
    }
}
