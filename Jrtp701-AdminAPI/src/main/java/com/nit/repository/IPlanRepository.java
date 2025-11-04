package com.nit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nit.entity.PlanEntity;

public interface IPlanRepository extends JpaRepository<PlanEntity, Integer> {

}
