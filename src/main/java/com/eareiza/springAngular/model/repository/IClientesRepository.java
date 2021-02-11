package com.eareiza.springAngular.model.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eareiza.springAngular.model.entity.Cliente;
import com.eareiza.springAngular.model.entity.Region;

@Repository
public interface IClientesRepository extends JpaRepository<Cliente, Integer> {

	@Query("from Region")
	public List<Region> findAllRegiones();
	Page<Cliente> findAllById(Integer id, Pageable pageable);
}
