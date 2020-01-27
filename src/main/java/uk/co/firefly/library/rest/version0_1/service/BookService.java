package uk.co.firefly.library.rest.version0_1.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import uk.co.firefly.library.rest.version0_1.exception.BadRequestException;
import uk.co.firefly.library.rest.version0_1.exception.ResourceNotFoundException;
import uk.co.firefly.library.rest.version0_1.model.Author;
import uk.co.firefly.library.rest.version0_1.model.Book;
import uk.co.firefly.library.rest.version0_1.model.Picture;
import uk.co.firefly.library.rest.version0_1.model.Publisher;
import uk.co.firefly.library.rest.version0_1.repository.AuthorRepository;
import uk.co.firefly.library.rest.version0_1.repository.BookRepository;
import uk.co.firefly.library.rest.version0_1.repository.PublisherRepository;

@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private AuthorRepository authorRepository;
	
	@Autowired
	private PublisherRepository publisherRepository;
	
	BookService(){}
	
	public List<Book> getAll(){
		return bookRepository.findAll().stream().
		map(book -> book.updateTransitiveAttributes()).collect(Collectors.toList());
	}
	
	public List<Book> getAll(Integer page, Integer perPage, String sortDirection, String sortField) {
		if (page != null && perPage != null && sortDirection != null && sortField != null) {
			Direction direction = Direction.ASC;
			try {
				direction = Direction.fromString(sortDirection);
			} catch (IllegalArgumentException e) {
				throw new BadRequestException("Bad request." + e.getLocalizedMessage());
			}	
			return (List<Book>) bookRepository.findAll(
					PageRequest.of(page, perPage, Sort.by(direction, sortField)))
					.stream().map(book -> book.updateTransitiveAttributes())
					.collect(Collectors.toList());
		} else {
			return getAll();	
		}
	}
	
	public Long getCount() {
		return bookRepository.count();
	}
	
	public Book get(Long bookId) {
		return bookRepository.findById(bookId)
		.orElseThrow(() -> new ResourceNotFoundException("Resource " + bookId + " not found.")).updateTransitiveAttributes();
	}
	
	public Author getAuthor(Long bookId) {
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new ResourceNotFoundException("Book resource " + bookId + " not found."));
		Optional<Author> author = Optional.ofNullable(book.getAuthor());
		return author.orElseThrow(() -> new ResourceNotFoundException("Author resource not found."));
	}
	
	public Publisher getPublisher(Long bookId) {
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new ResourceNotFoundException("Book resource " + bookId + " not found."));
		Optional<Publisher> publisher = Optional.ofNullable(book.getPublisher());
		return publisher.orElseThrow(() -> new ResourceNotFoundException("Publisher resource not found."));
	}
	
	public Set<Picture> getPictures(Long bookId) {
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new ResourceNotFoundException("Book resource " + bookId + " not found."));
		Optional<Set<Picture>> pictures = Optional.ofNullable(
				book.getPictures().stream().map(Picture::updateTransitiveAttributes).collect(Collectors.toSet()));
		return pictures.orElseThrow(
						() -> new ResourceNotFoundException("Pictures resources not found."));
	}
	
	public Book put(Book newBook, Long oldBookId) {
		return bookRepository.findById(oldBookId).map(resource -> {
			resource.setTitle(newBook.getTitle());
			resource.setIsbn10(newBook.getIsbn10());
			resource.setIsbn13(newBook.getIsbn13());
			updateRelations(newBook, resource);
			return bookRepository.save(resource).updateTransitiveAttributes();
		}).orElseThrow(() -> new ResourceNotFoundException("Resource " + oldBookId + " not found."));
	}
	
	public Book add(Book book) {
		addRelations(book);
		return bookRepository.save(book).updateTransitiveAttributes();
	}
	
	public void delete(Long bookId) {
		bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Resource " + bookId + " not found."));
		bookRepository.deleteById(bookId);
	}
	
	public void addRelations(Book book) {
		Optional<Long> authorId = Optional.ofNullable(book.getAuthorId());
		if (authorId.isPresent()) {
			book.setAuthor(
					authorRepository.findById(authorId.get()).
						orElseThrow(
								() -> new ResourceNotFoundException("Book author resource " + authorId.get() + " not found.")));
		}
		Optional<Long> publisherId = Optional.ofNullable(book.getPublisherId());
		if (publisherId.isPresent()) {
			book.setPublisher(
					publisherRepository.findById(publisherId.get()).
						orElseThrow(
								() -> new ResourceNotFoundException("Book publisher resource " + publisherId.get() + " not found.")));
		}
	}
	
	public void updateRelations(Book newBook, Book oldBook) {
		Optional<Long> authorId = Optional.ofNullable(newBook.getAuthorId());
		if (authorId.isPresent()) {
			oldBook.setAuthor(authorRepository.findById(authorId.get()).orElseThrow(
					() -> new ResourceNotFoundException("Book author resource " + authorId.get() + " not found.")));
		}
		Optional<Long> publisherId = Optional.ofNullable(newBook.getPublisherId());
		if (publisherId.isPresent()) {
			oldBook.setPublisher(
					publisherRepository.findById(publisherId.get()).orElseThrow(() -> new ResourceNotFoundException(
							"Book publisher resource " + publisherId.get() + " not found.")));
		}
	}
	
	public void removePicture(Picture picture) {
		
	}
	
}