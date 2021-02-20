package com.eareiza.springAngular.auth;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
//habilitar un servidor de autorización (es decir, an AuthorizationEndpointy a TokenEndpoint) en el contexto de la aplicación actual,
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter{

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private InfoAdicionalToken infoAdicionalToken;


	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		//Genera el token
		security.tokenKeyAccess("permitAll()")
		//verifica el token y su firma
		.checkTokenAccess("isAuthenticated()");
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		//Se almacena en memoria el cliente con su clave
		clients.inMemory().withClient("angularapp")
		.secret(passwordEncoder.encode("12345"))
		//Se configura alcance del cliente para leectura y escritura
		.scopes("read","write")
		//Se configura el tipo de consesion del token, como se obtiene el toke --> password
		.authorizedGrantTypes("password", "refresh_token")
		//Configurar en cuanto tiempo caduca el token
		.accessTokenValiditySeconds(43200) //12 horas
		//Tiempo de expiracion del refreshToken
		.refreshTokenValiditySeconds(2592000); //30 dias
	}
	

	//Configuracion del endpoint del authorizationServer
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(infoAdicionalToken,accessTokenConverter()));
		
		//Se registra el authenticationManager
		endpoints.authenticationManager(authenticationManager)
		.tokenStore(tokenStore())
		//almacena los datos de authenticacion del token
		.accessTokenConverter(accessTokenConverter())
		.tokenEnhancer(tokenEnhancerChain);
		
	}
	
	@Bean
	public  JwtTokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
		//Se firma el token con la privada
		jwtAccessTokenConverter.setSigningKey(JwtConfig.RSA_PRIVADA);
		//Se verifica utentisidad del token
		jwtAccessTokenConverter.setVerifierKey(JwtConfig.RSA_PUBLICA);
		return jwtAccessTokenConverter;
	}
}
