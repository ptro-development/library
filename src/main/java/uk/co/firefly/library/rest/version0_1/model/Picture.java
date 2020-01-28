package uk.co.firefly.library.rest.version0_1.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.annotations.ApiModelProperty;

@Entity
public class Picture {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(PictureView.FileInfo.class)
	private Long id;

	@Column(name = "name", length = 255)
	@JsonView(PictureView.FileInfo.class)
	@Size(min = 1, max = 255)
	@NotBlank
	@NotNull
	private String name;
	
	@Column(name = "pageNumber")
	@JsonView(PictureView.FileInfo.class)
	private Long pageNumber;

	@ApiModelProperty(dataType = "string", allowableValues = "FRONT_COVER, BACK_COVER, PAGE")
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "picture_type", columnDefinition = "smallint")
	@JsonView(PictureView.FileInfo.class)
	@NotNull
	private PictureType pictureType;

	@Column(name = "mimetype", length = 255)
	@JsonView(PictureView.FileInfo.class)
	@Size(max = 255)
	@NotBlank
	@NotNull
	private String mimetype;

	@Lob
	@Column(name = "picture", columnDefinition = "BLOB")
	@JsonIgnore
	@NotNull
	private byte[] picture;

	@ManyToOne
	@JoinColumn(name = "book_id", referencedColumnName = "id")
	@JsonIgnore
	@NotNull
	Book book;
	
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

	public byte[] getPicture() {
		return picture;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
	}

	public Picture() {
	}

	public Picture(String name, String mimetype, byte[] picture, Book book, PictureType pictureType, Long pageNumber) {
		this.name = name;
		this.mimetype = mimetype;
		this.picture = picture;
		this.book = book;
		this.pictureType = pictureType;
		this.pageNumber = pageNumber;
	}
	
	public Picture updateTransitiveAttributes() {
		if (this.book != null)
			this.bookId = this.book.getId();
		return this;
	}

}