package uk.co.firefly.library.rest.version0_1.model;

import java.util.Arrays;

public enum PictureSize {
	SMALL, MEDIUM;
	
	public static boolean hasValue(String value) {
		return Arrays.asList(PictureSize.values())
				.stream().anyMatch(p -> p.name().equals(value));
	}
}
