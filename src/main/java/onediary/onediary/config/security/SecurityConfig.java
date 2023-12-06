package onediary.onediary.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import onediary.onediary.config.properties.AppProperties;
import onediary.onediary.config.properties.CorsProperties;
import onediary.onediary.oauth.entryPoint.RestAuthenticationEntryPoint;
import onediary.onediary.oauth.filter.TokenAuthenticationFilter;
import onediary.onediary.oauth.handler.OAuth2AuthenticationFailureHandler;
import onediary.onediary.oauth.handler.OAuth2AuthenticationSuccessHandler;
import onediary.onediary.oauth.token.AuthTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {


    private final CorsProperties corsProperties;
    private final AppProperties appProperties;
    private final AuthTokenProvider tokenProvider;
    private final OAuth2UserService oAuth2UserService;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(AntPathRequestMatcher.antMatcher("/v2/api-docs"),
                        AntPathRequestMatcher.antMatcher("/configuration/**"),
                        AntPathRequestMatcher.antMatcher("/swagger*/**"),
                        AntPathRequestMatcher.antMatcher("/webjars/**"));
    }


    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
        http
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .formLogin()
                .disable()
                .httpBasic().disable()
                .exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
             //   .accessDeniedHandler(tokenAccessDeniedHandler)
                .and()
                .authorizeHttpRequests(authorizeRequests->
                        authorizeRequests
                                .requestMatchers(
                                        CorsUtils::isPreFlightRequest,
                                        AntPathRequestMatcher.antMatcher("/**"),
                                        AntPathRequestMatcher.antMatcher("/api/**"),

                                        AntPathRequestMatcher.antMatcher("/api/v1/login/**"),
                                        //AntPathRequestMatcher.antMatcher("/api/token/refresh"),
                                        AntPathRequestMatcher.antMatcher("/api/records"),
                               //         AntPathRequestMatcher.antMatcher("/api/v1/auth/login/**"),
                                 //       AntPathRequestMatcher.antMatcher("/api/v1/auth/refresh/**"),
                                        AntPathRequestMatcher.antMatcher("/swagger-ui.html"),
                                        AntPathRequestMatcher.antMatcher("/api/success/**"),
                                        AntPathRequestMatcher.antMatcher("/swagger-ui/**"),
                                        AntPathRequestMatcher.antMatcher("/v3/**"),
                                        AntPathRequestMatcher.antMatcher("/v2/api-docs/**"),
                                        AntPathRequestMatcher.antMatcher("/swagger-resources/**"),
                                        AntPathRequestMatcher.antMatcher("/webjars/**"),
                                        AntPathRequestMatcher.antMatcher(HttpMethod.OPTIONS)).permitAll()

                                //     AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/exception/**")).permitAll()
                                //            AntPathRequestMatcher.antMatcher("/api/v1/auth/login/**"),
                                //       AntPathRequestMatcher.antMatcher("/api/v1/auth/refresh/**"),
                                //              AntPathRequestMatcher.antMatcher("/swagger-ui"))
                                //.permitAll()
                                //    .requestMatchers(
                                // AntPathRequestMatcher.antMatcher("/api/**/admin/**")).hasAnyAuthority(Role.ADMIN.getCode())
                                .anyRequest().authenticated()
                )
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorization")
              //  .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
                .and()
                .redirectionEndpoint()
                .baseUri("/*/oauth2/code/*")
                .and()
                .userInfoEndpoint()
                .userService(oAuth2UserService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler())
                .failureHandler(oAuth2AuthenticationFailureHandler());

        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.logout()
                // .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/");
//                .invalidateHttpSession(true)
//                .deleteCookies("JSESSIONID")
//                .permitAll();
        return http.build();
    }


    /*
     * auth 매니저 설정
     * */
    @Bean
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    /*
     * 토큰 필터 설정
     * */
    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }


    /*
     * Oauth 인증 성공 핸들러
     * */
    @Bean
    public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return new OAuth2AuthenticationSuccessHandler(
                tokenProvider,
                appProperties
        );
    }
    /*
     * Oauth 인증 실패 핸들러
     * */
    @Bean
    public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
        return new OAuth2AuthenticationFailureHandler();
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