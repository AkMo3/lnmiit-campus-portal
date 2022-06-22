package com.example.application.security;

import com.vaadin.flow.spring.security.VaadinAwareSecurityContextHolderStrategy;
import com.vaadin.flow.spring.security.VaadinWebSecurityConfigurerAdapter;
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

@EnableWebSecurity
@Configuration
@Order(2)
public class SecurityConfig extends VaadinWebSecurityConfigurerAdapter {

  @Autowired private DataSource dataSource;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    SecurityContextHolder.setStrategyName(
            VaadinAwareSecurityContextHolderStrategy.class.getName());
    http
            .requestMatchers().antMatchers("/user/**")
            .and()
            .authorizeRequests()
            .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
            .antMatchers("/user/login**").permitAll()
            .antMatchers("/user/**").hasAuthority("STUDENT")
            .and()
            .requestCache().requestCache(customRequestCache())
            .and().formLogin().loginPage("/user/login")
            .defaultSuccessUrl("/user/home", true)
            .failureUrl("/user/login?error")
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
            .usersByUsernameQuery("select roll_number, hashed_password, 'true' as enabled from student where roll_number=?")
            .authoritiesByUsernameQuery("select roll_number, role from student where roll_number=?");
  }
}
