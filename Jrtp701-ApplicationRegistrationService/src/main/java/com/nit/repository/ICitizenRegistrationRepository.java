package com.nit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nit.entity.CitizenAppRegistrationEntity;

public interface ICitizenRegistrationRepository extends JpaRepository<CitizenAppRegistrationEntity, Integer> {

}
