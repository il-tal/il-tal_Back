package com.example.sherlockescape.config;

import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.request.ReviewRequestDto;
import com.example.sherlockescape.dto.response.MyReviewResponseDto;
import com.example.sherlockescape.dto.response.ReviewResponseDto;
import com.example.sherlockescape.security.jwt.AuthenticationEntryPointException;
import com.example.sherlockescape.security.jwt.JwtAuthFilter;
import com.example.sherlockescape.security.jwt.JwtUtil;
import com.example.sherlockescape.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.cors.CorsConfiguration;

import javax.validation.Valid;
import java.util.List;

@Configuration
@EnableWebSecurity //시큐리티 활성화
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final AuthenticationEntryPointException authenticationEntryPointException;

    //password를 암호화 하지않으면 spring security가 접근을 허가하지 않는다.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        //h2-console 사용에 대한 허용 (CSRF, FrameOptions 무시)
        return (web) -> web.ignoring()
                .antMatchers("/h2-console/**");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.cors().configurationSource(request -> {
            var cors = new CorsConfiguration();
            cors.setAllowedOriginPatterns(List.of("*"));
            cors.setAllowedMethods(List.of("*"));
            cors.setAllowedHeaders(List.of("*"));
            cors.addExposedHeader("Access_Token");
            cors.addExposedHeader("Refresh_Token");
            cors.setAllowCredentials(true);
            return cors;
        });

        http.csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPointException);

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                //MemberController
                .antMatchers(HttpMethod.POST,"/signup").permitAll()
                .antMatchers(HttpMethod.POST,"/login").permitAll()
                .antMatchers(HttpMethod.PUT, "/nickname").authenticated()

                //BadgeController
                .antMatchers(HttpMethod.POST, "/badge/give").authenticated()

                //MemberBadgeController
                .antMatchers(HttpMethod.PUT, "/badge/{badgeId}").authenticated()
                .antMatchers(HttpMethod.GET, "/main/achieve").authenticated()

                //MyPageController
                .antMatchers(HttpMethod.GET, "/myreviews").authenticated()
                .antMatchers(HttpMethod.GET, "/mythemes").authenticated()
                .antMatchers(HttpMethod.GET, "/mycompanies").authenticated()
                .antMatchers(HttpMethod.POST, "/tendency").authenticated()
                .antMatchers(HttpMethod.PUT, "/tendency").authenticated()
                .antMatchers(HttpMethod.GET, "/mypage").authenticated()

                //ReviewController
                .antMatchers(HttpMethod.POST, "/theme/{themeId}/review").authenticated()
                .antMatchers(HttpMethod.PUT, "/theme/review/{reviewId}").authenticated()
                .antMatchers(HttpMethod.DELETE, "/theme/review/{reviewId}").authenticated()
                .anyRequest().permitAll()
                .and().addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }
}