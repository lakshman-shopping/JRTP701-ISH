package com.nit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nit.entity.CoTriggersEntity;

public interface ICoTriggersRepository extends JpaRepository<CoTriggersEntity, Integer> {
     public  List<CoTriggersEntity>   findByTriggerStatus(String status);
     public CoTriggersEntity  findByCaseNo(int caseNo);
}
