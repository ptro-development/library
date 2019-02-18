package uk.co.firefly.library.rest.version0_1.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;

@Entity
public class Author {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@ApiModelProperty
	private Long id;

	@ApiModelProperty(required = true)
	@NotBlank
	@Size(min = 1, max = 25)
	@Column(length = 25)
	@NotNull
	private String firstName;

	@ApiModelProperty(required = false)
	@Size(min = 1, max = 25)
	@Column(length = 25)
	private String middleName;

	@ApiModelProperty(required = true)
	@NotBlank
	@Size(min = 1, max = 25)
	@Column(length = 25)
	@NotNull
	private String lastName;

	@ApiModelProperty(required = false)
	@Size(max = 255)
	@Column(length = 255)
	private String about;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String secondName) {
		this.middleName = secondName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String thirdName) {
		this.lastName = thirdName;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	@Override
	public String toString() {
		return "Author [id=" + id + ", firstName=" + firstName + ", middleName=" + middleName + ", lastName=" + lastName
				+ ", about=" + about + "]";
	}

	public Author(String firstName, String middleName, String lastName, String about) {
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.about = about;
	}

	public Author() {
	}
}
