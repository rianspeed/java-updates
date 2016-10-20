package com.spring.employee.app.controller;

import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.spring.employee.app.view.EmployeeView;

@Controller
@RequestMapping("/emp")
public class EmployeeController {
	
	@RequestMapping(method=RequestMethod.GET)
	public String index(HttpServletRequest request, 
	        HttpServletResponse response) {
		
		return "index";
	}

	@RequestMapping(value="/post", method=RequestMethod.POST, consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public String postData(@ModelAttribute EmployeeView employeeView, HttpServletRequest request, 
	        HttpServletResponse response) {
		
		System.out.println(employeeView == null);
		System.out.println(employeeView.getName());
		System.out.println(employeeView.getFile().getName());
		return "redirect:../emp";
	}

}
