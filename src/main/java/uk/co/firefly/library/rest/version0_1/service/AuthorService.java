package uk.co.firefly.library.rest.version0_1.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import uk.co.firefly.library.rest.version0_1.exception.BadRequestException;
import uk.co.firefly.library.rest.version0_1.exception.ResourceNotFoundException;
import uk.co.firefly.library.rest.version0_1.model.Author;
import uk.co.firefly.library.rest.version0_1.model.PictureSummary;
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
	
	public List<Author> getAll(Integer page, Integer perPage, String sortDirection, String sortField) {
		if (page != null && perPage != null && sortDirection != null && sortField != null) {
			Direction direction = Direction.ASC;
			try {
				direction = Direction.fromString(sortDirection);
			} catch (IllegalArgumentException e) {
				throw new BadRequestException("Bad request." + e.getLocalizedMessage());
			}
			return (List<Author>) repository.findAll(
					PageRequest.of(page, perPage, Sort.by(direction, sortField))).stream().collect(Collectors.toList());
		} else {
			return getAll();	
		}
	}
	
	public Long getCount() {
		return repository.count();
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