package com.futevoleidolago.backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "checkin")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Checkin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_aluno", nullable = false)
    private Aluno idAluno;
    private LocalDateTime dataHora;

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
