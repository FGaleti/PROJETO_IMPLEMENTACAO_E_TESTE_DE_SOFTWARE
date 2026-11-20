# Semana 14: teste de usuário final (Mercado Livre)

Enunciado: [enunciado/TESTE USUARIO FINAL.pdf](enunciado/TESTE%20USUARIO%20FINAL.pdf) (cópia do repositório oficial).
Cenário: comprar um fone de ouvido sem fio, achar um modelo bem avaliado, comparar opções e simular a compra até o carrinho, **sem pagar**.

> **Situação:** preparação concluída; **sessões com participantes reais pendentes**.
> Os CSVs reais estão versionados só com o cabeçalho e as linhas de tarefa vazias. Nenhum resultado foi estimado ou inventado.

| Arquivo | Conteúdo |
|---|---|
| [01-plano-de-teste.md](01-plano-de-teste.md) | Objetivo, escopo, cenário, 8 tarefas com critério de conclusão, perfil e nº de participantes, métricas, critérios, papéis, riscos e ética |
| [02-roteiro-sessao.md](02-roteiro-sessao.md) | Script do avaliador: preparação, *briefing*, *think-aloud*, quando ajudar, encerramento |
| [03-planilha-observacao.csv](03-planilha-observacao.csv) / [.md](03-planilha-observacao.md) | Planilha com as colunas do modelo do professor, mais `participante` e `com_ajuda`; definição de erro |
| [04-questionario-sus.csv](04-questionario-sus.csv) / [.md](04-questionario-sus.md) | Os 10 itens do SUS, fórmula de apuração e faixas de interpretação |
| [05-termo-de-consentimento.md](05-termo-de-consentimento.md) | Termo curto, sem dado pessoal identificável |
| [06-relatorio-final.md](06-relatorio-final.md) | Estrutura completa do relatório; resultados **pendentes de coleta** |
| [KIT-SESSAO-RAPIDA.md](KIT-SESSAO-RAPIDA.md) | **Uma página para conduzir cada sessão em ~8 min** (briefing, tarefas, o que anotar, como exportar) |
| [04b-sus-google-forms.txt](04b-sus-google-forms.txt) | SUS pronto para colar no Google Forms (o CSV exportado é aceito direto) |
| [03b-planilha-simplificada.csv](03b-planilha-simplificada.csv) | Modelo de planilha simplificada para o avaliador preencher no celular (Google Sheets → CSV, aceito direto) |
| [analise/](analise/) | Analisador automatizado (Maven, Java 17, JUnit 5), com 82 testes e JaCoCo 100% |
| `evidencias/` | Prints das sessões (sem dados pessoais) e `analise.txt` (saída do analisador, **gerada após a coleta**). Hoje há [analise-verify.txt](evidencias/analise-verify.txt): saída real dos 82 testes e da checagem de cobertura do analisador |

## Analisador

```bash
cd analise
./mvnw verify                # 82 testes + checagem de 100% de cobertura (ou mvn, se instalado)
./mvnw -q compile exec:java  # lê ../03-planilha-observacao.csv e ../04-questionario-sus.csv
./mvnw -q compile exec:java > ../evidencias/analise.txt   # salva a evidência
```

Ele calcula: taxa de sucesso **sem ajuda** por tarefa e global (fluxo inteiro), erros por tarefa, tempo total por participante, tempo médio e mediano,
escore SUS por participante e médio (com interpretação), e avalia os critérios **C1 (≥ 90% adicionam ao carrinho)** e **C2 (tempo médio ≤ 240 s)**.
Linhas com `Sucesso (S/N)` em branco contam como "ainda não coletadas" e são ignoradas.
Valores inválidos (por exemplo `Sucesso = X` ou uma nota SUS 6) param a análise com uma mensagem que indica participante, tarefa e coluna.

Os testes usam só as **fixtures fictícias** de `analise/src/test/resources/fixtures/FICTICIO-*.csv`, rotuladas como dados sintéticos.

## Limites respeitados

- **O Mercado Livre não foi automatizado** (sem Playwright, Selenium nem robôs). O site tem verificação anti-bot, e o teste exige **pessoas reais**.
- Sem login, sem dados pessoais, sem pagamento.
- Nenhum participante, tempo, erro ou resposta SUS foi inventado.
