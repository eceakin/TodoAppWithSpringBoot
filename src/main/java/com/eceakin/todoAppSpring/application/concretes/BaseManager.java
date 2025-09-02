package com.eceakin.todoAppSpring.application.concretes;

import java.util.List;
import java.util.stream.Collectors;


import com.eceakin.todoAppSpring.application.services.BaseService;
import com.eceakin.todoAppSpring.infrastructure.repositories.BaseRepository;
import com.eceakin.todoAppSpring.shared.mappers.ModelMapperService;

public abstract class BaseManager<
T, // Entity
GetAllResponse,
GetByIdResponse,
CreateRequest,
UpdateRequest
> implements BaseService<GetAllResponse, GetByIdResponse, CreateRequest, UpdateRequest> {

protected final BaseRepository<T, Integer> repository;
protected final ModelMapperService mapperService;
private final Class<T> entityClass;
private final Class<GetAllResponse> getAllResponseClass;
private final Class<GetByIdResponse> getByIdResponseClass;

public BaseManager(BaseRepository<T, Integer> repository, ModelMapperService mapperService, Class<T> entityClass, Class<GetAllResponse> getAllResponseClass, Class<GetByIdResponse> getByIdResponseClass) {
this.repository = repository;
this.mapperService = mapperService;
this.entityClass = entityClass;
this.getAllResponseClass = getAllResponseClass;
this.getByIdResponseClass = getByIdResponseClass;
}

@Override
public List<GetAllResponse> getAll() {
List<T> entities = repository.findAll();
return entities.stream()
        .map(entity -> this.mapperService.forResponse().map(entity, getAllResponseClass))
        .collect(Collectors.toList());
}

@Override
public GetByIdResponse getById(int id) {
T entity = repository.findById(id).orElseThrow(() -> new RuntimeException("Entity not found with id: " + id)); // Kendi exception'ını kullanabilirsin
return this.mapperService.forResponse().map(entity, getByIdResponseClass);
}

@Override
public void add(CreateRequest createRequest) {
T entity = this.mapperService.forRequest().map(createRequest, entityClass);
this.repository.save(entity);
}

@Override
public void update(int id, UpdateRequest updateRequest) {
	  T existingEntity = repository.findById(id)
		        .orElseThrow(() -> new RuntimeException("Entity not found with id: " + id));
		    
		    // Sadece değişmesi gereken alanları güncelle
		    mapperService.forRequest().map(updateRequest, existingEntity);
		    
		    this.repository.save(existingEntity);
}

@Override
public void delete(int id) {
this.repository.deleteById(id);
}
}