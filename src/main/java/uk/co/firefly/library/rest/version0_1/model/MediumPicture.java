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
public class MediumPicture extends Picture {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Lob
	@Column(name = "data", columnDefinition = "MEDIUMBLOB")
	@JsonIgnore
	@NotNull
	private byte[] data;

	@OneToOne(mappedBy = "mediumPicture")
	@JsonIgnore
	@NotNull
	PictureSummary pictureSummary;

	@ApiModelProperty
	@Transient
	private Long pictureSummaryId;

	public Long getPictureSummaryId() {
		return pictureSummaryId;
	}

	public void setPictureSummaryId(Long pictureId) {
		this.pictureSummaryId = pictureId;
	}

	@Override
	public PictureSummary getPictureSummary() {
		return pictureSummary;
	}

	@Override
	public void setPictureSummary(PictureSummary pictureSummary) {
		this.pictureSummary = pictureSummary;
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

	public MediumPicture() {
	}

	public MediumPicture(@NotNull byte[] data, @NotNull PictureSummary pictureSummary) {
		this.data = data;
		this.pictureSummary = pictureSummary;
	}
	
	public MediumPicture updateTransitiveAttributes() {
		if (this.pictureSummary != null)
			this.pictureSummaryId = this.pictureSummary.getId();
		return this;
	}

}
