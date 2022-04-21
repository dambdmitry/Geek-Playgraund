package edu.mitin.playground.users.config;

import edu.mitin.playground.users.UserService;
import edu.mitin.playground.users.entity.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
                    .antMatchers("/tournaments").permitAll()
                    .antMatchers("/tournaments/*").permitAll()
                    .antMatchers(HttpMethod.POST, "/tournaments/*").permitAll()
                    .antMatchers("/player").permitAll()
                    .antMatchers("/organizer").permitAll()
                     .antMatchers("/user").hasAuthority(Permission.USER_PROFILE.getPermission())
                //.antMatchers("/admin/**").hasAuthority(Permission.ADMIN_PROFILE.getPermission())
                    //Доступ разрешен всем пользователей

                    .antMatchers("/css/*").permitAll()
                    .antMatchers("/css/security/hiddenByAnonymous.css").hasRole("ANONYMOUS")
                .antMatchers("/css/security/hiddenByAuthed.css").authenticated()
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
