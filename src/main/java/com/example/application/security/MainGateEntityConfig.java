package com.example.application.security;

import com.vaadin.flow.spring.security.VaadinAwareSecurityContextHolderStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

import static com.example.application.security.AdminSecurityConfig.customRequestCache;

@Configuration
@EnableWebSecurity
@Order(3)
public class MainGateEntityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        SecurityContextHolder.setStrategyName(
                VaadinAwareSecurityContextHolderStrategy.class.getName());
        http
                .requestMatchers().antMatchers("/security/**")
                .and()
                .authorizeRequests()
                .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
                .antMatchers("/security/login**").permitAll()
                .antMatchers("/security/**").hasAuthority("SECURITY_OFFICER")
                .and()
                .requestCache().requestCache(customRequestCache())
                .and().formLogin().loginPage("/security/login")
                .defaultSuccessUrl("/security/out", true)
                .failureUrl("/security/login?error")
                .and()
                .logout().logoutSuccessUrl("/")
                .and()
                .csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        web.ignoring().antMatchers("/images/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .passwordEncoder(new BCryptPasswordEncoder())
                .dataSource(dataSource)
                .usersByUsernameQuery("select email_id, hashed_password, 'true' as " +
                        "enabled from security_officer where email_id=?")
                .authoritiesByUsernameQuery("select email_id, role from " +
                        "security_officer where email_id=?");
    }
}
