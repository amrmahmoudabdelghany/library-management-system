package com.bookmanagement.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.AntPathMatcher;

import com.bookmanagement.demo.models.AppUserRole;
import com.bookmanagement.demo.repo.IAppUserRepo;
import com.bookmanagement.demo.security.AppUserDetails;
import com.bookmanagement.demo.security.JwtAuthFilter;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
	
	@Bean
	UserDetailsService userDetailsService(IAppUserRepo repo) { 
		return userName ->repo.findByEmail(userName).map(AppUserDetails::new)
				.orElseThrow(()->new UsernameNotFoundException("User " + userName + "not found" )); 
	} 
	

	@Bean
	AuthenticationProvider authenticationProvider(UserDetailsService uds) { 
	  DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider() ; 
	   daoProvider.setUserDetailsService(uds);
	   daoProvider.setPasswordEncoder(passwordEncoder());
	  return daoProvider; 
	}
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception { 
		return authConfig.getAuthenticationManager() ; 
	}
	
	@Bean
	PasswordEncoder passwordEncoder() { 
		return new BCryptPasswordEncoder() ; 
	}
	
	
	   @Bean
	    SecurityFilterChain securityFilterChain(HttpSecurity http ,
	    		AuthenticationProvider authProvider , JwtAuthFilter jwtAuthFilter) throws Exception { 
		   
		   
           http.csrf(csrf -> csrf
                   .disable())
//           		   .authorizeHttpRequests(requests -> requests
//                		   
//           				   .requestMatchers(new AntPathRequestMatcher("/api/auth", "post"))  
//           				   
//                		  
//                		   //.hasAnyAuthority(AppUserRole.ROLE_ADMIN.name())
//                           )
                   // stateless auth 
                   .sessionManagement(management -> management
                           .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                   .authenticationProvider(authProvider)
                   .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) ;

		   
	        return http.build();
	    }
}
