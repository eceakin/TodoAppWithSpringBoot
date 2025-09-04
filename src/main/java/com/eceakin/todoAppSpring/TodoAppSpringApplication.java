package com.eceakin.todoAppSpring;

import java.util.HashMap;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.eceakin.todoAppSpring.shared.exceptions.BusinessException;
import com.eceakin.todoAppSpring.shared.exceptions.ProblemDetails;
import com.eceakin.todoAppSpring.shared.exceptions.ValidationProblemDetails;


@RestControllerAdvice
@SpringBootApplication(scanBasePackages = "com.eceakin.todoAppSpring")
@EntityScan("com.eceakin.todoAppSpring.entities")
@EnableJpaRepositories("com.eceakin.todoAppSpring.infrastructure.repositories")
public class TodoAppSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoAppSpringApplication.class, args);
	}


	@ExceptionHandler
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ProblemDetails handleBusinessException(BusinessException businessException) {
		ProblemDetails problemDetails = new ProblemDetails();
		problemDetails.setMessage(businessException.getMessage());
		return problemDetails;
	}

	@ExceptionHandler
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ProblemDetails handleValidationException(MethodArgumentNotValidException methodArgumentNotValidException) {
		ValidationProblemDetails problemDetails = new ValidationProblemDetails();
		problemDetails.setMessage("VALIDATION.EXCEPTION");
		problemDetails.setValidationErrors(new HashMap<String,String>());
		
		for (FieldError fieldError : methodArgumentNotValidException.getBindingResult().getFieldErrors()) {
			problemDetails.getValidationErrors().put(fieldError.getField(), fieldError.getDefaultMessage());
		}
		
		
		return problemDetails;
	}

	@Bean
	public ModelMapper getModelMapper() {
		return new ModelMapper();
	}

}
