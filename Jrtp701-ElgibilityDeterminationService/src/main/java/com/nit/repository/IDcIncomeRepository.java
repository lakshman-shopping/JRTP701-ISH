package com.nit.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.nit.entity.DcIncomeEntity;

public interface IDcIncomeRepository extends JpaRepository<DcIncomeEntity, Integer> {
     public  DcIncomeEntity  findByCaseNo(int casNo);
}
