package com.moveltrack.reactbackend.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.moveltrack.reactbackend.security.token.CustomTokenEnhancer;

@Configuration
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	private static final String GRANT_TYPE_PASSWORD = "password";
	private static final String REFRESH_TOKEN = "refresh_token";
	private static final String SCOPE_READ = "read";
	private static final String SCOPE_WRITE = "write";
	
	@Value("${autorizacao.client-secret}") 
	private String clientSecret;
	
	@Value("${autorizacao.client-id}") 
	private String clientId;
	
	@Value("${autorizacao.access-token-validity-seconds}") 
	private int tokenValiditySeconds; 
	
	@Value("${autorizacao.refresh-token-validity-seconds}") 
	private int refreshTokenValiditySeconds;


	private AuthenticationManager authenticationManager;
	private UserService userDetailsService;

	@Autowired
	public AuthorizationServerConfig(AuthenticationManager authenticationManager, UserService userDetailsService) {
		this.authenticationManager = authenticationManager;
		this.userDetailsService = userDetailsService;
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
		configurer
				.inMemory()
				.withClient(clientId)
				.secret(clientSecret) 
				.authorizedGrantTypes(GRANT_TYPE_PASSWORD, REFRESH_TOKEN)
				.scopes(SCOPE_READ, SCOPE_WRITE)
				.accessTokenValiditySeconds(tokenValiditySeconds)
				.refreshTokenValiditySeconds(refreshTokenValiditySeconds);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));

		endpoints
				.tokenStore(tokenStore())
				.tokenEnhancer(tokenEnhancerChain)
				.accessTokenConverter(accessTokenConverter()) //?
				.reuseRefreshTokens(false)
				.userDetailsService(userDetailsService)
				.authenticationManager(authenticationManager);
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
		accessTokenConverter.setSigningKey("spia00");

		return accessTokenConverter;
	}

	@Bean
	public TokenEnhancer tokenEnhancer() {
		return new CustomTokenEnhancer();
	}

	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}
}