```mermaid
flowchart TD
    N1["1 - Inicio<br>desconto = 0"]
    N2{"2 - valor >= 500?"}
    N3["3 - desconto = 10"]
    N4{"4 - clienteVip?"}
    N5["5 - desconto += 5"]
    N6{"6 - !pagamentoAprovado?"}
    N7["7 - return PAGAMENTO RECUSADO"]
    N8["8 - valorFinal = valor - (valor * desconto / 100)"]
    N9["9 - return PEDIDO APROVADO: valorFinal"]
    N10(["10 - Fim"])

    N1 --> N2
    N2 -->|Verdadeiro| N3
    N2 -->|Falso| N4
    N3 --> N4
    N4 -->|Verdadeiro| N5
    N4 -->|Falso| N6
    N5 --> N6
    N6 -->|Verdadeiro| N7
    N6 -->|Falso| N8
    N7 --> N10
    N8 --> N9
    N9 --> N10
```

Caminhos independentes:

P1: 1 -> 2(F) -> 4(F) -> 6(F) -> 8 -> 9 -> 10
Entrada: valor = 100, clienteVip = false, pagamentoAprovado = true
Resultado: PEDIDO APROVADO: 100.0

P2: 1 -> 2(V) -> 3 -> 4(F) -> 6(F) -> 8 -> 9 -> 10
Entrada: valor = 500, clienteVip = false, pagamentoAprovado = true
Resultado: PEDIDO APROVADO: 450.0

P3: 1 -> 2(F) -> 4(V) -> 5 -> 6(F) -> 8 -> 9 -> 10
Entrada: valor = 100, clienteVip = true, pagamentoAprovado = true
Resultado: PEDIDO APROVADO: 95.0

P4: 1 -> 2(F) -> 4(F) -> 6(V) -> 7 -> 10
Entrada: valor = 100, clienteVip = false, pagamentoAprovado = false
Resultado: PAGAMENTO RECUSADO

