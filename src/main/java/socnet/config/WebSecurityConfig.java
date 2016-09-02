package socnet.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    DataSource dataSource;
    
    @Autowired
    UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/account*").anonymous()
                .antMatchers("/login**").anonymous()
                .antMatchers("/static/**", "/signup", "/error", "/403").permitAll()
                .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/profile", true)
                    .failureUrl("/login?error")
                .and()
                    .rememberMe()
                    .key("socnet")
                .and()
                    .httpBasic()
                    .realmName("socnet")
                .and()
                    .logout()
                    .logoutUrl("/logout").permitAll()
                    .logoutSuccessUrl("/login?logout")
                .and()
                    .csrf()
                .and()
                    .exceptionHandling()
                    .accessDeniedPage("/403");
    }
}
