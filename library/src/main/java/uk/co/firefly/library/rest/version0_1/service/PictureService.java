package uk.co.firefly.library.rest.version0_1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
	
	public Picture add(
			MultipartFile file,
			PictureType type,
			Long bookId) {
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new ResourceNotFoundException("Book resource " + bookId + " not found."));
		try {
			Picture picture = new Picture(file.getOriginalFilename(), file.getContentType(), file.getBytes(), book, type);
			pictureRepository.save(picture);
			book.add(picture);
			bookRepository.save(book);
			return picture.updateTransitiveAttributes();
		} catch (Exception e) {
			throw new ResourceNotFoundException("Problem with payload." + e.getLocalizedMessage());
		}		
	}

	public Picture get(Long id) {
		return pictureRepository.findById(id)
		.orElseThrow(() -> new ResourceNotFoundException("Resource " + id + " not found."));
	}

	public void delete(Long id) {
		Picture picture = pictureRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Resource " + id + " not found."));
		pictureRepository.delete(picture);
	}
}
