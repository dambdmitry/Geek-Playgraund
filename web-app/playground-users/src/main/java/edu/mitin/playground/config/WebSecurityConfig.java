package edu.mitin.playground.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.mitin.playground.UserService;
import edu.mitin.playground.model.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userService;



    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf()
                    .disable()
                .authorizeRequests()
                    //Доступ только для не зарегистрированных пользователей
                    .antMatchers(HttpMethod.POST, "/auth/registration").permitAll()
                    .antMatchers(HttpMethod.GET, "/auth/registration").permitAll()
                    .antMatchers("/").permitAll()
                    .antMatchers("/errors/**").permitAll()
                    .antMatchers("/tournament").permitAll()
                    .antMatchers("/player").permitAll()
                    .antMatchers("/organizer").permitAll()
                     .antMatchers("/user").hasAuthority(Permission.USER_PROFILE.getPermission())
                    //Доступ разрешен всем пользователей

                    .antMatchers("/css/auth.css").permitAll()
                    .antMatchers("/css/hiddenByAnonymous.css").hasRole("ANONYMOUS")
                .antMatchers("/css/hiddenByAuthed.css").authenticated()
                //Все остальные страницы требуют аутентификации
                .anyRequest().authenticated()
                //.antMatchers("/css/authed.css").permitAll()
                .and()

                    //Настройка для входа в систему
                    .formLogin()
                    .loginPage("/auth/login").permitAll()

                    //Перенарпавление на главную страницу после успешного входа
                    .defaultSuccessUrl("/", true)
                    .failureUrl("/auth/login?error=true")
                .and()
                    .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", "POST"))
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                    .permitAll()
                    .logoutSuccessUrl("/");
    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth, BCryptPasswordEncoder encoder) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(encoder);
    }
}
