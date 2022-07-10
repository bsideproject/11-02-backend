package com.bside.config

import com.bside.config.jwt.JwtAccessDeniedHandler
import com.bside.config.jwt.JwtAuthenticationEntryPoint
import com.bside.config.jwt.TokenProvider
import com.bside.common.type.Authority
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
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
    val jwtAccessDeniedHandler: JwtAccessDeniedHandler
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
        web!!.ignoring()
            .antMatchers("/resource")
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
            .antMatchers("/admin/**").hasAnyAuthority(Authority.ROLE_ADMIN.name)
            .anyRequest().authenticated()   // 나머지 API 는 전부 인증 필요
            //JwtConfig 등록
            .and()
            .apply(JwtConfig(tokenProvider))
    }
}