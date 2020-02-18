package uk.co.firefly.library.rest.version0_1.model;

import javax.validation.constraints.NotNull;

public abstract class Picture {
	
	public Picture() {}
	public Picture(@NotNull byte[] data, @NotNull PictureSummary pictureSummary) {}

	public abstract void setPictureSummary(PictureSummary pictureSummary);
	public abstract PictureSummary getPictureSummary();
	public abstract Long getId();
	public abstract void setId(Long id);
	public abstract byte[] getData();
	public abstract void setData(byte[] data);	
}
