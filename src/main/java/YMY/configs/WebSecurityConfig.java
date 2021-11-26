package YMY.configs;

import YMY.services.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    final UserService userService;
    public WebSecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(userService.encoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/yonetim/**").hasRole("ADMIN")
                .antMatchers("/firma/**").hasRole("ADMIN")
                .antMatchers("/musteri/**").hasRole("ADMIN")
                .antMatchers("/irsaliye/**").hasRole("ADMIN")
                .antMatchers("/istatistikler/**").hasRole("ADMIN")
                .antMatchers("/notlarım/**").hasRole("ADMIN")
                .antMatchers("/ana_sayfa/**").permitAll()
                .antMatchers("/static/**").permitAll()
                .antMatchers("/templates/**").permitAll()
                .antMatchers("/dashboardPage/**").permitAll()
                .antMatchers("/homePage/**").permitAll()
                .antMatchers("/loginPage/**").permitAll()
                .antMatchers("/customizeJs/**").permitAll()
                .antMatchers("/error/**").permitAll()
                .antMatchers("/inc/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/giris_yap")
                .defaultSuccessUrl("/yonetim",true)
                .permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/cikis_yap"))
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/giris_yap")
                .permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/403");

        //Disable the post methods data actions
        http.csrf().disable();

        //Create Cookie with Spring Security
        http
                .rememberMe()
                .key("deneme")
                .rememberMeCookieName("remember-me")
                .tokenValiditySeconds(60*60*24)
                .alwaysRemember(true)
                .useSecureCookie(true);

        //Iframe showing options
        http.headers().frameOptions().disable();

    }

}
