package uk.co.firefly.library.rest.version0_1.controller;

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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import uk.co.firefly.library.rest.version0_1.model.Picture;
import uk.co.firefly.library.rest.version0_1.model.PictureType;
import uk.co.firefly.library.rest.version0_1.service.PictureService;

@RestController
@RequestMapping("/library")
@Api(value = "homelibrary")
public class PictureController {

	@Autowired
	private PictureService service;

	@ApiOperation(value = "Add a book picture")
	@PostMapping(value = "/pictures", produces = "application/json")
	ResponseEntity<Picture> add(@RequestParam("uploadfile") MultipartFile file,
			@ApiParam(allowableValues = "FRONT_COVER, BACK_COVER", required = true) @RequestParam("type") PictureType type,
			@RequestParam("bookId") Long bookId) {
		return new ResponseEntity(service.add(file, type, bookId), HttpStatus.CREATED);
	}

	@ApiOperation(value = "Get an existing picture with an ID")
	@GetMapping(value = "/pictures/{id}")
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
