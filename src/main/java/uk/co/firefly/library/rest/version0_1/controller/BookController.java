package uk.co.firefly.library.rest.version0_1.controller;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import uk.co.firefly.library.rest.version0_1.model.Author;
import uk.co.firefly.library.rest.version0_1.model.Book;
import uk.co.firefly.library.rest.version0_1.model.Picture;
import uk.co.firefly.library.rest.version0_1.model.Publisher;
import uk.co.firefly.library.rest.version0_1.service.BookService;

@RestController
@RequestMapping("/library")
@Api(value = "homelibrary")
class BookController {

	@Autowired
	private BookService service;

	@GetMapping(value = "/books", produces = "application/json")
	@ApiOperation(value = "View a list of available books")
	ResponseEntity<List<Book>> all(
		@ApiParam(required = false)
		@RequestParam(name = "_page", required = false) Integer _page,
		@ApiParam(required = false)
		@RequestParam(name = "_perPage", required = false) Integer _perPage,
		@ApiParam(required = false, allowableValues = "ASC, DESC")
		@RequestParam(name = "_sortDir", required = false) String _sortDir,
		@ApiParam(required = false)
		@RequestParam(name="_sortField", required = false) String _sortField) {
	    List list = service.getAll(_page, _perPage, _sortDir, _sortField);
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("access-control-expose-headers", "X-Total-Count");
	    headers.add("x-total-count", service.getCount().toString());
	    return new ResponseEntity(list, headers, HttpStatus.OK);
	}

	@ApiOperation(value = "View a book with an ID")
	@GetMapping(value = "/books/{id}", produces = "application/json")
	Book get(@PathVariable Long id) {
		return service.get(id);
	}

	@ApiOperation(value = "View an author of book")
	@GetMapping(value = "/books/{id}/author", produces = "application/json")
	Author getAuthor(@PathVariable Long id) {
		return service.getAuthor(id);
	}

	@ApiOperation(value = "View an publisher of book")
	@GetMapping(value = "/books/{id}/publisher", produces = "application/json")
	Publisher getPublisher(@PathVariable Long id) {
		return service.getPublisher(id);
	}

	@ApiOperation(value = "View all book's pictures")
	@GetMapping(value = "/books/{id}/pictures", produces = "application/json")
	ResponseEntity<Set<Picture>> getPictures(@PathVariable Long id) {
		return new ResponseEntity(service.getPictures(id), HttpStatus.OK);
	}

	@PostMapping(value = "/books", produces = "application/json")
	@ApiOperation(value = "Add a new book")
	ResponseEntity<Book> add(@RequestBody @Valid Book book) {
		return new ResponseEntity(service.add(book), HttpStatus.CREATED);
	}

	@PutMapping(value = "/books/{id}", produces = "application/json")
	@ApiOperation(value = "Modify an existing book with an ID")
	Book put(@RequestBody Book newBook, @PathVariable Long id) {
		return service.put(newBook, id);
	}

	@DeleteMapping(value = "/books/{id}", produces = "application/json")
	@ApiOperation(value = "Delete an existing book with an ID")
	ResponseEntity<String> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("{}");
	}

}