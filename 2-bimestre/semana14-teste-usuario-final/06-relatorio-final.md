# 06: Relatório final do teste de usuário (Mercado Livre)

> **Situação: PENDENTE DE COLETA.** As sessões com participantes reais ainda não foram feitas.
> As seções marcadas com ⏳ só serão preenchidas com os números calculados pelo analisador
> (`analise/`, saída em `evidencias/analise.txt`). **Nenhum número deste relatório pode ser estimado ou inventado.**

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
