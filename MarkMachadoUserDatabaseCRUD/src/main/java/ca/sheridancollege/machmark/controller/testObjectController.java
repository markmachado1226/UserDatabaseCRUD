package ca.sheridancollege.machmark.controller;

import java.security.Principal;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import ca.sheridancollege.machmark.beans.TestObject;
import ca.sheridancollege.machmark.database.DatabaseAccess;

@Controller
public class testObjectController {

	// This keeps it Thread safe
	List<TestObject> testObjectList = new CopyOnWriteArrayList<>();

	//If need to pull from rest not from database
	final String REST_URL = "http://localhost:8080/api/v1/testObject";
	
	// Database access object
	@Autowired
	@Lazy
	private DatabaseAccess da;

	// When logging in this will show the secured index page
	@GetMapping("/secure")
	public String secureIndex(Model model, Principal principal, RestTemplate restTemplate) {
		
		//This is used to pull the list from REST instead of database
		ResponseEntity<TestObject[]> responseEntity = restTemplate.getForEntity
				(REST_URL, TestObject[].class);
				model.addAttribute("testObjectList", responseEntity.getBody());
		
		// Adding the testObject and testObject list so it can display all the polls ALREADY
		// recorded
		model.addAttribute("testObject", new TestObject());
		//model.addAttribute("testObjectList", da.getList());
		return "/secure/secureIndex";
	}

	@GetMapping("/secure/pickTestObject")
	// This directs to the pick testObject page in the secure folder
	public String pickTestObject(Model model) {
		model.addAttribute("testObject", new TestObject());
		model.addAttribute("testObjectList", da.getList());
		return "/secure/pickTestObject";
	}

	@GetMapping("/secure/addTestObject")
	// This directs to the add testObject page in the secure folder that only admins can
	// access
	public String addTestObject(Model model) {
		model.addAttribute("testObject", new TestObject());
		model.addAttribute("testObjectList", da.getList());
		return "/secure/addTestObject";
	}

	@GetMapping("/secure/deleteTestObject")
	// This directs to the delete testObject page in the secure folder that only admins
	// can access
	public String deleteTestObject(Model model) {
		model.addAttribute("testObject", new TestObject());
		model.addAttribute("testObjectList", da.getList());
		return "/secure/deleteTestObject";
	}

	@PostMapping("/secure/deleteTestObjectMethod")
	// This directs to the delete testObject page in the secure folder that only admins
	// can access
	public String deleteTestObjectMethod(Model model, @ModelAttribute TestObject testObject) {
		da.deleteByObject(testObject);
		return "redirect:/secure";
	}

	@GetMapping("/secure/updateObject")
	public String updateObjectPage(Model model, TestObject testObject) {
		// This directs to the choose testObject page in the secure folder, this uses the
		// object that was chosen in pick testObject page
		model.addAttribute("selectedObject", da.getObject(testObject).get(0));
		return "/secure/updateObject";
	}
	
	@PostMapping("/secure/updateObject/{id}")
	public String updateObject(Model model, @ModelAttribute TestObject updatedObject, @PathVariable Long id) {
		// This directs to the choose testObject page in the secure folder, this uses the
		// object that was chosen in pick testObject page
		da.updateById(id, updatedObject);
		return "redirect:/secure";
	}


	@PostMapping("/secure/addTestObjectMethod")
	// This post mapping method adds a testObject to the list and redirects to the secure
	// index
	public String addTestObjectMethod(Model model, @ModelAttribute TestObject testObject) {
		da.add(testObject);
		model.addAttribute("testObject", new TestObject());
		model.addAttribute("testObjectList", da.getList());
		return "redirect:/secure";
	}

}