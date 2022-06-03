package com.unicap.os.resources.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError {
	private static final long serialVersionUID = 1L;

	private List<FieldMessage> errors = new ArrayList<>();

	public ValidationError() {
		super();
	}

	public ValidationError(Integer status, Long timestamp, String error) {
		super(status, timestamp, error);
	}

	public List<FieldMessage> getList() {
		return errors;
	}

	public void addError(String field, String message) {
		this.errors.add(new FieldMessage(field, message));
	}

}
