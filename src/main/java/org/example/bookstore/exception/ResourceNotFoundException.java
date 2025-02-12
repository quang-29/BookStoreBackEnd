package org.example.bookstore.exception;

import java.util.UUID;

public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	String resourceName;
	String field;
	String fieldName;
	Long fieldId;

	public ResourceNotFoundException() {
	}

	public ResourceNotFoundException(String resourceName, String field, String fieldName) {
		super(String.format("%s not found with %s: %s", resourceName, field, fieldName));
		this.resourceName = resourceName;
		this.field = field;
		this.fieldName = fieldName;
	}

//	public ResourceNotFoundException(String resourceName, String field, int fieldId) {
//		super(String.format("%s not found with %s: %d", resourceName, field, fieldId));
//		this.resourceName = resourceName;
//		this.field = field;
//		this.fieldId = fieldId;
//	}
	public ResourceNotFoundException(String resourceName, String field, Long fieldId) {
		super(String.format("%s not found with %s: %d", resourceName, field, fieldId));
		this.resourceName = resourceName;
		this.field = field;
		this.fieldId = fieldId;
	}

	public ResourceNotFoundException(String resourceName, String field, UUID id) {
		super(String.format("%s not found with %s: %s", resourceName, field, id));
		this.resourceName = resourceName;
		this.field = field;
		this.fieldName = id.toString();
	}


}
