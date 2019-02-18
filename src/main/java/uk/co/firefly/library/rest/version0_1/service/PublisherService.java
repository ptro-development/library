package uk.co.firefly.library.rest.version0_1.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.co.firefly.library.rest.version0_1.exception.ResourceNotFoundException;
import uk.co.firefly.library.rest.version0_1.model.Publisher;
import uk.co.firefly.library.rest.version0_1.repository.PublisherRepository;

@Service
public class PublisherService {

	@Autowired
	private PublisherRepository repository;
	
	public List<Publisher> getAll() {
		return repository.findAll();
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
