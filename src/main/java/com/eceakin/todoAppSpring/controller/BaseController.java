package com.eceakin.todoAppSpring.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.eceakin.todoAppSpring.application.services.BaseService;

import java.util.List;

public abstract class BaseController<
        GetAllResponse,
        GetByIdResponse,
        CreateRequest,
        UpdateRequest> {

    protected final BaseService<GetAllResponse, GetByIdResponse, CreateRequest, UpdateRequest> service;

    public BaseController(BaseService<GetAllResponse, GetByIdResponse, CreateRequest, UpdateRequest> service) {
        this.service = service;
    }

    @GetMapping
    public List<GetAllResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public GetByIdResponse getById(@PathVariable int id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public void add(@RequestBody @Valid CreateRequest createRequest) {
        service.add(createRequest);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable int id, @RequestBody @Valid UpdateRequest updateRequest) {
        service.update(id, updateRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        service.delete(id);
    }
}