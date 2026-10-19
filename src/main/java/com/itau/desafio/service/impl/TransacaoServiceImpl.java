package com.itau.desafio.service.impl;

import com.itau.desafio.dto.EstatisticaResponse;
import com.itau.desafio.dto.TransacaoRequest;
import com.itau.desafio.service.TransacaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.DoubleSummaryStatistics;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Slf4j
@Service
public class TransacaoServiceImpl implements TransacaoService {

    // Coleção thread-safe para armazenar as transações em memória
    private final Queue<TransacaoRequest> transacoes = new ConcurrentLinkedQueue<>();


    @Override
    public void adicionarTransacao(TransacaoRequest request) {
        log.info("Recebida transação para processamento: {}", request);

        // TODO: Sua vez de implementar!
        // 1. Validar se o valor é negativo (< 0) -> se for, lançar IllegalArgumentException
        if(request.getValor().compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("O valor não pode ser negativo.");

        OffsetDateTime now = OffsetDateTime.now();
        // 2. Validar se a dataHora está no futuro -> se for, lançar IllegalArgumentException
        if(request.getDataHora().isAfter(now)) throw new IllegalArgumentException("A data não pode ser no futuro");
        
        // 3. Validar se a dataHora ocorreu há mais de 60 segundos -> se for, lançar IllegalArgumentException
        if(request.getDataHora().isBefore(now.minusSeconds(60))) throw new IllegalArgumentException("A data ocorreu há mais de 60 segundos");


        // 4. Se passar em todas as validações, adicionar à coleção 'transacoes'
        transacoes.add(request);
    }

    @Override
    public void limparTransacoes() {
        // TODO: Limpar todas as transações da memória
        transacoes.clear();
    }

    @Override
    public EstatisticaResponse calcularEstatisticas() {
        // TODO: Calcular estatísticas dos últimos 60 segundos
        // Dica: Use stream(), filter() pegando apenas transações dos últimos 60s,
        
        OffsetDateTime now = OffsetDateTime.now();

        DoubleSummaryStatistics stats = transacoes.stream()
                .filter(valor -> !valor.getDataHora().isBefore(now.minusSeconds(60)))
                .mapToDouble(transacao -> transacao.getValor().doubleValue()).summaryStatistics();

        return EstatisticaResponse.fromDoubleSummaryStatistics(stats);
    }
}
