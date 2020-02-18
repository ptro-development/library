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
import uk.co.firefly.library.rest.version0_1.model.MediumPicture;
import uk.co.firefly.library.rest.version0_1.repository.MediumPictureRepository;
import uk.co.firefly.library.rest.version0_1.repository.PictureSummaryRepository;

@Service
public class MediumPictureService {
	
	@Autowired
	private PictureSummaryRepository pictureSummaryRepository;

	@Autowired
	private MediumPictureRepository mediumPictureRepository;

	public MediumPicture get(Long id) {
		MediumPicture picture = mediumPictureRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Resource " + id + " not found."));
		return picture;
	}

	public void delete(Long id) {
		MediumPicture picture = mediumPictureRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Resource " + id + " not found."));
		PictureSummary pictureSummary = picture.getPictureSummary();
		pictureSummary.setMediumPicture(null, null, null);
		pictureSummaryRepository.save(pictureSummary);
	}

	public List<MediumPicture> getAll() {
		return mediumPictureRepository.findAll().stream().map(picture -> picture.updateTransitiveAttributes()).collect(Collectors.toList());
	}
	
	public List<MediumPicture> getAll(Integer page, Integer perPage, String sortDirection, String sortField) {
		if (page != null && perPage != null && sortDirection != null && sortField != null) {
			Direction direction = Direction.ASC;
			try {
				direction = Direction.fromString(sortDirection);
			} catch (IllegalArgumentException e) {
				throw new BadRequestException("Bad request." + e.getLocalizedMessage());
			}
			return (List<MediumPicture>) mediumPictureRepository.findAll(
					PageRequest.of(page, perPage, Sort.by(direction, sortField))).map(picture -> picture.updateTransitiveAttributes())
					.stream().collect(Collectors.toList());
		} else {
			return getAll();
		}
	}
	
	public Long getCount() {
		return mediumPictureRepository.count();
	}
	
	public String getMimetype(MediumPicture smallPicture) {
		return smallPicture.getPictureSummary().getMimetype();
	}
	
	public String getName(MediumPicture smallPicture) {
		return smallPicture.getPictureSummary().getName();
	}

}