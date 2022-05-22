package ru.isu.observer.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
         http
                 .csrf().disable()
                 .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                 .and()
                 .authorizeRequests()
                 .antMatchers("/auth/login").permitAll()
                 .antMatchers("/auth/updateAccessToken").permitAll()
                 .antMatchers("/auth/logout").authenticated()
                 .antMatchers(HttpMethod.POST,"/hierarchy/**").hasAnyAuthority(Permission.HIERARCHY_WRITE.getPermission())
                 .antMatchers(HttpMethod.GET,"/hierarchy/**").hasAnyAuthority(Permission.HIERARCHY_READ.getPermission())
                 .antMatchers(HttpMethod.POST,"/tests/**").hasAnyAuthority(
                         Permission.TESTS_CREATE.getPermission(),
                         Permission.TESTS_EDIT.getPermission())
                 .antMatchers(HttpMethod.GET,"/tests/**").hasAnyAuthority(Permission.TESTS_GET.getPermission())
                 .antMatchers(HttpMethod.POST,"/users/save/**").hasAnyAuthority(Permission.USERS_CREATE.getPermission())
                 .antMatchers(HttpMethod.POST,"/users/update/**").authenticated()
                 .antMatchers(HttpMethod.GET,"/users/**").hasAnyAuthority(Permission.USERS_GET.getPermission())
                 .antMatchers(HttpMethod.POST,"/testAnswers/save").hasAnyAuthority(Permission.TEST_ANSWERS_CREATE.getPermission())
                 .antMatchers(HttpMethod.POST,"/testAnswers/set/**").hasAnyAuthority(Permission.TEST_ANSWERS_EDIT.getPermission())
                 .antMatchers(HttpMethod.GET,"/testAnswers/**").hasAnyAuthority(Permission.TEST_ANSWERS_GET.getPermission())
                 .antMatchers(HttpMethod.POST,"/subjects/save").hasAnyAuthority(Permission.SUBJECTS_CREATE.getPermission())
                 .antMatchers(HttpMethod.POST,"/subjects/update/**").hasAnyAuthority(Permission.SUBJECTS_EDIT.getPermission())
                 .antMatchers(HttpMethod.GET,"/subjects/**").hasAnyAuthority(Permission.SUBJECTS_GET.getPermission())
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
