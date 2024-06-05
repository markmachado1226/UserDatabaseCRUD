package ca.sheridancollege.machmark.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.sheridancollege.machmark.beans.TestObject;
import ca.sheridancollege.machmark.database.DatabaseAccess;

//This is the API controller
@RestController
@RequestMapping("/api/v1/testObject")
public class RestTestObjectController {

	//This is the database object to access the database
	@Autowired
	@Lazy
	private DatabaseAccess da;

	//This returns all POLLS
	@GetMapping
	public List<TestObject> getCollection() {
		return da.getList();
	}

	//This gets an individual object using an id number
	@GetMapping(value = "/{id}") // "value" only here to illustrate our Mappings can do more!
	public TestObject getIndividual(@PathVariable Long id) {
		return  da.getObjectById(id);
	}

	//This adds an object to the collection
	@PostMapping(consumes = "application/json")
	public String post(@RequestBody TestObject testObject) {
		return "http://localhost:8080/api/v1/testObject/" + da.save(testObject);
	}

	//This deletes the current collection and adds a whole new collection
	@PutMapping(consumes = "application/json")
	public String putCollection(@RequestBody List<TestObject> testObjectList) {
		da.deleteAll();
		da.saveAll(testObjectList);
		return "Total Records: " + da.count();
	}

	//This UPDATES an object if specified an id
	@PutMapping(value = "/{id}", consumes = "application/json")
	public String updateById(@PathVariable Long id, @RequestBody TestObject testObject) {
		int rowsAffected = da.updateById(id, testObject);
		if (rowsAffected > 0) {
			return "updated";
		} else {
			return "not found or no changes made";
		}
	}

	//If sending a delete request with no parameters, it will delete all
	@DeleteMapping
	public String deleteAll() {
		da.deleteAll();
		return "All deleted.";
	}

	//Deletes object by id
	@DeleteMapping(value = "/{id}")
	public String deleteById(@PathVariable Long id) {
		TestObject PollToDelete =  da.getObjectById(id);
		if (PollToDelete != null) {
			da.deleteByID(id);
			return "TestObject with ID " + id + " deleted.";
		} else {
			return "TestObject with ID " + id + " not found.";
		}
	}
}