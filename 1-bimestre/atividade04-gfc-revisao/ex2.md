```mermaid
flowchart TD
    N1["1 - Inicio<br>alertas = 0<br>i = 0"]
    N2{"2 - i < tamanho do vetor?"}
    N3{"3 - temperatura[i] < 0?"}
    N4["4 - alertas += 2"]
    N5{"5 - temperatura[i] > 35?"}
    N6["6 - alertas++"]
    N7["7 - i++"]
    N8["8 - return alertas"]
    N9(["9 - Fim"])

    N1 --> N2
    N2 -->|Verdadeiro| N3
    N2 -->|Falso| N8
    N3 -->|Verdadeiro| N4
    N3 -->|Falso| N5
    N4 --> N7
    N5 -->|Verdadeiro| N6
    N5 -->|Falso| N7
    N6 --> N7
    N7 --> N2
    N8 --> N9

```

Caminhos independentes:

P1: 1 -> 2(F) -> 8 -> 9
Entrada: temperaturas = {}
Resultado: 0

P2: 1 -> 2(V) -> 3(V) -> 4 -> 7 -> 2(F) -> 8 -> 9
Entrada: temperaturas = {-5}
Resultado: 2

P3: 1 -> 2(V) -> 3(F) -> 5(V) -> 6 -> 7 -> 2(F) -> 8 -> 9
Entrada: temperaturas = {40}
Resultado: 1

P4: 1 -> 2(V) -> 3(F) -> 5(F) -> 7 -> 2(F) -> 8 -> 9
Entrada: temperaturas = {20}
Resultado: 0

