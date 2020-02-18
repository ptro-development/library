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
import uk.co.firefly.library.rest.version0_1.model.PictureSummary;
import uk.co.firefly.library.rest.version0_1.model.Publisher;
import uk.co.firefly.library.rest.version0_1.repository.PublisherRepository;

@Service
public class PublisherService {

	@Autowired
	private PublisherRepository repository;
	
	public List<Publisher> getAll() {
		return repository.findAll();
	}

	public List<Publisher> getAll(Integer page, Integer perPage, String sortDirection, String sortField) {
		if (page != null && perPage != null && sortDirection != null && sortField != null) {
			Direction direction = Direction.ASC;
			try {
				direction = Direction.fromString(sortDirection);
			} catch (IllegalArgumentException e) {
				throw new BadRequestException("Bad request." + e.getLocalizedMessage());
			}
			return (List<Publisher>) repository.findAll(
					PageRequest.of(page, perPage, Sort.by(direction, sortField)))
					.stream().collect(Collectors.toList());
		} else {
			return getAll();	
		}
	}
	
	public Long getCount() {
		return repository.count();
	}
	
	public Publisher get(Long publisherId) {
		return repository.findById(publisherId)
				.orElseThrow(() -> new ResourceNotFoundException("Resource " + publisherId + " not found."));
	}
	
	public Publisher add(Publisher newPublisher) {
		return repository.save(newPublisher);
	}

	public Publisher put(Publisher newPublisher, Long id) {
		return repository.findById(id).map(resource -> {
			resource.setName(newPublisher.getName());
			resource.setAddress(newPublisher.getAddress());
			resource.setWebAddress(newPublisher.getWebAddress());
			return repository.save(resource);
		}).orElseThrow(() -> new ResourceNotFoundException("Resource " + id + " not found."));
	}
	
	public void delete(Long id) {
		repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource " + id + " not found."));
		repository.deleteById(id);
	}
}
