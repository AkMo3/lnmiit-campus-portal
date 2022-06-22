package com.example.application.security;

import com.vaadin.flow.spring.security.VaadinWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@Order(1)
public class AdminSecurityConfig extends VaadinWebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        System.out.println("Admin Security Config");
        http
                .requestMatchers().antMatchers("/admin/**")
                .and()
                .authorizeRequests()
                .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
                .antMatchers("/admin/login**").permitAll()
                .antMatchers("/admin/request")
                .hasAnyAuthority("HOSTEL_ADMIN", "WARDEN", "CARETAKER")
                .anyRequest().hasAuthority("HOSTEL_ADMIN")
                .and()
                .requestCache().requestCache(customRequestCache())
                .and().formLogin().loginPage("/admin/login")
                .defaultSuccessUrl("/admin/request", true)
                .failureUrl("/admin/login?error")
                .and()
                .logout().logoutSuccessUrl("/admin/login")
                .deleteCookies("JSESSIONID")
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
                .passwordEncoder(passwordEncoder())
                .dataSource(dataSource)
                .usersByUsernameQuery("select email_id, hashed_password, 'true' as enabled from" +
                        " hostel_admin where email_id=?")
                .authoritiesByUsernameQuery("select email_id, role from hostel_admin where email_id=?");
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public static CustomRequestCache customRequestCache() {
        return new CustomRequestCache();
    }
}
