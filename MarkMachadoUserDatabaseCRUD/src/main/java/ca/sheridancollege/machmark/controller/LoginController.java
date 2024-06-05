package ca.sheridancollege.machmark.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sheridancollege.machmark.beans.User;
import ca.sheridancollege.machmark.database.DatabaseAccess;


@Controller
public class LoginController {

	// Database access object
	@Autowired
	@Lazy
	private DatabaseAccess da;

	//If need to pull from rest not from database
	// final String REST_URL = "http://localhost:8080/api/v1/testObject";
	
	//This is the principal object that holds the authenticated user
	private Principal principal;
	
	//This lets every class access the authenticated user
	@ModelAttribute
	public void setPrincipal(Principal principal) {
		this.principal = principal;
	}
	
	// This will retrieve the login page when entering the website
	@GetMapping("/login")
	public String login() {
		return "login";
	}

	// This will redirect to a permission-denied when a user attempts to reach a
	// page they are not allowed
	@GetMapping("/permission-denied")
	public String permissionDenied() {
		return "/error/permission-denied";
	}

	@GetMapping("/")
	// This is the welcome page that shows when the user logs in
	public String index(Model model) {
		if (principal != null) {
			String email = principal.getName(); // Get logged-in user's email
			User user = da.findUserAccount(email); //This stores the authenticated user in a object called user

			if (user != null) {
				//This adds the users information to the model
				model.addAttribute("firstName", user.getFirstName());
				model.addAttribute("lastName", user.getLastName());
				model.addAttribute("email", user.getEmail());
				//This gets the users role to present
				List<String> roleList = da.getRolesById(user.getUserId());
				model.addAttribute("roleList", roleList);

			}
		}
		return "index";
	}
	@GetMapping("/register")
	// This will return the register page when pressing register on login
	public String getRegister() {
		return "register";
	}

	@PostMapping("/register")
	// This gathers all the parameters in the register page and calls the database
	// add user method
	//The user will also choose whether it wants to be a user or admin
	public String postRegister(@RequestParam String username, @RequestParam String password,
			@RequestParam String firstName, @RequestParam String lastName, @RequestParam String role) {
		da.addUser(username, password, firstName, lastName);
		// This grabs the id at the userid index
		Long userId = da.findUserAccount(username).getUserId();
		// This adds the USER role if they chose user
		if (role.equals("USER"))
		da.addRole(userId, Long.valueOf(2));
		// This adds the admin role if they choose admin
		if (role.equals("ADMIN"))
		da.addRole(userId, Long.valueOf(1));
		return "login";
	}

}
