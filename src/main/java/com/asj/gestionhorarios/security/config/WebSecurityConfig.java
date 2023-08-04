package com.asj.gestionhorarios.security.config;

import com.asj.gestionhorarios.security.exception.handler.SecurityHandler;
import com.asj.gestionhorarios.security.filter.JWTTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collections;

@RequiredArgsConstructor
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final JWTTokenFilter jwtTokenFilter;
    private final UserDetailsService userDetailsService;
    private final SecurityHandler securityHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        auth.authenticationProvider(provider);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> securityHandler.handle(authException, response))
                .accessDeniedHandler((request, response, accessDeniedException) -> securityHandler.handle(accessDeniedException, response))
                .and()
                .cors().configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Collections.singletonList("*"));
                    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE","PATCH"));
                    config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
                    return config;
                })
                .and()
                .csrf().disable()
                .authorizeRequests()
                //EJEMPLOS
                /*
                PERMITIR EL ACCESO A INCOGNITOS (SIN TOKEN) O AUTENTICADOS (CON TOKEN)
                .antMatchers(HttpMethod.POST, "/person").permitAll()
                PERMITIR EL ACCESO SOLO A AUTENTICADOS (CON TOKEN)
                .antMatchers(HttpMethod.POST, "/person").authenticated()
                PERMITIR EL ACCESO UNICAMENTE A UN ROL
                .antMatchers(HttpMethod.GET, "/test/developer").hasRole("DEVELOPER")
                PERMITIR EL ACCESO UNICAMENTE A ROLES ESPECIFICOS
                .antMatchers(HttpMethod.POST, "/dev/{email}").hasAnyRole("DEVELOPER","ADMIN")
                PERMITIR EL ACCESO A UNA RUTA COMPLETA A UN ROL
                .antMatchers("/admin").hasRole("ADMIN")
                BLOQEAR EL ACCESO A UNA RUTA COMPLETA
                .antMatchers("/nulo/**").denyAll()
                PARA BLOQUEAR EL ACCEDO A UN ROL
                .antMatchers("/test").not().hasRole("BLOCKED")
                PARA BLOQUEAR EL ACCESO A VARIOS ROLES
                .antMatchers(HttpMethod.GET, "/test/developer").not().hasAnyRole("BLOCKED","PENDING","ADMIN","MANAGEMENT")
                 */
                .antMatchers(publicEndpoint).permitAll()
                .antMatchers(HttpMethod.PUT, "/api/v1/person/password/**").not().hasAnyAuthority("BLOCKED", "PENDING")
                .antMatchers(HttpMethod.PUT, "/api/v1/person").not().hasAnyAuthority("BLOCKED","PENDING")
                .antMatchers(HttpMethod.PUT, "/api/v1/person").authenticated()
                .antMatchers(HttpMethod.POST, "/api/v1/person").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/person/{email}").not().hasAnyAuthority("BLOCKED", "PENDING")
                .antMatchers(HttpMethod.DELETE, "/api/v1/person/{email}").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/v1/person").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/v1/person/admin/add-role").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/v1/person/admin/get-blocked").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/v1/person/admin/{email}").hasAuthority("ADMIN")
                //TASK
                .antMatchers(HttpMethod.PUT, "/api/v1/task").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/v1/task").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PATCH,"/api/v1/task/add-dev/**").hasAnyAuthority("ADMIN","DEVELOPER")
                .antMatchers(HttpMethod.PATCH,"/api/v1/task/delete-dev/**").hasAuthority("DEVELOPER")
                .antMatchers(HttpMethod.PATCH,"/api/v1/task/change-status/**").hasAuthority("DEVELOPER")

                .antMatchers(HttpMethod.GET, "/api/v1/project").authenticated()
                .antMatchers(HttpMethod.GET, "/api/v1/project/{project_id}").authenticated()
                .antMatchers(HttpMethod.PUT, "/api/v1/project/{project_id}").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/v1/project").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/v1/project/{project_id}").hasAuthority("ADMIN")

                .antMatchers(HttpMethod.GET, "/api/v1/month").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/v1/month").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/month").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/month/all").permitAll()

                .antMatchers(HttpMethod.GET, "/api/v1/client").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/v1/client/{email}").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/v1/client").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/v1/client/{email}").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/v1/client/{email}").hasAuthority("ADMIN")

                .antMatchers(HttpMethod.GET, "/api/v1/sprint").hasAnyAuthority("ADMIN", "DEVELOPER", "MANAGEMENT")
                .antMatchers(HttpMethod.GET, "/api/v1/sprint/{sprint_id}").hasAnyAuthority("ADMIN", "DEVELOPER", "MANAGEMENT")
                .antMatchers(HttpMethod.POST, "/api/v1/sprint").hasAnyAuthority("ADMIN", "DEVELOPER", "MANAGEMENT")
                .antMatchers(HttpMethod.PUT, "/api/v1/sprint/{sprint_id}").hasAnyAuthority("ADMIN", "DEVELOPER", "MANAGEMENT")
                .antMatchers(HttpMethod.DELETE, "/api/v1/sprint/{sprint_id}").hasAnyAuthority("ADMIN", "DEVELOPER", "MANAGEMENT")

                .antMatchers(HttpMethod.GET, "/api/v1/report/**").hasAnyAuthority("ADMIN", "DEVELOPER", "MANAGEMENT")
                .antMatchers(HttpMethod.POST, "/api/v1/dev/{email}").not().hasAnyAuthority("BLOCKED", "PENDING", "ADMIN", "MANAGEMENT")
                .antMatchers(HttpMethod.GET, "/api/v1/test/developer").hasAuthority("DEVELOPER")
                .antMatchers(HttpMethod.GET, "/api/v1/role/{email}").permitAll()
                //SWAGGER
                .antMatchers("/v3/api-docs", "/swagger-resources/**", "/swagger-ui/**").permitAll()
                //LOGIN
                .antMatchers("/api/v1/auth/login").permitAll()
                //TEST
                .antMatchers(HttpMethod.GET, "/api/v1/test/person/{email}").authenticated()
                .antMatchers(HttpMethod.GET, "/api/v1/test/developer").hasAuthority("DEVELOPER")
                .antMatchers(HttpMethod.GET, "/api/v1/test/admin").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/v1/test/management").hasAuthority("MANAGEMENT")
                .antMatchers(HttpMethod.GET, "/api/v1/test/blocked").hasAuthority("BLOCKED")
                .antMatchers(HttpMethod.GET, "/api/v1/test/pending").hasAuthority("PENDING")
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web)  {
        web.ignoring().antMatchers("/v3/api-docs",
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/api/v1/logged/**"
        );
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private static final String[] publicEndpoint = {
            // -- Swagger UI v2
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**"
            // other public endpoints of your API may be appended to this array
    };
}
