package ru.arcadoss.collagemaker.json;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Created by arcturus at 04.07.14
 */
public enum ErrorType {
	NONE(null), NOT_ALLOWED("APINotAllowedError"), OTHER("");
	private final String msg;

	ErrorType(String msg) {
		this.msg = msg;
	}

	@JsonCreator
	public static ErrorType creator(String msg) {
		if (msg == null) {
			return NONE;
		}

		for (ErrorType type : ErrorType.values()) {
			if (msg.equals(type.msg)) {
				return type;
			}
		}

		return OTHER;
	}
}
