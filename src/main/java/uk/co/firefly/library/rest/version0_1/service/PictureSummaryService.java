package uk.co.firefly.library.rest.version0_1.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.BooleanBuilder;

import uk.co.firefly.library.rest.version0_1.exception.BadRequestException;
import uk.co.firefly.library.rest.version0_1.exception.ResourceNotFoundException;
import uk.co.firefly.library.rest.version0_1.model.Book;
import uk.co.firefly.library.rest.version0_1.model.MediumPicture;
import uk.co.firefly.library.rest.version0_1.model.Picture;
import uk.co.firefly.library.rest.version0_1.model.PictureSize;
import uk.co.firefly.library.rest.version0_1.model.PictureSummary;
import uk.co.firefly.library.rest.version0_1.model.PictureType;
import uk.co.firefly.library.rest.version0_1.model.QPictureSummary;
import uk.co.firefly.library.rest.version0_1.model.SmallPicture;
import uk.co.firefly.library.rest.version0_1.repository.BookRepository;
import uk.co.firefly.library.rest.version0_1.repository.MediumPictureRepository;
import uk.co.firefly.library.rest.version0_1.repository.PictureSummaryRepository;
import uk.co.firefly.library.rest.version0_1.repository.SmallPictureRepository;

@Service
public class PictureSummaryService {

	// 64KB
	final Long SMALL = 65535L;
	// 16MB
	final Long MEDIUM = 16777215L;

	@Autowired
	private PictureSummaryRepository pictureSummaryRepository;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private SmallPictureRepository smallPictureRepository;

	@Autowired
	private MediumPictureRepository mediumPictureRepository;

	public PictureSummary add(PictureType type, Long bookId, Long pageNumber) {
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new ResourceNotFoundException("Book resource " + bookId + " not found."));
		if (type.equals(type.PAGE) && pageNumber == null) {
			throw new BadRequestException(
					"For a PAGE type picture you need to provide a positive integer page number.");
		}
		try {
			PictureSummary pictureSummary = new PictureSummary(book, type, pageNumber);
			pictureSummaryRepository.save(pictureSummary);
			book.add(pictureSummary);
			bookRepository.save(book);
			return pictureSummary.updateTransitiveAttributes();
		} catch (Exception e) {
			throw new ResourceNotFoundException("Problem with payload." + e.getLocalizedMessage());
		}
	}

	public PictureSummary get(Long id) {
		PictureSummary pictureSummary = pictureSummaryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Resource " + id + " not found."));
		return pictureSummary.updateTransitiveAttributes();
	}

	public void delete(Long id) {
		PictureSummary pictureSummary = pictureSummaryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Resource " + id + " not found."));
		Book book = pictureSummary.getBook();
		book.getPicturesSummaries().remove(pictureSummary);
		bookRepository.save(book);
	}

	public List<PictureSummary> getAll() {
		return (List<PictureSummary>) pictureSummaryRepository.findAll().stream()
				.map(summary -> summary.updateTransitiveAttributes()).collect(Collectors.toList());
	}

	public List<PictureSummary> getAll(Integer page, Integer perPage, String sortDirection, String sortField) {
		if (sortDirection == null)
			sortDirection = "ASC";
		if (sortField == null)
			sortField = "id";
		Direction direction;
		if (page != null && perPage != null) {
			try {
				direction = Direction.fromString(sortDirection);
			} catch (IllegalArgumentException e) {
				throw new BadRequestException("Bad request." + e.getLocalizedMessage());
			}
			return (List<PictureSummary>) pictureSummaryRepository
					.findAll(PageRequest.of(page, perPage, Sort.by(direction, sortField))).stream()
					.map(summary -> summary.updateTransitiveAttributes()).collect(Collectors.toList());
		} else {
			return getAll();
		}
	}

	public List<PictureSummary> getAll(
			Integer pageNumber, Integer perPage, String sortDirection, String sortField, String filter) {
		if (sortDirection == null)
			sortDirection = "ASC";
		if (sortField == null)
			sortField = "id";
		if (pageNumber != null && perPage != null && filter != null) {
			Direction direction;
			try {
				direction = Direction.fromString(sortDirection);
			} catch (IllegalArgumentException e) {
				throw new BadRequestException("Bad request." + e.getLocalizedMessage());
			}
			try {
				BooleanBuilder builder = buildFilter(filter);
				return pictureSummaryRepository
						.findAll(builder.getValue(), PageRequest.of(pageNumber, perPage, Sort.by(direction, sortField)))
						.getContent().stream().map(summary -> summary.updateTransitiveAttributes())
						.collect(Collectors.toList());
			} catch (IOException e) {
				throw new BadRequestException("Bad request." + e.getLocalizedMessage());
			}
		} else if (pageNumber != null && perPage != null && filter == null) {
			return getAll(pageNumber, perPage, sortDirection, sortField);
		} else {
			return getAll();
		}
	}

	public BooleanBuilder buildFilter(String jsonFilterString) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		BooleanBuilder booleanBuilder = new BooleanBuilder();
		JsonNode node;
		node = mapper.readTree(jsonFilterString);
		if (node.has("pictureType") && PictureType.hasValue(node.get("pictureType").asText())) {
			booleanBuilder.and(QPictureSummary.pictureSummary.pictureType
					.eq(PictureType.valueOf(node.get("pictureType").asText())));
		}
		if (node.has("pictureSize") && PictureSize.hasValue(node.get("pictureSize").asText())) {
			booleanBuilder.and(QPictureSummary.pictureSummary.pictureSize
					.eq(PictureSize.valueOf(node.get("pictureSize").asText())));
		}
		if (node.has("bookId")) {
			Book book = bookRepository.findById(node.get("bookId").asLong())
					.orElseThrow(() -> new ResourceNotFoundException("Book resource " + node.get("bookId").asText() + " not found."));
			booleanBuilder.and(QPictureSummary.pictureSummary.book
					.eq(book));
		}
		return booleanBuilder;
	}

	public Long getCount() {
		return pictureSummaryRepository.count();
	}

	public Picture addPicture(MultipartFile file, Long id) throws IOException {
		PictureSummary pictureSummary = pictureSummaryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Summary resource " + id + " not found."));
		Picture picture = null;
		if (pictureSummary.hasPicture()) {
			throw new BadRequestException("Picture already exists.");
		} else {
			if (file.getSize() < this.SMALL) {
				SmallPicture smallPicture = new SmallPicture(file.getBytes(), pictureSummary);
				SmallPicture saved = smallPictureRepository.save(smallPicture);
				pictureSummary.setSmallPicture(saved, file.getOriginalFilename(), file.getContentType());
				pictureSummaryRepository.save(pictureSummary);
				picture = (Picture) smallPicture.updateTransitiveAttributes();
			} else if (file.getSize() > this.SMALL && file.getSize() < this.MEDIUM) {
				MediumPicture mediumPicture = new MediumPicture(file.getBytes(), pictureSummary);
				MediumPicture saved = mediumPictureRepository.save(mediumPicture);
				pictureSummary.setMediumPicture(saved, file.getOriginalFilename(), file.getContentType());
				pictureSummaryRepository.save(pictureSummary);
				picture = (Picture) mediumPicture.updateTransitiveAttributes();
			} else {
				throw new BadRequestException("File is too big.");
			}
		}
		return picture;
	}

	public Picture getPicture(Long id) {
		PictureSummary summary = pictureSummaryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Summary resource " + id + " not found."));
		Picture picture = null;
		if (summary.hasPicture()) {
			if (summary.getSmallPicture() != null) {
				picture = (Picture) smallPictureRepository.findById(summary.getSmallPicture().getId())
						.orElseThrow(() -> new ResourceNotFoundException("Picture resource " + id + " not found."));
			} else {
				picture = (Picture) mediumPictureRepository.findById(summary.getMediumPicture().getId())
						.orElseThrow(() -> new ResourceNotFoundException("Picture resource " + id + " not found."));
			}
		} else {
			throw new ResourceNotFoundException("Picture data not found");
		}
		return picture;
	}

	public byte[] fetchPictureData(PictureSummary summary) {
		if (summary.hasPicture()) {
			if (summary.getSmallPicture() != null) {
				return smallPictureRepository.findById(summary.getSmallPicture().getId()).get().getData();
			} else {
				return mediumPictureRepository.findById(summary.getMediumPicture().getId()).get().getData();
			}
		} else {
			throw new ResourceNotFoundException("Picture data not found");
		}
	}

	public void deletePicture(Long id) {
		PictureSummary summary = pictureSummaryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Summary resource " + id + " not found."));
		if (summary.hasPicture()) {
			if (summary.getSmallPicture() != null) {
				summary.setSmallPicture(null, null, null);
			} else {
				summary.setMediumPicture(null, null, null);
			}
		} else {
			throw new ResourceNotFoundException("Picture not found.");
		}
		pictureSummaryRepository.save(summary);
	}

}
