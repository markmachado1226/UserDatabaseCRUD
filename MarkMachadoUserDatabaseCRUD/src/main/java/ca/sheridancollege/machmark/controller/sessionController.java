package ca.sheridancollege.machmark.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class sessionController {

	//This controller is to test Sessions
	@GetMapping("/test")
	public String test(Model model, HttpSession session) {
		session.setAttribute("sessionID", session.getId());
		if (session.isNew())
			session.setAttribute("myTest", "New Session");
		else
			session.setAttribute("myTest", "Welcome Back");
		return "test";
	}
}
