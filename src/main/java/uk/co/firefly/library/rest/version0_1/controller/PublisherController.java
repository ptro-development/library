package uk.co.firefly.library.rest.version0_1.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import uk.co.firefly.library.rest.version0_1.model.Publisher;
import uk.co.firefly.library.rest.version0_1.service.PublisherService;

@RestController
@RequestMapping("/library")
@Api(value = "homelibrary")
public class PublisherController {

	@Autowired
	private PublisherService service;

	@ApiOperation(value = "View a list of available publishers")
	@GetMapping(value = "/publishers", produces = "application/json")
	ResponseEntity<List<Publisher>> all() {
		return new ResponseEntity(service.getAll(), HttpStatus.OK);
	}

	@ApiOperation(value = "View an publisher with an ID")
	@GetMapping(value = "/publishers/{id}", produces = "application/json")
	Publisher get(@PathVariable Long id) {
		return service.get(id);
	}

	@ApiOperation(value = "Add a new publisher")
	@PostMapping(value = "/publishers")
	ResponseEntity<Publisher> add(@RequestBody @Valid Publisher newPublisher) {
		return new ResponseEntity(service.add(newPublisher), HttpStatus.CREATED);
	}

	@ApiOperation(value = "Modify an existing publisher with an ID")
	@PutMapping("/publishers/{id}")
	Publisher put(@RequestBody @Valid Publisher newPublisher, @PathVariable Long id) {
		return service.put(newPublisher, id);
	}

	@ApiOperation(value = "Delete an existing publisher with an ID")
	@DeleteMapping(value = "/publishers/{id}", produces = "application/json")
	ResponseEntity<String> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("{}");
	}
}
