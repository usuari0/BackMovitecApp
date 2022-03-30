package com.movitec.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.movitec.app.entity.Plan;
import com.movitec.app.repository.PlanRepository;

@Service
public class PlanServiceImpl implements PlanService{

	@Autowired
	private PlanRepository planRepository;
	
	@Transactional(readOnly = true)
	@Override
	public Iterable<Plan> findAll() {
		return planRepository.findAll();
	}
}
