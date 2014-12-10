package org.chip.ihl.surveymanager.config;

import org.chip.ihl.surveymanager.rest.controller.SurveyManagerController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

/**
 * Spring Security configuration file
 * Created by sboykin on 12/10/2014.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/admin/**")
//                .access("hasRole('ROLE_ADMIN')")
                .access("hasRole('myscilhs-sm')")
                .and().formLogin();

        // Allow non-CSRF requests for trigger web services
        http.csrf().requireCsrfProtectionMatcher(new RequestMatcher() {
            private String allowedMethodsRegex = "^(GET|HEAD|TRACE|OPTIONS)$";
            private RegexRequestMatcher apiMatcher = new RegexRequestMatcher(SurveyManagerController.TRIGGER_BASE_REQUEST_URI + "/.*", null);

            @Override
            public boolean matches(HttpServletRequest httpServletRequest) {
                // allowedMethodsRegex and requests don't need CSRF protection
                if (httpServletRequest.getMethod().matches(allowedMethodsRegex)) {
                    return false;
                }
                if (apiMatcher.matches(httpServletRequest)) {
                    return false;
                }
                // otherwise enable it
                return true;
            }
        });

    }
}