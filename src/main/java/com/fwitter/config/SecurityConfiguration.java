package com.fwitter.config;



import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
@EnableWebSecurity
@EnableWebMvc
public class SecurityConfiguration {
	
	private final RsaKeyProperties keys;
	
	//newly adding
	
//	
	
	@Autowired
	public SecurityConfiguration(RsaKeyProperties keys) {
		this.keys = keys;
		System.out.println("SecurityConfig initialized with keys: " + keys);

	}
	



	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
	    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
	    converter.setJwtGrantedAuthoritiesConverter(jwt -> {
	        // Extract roles/authorities from the JWT
	        Collection<String> authorities = jwt.getClaimAsStringList("scope");
	        return authorities.stream()
	                .map(SimpleGrantedAuthority::new)
	                .collect(Collectors.toList());
	    });
	    return converter;
	}
	
	
	@Bean
	public AuthenticationManager authManager(UserDetailsService detailsService) {
		
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
	    provider.setUserDetailsService(detailsService);
	    provider.setPasswordEncoder(passwordEncoder());
	    return new ProviderManager(provider);
	
	}

	
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration configuration = new CorsConfiguration();
	    configuration.addAllowedOrigin("http://localhost:3000");
	    configuration.addAllowedHeader("*");
	    configuration.addAllowedMethod("*");
	    configuration.setAllowCredentials(true);
	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", configuration);
	    return source;
	}


	
//	@Bean
//public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//    return http
//            .cors()  // Enable CORS handling explicitly
//            .and()
//            .csrf(csrf -> csrf.disable())
//            .authorizeRequests(auth -> auth
//                    .requestMatchers("/auth/**").permitAll()
//                    .requestMatchers("/images/**").permitAll()
//                    .requestMatchers("user/followers/**").permitAll()
//                    .requestMatchers("/users/following/**").permitAll()
//                    .requestMatchers("/api/**").permitAll()
//                    .requestMatchers("/posts/").permitAll()
//					.requestMatchers("/ws/**").permitAll() // Allow only authenticated users
//
//
//
//
//                    .requestMatchers("/posts/id/**").permitAll()
//                    .requestMatchers("/discovery/**").permitAll()
//                    .anyRequest().authenticated())
//            .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
//            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//            .build();
//}
//

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
				.csrf(csrf ->csrf.disable() )
				.cors(cors->cors.disable())
				.cors().configurationSource(corsConfigurationSource()).and()
				.headers(headers->headers.frameOptions().sameOrigin())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/auth/**").permitAll()
						.requestMatchers("/images/**").permitAll()
						.requestMatchers("/user/followers/**").permitAll()
						.requestMatchers("/users/following/**").permitAll()
						.requestMatchers("/api/**").permitAll()
						.requestMatchers("/posts/").permitAll()
						.requestMatchers("/categories").permitAll()
						.requestMatchers("/posts/id/**").permitAll()
						.requestMatchers("/discovery/**").permitAll()
						.requestMatchers("/users/**").permitAll()
						.requestMatchers("/ws/**").permitAll()
						.anyRequest().authenticated())
				.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.build();
	}


    @Bean
    JwtDecoder jwtDecoder() {
		
		return NimbusJwtDecoder.withPublicKey(keys.getPublickey()).build();
    }
		
		  @Bean
		    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		        return config.getAuthenticationManager();
		    }
		
	

    @Bean
    JwtEncoder jwtEncoder() {
	    System.out.println("Creating JwtEncoder bean");
	    JWK jwk = new RSAKey.Builder(keys.getPublickey())
	            .privateKey(keys.getPrivateKey())
	            .build();
	    JWKSource<SecurityContext> jwks = new ImmutableJWKSet<SecurityContext>(new JWKSet(jwk));
	    return new NimbusJwtEncoder(jwks);
	}


}
	
