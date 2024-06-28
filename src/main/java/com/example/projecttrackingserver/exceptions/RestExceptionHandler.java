package com.example.projecttrackingserver.exceptions;

import java.time.format.DateTimeParseException;
import java.util.HashMap;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Global exception handler for REST controllers.
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

       /**
        * Handles EntityNotFoundException and returns a 404 Not Found response.
        *
        * @param ex the EntityNotFoundException to handle
        * @return ResponseEntity with the exception message and HTTP status 404 Not Found
        */
	   @ExceptionHandler(value = EntityNotFoundException.class)
	   protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
		   return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	   }
	   
	   /**
	    * Handles UnauthorizedException and returns a 401 Unauthorized response.
	    *
	    * @param ex the UnauthorizedException to handle
	    * @return ResponseEntity with the exception message and HTTP status 401 Unauthorized
	    */
	   @ExceptionHandler(value = UnauthorizedException.class)
	   protected ResponseEntity<Object> handleUnauthorized(UnauthorizedException ex) {
		   return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
	   }
	   
	   /**
	    * Handles EntityAlreadyExistsException and returns a 409 Conflict response.
	    *
	    * @param ex the EntityAlreadyExistsException to handle
	    * @return ResponseEntity with the exception message and HTTP status 409 Conflict
	    */
	   @ExceptionHandler(value = EntityAlreadyExistsException.class)
	   protected ResponseEntity<Object> handleEntityAlreadyExists(EntityAlreadyExistsException ex) {
		   return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
	   }
	   
	   /**
	    * Handles DateTimeParseException and returns a 409 Conflict response.
	    *
	    * @param ex the DateTimeParseException to handle
	    * @return ResponseEntity with the exception message and HTTP status 409 Conflict
	    */
	   @ExceptionHandler(value = DateTimeParseException.class)
	   protected ResponseEntity<Object> handleDateTimeParse(DateTimeParseException ex) {
		   return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
	   }
	   
	   /**
	    * Handles ValueNotAllowedException and returns a 400 Bad Request response.
	    *
	    * @param ex the ValueNotAllowedException to handle
	    * @return ResponseEntity with the exception message and HTTP status 400 Bad Request
	    */
	   @ExceptionHandler(value = ValueNotAllowedException.class)
	   protected ResponseEntity<Object> handleValueNotAllowed(ValueNotAllowedException ex) {
		   return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	   }
	   
	   /**
	    * {@inheritDoc}
	    */
	   @Override
	   public ResponseEntity<Object> handleMethodArgumentNotValid(
			   MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request
	   ) {
		   HashMap<String, String> errors = new HashMap<>();
		   ex.getBindingResult().getAllErrors().forEach(error -> {
			   String fieldName = ((FieldError) error).getField();
			   String errorMsg = error.getDefaultMessage();
			   errors.put(fieldName, errorMsg);
		   });
		   return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	   }
}
