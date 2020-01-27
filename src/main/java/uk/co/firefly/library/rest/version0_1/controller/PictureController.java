package uk.co.firefly.library.rest.version0_1.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.data.domain.Sort.Direction;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import uk.co.firefly.library.rest.version0_1.exception.BadRequestException;
import uk.co.firefly.library.rest.version0_1.exception.ResourceNotFoundException;
import uk.co.firefly.library.rest.version0_1.model.Author;
import uk.co.firefly.library.rest.version0_1.model.Picture;
import uk.co.firefly.library.rest.version0_1.model.PictureType;
import uk.co.firefly.library.rest.version0_1.service.PictureService;

@RestController
@RequestMapping("/library")
@Api(value = "homelibrary")
public class PictureController {

	@Autowired
	private PictureService service;
	
	@ApiOperation(value = "View a list of available pictures (metadata)")
	@GetMapping(value = "/pictures", produces = "application/json")
    ResponseEntity<List<Picture>> all(
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

	@ApiOperation(value = "Add a book picture")
	@PostMapping(value = "/pictures", produces = "application/json")
	ResponseEntity<Picture> add(
			@RequestParam("file") MultipartFile file,
			@ApiParam(allowableValues = "FRONT_COVER, BACK_COVER", required = true)
			@RequestParam("type") PictureType type,
			@RequestParam("bookId") Long bookId) {
		return new ResponseEntity(service.add(file, type, bookId), HttpStatus.CREATED);
	}

	@ApiOperation(value = "Get an existing picture with an ID (metadata)")
	@GetMapping(value = "/pictures/{id}")
	public Picture getMetadata(@PathVariable Long id) {
		return service.get(id);
	}

	@ApiOperation(value = "Get an existing picture with an ID")
	@GetMapping(value = "/pictures/{id}/data")
	public ResponseEntity<byte[]> getFile(@PathVariable Long id) {
		Picture picture = service.get(id);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + picture.getName() + "\"")
				.header(HttpHeaders.CONTENT_TYPE, picture.getMimetype()).body(picture.getPicture());
	}

	@DeleteMapping(value = "/pictures/{id}", produces = "application/json")
	@ApiOperation(value = "Delete an existing picture with an ID")
	ResponseEntity<String> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("{}");
	}

}
