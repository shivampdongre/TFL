package com.trust.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trust.server.entities.CustomerDetails;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerDetails, String>{

}
