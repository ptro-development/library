package uk.co.firefly.library.rest.version0_1.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.co.firefly.library.rest.version0_1.exception.ResourceNotFoundException;
import uk.co.firefly.library.rest.version0_1.model.Author;
import uk.co.firefly.library.rest.version0_1.repository.AuthorRepository;

@Service
public class AuthorService {

	@Autowired
	private AuthorRepository repository;

	public Author get(Long authorId) {
		return repository.findById(authorId)
		.orElseThrow(() -> new ResourceNotFoundException("Resource " + authorId + " not found."));
	}
	
	public List<Author> getAll() {
		return repository.findAll();
	}

	public Author save(Author newAuthor) {
		return repository.save(newAuthor);
	}
	
	public Author put(Long oldAuthorId, Author newAuthor) {
		return repository.findById(oldAuthorId).map(resource -> {
			resource.setFirstName(newAuthor.getFirstName());
			resource.setMiddleName(newAuthor.getMiddleName());
			resource.setLastName(newAuthor.getLastName());
			resource.setAbout(newAuthor.getAbout());
			return repository.save(resource);
		}).orElseThrow(() -> new ResourceNotFoundException("Resource " + oldAuthorId + " not found."));
	}
	
	public void delete(Long authorId) {
		repository.findById(authorId).orElseThrow(() -> new ResourceNotFoundException("Resource " + authorId + " not found."));
		repository.deleteById(authorId);
	}
}