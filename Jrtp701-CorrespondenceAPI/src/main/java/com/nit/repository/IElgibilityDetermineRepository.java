package com.nit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nit.entity.ElgibilityDetailsEntity;

public interface IElgibilityDetermineRepository extends JpaRepository<ElgibilityDetailsEntity, Integer> {
    public ElgibilityDetailsEntity  findByCaseNo(Integer caseNo);
}
