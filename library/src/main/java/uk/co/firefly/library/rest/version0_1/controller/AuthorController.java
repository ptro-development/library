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
import uk.co.firefly.library.rest.version0_1.exception.ResourceNotFoundException;
import uk.co.firefly.library.rest.version0_1.model.Author;
import uk.co.firefly.library.rest.version0_1.repository.AuthorRepository;
import uk.co.firefly.library.rest.version0_1.service.AuthorService;

@RestController
@RequestMapping("/library")
@Api(value = "homelibrary")
public class AuthorController {

	@Autowired
	private AuthorService service;

	@ApiOperation(value = "View a list of available authors")
	@GetMapping(value = "/authors", produces = "application/json")
	ResponseEntity<List<Author>> all() {
		return new ResponseEntity(service.getAll(), HttpStatus.OK);
	}

	@ApiOperation(value = "View an author with an ID")
	@GetMapping(value = "/authors/{id}", produces = "application/json")
	Author get(@PathVariable Long id) {
		return service.get(id);
	}

	@ApiOperation(value = "Add a new author")
	@PostMapping(value = "/authors")
	ResponseEntity<Author> add(@RequestBody @Valid Author newAuthor) {
		return new ResponseEntity(service.save(newAuthor), HttpStatus.CREATED);
	}

	@ApiOperation(value = "Modify an existing author with an ID")
	@PutMapping("/authors/{id}")
	Author put(@RequestBody @Valid Author newAuthor, @PathVariable Long id) {
		return service.put(id, newAuthor);
	}

	@ApiOperation(value = "Delete an existing author with an ID")
	@DeleteMapping(value = "/authors/{id}", produces = "application/json")
	ResponseEntity<String> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("{}");
	}
}