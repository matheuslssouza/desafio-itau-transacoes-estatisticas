package com.itau.desafio.service;

import com.itau.desafio.dto.EstatisticaResponse;
import com.itau.desafio.dto.TransacaoRequest;

public interface TransacaoService {

    void adicionarTransacao(TransacaoRequest request);

    void limparTransacoes();

    EstatisticaResponse calcularEstatisticas();
}
