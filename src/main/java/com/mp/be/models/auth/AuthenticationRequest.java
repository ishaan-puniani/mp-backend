/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.models.auth;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Auth_LoginRequest", description = "Authentication Login Credentials Request Model")
public class AuthenticationRequest {
    public String email;
    public String password;
    
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
    
	
}
