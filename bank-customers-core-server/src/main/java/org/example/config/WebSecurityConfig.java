package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {


  @Bean
  public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
    UserDetails admin = User.withUsername("test")
      .password(passwordEncoder.encode("password123"))
      .roles("ADMIN_ROLE")
      .build();

    return new InMemoryUserDetailsManager(admin);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(authorize -> authorize
        // Regular endpoints (protected by JWT)
        .requestMatchers(
          "/api/**",
          "/jwk/**",
          "/swagger-ui.html",
          "/swagger-resources/**",
          "/v3/api-docs/**",
          "/management/health/**")
        .permitAll()
        // Other management-endponts require management-role
        .requestMatchers("/management/**")
        .hasRole("ADMIN_ROLE")
      )
      .httpBasic()
      .and()
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
      .csrf().disable() //NOSONAR
      .formLogin().disable();

    return http.build();
  }

}

