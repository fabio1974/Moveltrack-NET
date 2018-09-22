package com.moveltrack.cartaoprograma.rest.api.repository.st500;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.moveltrack.cartaoprograma.model.st500.Location2;



@Repository
public interface Location2Rep extends JpaRepository<Location2,Long>, JpaSpecificationExecutor<Location2>  {

	


}
