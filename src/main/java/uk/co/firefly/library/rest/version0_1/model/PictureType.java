package uk.co.firefly.library.rest.version0_1.model;

import java.util.Arrays;

public enum PictureType {
	FRONT_COVER, BACK_COVER, PAGE;
	
	public static boolean hasValue(String value) {
		return Arrays.asList(PictureType.values())
				.stream().anyMatch(p -> p.name().equals(value));
	}
}
