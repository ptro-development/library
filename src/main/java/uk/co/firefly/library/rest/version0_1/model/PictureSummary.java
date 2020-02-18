package uk.co.firefly.library.rest.version0_1.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.querydsl.core.annotations.QueryEntity;

import io.swagger.annotations.ApiModelProperty;

@Entity
@QueryEntity
public class PictureSummary {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(PictureSummaryView.FileInfo.class)
	private Long id;
	
	@Column(name = "name", length = 255)
	//@Size(min = 1, max = 255)
	@JsonView(PictureSummaryView.FileInfo.class)
	private String name;
	
	@Column(name = "pageNumber")
	@JsonView(PictureSummaryView.FileInfo.class)
	private Long pageNumber;

	@ApiModelProperty(dataType = "string", allowableValues = "FRONT_COVER, BACK_COVER, PAGE")
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "picture_type", columnDefinition = "smallint")
	@JsonView(PictureSummaryView.FileInfo.class)
	@NotNull
	private PictureType pictureType;
	
	@ApiModelProperty(dataType = "string", allowableValues = "SMALL, MEDIUM")
	@Enumerated(EnumType.ORDINAL)
	@JsonView(PictureSummaryView.FileInfo.class)
	@Column(name = "picture_size", columnDefinition = "smallint")
	private PictureSize pictureSize;

	@Column(name = "mimetype", length = 255)
	@JsonView(PictureSummaryView.FileInfo.class)
	//@Size(max = 255)
	private String mimetype;

	@ManyToOne
	@JoinColumn(name = "book_id", referencedColumnName = "id")
	@JsonIgnore
	@NotNull
	Book book;

	@ApiModelProperty(hidden = true)
	@JsonIgnore
	@OneToOne(orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "small_picture_id", referencedColumnName = "id")
	private SmallPicture smallPicture;

	@ApiModelProperty(hidden = true)
	@JsonIgnore
	@OneToOne(orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "medium_picture_id", referencedColumnName = "id")
	private MediumPicture mediumPicture;
	
	@ApiModelProperty
	@Transient
	private Long bookId;
	
	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}
	
	public PictureType getPictureType() {
		return pictureType;
	}

	public void setPictureType(PictureType pictureType) {
		this.pictureType = pictureType;
	}

	public void setPageNumber(Long pageNumber) {
		this.pageNumber = pageNumber;
	}
	
	public Long getPageNumber() {
		return pageNumber;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMimetype() {
		return mimetype;
	}

	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	public PictureSummary() {}

	public PictureSummary(String name, String mimetype, Book book, PictureType pictureType, Long pageNumber, PictureSize pictureSize) {
		this.name = name;
		this.mimetype = mimetype;
		this.book = book;
		this.pictureType = pictureType;
		this.pageNumber = pageNumber;
		this.pictureSize = pictureSize;
	}

	public PictureSummary(Book book, PictureType pictureType, Long pageNumber) {
		this.book = book;
		this.pictureType = pictureType;
		this.pageNumber = pageNumber;
	}
	
	public PictureSummary updateTransitiveAttributes() {
		if (this.book != null)
			this.bookId = this.book.getId();
		return this;
	}

	public SmallPicture getSmallPicture() {
		return smallPicture;
	}

	public boolean setSmallPicture(SmallPicture smallPicture, String name, String mimetype) {
		if (this.mediumPicture == null) {
		    this.smallPicture = smallPicture;
			this.pictureSize = PictureSize.SMALL;
			this.name = name;
			this.mimetype = mimetype;
			return true;
		} else {
			return false;
		}
	}

	public MediumPicture getMediumPicture() {
		return mediumPicture;
	}

	public boolean setMediumPicture(MediumPicture mediumPicture, String name, String mimetype) {
		if (this.smallPicture == null){
			this.mediumPicture = mediumPicture;
			this.pictureSize = PictureSize.MEDIUM;
			this.name = name;
			this.mimetype = mimetype;
			return true;
		} else {
			return false;
		}
	}
	
	public PictureSize getPictureSize() {
		return pictureSize;
	}

	public void setPictureSize(PictureSize pictureSize) {
		this.pictureSize = pictureSize;
	}

	public boolean hasPicture() {
		return (this.smallPicture != null || this.mediumPicture != null);
	}
	
	public Picture fetchPicture() {
		if(hasPicture()) {
			if (smallPicture != null) {
				return smallPicture;
			} else {
				return mediumPicture;
			}
		} else {
			return null;
		}
	}
}