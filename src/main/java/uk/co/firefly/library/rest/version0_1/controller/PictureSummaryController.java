package uk.co.firefly.library.rest.version0_1.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import uk.co.firefly.library.rest.version0_1.exception.ResourceNotFoundException;
import uk.co.firefly.library.rest.version0_1.model.PictureSize;
import uk.co.firefly.library.rest.version0_1.model.PictureSummary;
import uk.co.firefly.library.rest.version0_1.model.PictureType;
import uk.co.firefly.library.rest.version0_1.model.QPictureSummary;
import uk.co.firefly.library.rest.version0_1.model.SmallPicture;
import uk.co.firefly.library.rest.version0_1.repository.PictureSummaryRepository;
import uk.co.firefly.library.rest.version0_1.service.PictureSummaryService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

@RestController
@RequestMapping("/library")
@Api(value = "homelibrary")
public class PictureSummaryController {

	@Autowired
	private PictureSummaryService service;

	/*
	@ApiOperation(value = "View a list of available pictures summaries")
	@GetMapping(value = "/picturesSummaries", produces = "application/json")
    ResponseEntity<List<PictureSummary>> all(
			@ApiParam(required = false)
			@RequestParam(name = "_page", required = false) Integer _page,
			@ApiParam(required = false)
			@RequestParam(name = "_perPage", required = false) Integer _perPage,
			@ApiParam(required = false, allowableValues = "ASC, DESC")
			@RequestParam(name = "_sortDir", required = false) String _sortDir,
			@ApiParam(required = false)
			@RequestParam(name="_sortField", required = false) String _sortField,
			@ApiParam(required = false)
			@RequestParam(name="_filters", required = false) String _filters) {
		// http://localhost:8080/library/picturesSummaries?
		// _filters={"pictureType":"FRONT_COVER","pictureSize":"SMALL"}&_page=0&_perPage=5&_sortDir=DESC&_sortField=id
		    List list = service.getAll(_page, _perPage, _sortDir, _sortField);
		    HttpHeaders headers = new HttpHeaders();
		    headers.add("access-control-expose-headers", "X-Total-Count");
		    headers.add("x-total-count", service.getCount().toString());
		    return new ResponseEntity(list, headers, HttpStatus.OK);
	} */

	@ApiOperation(value = "View a list of available pictures summaries")
	@GetMapping(value = "/picturesSummaries", produces = "application/json")
    ResponseEntity<List<PictureSummary>> all(
    		@ApiParam(required = false)
			@RequestParam(name = "_page", required = false) Integer _page,
			@ApiParam(required = false)
			@RequestParam(name = "_perPage", required = false) Integer _perPage,
			@ApiParam(required = false, allowableValues = "ASC, DESC")
			@RequestParam(name = "_sortDir", required = false) String _sortDir,
			@ApiParam(required = false)
			@RequestParam(name="_sortField", required = false) String _sortField,
			@ApiParam(required = false)
			@RequestParam(name="_filters", required = false) String _filters) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("access-control-expose-headers", "X-Total-Count");
		headers.add("x-total-count", service.getCount().toString());
		return new ResponseEntity(
				service.getAll(_page, _perPage, _sortDir, _sortField, _filters), headers, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Add a book picture summary")
	@PostMapping(value = "/picturesSummaries", produces = "application/json")
	ResponseEntity<PictureSummary> add(
			@ApiParam(allowableValues = "FRONT_COVER, BACK_COVER, PAGE", required = true)
			@RequestParam("type") PictureType type,
			@RequestParam(name = "pageNumber", required = false) Long pageNumber,
			@RequestParam("bookId") Long bookId) {
		return new ResponseEntity(service.add(type, bookId, pageNumber), HttpStatus.CREATED);
	}

	@ApiOperation(value = "Add a book picture")
	@PostMapping(value = "/picturesSummaries/{id}/picture", produces = "application/json")
	ResponseEntity<SmallPicture> addPicture(
			@PathVariable Long id,
			@RequestParam("file") MultipartFile file) throws IOException {
		return new ResponseEntity(service.addPicture(file, id), HttpStatus.CREATED);
	}
	
	@ApiOperation(value = "Get a book picture")
	@GetMapping(value = "/picturesSummaries/{id}/picture")
	ResponseEntity<byte[]> getPicture(
			@PathVariable Long id) throws IOException {
		PictureSummary summary = service.get(id);
		if (summary.hasPicture()) {
			return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + summary.getName() + "\"")
				.header(HttpHeaders.CONTENT_TYPE, summary.getMimetype()).body(service.fetchPictureData(summary));
		} else {
			throw new ResourceNotFoundException("No picture was found.");
		}
	}

	@ApiOperation(value = "Delete a picture")
	@DeleteMapping(value = "/picturesSummaries/{id}/picture", produces = "application/json")
	ResponseEntity<String> deletePicture(@PathVariable Long id) {
		service.deletePicture(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("{}");
	}
	
	@ApiOperation(value = "Get an existing picture summary with an ID")
	@GetMapping(value = "/picturesSummaries/{id}")
	public PictureSummary get(@PathVariable Long id) {
		return service.get(id);
	}

	@DeleteMapping(value = "/picturesSummaries/{id}", produces = "application/json")
	@ApiOperation(value = "Delete an existing picture summary with an ID")
	ResponseEntity<String> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("{}");
	}

}
