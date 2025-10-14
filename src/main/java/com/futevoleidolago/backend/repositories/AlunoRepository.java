package com.futevoleidolago.backend.repositories;

import com.futevoleidolago.backend.enums.StatusSolicitacao;
import com.futevoleidolago.backend.models.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import java.util.List;
import java.util.Optional;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
<<<<<<< HEAD
    Optional<Aluno> findByEmail(String email);
    List<Aluno> findByStatusSolicitacao(StatusSolicitacao status);
    List<Aluno> findByAtivoTrue();
    List<Aluno> findByAtivoFalse();
}
=======
    List<Aluno> findAllByAtivoTrue();
}
>>>>>>> cfe24039d74685826ed4fe75cb3b3e5f88051e03
