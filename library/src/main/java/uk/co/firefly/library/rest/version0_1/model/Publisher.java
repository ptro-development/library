package uk.co.firefly.library.rest.version0_1.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;

@Entity
public class Publisher {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ApiModelProperty(required = true)
	@NotBlank
	@NotNull
	@Size(min = 1, max = 60)
	@Column(length = 60)
	private String name;
	
	@ApiModelProperty(required = false)
	@Size(max = 255)
	@Column(length = 255)
	private String address;
	
	@ApiModelProperty(required = false)
	@Size(max = 255)
	@Column(length = 255)
	private String webAddress;
	
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getWebAddress() {
		return webAddress;
	}
	public void setWebAddress(String webAddress) {
		this.webAddress = webAddress;
	}
	
	@Override
	public String toString() {
		return "Publisher [id=" + id + ", name=" + name + ", address=" + address + ", webAddress=" + webAddress + "]";
	}
	public Publisher(String name, String address, String web_address) {
		this.name = name;
		this.address = address;
		this.webAddress = webAddress;
	}
	
	public Publisher() {}
	
}
