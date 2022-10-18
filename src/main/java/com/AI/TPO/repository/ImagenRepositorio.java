package com.AI.TPO.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.AI.TPO.entity.Imagen;

@Repository
public interface ImagenRepositorio extends JpaRepository<Imagen, Integer>{

}
