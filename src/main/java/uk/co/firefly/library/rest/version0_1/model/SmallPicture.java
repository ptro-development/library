package uk.co.firefly.library.rest.version0_1.model;

import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.annotations.ApiModelProperty;

@Entity
public class SmallPicture extends Picture {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Lob
	@Column(name = "data", columnDefinition = "BLOB")
	@JsonIgnore
	@NotNull
	private byte[] data;

	@OneToOne(mappedBy = "smallPicture")
	@JsonIgnore
	@NotNull
	PictureSummary pictureSummary;

	@ApiModelProperty
	@Transient
	private Long pictureSummaryId;

	public Long getPictureSummaryId() {
		return pictureSummaryId;
	}

	public void setPictureSummaryId(Long pictureSummaryId) {
		this.pictureSummaryId = pictureSummaryId;
	}

	public SmallPicture() {
	}

	public SmallPicture(@NotNull byte[] data, @NotNull PictureSummary pictureSummary) {
		this.data = data;
		this.pictureSummary = pictureSummary;
	}

	@Override
	public void setPictureSummary(PictureSummary pictureSummary) {
		this.pictureSummary = pictureSummary;
	}
	
	@Override
	public PictureSummary getPictureSummary() {
		return pictureSummary;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public byte[] getData() {
		return data;
	}

	@Override
	public void setData(byte[] data) {
		this.data = data;
	}

	public SmallPicture updateTransitiveAttributes() {
		if (this.pictureSummary != null)
			this.pictureSummaryId = this.pictureSummary.getId();
		return this;
	}

}
