package com.futevoleidolago.backend.repositories;

import com.futevoleidolago.backend.enums.StatusSolicitacao;
import com.futevoleidolago.backend.models.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    Optional<Aluno> findByEmail(String email);
    List<Aluno> findByStatusSolicitacao(StatusSolicitacao status);
    List<Aluno> findByAtivoTrue();
    List<Aluno> findByAtivoFalse();
    List<Aluno> findAllByAtivoTrue();
}
