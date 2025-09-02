package com.eceakin.todoAppSpring.application.services;

import java.util.List;

public interface BaseService<GetAllResponse, GetByIdResponse, CreateRequest, UpdateRequest> {
	List<GetAllResponse> getAll();

	GetByIdResponse getById(int id);

	void add(CreateRequest createRequest);

	  void update(int id, UpdateRequest updateRequest);

	void delete(int id);
}
