package com.movitec.app.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.movitec.app.entity.Plan;

@Repository
public interface PlanRepository extends CrudRepository<Plan, Integer>{
	
}
