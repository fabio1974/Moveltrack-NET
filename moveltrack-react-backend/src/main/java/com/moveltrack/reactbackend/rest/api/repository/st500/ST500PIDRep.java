package com.moveltrack.reactbackend.rest.api.repository.st500;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.moveltrack.reactbackend.model.st500.ST500PID;



@Repository
public interface ST500PIDRep extends JpaRepository<ST500PID,Long>{

	ST500PID findTopBySerialOrderByDeviceDateDesc(String serial);


	ST500PID findTopBySerialOrderByDeviceDate(String serial);

	
	


}
