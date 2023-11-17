package onediary.onediary.config;

import lombok.RequiredArgsConstructor;
import onediary.onediary.config.auth.CustomOAuth2UserService;
import onediary.onediary.config.auth.OAuth2SuccessHandler;
import onediary.onediary.config.jwt.JwtAuthFilter;
import onediary.onediary.config.jwt.JwtTokenProvider;
import onediary.onediary.config.jwt.TokenBlackListRepository;
import onediary.onediary.domain.member.MemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler successHandler;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final TokenBlackListRepository tokenBlackListRepository;
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web) -> web.ignoring().requestMatchers("/예외URl", "/예외 url");
    }
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf((csrfConfig)->
                        csrfConfig.disable()
                )
            .headers((headerConfigurer)->
                headerConfigurer.frameOptions(frameOptionsConfig ->
                        frameOptionsConfig.disable()
                )
            ).authorizeHttpRequests(authorizeRequests -> authorizeRequests
                                .requestMatchers(
                                        AntPathRequestMatcher.antMatcher("/**")).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagementConfigure->
                        sessionManagementConfigure
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(new JwtAuthFilter(jwtTokenProvider, memberRepository,tokenBlackListRepository), UsernamePasswordAuthenticationFilter.class)
                .logout((logoutConfig)->
                    logoutConfig.logoutSuccessUrl("/")
                )
                .oauth2Login(oAuth2LoginConfigurer ->
                        oAuth2LoginConfigurer
                                .authorizationEndpoint(authorizationEndpointConfig ->
                                        authorizationEndpointConfig
                                                .baseUri("/oauth2/authorization")
                                )
                                .redirectionEndpoint(redirectionEndpointConfig ->
                                        redirectionEndpointConfig
                                                .baseUri("/oauth2/callback/**"))
                                .userInfoEndpoint(userInfoEndpointConfigurer->
                                        userInfoEndpointConfigurer
                                        .userService(customOAuth2UserService)
                                )
                                .successHandler(successHandler)
                                .userInfoEndpoint(Customizer.withDefaults())
                );
        return http.build();
    }
}
