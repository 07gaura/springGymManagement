package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.entity.User;
import com.example.repository.AdminRepository;

public class userDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	private AdminRepository adminRepo;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = adminRepo.getUserByUserName(username);
		if(user==null) {
			throw new UsernameNotFoundException("could not found user");
		}
		
		customUserDetails obj = new customUserDetails(user);
		return obj;
	}

}
