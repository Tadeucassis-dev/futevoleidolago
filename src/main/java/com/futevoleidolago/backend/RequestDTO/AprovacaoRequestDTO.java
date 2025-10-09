package com.futevoleidolago.backend.RequestDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AprovacaoRequestDTO {
    private Boolean aprovado;
    private String motivoRejeicao; // Opcional, usado apenas se rejeitado
}