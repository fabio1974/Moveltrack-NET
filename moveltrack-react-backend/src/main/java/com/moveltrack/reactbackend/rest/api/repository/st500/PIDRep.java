package com.moveltrack.reactbackend.rest.api.repository.st500;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.moveltrack.reactbackend.model.st500.PID;



@Repository
public interface PIDRep extends JpaRepository<PID,Long>{

	


}
