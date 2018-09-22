package com.moveltrack.cartaoprograma.rest.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.moveltrack.cartaoprograma.model.GeoEndereco;

@Repository
public interface GeoEnderecoRep extends JpaRepository<GeoEndereco,Long>, JpaSpecificationExecutor<GeoEndereco>, GeoEnderecoRepCustom {

	

	


}
