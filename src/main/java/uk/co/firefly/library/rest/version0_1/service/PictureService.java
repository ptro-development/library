package uk.co.firefly.library.rest.version0_1.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import uk.co.firefly.library.rest.version0_1.exception.BadRequestException;
import uk.co.firefly.library.rest.version0_1.exception.ResourceNotFoundException;
import uk.co.firefly.library.rest.version0_1.model.Book;
import uk.co.firefly.library.rest.version0_1.model.Picture;
import uk.co.firefly.library.rest.version0_1.model.PictureType;
import uk.co.firefly.library.rest.version0_1.repository.BookRepository;
import uk.co.firefly.library.rest.version0_1.repository.PictureRepository;

@Service
public class PictureService {

	@Autowired
	private PictureRepository pictureRepository;

	@Autowired
	private BookRepository bookRepository;

	public Picture add(MultipartFile file, PictureType type, Long bookId, Long pageNumber) {
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new ResourceNotFoundException("Book resource " + bookId + " not found."));
		if (type.equals(type.PAGE) && pageNumber == null) {
			throw new BadRequestException("For a PAGE type picture you need to provide a positive integer page number.");
		}
		try {
			Picture picture = new Picture(file.getOriginalFilename(), file.getContentType(), file.getBytes(), book,
					type, pageNumber);
			pictureRepository.save(picture);
			book.add(picture);
			bookRepository.save(book);
			return picture.updateTransitiveAttributes();
		} catch (Exception e) {
			throw new ResourceNotFoundException("Problem with payload." + e.getLocalizedMessage());
		}
	}

	public Picture get(Long id) {
		Picture picture = pictureRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Resource " + id + " not found."));
		return picture.updateTransitiveAttributes();
	}

	public void delete(Long id) {
		Picture picture = pictureRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Resource " + id + " not found."));
		Book book = picture.getBook();
		book.getPictures().remove(picture);
		bookRepository.save(book);
	}

	public List<Picture> getAll() {
		return (List<Picture>) pictureRepository.findAll().stream().map(picture -> picture.updateTransitiveAttributes())
				.collect(Collectors.toList());
	}
	
	public List<Picture> getAll(Integer page, Integer perPage, String sortDirection, String sortField) {
		if (page != null && perPage != null && sortDirection != null && sortField != null) {
			Direction direction = Direction.ASC;
			try {
				direction = Direction.fromString(sortDirection);
			} catch (IllegalArgumentException e) {
				throw new BadRequestException("Bad request." + e.getLocalizedMessage());
			}
			return (List<Picture>) pictureRepository.findAll(
					PageRequest.of(page, perPage, Sort.by(direction, sortField)))
					.stream().map(picture -> picture.updateTransitiveAttributes())
					.collect(Collectors.toList());
		} else {
			return getAll();	
		}
	}
	
	public Long getCount() {
		return pictureRepository.count();
	}
	
}
