package com.lms.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    
    @NotBlank
    @JsonAlias({"username", "email", "usernameOrEmail"})
    private String usernameOrEmail;
    
    @NotBlank
    private String password;
    
    public LoginRequest() {}
    
    public LoginRequest(String usernameOrEmail, String password) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
    }
    
    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }
    
    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String string) {
		// TODO Auto-generated method stub
		
	}
}

