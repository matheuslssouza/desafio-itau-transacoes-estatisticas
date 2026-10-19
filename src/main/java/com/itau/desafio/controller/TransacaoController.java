package com.itau.desafio.controller;

import com.itau.desafio.dto.TransacaoRequest;
import com.itau.desafio.service.TransacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transacao")
@RequiredArgsConstructor
public class TransacaoController {

    private final TransacaoService transacaoService;

    @PostMapping
    public ResponseEntity<Void> criarTransacao(@Valid @RequestBody TransacaoRequest request) {
        transacaoService.adicionarTransacao(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> limparTransacoes() {
        transacaoService.limparTransacoes();
        return ResponseEntity.ok().build();
    }
}
