package com.futevoleidolago.backend.repositories;

import com.futevoleidolago.backend.models.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
}
