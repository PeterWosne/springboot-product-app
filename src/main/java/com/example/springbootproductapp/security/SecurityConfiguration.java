package com.example.springbootproductapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
public class SecurityConfiguration {

    //этим методом настраиваем конфигурацию аутентификацию+метод получает 3 параметра
    //PasswordEncoder -> чтобы не хранить пароли в DB в прямом виде
    @Autowired
    public void authConfigure(AuthenticationManagerBuilder auth,
                              UserDetailsService userAuthService,
                              PasswordEncoder passwordEncoder) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("mem_user")
                .password(passwordEncoder.encode("password"))
                .roles("SUPERADMIN");

        //@TODO добавляем аутентификейшнПровайдер который берет инфу о пользователях из database
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userAuthService);
        provider.setPasswordEncoder(passwordEncoder);
        auth.authenticationProvider(provider);//указываем AuthenticationManagerBuilder что мы будем использовать второй провайдер
    }

    //делаем нормальную авторизацию для REST-контроллера(например для UserResource, чтоб доступ был только у админа) -> используем т.н базовую авторизацию
    //как теперь авторизовываться?
    //Базовая авторизация делается через заголовок, то есть снизу запроса пишем(вероятно в хедере) прописываем Authorization: Basic login password
    @Configuration
    @Order(1)
    public static class ApiWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .antMatcher("/api/**")
                    .authorizeRequests()
                    .anyRequest().hasAnyRole("ADMIN", "SUPERADMIN")
                    .and()
                    .httpBasic()
                    .authenticationEntryPoint((request, response, authException) -> {
                        response.setContentType("application/json");
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().println("Error" + authException.getMessage());
                    })
                    .and()
                    .csrf().disable()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }
    }


    //чтобы защищать паролем не все приложение, а отдельные части
    @Configuration
    @Order(2)
    public static class UiWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .antMatchers("/**/*.css", "/**/*.js").permitAll() //помимо этого есть еще anonymous онли для анонимных юзеров
                    .antMatchers("/product/**").hasAnyRole("ADMIN", "SUPERADMIN") //** для сколько угодно сегментов vs * для одного
                    .antMatchers("/user/**").hasAnyRole("ADMIN", "SUPERADMIN")
                    .and()
                    .formLogin()// значит что мы авторизовываемся через форму логина
                    .loginPage("/login") //указываем URL где будет располагаться страница логина
                    .defaultSuccessUrl("/catalog")
                    .and()
                    .exceptionHandling()
                    .accessDeniedPage("/access_denied");
        }

        //для отладки добавить в эппликейшн пропертиз
        //logging.level.org.springframework.sequrity=DEBUG
    }

    //@TODO далее требуется добавить кнопку отображающую username и которая позволяет сделать logout
    //в соотв темплейт добавляем новое пространство имен http://www.thymeleaf.org/extras/spring-security и форму,
    //которая видна когда мы авторизованы sec:authorize="isAuthenticated()"
    //форма переходит на ссылку logout, отправка методом post, внутри кнопка с содержимым Logout + span(sec:authentication="principal.username")

    // @TODO ВАЖНО
    // злоумышленник может сформировать post-запрос на Users/Products не имея доступа к кнокам редактирования-удаления и отправить его на сервер
    // чтобы избежать вешаем на контроллер аннотацию @EnableGlobalMethodSecurity(securedEnabled=true), уже затем над методом @Secured("SUPERADMIN")
}
