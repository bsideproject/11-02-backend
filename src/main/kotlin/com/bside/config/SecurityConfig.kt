package com.bside.config

import com.bside.common.type.Authority
import com.bside.config.jwt.JwtAccessDeniedHandler
import com.bside.config.jwt.JwtAuthenticationEntryPoint
import com.bside.config.jwt.TokenProvider
import com.bside.config.oauth.AppProperties
import com.bside.config.oauth.handler.OAuth2AuthenticationFailureHandler
import com.bside.config.oauth.handler.OAuth2AuthenticationSuccessHandler
import com.bside.repository.OAuth2AuthorizationRequestBasedOnCookieRepository
import com.bside.repository.TokenRepository
import com.bside.service.CustomOAuth2UserService
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.util.*


/**
 * name : SecurityConfig
 * author : jisun.noh
 */
@EnableWebSecurity
class SecurityConfig(
    val tokenProvider: TokenProvider,
    val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
    val jwtAccessDeniedHandler: JwtAccessDeniedHandler,
    val oAuth2UserService: CustomOAuth2UserService,
    val appProperties: AppProperties,
    val tokenRepository: TokenRepository
) : WebSecurityConfigurerAdapter() {
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource? {
        val configuration = CorsConfiguration()
        configuration.addAllowedOrigin("*")
        configuration.allowedMethods = Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE")
        configuration.addAllowedHeader("*")
        configuration.allowCredentials = true
        configuration.maxAge = 3600L
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    override fun configure(web: WebSecurity?) {
        web!!.ignoring().mvcMatchers("/favicon.ico")
    }



    override fun configure(http: HttpSecurity?) {
        //csrf 설정
        http!!.cors()
            .and()
                .csrf().disable()
                //exception handling 설정
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
            //세션 사용 설정 stateless
            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            //로그인, 회원가입 API는 토큰이 없는 상태에서 요청이 들어오기 때문에 permitAll로 설정
            .and()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/login/**").permitAll() // oauth 로그인 test용 페이지 이동용 임시 허용
                .antMatchers("/v3/api-docs",
                    "/configuration/ui",
                    "/swagger-resources/**",
                    "/configuration/security",
                    "/swagger-ui/**").permitAll()
                .antMatchers("/admin/**").hasAnyAuthority(Authority.ROLE_ADMIN.name)
                .anyRequest().authenticated()   // 나머지 API 는 전부 인증 필요
            //OAUTH 설정
            .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorization")
                .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
            .and()
                .redirectionEndpoint()
                .baseUri("/*/oauth2/code/*")
                .and()
                .userInfoEndpoint()
                .userService(oAuth2UserService)
            .and()
                .successHandler(oAuth2AuthenticationSuccessHandler())
                .failureHandler(oAuth2AuthenticationFailureHandler())
            //JwtConfig 등록
            .and()
                .apply(JwtConfig(tokenProvider))
    }

    private fun oAuth2AuthenticationFailureHandler(): AuthenticationFailureHandler? {
        return OAuth2AuthenticationFailureHandler(oAuth2AuthorizationRequestBasedOnCookieRepository());
    }

    private fun oAuth2AuthenticationSuccessHandler(): AuthenticationSuccessHandler? {
        return OAuth2AuthenticationSuccessHandler(
            tokenProvider,
            tokenRepository,
            appProperties,
            oAuth2AuthorizationRequestBasedOnCookieRepository()
        )
    }

    /*
    * 쿠키 기반 인가 Repository
    * 인가 응답을 연계 하고 검증할 때 사용.
    * */
    @Bean
    fun oAuth2AuthorizationRequestBasedOnCookieRepository(): OAuth2AuthorizationRequestBasedOnCookieRepository {
        return OAuth2AuthorizationRequestBasedOnCookieRepository()
    }


}