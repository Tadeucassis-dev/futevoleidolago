package com.futevoleidolago.backend.models;

import com.futevoleidolago.backend.enums.StatusSolicitacao;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "aluno")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Aluno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    private String email;
    private String telefone;
    private LocalDate dataNascimento;
    private Integer idade;
    private String instituicaoEnsino;
    
    @Enumerated(EnumType.STRING)
    private StatusSolicitacao statusSolicitacao;
    
    private LocalDateTime dataSolicitacao;
    private LocalDateTime dataAprovacao;
    private String motivoRejeicao;
    private Boolean ativo;
}
