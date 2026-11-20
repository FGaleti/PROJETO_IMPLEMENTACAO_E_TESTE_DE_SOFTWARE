# 04: Questionário SUS (System Usability Scale)

Arquivo de dados: [04-questionario-sus.csv](04-questionario-sus.csv) (colunas `participante, Q1 … Q10`; uma linha por participante).
Aplicar **logo após a última tarefa**, antes da conversa final. O participante responde sozinho.

## Instrução ao participante

> Para cada afirmação, marque um número de **1 (discordo totalmente)** a **5 (concordo totalmente)**, pensando no site do Mercado Livre que você acabou de usar.
> Responda rápido e marque todas. Se não tiver opinião, marque 3.

| # | Afirmação | 1 | 2 | 3 | 4 | 5 |
|---|---|:-:|:-:|:-:|:-:|:-:|
| Q1 | Eu acho que gostaria de usar este site com frequência. | ☐ | ☐ | ☐ | ☐ | ☐ |
| Q2 | Eu achei o site desnecessariamente complexo. | ☐ | ☐ | ☐ | ☐ | ☐ |
| Q3 | Eu achei o site fácil de usar. | ☐ | ☐ | ☐ | ☐ | ☐ |
| Q4 | Eu acho que precisaria da ajuda de uma pessoa com conhecimentos técnicos para usar o site. | ☐ | ☐ | ☐ | ☐ | ☐ |
| Q5 | Eu achei que as várias funções do site estão bem integradas. | ☐ | ☐ | ☐ | ☐ | ☐ |
| Q6 | Eu achei que há muita inconsistência no site. | ☐ | ☐ | ☐ | ☐ | ☐ |
| Q7 | Eu imagino que a maioria das pessoas aprenderia a usar este site rapidamente. | ☐ | ☐ | ☐ | ☐ | ☐ |
| Q8 | Eu achei o site muito complicado de usar. | ☐ | ☐ | ☐ | ☐ | ☐ |
| Q9 | Eu me senti muito confiante ao usar o site. | ☐ | ☐ | ☐ | ☐ | ☐ |
| Q10 | Eu precisei aprender várias coisas antes de conseguir usar o site. | ☐ | ☐ | ☐ | ☐ | ☐ |

Para o Google Forms, crie 10 perguntas de "escala linear" de 1 a 5 com esses textos e uma pergunta "Código do participante" (P01…).
Exporte as respostas e copie para o CSV.

## Apuração (feita pelo analisador)

1. **Itens ímpares** (Q1, Q3, Q5, Q7, Q9, afirmações positivas): contribuição = **nota − 1**.
2. **Itens pares** (Q2, Q4, Q6, Q8, Q10, afirmações negativas): contribuição = **5 − nota**.
3. **Escore SUS = soma das 10 contribuições × 2,5**, de 0 a 100.

Conferências de referência, cobertas por testes automatizados em `CalculadoraSusTest`:

| Respostas | Escore |
|---|---:|
| todas 3 | 50 |
| ímpares 5, pares 1 (melhor caso) | 100 |
| ímpares 1, pares 5 (pior caso) | 0 |

O escore **não é um percentual**. Questionário incompleto ou nota fora de 1–5 gera erro no analisador.
Participante sem nenhuma resposta aparece como "sem resposta" e fica fora da média.

## Faixas de interpretação

| Escore | Interpretação |
|---|---|
| < 51 | Inaceitável |
| 51 a < 68 | Marginal (abaixo da média) |
| 68 a < 80,3 | Bom (acima da média) |
| ≥ 80,3 | Excelente |

68 é a média de referência de mais de 500 estudos, e 80,3 marca aproximadamente os 10% melhores
(Sauro, J. *A Practical Guide to the System Usability Scale*, 2011; Bangor, Kortum e Miller, *Determining What Individual SUS Scores Mean*, JUS, 2009).
Escala original: Brooke, J. *SUS: a "quick and dirty" usability scale*, 1996.
