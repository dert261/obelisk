package ru.obelisk.monitor.web.security;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

//@Service
public class PasswordCrypto {
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	private static PasswordCrypto instance;

	public static PasswordCrypto getInstance() {
		if(instance == null) {
			instance = new PasswordCrypto();
	    }
		return instance;
	}

	public String encrypt(String str) {
		return passwordEncoder.encode(str);
	}
}
