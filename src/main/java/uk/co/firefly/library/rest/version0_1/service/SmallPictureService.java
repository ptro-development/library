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
import uk.co.firefly.library.rest.version0_1.model.SmallPicture;
import uk.co.firefly.library.rest.version0_1.model.PictureSummary;
import uk.co.firefly.library.rest.version0_1.repository.PictureSummaryRepository;
import uk.co.firefly.library.rest.version0_1.repository.SmallPictureRepository;

@Service
public class SmallPictureService {
	
	@Autowired
	private PictureSummaryRepository pictureSummaryRepository;

	@Autowired
	private SmallPictureRepository smallPictureRepository;

	public SmallPicture get(Long id) {
		SmallPicture picture = smallPictureRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Resource " + id + " not found."));
		return picture;
	}

	public void delete(Long id) {
		SmallPicture picture = smallPictureRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Resource " + id + " not found."));
		PictureSummary pictureSummary = picture.getPictureSummary();
		pictureSummary.setSmallPicture(null, null, null);
		pictureSummaryRepository.save(pictureSummary);
	}

	public List<SmallPicture> getAll() {
		return smallPictureRepository.findAll().stream().map(picture -> picture.updateTransitiveAttributes()).collect(Collectors.toList());
	}
	
	public List<SmallPicture> getAll(Integer page, Integer perPage, String sortDirection, String sortField) {
		if (page != null && perPage != null && sortDirection != null && sortField != null) {
			Direction direction = Direction.ASC;
			try {
				direction = Direction.fromString(sortDirection);
			} catch (IllegalArgumentException e) {
				throw new BadRequestException("Bad request." + e.getLocalizedMessage());
			}
			return (List<SmallPicture>) smallPictureRepository.findAll(
					PageRequest.of(page, perPage, Sort.by(direction, sortField))).map(picture -> picture.updateTransitiveAttributes())
					.stream().collect(Collectors.toList());
		} else {
			return getAll();
		}
	}
	
	public Long getCount() {
		return smallPictureRepository.count();
	}
	
	public String getMimetype(SmallPicture smallPicture) {
		return smallPicture.getPictureSummary().getMimetype();
	}
	
	public String getName(SmallPicture smallPicture) {
		return smallPicture.getPictureSummary().getName();
	}

}
