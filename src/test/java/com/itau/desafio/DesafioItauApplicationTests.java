package com.itau.desafio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itau.desafio.dto.TransacaoRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DesafioItauApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc.perform(delete("/transacao"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve registrar transação válida e retornar 201 Created")
    void deveRegistrarTransacaoValida() throws Exception {
        TransacaoRequest request = TransacaoRequest.builder()
                .valor(new BigDecimal("150.00"))
                .dataHora(OffsetDateTime.now().minusSeconds(10))
                .build();

        mockMvc.perform(post("/transacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Deve rejeitar com 422 Unprocessable Entity quando dataHora estiver no futuro")
    void deveRejeitarDataNoFuturo() throws Exception {
        TransacaoRequest request = TransacaoRequest.builder()
                .valor(new BigDecimal("100.00"))
                .dataHora(OffsetDateTime.now().plusSeconds(60))
                .build();

        mockMvc.perform(post("/transacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Deve rejeitar com 422 Unprocessable Entity quando valor for negativo")
    void deveRejeitarValorNegativo() throws Exception {
        TransacaoRequest request = TransacaoRequest.builder()
                .valor(new BigDecimal("-10.00"))
                .dataHora(OffsetDateTime.now())
                .build();

        mockMvc.perform(post("/transacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Deve rejeitar com 422 Unprocessable Entity quando transação for mais antiga que 60 segundos")
    void deveRejeitarTransacaoMaisAntigaQue60Segundos() throws Exception {
        TransacaoRequest request = TransacaoRequest.builder()
                .valor(new BigDecimal("50.00"))
                .dataHora(OffsetDateTime.now().minusSeconds(65))
                .build();

        mockMvc.perform(post("/transacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Deve calcular estatísticas dos últimos 60 segundos com sucesso")
    void deveCalcularEstatisticasComSucesso() throws Exception {
        // Insere 2 transações dentro dos últimos 60s
        TransacaoRequest t1 = TransacaoRequest.builder()
                .valor(new BigDecimal("100.00"))
                .dataHora(OffsetDateTime.now().minusSeconds(5))
                .build();
        TransacaoRequest t2 = TransacaoRequest.builder()
                .valor(new BigDecimal("200.00"))
                .dataHora(OffsetDateTime.now().minusSeconds(15))
                .build();

        mockMvc.perform(post("/transacao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(t1)));

        mockMvc.perform(post("/transacao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(t2)));

        mockMvc.perform(get("/estatistica"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count", is(2)))
                .andExpect(jsonPath("$.sum", is(300.0)))
                .andExpect(jsonPath("$.avg", is(150.0)))
                .andExpect(jsonPath("$.min", is(100.0)))
                .andExpect(jsonPath("$.max", is(200.0)));
    }
}
