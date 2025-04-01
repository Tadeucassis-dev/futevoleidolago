package com.futevoleidolago.backend.repositories;

import com.futevoleidolago.backend.models.Checkin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheckinRepository extends JpaRepository<Checkin, Long> {
    List<Checkin> findByIdAlunoId(Long id);
}

