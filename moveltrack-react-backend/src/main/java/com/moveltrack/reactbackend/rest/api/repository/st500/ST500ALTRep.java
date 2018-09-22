package com.moveltrack.reactbackend.rest.api.repository.st500;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.moveltrack.reactbackend.model.st500.ST500ALT;



@Repository
public interface ST500ALTRep extends JpaRepository<ST500ALT,Long>{

	List<ST500ALT> findTop10BySerialOrderByDeviceDateDesc(String serial);

	


}
