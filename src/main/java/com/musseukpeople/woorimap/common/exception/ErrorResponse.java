package com.musseukpeople.woorimap.common.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

@Getter
public class ErrorResponse {

	private final String message;
	private final String code;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private final List<FieldError> errors;

	private ErrorResponse(ErrorCode code, List<FieldError> errors) {
		this.message = code.getMessage();
		this.code = code.getCode();
		this.errors = errors;
	}

	public static ErrorResponse of(ErrorCode code, BindingResult bindingResult) {
		return new ErrorResponse(code, FieldError.of(bindingResult));
	}

	public static ErrorResponse of(ErrorCode code) {
		return new ErrorResponse(code, null);
	}

	@Getter
	public static class FieldError {
		private final String field;
		private final String value;
		private final String reason;

		private FieldError(String field, String value, String reason) {
			this.field = field;
			this.value = value;
			this.reason = reason;
		}

		private static List<FieldError> of(final BindingResult bindingResult) {
			return bindingResult.getFieldErrors().stream()
				.map(error -> new FieldError(
					error.getField(),
					error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
					error.getDefaultMessage()))
				.collect(Collectors.toList());
		}
	}
}