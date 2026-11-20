# 06: Relatório final do teste de usuário (Mercado Livre)

> **Situação: PENDENTE DE COLETA.** As sessões com participantes reais ainda não foram feitas.
> As seções marcadas com ⏳ só serão preenchidas com os números calculados pelo analisador
> (`analise/`, saída em `evidencias/analise.txt`, arquivo que será gerado após a coleta). **Nenhum número deste relatório pode ser estimado ou inventado.**

## 1. Resumo executivo ⏳

*Pendente de coleta.* O resumo terá: nº de participantes, resultado dos critérios C1 (≥ 90% adicionam ao carrinho) e C2 (tempo médio ≤ 240 s),
SUS médio com interpretação e os 3 principais achados.

## 2. Método

- **Produto avaliado:** site Mercado Livre (https://www.mercadolivre.com.br), versão web, nas datas das sessões.
- **Tipo de teste:** teste de usabilidade moderado e presencial, com protocolo *think-aloud* e questionário SUS ao final.
- **Plano e tarefas:** [01-plano-de-teste.md](01-plano-de-teste.md) (8 tarefas com critério operacional de conclusão).
- **Condução:** [02-roteiro-sessao.md](02-roteiro-sessao.md), com o mesmo roteiro para todas as sessões e janela anônima no navegador do participante.
- **Instrumentos:** planilha de observação ([03](03-planilha-observacao.md)), SUS ([04](04-questionario-sus.md)) e termo de consentimento ([05](05-termo-de-consentimento.md)).
- **Análise:** automatizada e testada (82 testes, JaCoCo 100%). Nenhuma conta manual.
- **Avaliação heurística prévia (especialista, não usuários):** [07-avaliacao-heuristica.md](07-avaliacao-heuristica.md), com 9 hipóteses de problema (Nielsen) a confirmar nas sessões.
- **Validação do processo de análise:** demonstração ponta a ponta com dados **simulados e rotulados** (Apêndice A). **Esses números não são resultados do teste.**
- **Participantes:** ⏳ *n = pendente*; perfil resumido (faixa etária e frequência de compra), sem dados identificáveis.
- **Datas, navegadores e dispositivos das sessões:** ⏳ *pendente*.

## 3. Resultados ⏳

Todos os valores abaixo serão copiados de `evidencias/analise.txt`.

### 3.1 Taxa de sucesso (sem ajuda) e erros por tarefa ⏳

| Tarefa | Sucesso sem ajuda | Erros |
|---|---|---|
| Acessar site | pendente | pendente |
| Buscar produto | pendente | pendente |
| Aplicar filtros | pendente | pendente |
| Abrir detalhes | pendente | pendente |
| Adicionar ao carrinho | pendente | pendente |
| Simular compra | pendente | pendente |
| Retornar à tela inicial | pendente | pendente |
| **Fluxo inteiro (global)** | pendente | **total:** pendente |

### 3.2 Tempo ⏳

Tempo total por participante, tempo médio e mediana: *pendente*.

### 3.3 SUS ⏳

Escore por participante, SUS médio e interpretação: *pendente*.

### 3.4 Critérios de sucesso ⏳

| Critério | Meta | Resultado | Situação |
|---|---|---|---|
| C1: adicionar ao carrinho | ≥ 90% sem ajuda | pendente | PENDENTE |
| C2: tempo médio | ≤ 240 s | pendente | PENDENTE |

## 4. Achados de usabilidade ⏳

Cada achado precisa ter evidência: participante(s), tarefa, fala ou observação, e print em `evidencias/`.
Severidade pela escala de **Nielsen (0–4)**:

| Grau | Significado |
|---|---|
| 0 | Não é problema de usabilidade |
| 1 | Cosmético: corrigir só se houver tempo |
| 2 | Pequeno: correção de baixa prioridade |
| 3 | Grande: correção importante, alta prioridade |
| 4 | Catástrofe: precisa ser corrigido antes do lançamento |

A severidade combina **frequência** (quantos participantes), **impacto** (atrasou ou impediu a tarefa) e **persistência** (se repete).

| # | Achado | Tarefa | Participantes afetados | Evidência | Severidade (0–4) |
|---|---|---|---|---|---|
| A1 | *pendente de coleta* | | | | |

Ponto de partida: as hipóteses H1–H9 da [avaliação heurística](07-avaliacao-heuristica.md) (com destaque para H1, ordenação "melhores avaliados", e H6, login antes do carrinho ou checkout). Cada uma só vira achado se for **observada** nas sessões.

## 5. Recomendações ⏳

*Pendente.* Uma recomendação por achado de severidade ≥ 2, ligada ao número do achado.

## 6. Limitações

- **Amostra pequena e de conveniência** (colegas de curso). Isso basta para revelar problemas, mas não para estimar taxas com precisão estatística.
- **Ambiente artificial:** o participante sabe que está sendo observado e não usa a própria conta (janela anônima e sem login).
  Isso muda o comportamento nas etapas de carrinho e checkout.
- **O site muda com o tempo:** filtros, ordenações e *layout* podem variar entre sessões (risco R2 do plano). A data de cada sessão é registrada.
- **Cronometragem manual**, com imprecisão estimada de ±2 s por tarefa.
- A tarefa 7 termina na tela de login ou de checkout. A usabilidade das etapas seguintes **não foi avaliada**.
- ⏳ Outras limitações observadas durante as sessões.

## Apêndice A: demonstração do processo de análise (dados SIMULADOS)

> ⚠️ **Os números abaixo NÃO são resultados de teste com usuários.** Os participantes S01–S05 não existem.
> Os dados foram escritos pela equipe ([simulacao/](simulacao/LEIA-ME.md)) só para demonstrar que a cadeia
> planilha/Google Forms → CSV → analisador → relatório funciona. A saída completa do analisador está em
> [evidencias/DEMONSTRACAO-SIMULADA.txt](evidencias/DEMONSTRACAO-SIMULADA.txt).

O que a demonstração comprova sobre o **processo**, não sobre o site:
- O analisador leu a planilha no formato do professor e o SUS no **formato exportado pelo Google Forms**, sem edição.
- Ele calculou a taxa de sucesso por tarefa e global, os erros, os tempos (média e mediana), o SUS por participante e médio com interpretação, e avaliou os critérios C1 e C2.
- Os valores calculados batem com a conferência manual feita pela equipe (por exemplo, tempo médio (166 + 238 + 133 + 317 + 214) / 5 = 213,6 s e SUS médio (75 + 60 + 92,5 + 37,5 + 70) / 5 = 67,0).

Quando os CSVs reais existirem, o mesmo comando gera `evidencias/analise.txt`, e só esse arquivo alimenta as seções 1, 3, 4 e 5 deste relatório.
