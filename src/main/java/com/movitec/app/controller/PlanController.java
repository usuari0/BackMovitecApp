package com.movitec.app.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movitec.app.entity.Plan;
import com.movitec.app.service.PlanService;

@RestController
@RequestMapping("/api/planes")
public class PlanController {
	
	@Autowired
	private PlanService planService;
	
	//@PreAuthorize("hasRole('ADMIN')")
	@GetMapping()
	public List<Plan> traerTodo(){
		return StreamSupport.stream(planService.findAll().spliterator(), false)
				.collect(Collectors.toList());
	}
}
