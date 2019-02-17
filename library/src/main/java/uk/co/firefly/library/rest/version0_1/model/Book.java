package uk.co.firefly.library.rest.version0_1.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.annotations.ApiModelProperty;

@Entity
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ApiModelProperty(hidden = true)
	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "author_id", referencedColumnName = "id")
	private Author author;

	@ApiModelProperty(required = false)
	@Transient
	private Long authorId;

	@ApiModelProperty(hidden = true)
	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "publisher_id", referencedColumnName = "id")
	private Publisher publisher;

	@ApiModelProperty(required = false)
	@Transient
	private Long publisherId;

	@ApiModelProperty(hidden = true)
	@JsonIgnore
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "book", cascade = CascadeType.ALL)
	private Set<Picture> pictures;

	@JsonView(PictureView.FileInfo.class)
	@Size(min = 1, max = 255)
	@Column(length = 255)
	@NotBlank
	@NotNull
	private String title;

	@JsonView(PictureView.FileInfo.class)
	@Size(max = 10)
	@Column(length = 10)
	private String isbn10;

	@JsonView(PictureView.FileInfo.class)
	@Size(max = 14)
	@Column(length = 14)
	private String isbn13;
	
	public Publisher getPublisher() {
		return publisher;
	}

	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}

	public Set<Picture> getPictures() {
		return pictures;
	}

	public void setPictures(Set<Picture> pictures) {
		this.pictures = pictures;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	public Long getPublisherId() {
		return publisherId;
	}

	public void setPublisherId(Long publisherId) {
		this.publisherId = publisherId;
	}

	public Book() {
	}

	public Book(String title, Author author, Publisher publisher, Set<Picture> pictures) {
		this.title = title;
		this.author = author;
		this.publisher = publisher;
		this.pictures = pictures;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", author=" + author + ", publisher=" + publisher + ", pictures=" + pictures
				+ ", title=" + title + "]";
	}

	public void add(Picture picture) {
		pictures.add(picture);

	}

	public Book updateTransitiveAttributes() {
		if (this.publisher != null)
			this.publisherId = this.publisher.getId();
		if (this.author != null)
			this.authorId = this.author.getId();
		return this;
	}
	
	public String getIsbn13() {
		return isbn13;
	}

	public void setIsbn13(String isbn13) {
		this.isbn13 = isbn13;
	}

	public String getIsbn10() {
		return isbn10;
	}

	public void setIsbn10(String isbn10) {
		this.isbn10 = isbn10;
	}
	
}