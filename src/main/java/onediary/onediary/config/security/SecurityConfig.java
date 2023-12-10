package onediary.onediary.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import onediary.onediary.config.properties.CorsProperties;
import onediary.onediary.oauth.filter.TokenAuthenticationFilter;
import onediary.onediary.oauth.token.AuthTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {



    private final AuthTokenProvider tokenProvider;
    private final CorsProperties corsProperties;


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(AntPathRequestMatcher.antMatcher("/v2/api-docs"),
                        AntPathRequestMatcher.antMatcher("/api-docs/**"),
                        AntPathRequestMatcher.antMatcher("/configuration/**"),
                        AntPathRequestMatcher.antMatcher("/swagger*/**"),
                        AntPathRequestMatcher.antMatcher("/h2-console/**"),
                        AntPathRequestMatcher.antMatcher("/webjars/**"));
    }


    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
        TokenAuthenticationFilter tokenAuthenticationFilter = new TokenAuthenticationFilter(tokenProvider);
        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
//                .headers((headerConfigurer) ->
//                        headerConfigurer.frameOptions(frameOptionsConfig ->
//                                frameOptionsConfig.disable()
//                        )
//                )
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer.disable())
                .httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer.disable())
                .authorizeHttpRequests(authorizeRequests->
                        authorizeRequests
                                .requestMatchers(
                                        AntPathRequestMatcher.antMatcher("/auth/login/kakao/**"),
                                        AntPathRequestMatcher.antMatcher("/auth/refresh/**"),
                                        AntPathRequestMatcher.antMatcher("/"),
                                        AntPathRequestMatcher.antMatcher("/**"),
                                        AntPathRequestMatcher.antMatcher("/api/**"),
                                        AntPathRequestMatcher.antMatcher("api/success"),
                                        AntPathRequestMatcher.antMatcher("/h2-console/**"),
                                        AntPathRequestMatcher.antMatcher("/swagger-ui-onediary.html"),
                                        AntPathRequestMatcher.antMatcher("/api/success/**"),
                                        AntPathRequestMatcher.antMatcher("/v2/api-docs/"),
                                        AntPathRequestMatcher.antMatcher("/swagger-ui-onediary/**"),
                                        AntPathRequestMatcher.antMatcher(HttpMethod.OPTIONS)).permitAll()

                                //     AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/exception/**")).permitAll()
                                //            AntPathRequestMatcher.antMatcher("/api/v1/auth/login/**"),
                                //       AntPathRequestMatcher.antMatcher("/api/v1/auth/refresh/**"),
                                //              AntPathRequestMatcher.antMatcher("/swagger-ui"))
                                //.permitAll()
                                //    .requestMatchers(
                                // AntPathRequestMatcher.antMatcher("/api/**/admin/**")).hasAnyAuthority(Role.ADMIN.getCode())
                                .anyRequest().authenticated()
                );

        http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer.logoutSuccessUrl("/"));
                // .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                .invalidateHttpSession(true)
//                .deleteCookies("JSESSIONID")
//                .permitAll();
        return http.build();
    }




    /*
     * Cors 설정
     * */
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        log.debug("Cors Configuration - Creating CORS configuration...");

        UrlBasedCorsConfigurationSource corsConfigSource = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedHeaders(Arrays.asList(corsProperties.getAllowedHeaders().split(",")));
        corsConfig.setAllowedMethods(Arrays.asList(corsProperties.getAllowedMethods().split(",")));
        corsConfig.setAllowedOrigins(Arrays.asList(corsProperties.getAllowedOrigins().split(",")));
        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(corsConfig.getMaxAge());

        corsConfigSource.registerCorsConfiguration("/**", corsConfig);
        return corsConfigSource;
    }

}