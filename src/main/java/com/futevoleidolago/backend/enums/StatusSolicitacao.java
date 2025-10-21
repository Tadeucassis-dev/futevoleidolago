package com.futevoleidolago.backend.enums;

public enum StatusSolicitacao {
    PENDENTE("Aguardando aprovação"),
    APROVADO("Solicitação aprovada"),
    REJEITADO("Solicitação rejeitada");

    private final String descricao;

    StatusSolicitacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}