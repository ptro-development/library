package uk.co.firefly.library.rest.version0_1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import uk.co.firefly.library.rest.version0_1.model.MediumPicture;
import uk.co.firefly.library.rest.version0_1.model.SmallPicture;
import uk.co.firefly.library.rest.version0_1.service.MediumPictureService;
import uk.co.firefly.library.rest.version0_1.service.SmallPictureService;

@RestController
@RequestMapping("/library")
@Api(value = "homelibrary")
public class MediumPictureController {
	
	@Autowired
	private MediumPictureService service;
	
	@ApiOperation(value = "View a list of available medium pictures")
	@GetMapping(value = "/mediumPictures", produces = "application/json")
    ResponseEntity<List<SmallPicture>> all(
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

	@ApiOperation(value = "Get an existing medium picture with an ID")
	@GetMapping(value = "/mediumPictures/{id}")
	public ResponseEntity<byte[]> getFile(
			@PathVariable Long id) {
		MediumPicture picture = service.get(id);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + service.getName(picture) + "\"")
				.header(HttpHeaders.CONTENT_TYPE, service.getMimetype(picture)).body(picture.getData());
	}

	@DeleteMapping(value = "/mediumPictures/{id}", produces = "application/json")
	@ApiOperation(value = "Delete an existing medium picture with an ID")
	ResponseEntity<String> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("{}");
	}
}