package com.moveltrack.cartaoprograma.rest.api.repository.st500;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.moveltrack.cartaoprograma.model.st500.Location;



@Repository
public interface LocationRep extends JpaRepository<Location,Long>, JpaSpecificationExecutor<Location>, LocationRepCustom {

	


}
