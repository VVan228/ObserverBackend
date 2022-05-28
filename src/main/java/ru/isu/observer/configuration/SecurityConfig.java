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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.isu.observer.model.user.Permission;
import ru.isu.observer.security.JwtConfigurer;

import java.util.List;

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
                 //.cors().disable()
                 .cors().and().csrf().disable()
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST"));
        // setAllowCredentials(true) is important, otherwise:
        // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.

        //configuration.setAllowCredentials(true);

        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        // will fail with 403 Invalid CORS request
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
