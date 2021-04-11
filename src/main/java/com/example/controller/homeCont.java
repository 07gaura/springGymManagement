package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.entity.User;
import com.example.repository.AdminRepository;

@Controller
public class homeCont {
	
	@Autowired()
	private AdminRepository adminrepo;
	
	@Autowired()
	private BCryptPasswordEncoder bcrypt;
	
	@GetMapping("/")
	public String home(Model m) {
		m.addAttribute("userval", new User());
		return "index";
	}
	
	@PostMapping("/saveusers")
	public String saveuser(@ModelAttribute("userval") User user) {
		user.setRole("ROLE_USER");
		user.setEnable(true);
		user.setPassword(bcrypt.encode(user.getPassword()));
		User result = this.adminrepo.save(user);
		return "index";
	}
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
}
