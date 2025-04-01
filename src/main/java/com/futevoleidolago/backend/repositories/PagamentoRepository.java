package com.futevoleidolago.backend.repositories;

import com.futevoleidolago.backend.models.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    List<Pagamento> findByIdAlunoId(Long id);
}