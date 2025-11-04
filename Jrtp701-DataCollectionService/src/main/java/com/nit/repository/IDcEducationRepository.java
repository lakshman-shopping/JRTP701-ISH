package com.nit.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.nit.entity.DcEducationEntity;

public interface IDcEducationRepository extends JpaRepository<DcEducationEntity, Integer> {
    public  DcEducationEntity  findByCaseNo(int caseNo);
}
