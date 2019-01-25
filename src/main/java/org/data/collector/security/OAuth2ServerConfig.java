package org.data.collector.security;

import java.util.Map;
import java.util.Map.Entry;

import org.data.collector.PropertiesConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

@Configuration
@EnableAuthorizationServer
//提供/oauth/authorize,/oauth/token,/oauth/check_token,/oauth/confirm_access,/oauth/error
public class OAuth2ServerConfig extends AuthorizationServerConfigurerAdapter {
	@Autowired
	PasswordEncoder bCryptPasswordEncoder;//定义在SecurityConfig 中@Bean public PasswordEncoder passwordEncoder()
	
	@Autowired
	PropertiesConfig propConfig;
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		oauthServer.tokenKeyAccess("permitAll()")
		.checkTokenAccess("isAuthenticated()") //allow check token
		.allowFormAuthenticationForClients();
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		InMemoryClientDetailsServiceBuilder mem = clients.inMemory();
		
		//init a useless for use and method.
		ClientDetailsServiceBuilder<InMemoryClientDetailsServiceBuilder>.ClientBuilder clientBuilder = mem.withClient("nonsense")
		.secret(bCryptPasswordEncoder.encode("xxxxxxxxxxxxxxxxxxxxxx"))
		.authorizedGrantTypes("password", "refresh_token")
		.scopes("all")
		.resourceIds("oauth2-resource");

		
		Map<String, String> users = propConfig.getClientsMap();
		for(Entry<String, String> user: users.entrySet()){
			clientBuilder.and()
			.withClient(user.getKey())
			.secret(bCryptPasswordEncoder.encode(user.getValue()))
			.authorizedGrantTypes("client_credentials", "refresh_token")
			.scopes("all")
			.resourceIds("oauth2-resource");
		}		
		clientBuilder.accessTokenValiditySeconds(1200)
			.refreshTokenValiditySeconds(5000)
		;
	}
}
