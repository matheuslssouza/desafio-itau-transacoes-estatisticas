package com.itau.desafio.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransacaoRequest {

    @NotNull(message = "O campo valor é obrigatório.")
    private BigDecimal valor;

    @NotNull(message = "O campo dataHora é obrigatório.")
    private OffsetDateTime dataHora;
}
