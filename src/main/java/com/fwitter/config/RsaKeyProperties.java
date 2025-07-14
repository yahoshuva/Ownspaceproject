package com.fwitter.config;




import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="rsa")
public class RsaKeyProperties {
	
	private RSAPublicKey publickey;
	private RSAPrivateKey privateKey;
	
	public RsaKeyProperties() {
		
	}

	public RsaKeyProperties(RSAPublicKey publickey, RSAPrivateKey privateKey) {
		this.publickey = publickey;
		this.privateKey = privateKey;
	}

	public RSAPublicKey getPublickey() {
		return publickey;
	}

	public void setPublickey(RSAPublicKey publickey) {
		this.publickey = publickey;
	}

	public RSAPrivateKey getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(RSAPrivateKey privateKey) {
		this.privateKey = privateKey;
	}
	

}
