//repository Interface
package com.nit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nit.entity.UserDetails;

public interface IUserDetailsRepository extends JpaRepository<UserDetails, Integer> {

	public UserDetails   findByEmailAndPassword(String email, String pwd);
	public UserDetails   findByNameAndEmail(String name, String email);
}
