package com.AI.TPO.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.AI.TPO.entity.Registro;

@Repository
public interface RegistroRepositorio extends JpaRepository<Registro,Integer>{

}
