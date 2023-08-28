package com.example.sa4.controller;

import com.example.sa4.model.ContaCorrentePF;
import org.springframework.data.repository.CrudRepository;

public interface BancoRepository extends CrudRepository<ContaCorrentePF, Long> {
}