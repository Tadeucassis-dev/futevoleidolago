package com.futevoleidolago.backend.controllers;

import com.futevoleidolago.backend.models.Checkin;
import com.futevoleidolago.backend.repositories.CheckinRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/checkins")
public class CheckinController {

    private final CheckinRepository checkinRepository;

    public CheckinController(CheckinRepository checkinRepository) {
        this.checkinRepository = checkinRepository;
    }

    @PostMapping
    public Checkin criarCheckin(@RequestBody Checkin checkin) {
        checkin.setDataHora(LocalDateTime.now());
        return checkinRepository.save(checkin);
    }

    @GetMapping("/{idAluno}")
    public List<Checkin> listarCheckins(@PathVariable Long idAluno) {
        return checkinRepository.findByIdAlunoId(idAluno);
    }
}