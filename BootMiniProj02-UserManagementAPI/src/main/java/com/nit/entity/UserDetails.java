//Entity.java
package com.nit.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@Table(name="JRTP_USER_DETAILS")
@AllArgsConstructor
@NoArgsConstructor
public class UserDetails {
	@Id
	//@GeneratedValue(strategy=GenerationType.IDENTITY)
	@NonNull
	private Integer userId;
	@Column(length=30)
	private String   name;
	@Column(length=30)
      private String   password;
	@Column(length=10)
      private String   gender;
	@Column(length=40,unique=true,nullable=false)
      private String  email;
      private Long    mobileNo;
      @Column(length=40)
      private Long  aadharNo;
      private LocalDate    dob;
      @Column(length=10)
      private String active_sw;
      
      
      @CreationTimestamp
      @Column(insertable=true, updatable=false)
      private LocalDateTime creationDate;
      @UpdateTimestamp
      @Column(insertable=false, updatable=true)
      private LocalDateTime updationDate;
      
      @Column(length=20)
      private String createdBy;
      @Column(length=20)
      private String updatedBy;
}
