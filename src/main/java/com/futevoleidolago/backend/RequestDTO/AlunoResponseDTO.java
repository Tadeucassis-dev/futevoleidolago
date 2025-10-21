package com.futevoleidolago.backend.RequestDTO;

import com.futevoleidolago.backend.enums.StatusSolicitacao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class AlunoResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private LocalDate dataNascimento;
    private Integer idade;
    private String instituicaoEnsino;
    private StatusSolicitacao statusSolicitacao;
    private LocalDateTime dataSolicitacao;
    private LocalDateTime dataAprovacao;
    private String motivoRejeicao;
    private Boolean ativo;
}