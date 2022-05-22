package ru.isu.observer.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.isu.observer.model.user.Permission;
import ru.isu.observer.security.JwtConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    JwtConfigurer jwtConfigurer;

    SecurityConfig(JwtConfigurer jwtConfigurer){
        this.jwtConfigurer = jwtConfigurer;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*http
                .csrf().disable()
                //.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //.and()
                .authorizeRequests()
                .antMatchers("/**").permitAll();*/
         http
                 .csrf().disable()
                 .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                 .and()
                 .authorizeRequests()
                 .antMatchers("/auth/login").permitAll()
                 .antMatchers("/auth/logout").authenticated()
                 //.antMatchers("/hierarchy").hasAnyAuthority(Permission.HIERARCHY_READ.getPermission())
                 .anyRequest().authenticated()
                 .and()
                 .apply(jwtConfigurer);
    }

    @Bean

    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }


}
