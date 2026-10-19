# Desafio Técnico Backend: Itaú Unibanco (Nível Pleno)

Este é o **desafio técnico oficial e clássico** aplicado pelo Itaú Unibanco em seus processos seletivos para Engenharia de Software Backend (Java / Spring Boot).

---

## 🎯 Objetivo
Construir uma **API REST em Spring Boot** de alta performance e concorrência para receber transações financeiras e calcular estatísticas em tempo real baseadas na janela temporal dos **últimos 60 segundos**.

---

## ⚠️ Regras e Restrições Técnicas Cruciais
1. **Sem Banco de Dados:** Não é permitido o uso de nenhum banco de dados (nem Postgres, MySQL, nem MongoDB ou Redis). Todos os dados devem ser mantidos **estritamente em memória**.
2. **Concorrência e Thread-Safety:** A aplicação receberá requisições simultâneas em múltiplas threads. Suas estruturas de dados devem ser thread-safe (ex: coleções concorrentes, locks, atomicidade).
3. **Desempenho da API de Estatísticas:** A consulta de estatísticas (`GET /estatistica`) deve responder em tempo mínimo (idealmente $O(1)$ ou complexidade linear muito baixa).
4. **Precisão de Moeda e Casas Decimais:** Valores monetários e cálculos estatísticos.

---

## 📌 Endpoints Obrigatórios

### 1. `POST /transacao`
Recebe uma nova transação financeira.

**Payload:**
```json
{
  "valor": 120.50,
  "dataHora": "2026-10-15T12:30:00.000-03:00"
}
```

**Respostas esperadas:**
* **`201 Created`**: Transação gravada com sucesso (sem corpo de resposta).
* **`422 Unprocessable Entity`**: Transação rejeitada pelas seguintes regras de negócio:
  * O `valor` é negativo (`< 0`).
  * A `dataHora` está no futuro (posterior a `OffsetDateTime.now()`).
  * A transação ocorreu há mais de 60 segundos (fora da janela analisável).
* **`400 Bad Request`**: Payload JSON malformado, campos nulos ou formato de data inválido.

---

### 2. `DELETE /transacao`
Limpa todas as transações armazenadas em memória.

**Respostas esperadas:**
* **`200 OK`**: Todas as transações foram apagadas com sucesso (sem corpo de resposta).

---

### 3. `GET /estatistica`
Retorna as métricas estatísticas calculadas **exclusivamente sobre as transações que ocorreram nos últimos 60 segundos**.

**Resposta esperada (`200 OK`):**
```json
{
  "count": 10,
  "sum": 1234.56,
  "avg": 123.456,
  "min": 12.34,
  "max": 567.89
}
```

* **Caso não haja nenhuma transação nos últimos 60 segundos:**
```json
{
  "count": 0,
  "sum": 0.0,
  "avg": 0.0,
  "min": 0.0,
  "max": 0.0
}
```

---

## 🚀 Como Executar e Testar

1. No terminal nesta pasta, execute a aplicação:
   ```powershell
   .\mvnw.cmd spring-boot:run
   ```
2. Para rodar a suíte de testes unitários e de integração:
   ```powershell
   .\mvnw.cmd test
   ```

---

## 💡 O Que os Entrevistadores de Big Tech Avaliam Neste Desafio (Nível Pleno):
* **Thread-Safety & Race Conditions:** Como você evita que duas requisições simultâneas corrompam a contagem de estatísticas.
* **Manipulação de Data/Hora:** Uso correto da API java.time (`OffsetDateTime` com fuso horário).
* **Uso da API Stream e Estatísticas:** Utilização de `DoubleSummaryStatistics` ou estruturas de janela deslizante (sliding window).
* **Tratamento de Exceções:** `@RestControllerAdvice` retornando exatamente os códigos HTTP exigidos (`201`, `422`, `400`, `200`).
* **Testes Automatizados:** Testes unitários com JUnit 5 / MockMvc simulando datas futuras, transações expiradas e concorrência.
