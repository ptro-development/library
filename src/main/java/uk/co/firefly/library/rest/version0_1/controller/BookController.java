package uk.co.firefly.library.rest.version0_1.controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import uk.co.firefly.library.rest.version0_1.exception.ResourceNotFoundException;
import uk.co.firefly.library.rest.version0_1.model.Author;
import uk.co.firefly.library.rest.version0_1.model.Book;
import uk.co.firefly.library.rest.version0_1.model.PictureSize;
import uk.co.firefly.library.rest.version0_1.model.PictureSummary;
import uk.co.firefly.library.rest.version0_1.model.PictureType;
import uk.co.firefly.library.rest.version0_1.model.Publisher;
import uk.co.firefly.library.rest.version0_1.model.SmallPicture;
import uk.co.firefly.library.rest.version0_1.service.BookService;
import uk.co.firefly.library.rest.version0_1.service.PictureSummaryService;

@RestController
@RequestMapping("/library")
@Api(value = "homelibrary")
class BookController {

	@Autowired
	private BookService bookService;
	
	@Autowired
	private PictureSummaryService pictureSummaryService;

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
	    List list = bookService.getAll(_page, _perPage, _sortDir, _sortField);
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("access-control-expose-headers", "X-Total-Count");
	    headers.add("x-total-count", bookService.getCount().toString());
	    return new ResponseEntity(list, headers, HttpStatus.OK);
	}

	@ApiOperation(value = "View a book with an ID")
	@GetMapping(value = "/books/{id}", produces = "application/json")
	Book get(@PathVariable Long id) {
		return bookService.get(id);
	}

	@ApiOperation(value = "View an author of book")
	@GetMapping(value = "/books/{id}/author", produces = "application/json")
	Author getAuthor(@PathVariable Long id) {
		return bookService.getAuthor(id);
	}

	@ApiOperation(value = "View an publisher of book")
	@GetMapping(value = "/books/{id}/publisher", produces = "application/json")
	Publisher getPublisher(@PathVariable Long id) {
		return bookService.getPublisher(id);
	}

	@ApiOperation(value = "View all book's pictures")
	@GetMapping(value = "/books/{id}/picturesSummaries", produces = "application/json")
	ResponseEntity<Set<PictureSummary>> getPicturesSummaries(@PathVariable Long id) {
		return new ResponseEntity(bookService.getPicturesSummaries(id), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Add a book picture summary")
	@PostMapping(value = "/books/{id}/picturesSummaries", produces = "application/json")
	ResponseEntity<PictureSummary> addPictureSummary(
			@PathVariable Long bookId,
			@ApiParam(allowableValues = "FRONT_COVER, BACK_COVER, PAGE", required = true)
			@RequestParam("type") PictureType type,
			@RequestParam(name = "pageNumber", required = false) Long pageNumber) {
		return new ResponseEntity(pictureSummaryService.add(type, bookId, pageNumber), HttpStatus.CREATED);
	}
	
	@ApiOperation(value = "Get an existing picture summary with an ID")
	@GetMapping(value = "/books/{id}/picturesSummaries/{summaryId}")
	public PictureSummary getPictureSummary(@PathVariable Long summaryId) {
		return pictureSummaryService.get(summaryId);
	}

	@DeleteMapping(value = "/books/{id}/picturesSummaries/{summaryId}", produces = "application/json")
	@ApiOperation(value = "Delete an existing picture summary with an ID")
	ResponseEntity<String> deletePictureSummary(@PathVariable Long summaryId) {
		pictureSummaryService.delete(summaryId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("{}");
	}

	@ApiOperation(value = "Add a book picture")
	@PostMapping(value = "/books/{id}/picturesSummaries/{summaryId}/picture", produces = "application/json")
	ResponseEntity<SmallPicture> addPicture(
			@PathVariable Long summaryId,
			@RequestParam("file") MultipartFile file) throws IOException {
		return new ResponseEntity(pictureSummaryService.addPicture(file, summaryId), HttpStatus.CREATED);
	}
	
	@ApiOperation(value = "Get a book picture")
	@GetMapping(value = "/books/{id}/picturesSummaries/{summaryId}/picture")
	ResponseEntity<byte[]> getPicture(
			@PathVariable Long summaryId) throws IOException {
		PictureSummary summary = pictureSummaryService.get(summaryId);
		if (summary.hasPicture()) {
			return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + summary.getName() + "\"")
				.header(HttpHeaders.CONTENT_TYPE, summary.getMimetype()).body(pictureSummaryService.fetchPictureData(summary));
		} else {
			throw new ResourceNotFoundException("No picture was found.");
		}
	}

	@ApiOperation(value = "Delete a picture")
	@DeleteMapping(value = "/books/{id}/picturesSummaries/{summaryId}/picture", produces = "application/json")
	ResponseEntity<String> deletePicture(@PathVariable Long summaryId) {
		pictureSummaryService.deletePicture(summaryId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("{}");
	}
	
	@PostMapping(value = "/books", produces = "application/json")
	@ApiOperation(value = "Add a new book")
	ResponseEntity<Book> add(@RequestBody @Valid Book book) {
		return new ResponseEntity(bookService.add(book), HttpStatus.CREATED);
	}

	@PutMapping(value = "/books/{id}", produces = "application/json")
	@ApiOperation(value = "Modify an existing book with an ID")
	Book put(@RequestBody Book newBook, @PathVariable Long id) {
		return bookService.put(newBook, id);
	}

	@DeleteMapping(value = "/books/{id}", produces = "application/json")
	@ApiOperation(value = "Delete an existing book with an ID")
	ResponseEntity<String> delete(@PathVariable Long id) {
		bookService.delete(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("{}");
	}

}