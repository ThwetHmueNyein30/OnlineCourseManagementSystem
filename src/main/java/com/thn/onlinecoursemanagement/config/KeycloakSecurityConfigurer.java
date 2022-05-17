//package com.thn.onlinecoursemanagement.config;
//
//import org.keycloak.adapters.KeycloakConfigResolver;
//import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
//import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
//import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
//import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
//import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
//import org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
//import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
//import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
//import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import java.util.ArrayList;
//import java.util.List;
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true)
//@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
//public class KeycloakSecurityConfigurer extends KeycloakWebSecurityConfigurerAdapter {
//    @Bean
//    public GrantedAuthoritiesMapper grantedAuthoritiesMapper() {
//        SimpleAuthorityMapper mapper = new SimpleAuthorityMapper();
//        mapper.setConvertToUpperCase(true);
//        return mapper;
//    }
//
//    @Override
//    protected KeycloakAuthenticationProvider keycloakAuthenticationProvider() {
//        final KeycloakAuthenticationProvider provider = super.keycloakAuthenticationProvider();
//        provider.setGrantedAuthoritiesMapper(grantedAuthoritiesMapper());
//        return provider;
//    }
//
//    @Override
//    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(keycloakAuthenticationProvider());
//    }
//
//
//
//    @Override
//    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
//        return new NullAuthenticatedSessionStrategy();
//    }
//
//
//    @Bean
//    KeycloakConfigResolver keycloakConfigResolver() {
//        return new KeycloakSpringBootConfigResolver();
//    }
//
//
//    @Override
//    protected void configure(final HttpSecurity http) throws Exception {
//        super.configure(http);
//        http.csrf().disable().cors().and()
//                .authorizeRequests()
//                .anyRequest()
//                .permitAll();
//    }
////
////    @Bean
////    public FilterRegistrationBean keycloakAuthenticationProcessingFilterRegistrationBean(
////            final KeycloakAuthenticationProcessingFilter filter) {
////        final FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
////        registrationBean.setEnabled(false);
////        return registrationBean;
////    }
////
////    @Bean
////    public FilterRegistrationBean keycloakPreAuthActionsFilterRegistrationBean(
////            final KeycloakPreAuthActionsFilter filter) {
////        final FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
////        registrationBean.setEnabled(false);
////        return registrationBean;
////    }
//
////    @Bean
////    public CorsConfigurationSource corsConfigurationSource() {
////        final CorsConfiguration configuration = new CorsConfiguration();
////        configuration.setAllowCredentials(true);
////        List<String> allowed = new ArrayList<>();
////        allowed.add("*");
////        configuration.setAllowedOrigins(allowed);
////        configuration.setAllowedMethods(allowed);
////        List<String> headers = new ArrayList<>();
////        headers.add("Authorization");
////        headers.add("Cache-Control");
////        headers.add("Content-Type");
////        configuration.setAllowedHeaders(headers);
////        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
////        source.registerCorsConfiguration("/**", configuration);
////        return source;
////    }
//}
