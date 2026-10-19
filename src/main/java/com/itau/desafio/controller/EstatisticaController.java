package com.itau.desafio.controller;

import com.itau.desafio.dto.EstatisticaResponse;
import com.itau.desafio.service.TransacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/estatistica")
@RequiredArgsConstructor
public class EstatisticaController {

    private final TransacaoService transacaoService;

    @GetMapping
    public ResponseEntity<EstatisticaResponse> obterEstatisticas() {
        return ResponseEntity.ok(transacaoService.calcularEstatisticas());
    }
}
