package com.example.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.example.entity.User;
import com.example.entity.Users;
import com.example.repository.userRepository;

@Controller
@RequestMapping(value={"/user"})
public class HomeController {
	
	@Autowired
	private userRepository userrepo;
	
	@GetMapping("/")
	public String home(Model m) {
		
		List<Users> user = this.userrepo.findTop(10);
		m.addAttribute("title", "Gym Management");		
		m.addAttribute("userarr", user);
		m.addAttribute("userval", new Users());
		return "user/index";
	}
	
	@PostMapping("/saveusers")
	public String saveusers(@ModelAttribute("userval") Users user, Model m) {
		user.setDates(new Date());		
		Users result = this.userrepo.save(user);
		System.out.println(result);
		m.addAttribute("title", "Gym Management");	
		return "user/index";
	}
	
	@GetMapping("/renewusers")
	public String users(Model m) throws ParseException {
		m.addAttribute("title", "Gym Management");	
		List<Users> user = (List<Users>) this.userrepo.findAll();
		List<Users> user1 = new ArrayList<Users>();
		
		SimpleDateFormat sdfo = new SimpleDateFormat("yyy-MM-dd");
		for(Users e : user) {
			
			Date date2 = sdfo.parse(e.getCdate());
			
			if(date2.before(new Date())){
				Date dateAfter = new Date();
				// calculate difference in millis
				long millis = Math.abs(dateAfter.getTime() - date2.getTime());

				// convert milliseconds to days
				long days = TimeUnit.DAYS.convert(millis, TimeUnit.MILLISECONDS);
				e.setDays(days);
				user1.add(e);
			}
			
		}
		
		m.addAttribute("userloop", user1);
		return "user/renewusers";
	}
	
	@GetMapping("/currentusers")
	public String currentusers(Model m) throws ParseException{
		m.addAttribute("title", "Gym Management");	
		List<Users> user = (List<Users>) this.userrepo.findAll();
		List<Users> user1 = new ArrayList<Users>();
		
		SimpleDateFormat sdfo = new SimpleDateFormat("yyy-MM-dd");
		for(Users e : user) {
			
			Date date2 = sdfo.parse(e.getCdate());
			
			if(date2.after(new Date())){
				Date dateAfter = new Date();
				// calculate difference in millis
				long millis = Math.abs( date2.getTime() - dateAfter.getTime());

				// convert milliseconds to days
				long days = TimeUnit.DAYS.convert(millis, TimeUnit.MILLISECONDS);
				e.setDays(days);
				user1.add(e);
			}
			
		}
		
		m.addAttribute("userloop", user1);
		return "user/currentuser";
	}
	
	@GetMapping("/update/{id}")
	public String update(@PathVariable("id") int id, Model m) {		
		m.addAttribute("title", "Gym Management");	
		Users user = this.userrepo.findById(id);		
		m.addAttribute("userval", user);
		return "user/update";
	}
	
	@PostMapping("/update/updateusers")
	public String updateuser(@ModelAttribute("userval") Users user) {
		Users result = this.userrepo.save(user);
		return "user/update";
	}
	
	@GetMapping("/search")
	public String searchuser(@RequestParam int id, Model m) {
		m.addAttribute("title", "Gym Management");	
		Users user = this.userrepo.findById(id);
		m.addAttribute("userloop", user);
		return "user/search";
	}
	
	//downloading csv method
	@GetMapping("/downloadcsv")
	public String dowloadCsv(HttpServletResponse response) throws IOException{
		
		try {
			response.setContentType("text/csv");
			response.setHeader("Content-Disposition", "attachment; filename=users.csv");
			ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
			String[] headings = {"Name", "Email", "Mobile no", "Date"};
			String[] propertyName = {"name", "email", "phoneno", "cdate"};
			
			csvWriter.writeHeader(headings);
			List<Users>user = (List<Users>) this.userrepo.findAll();
			if(null!=user && !user.isEmpty()) {
				for(Users e:user) {
					csvWriter.write(e, propertyName);
				}
			}
			csvWriter.close();
		}		
		catch(Exception e){
			e.printStackTrace();
		}
		
		return "user/download";
	}
	
}
