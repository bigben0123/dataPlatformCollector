package org.data.collector.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Created by wangyunfei on 2017/6/9.
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private DomainUserDetailsService userDetailsService;
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .csrf().disable()
        .anonymous().disable()        
            .authorizeRequests().antMatchers("/test/**").permitAll()
            .anyRequest().authenticated()            
        ;
//                .authenticated();.antMatcher("/api-docs/**")
//        .access("#oauth2.hasScope('select') and hasRole('ROLE_USER')")  
       
        
//        http.authorizeRequests()
//      .antMatchers("/product/**").access("#oauth2.hasScope('select') and hasRole('ROLE_USER')")
//      .antMatchers("/order/**").authenticated();//

/*        
        http
        .authorizeRequests()            1                                                   
              .antMatchers( "/resources/**", "/signup" , "/about").permitAll()  2
              .antMatchers( "/admin/**").hasRole("ADMIN" )                    3    
              .antMatchers( "/db/**").access("hasRole('ADMIN') and hasRole('DBA')")  4
              .anyRequest().authenticated()        5
                               
              .and()
         // ...
        .formLogin();
        
   */     
        /*     
 * 
 *    
 *    
 *    
        http
        .authorizeRequests()
        .antMatchers(permitURI).permitAll()
        .anyRequest().authenticated()
        .and()
        .addFilterAt(new JSONAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        .formLogin().loginProcessingUrl("/login")
        .successHandler(authSuccess).failureHandler(authFailure).permitAll()
        .and()
        .exceptionHandling().authenticationEntryPoint(authEntry)
        .and()
        .rememberMe().rememberMeCookieName("AUTOLOGIN")
        .and()
        .cors()
        .and()
        .logout().logoutUrl("/logout").permitAll()
        .clearAuthentication(true)
        .invalidateHttpSession(true)
        .deleteCookies("JSESSIONID", "AUTOLOGIN")
        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
        .permitAll();
 
http.csrf().disable();
*/

    }

    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }
     
    @Bean
    public DomainUserDetailsService userDetailsService(){
        return new DomainUserDetailsService();
    }
 
    @Bean
    public PasswordEncoder passwordEncoder() {
    	System.out.println("============================passwordEncoder  ");
//    	return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return new BCryptPasswordEncoder();
    }
 
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	System.out.println("============================configure  ");
		auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }
 
    //不定义没有password grant_type
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
 
    class DomainUserDetailsService implements UserDetailsService {
        @Autowired
        PasswordEncoder bCryptPasswordEncoder;

		@Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        	System.out.println("============================loadUserByUsername  "+username);
             return new User("app",bCryptPasswordEncoder.encode("app"),null);
        }
    }


}

