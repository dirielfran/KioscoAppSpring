package com.eareiza.springAngular.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eareiza.springAngular.model.entity.Inventario;

public interface IInventarioRepository extends JpaRepository<Inventario, Long> {

}
