package com.futevoleidolago.backend.RequestDTO;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CadastroAlunoRequestDTO {
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @Email(message = "Email deve ser válido")
    @NotBlank(message = "Email é obrigatório")
    private String email;

    private String telefone;

    @Past(message = "Data de nascimento deve ser no passado")
    @NotNull(message = "Data de nascimento é obrigatória")
    private LocalDate dataNascimento;

    // Idade será calculada automaticamente a partir da data de nascimento
    private Integer idade;

    @NotBlank(message = "Instituição de ensino é obrigatória")
    @Size(min = 2, max = 200, message = "Instituição de ensino deve ter entre 2 e 200 caracteres")
    private String instituicaoEnsino;
}