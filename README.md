# Projeto, Implementação e Teste de Software (2026)

| Aluno | RA |
|---|---|
| Breno Bertaglia Nosima | 24113673-2 |
| Felipe Galeti Gôngora | 24036480-2 |
| Henrique Kendi Ikeda | 24039456-2 |

- **Disciplina:** Projeto, Implementação e Teste de Software (2026), prof. João Choma
- **Curso:** Engenharia de Software, 6º semestre
- **Repositório oficial da disciplina:** https://github.com/JoaoChoma/teste_software_2026

## Avaliação prática da Prova 02: Semanas 10, 11 e 14

A estrutura de pastas espelha a do repositório oficial (`aulas/SEMANAxx/...`).

| Semana | Pasta | O que foi feito | Testes | Cobertura | Status |
|---|---|---|---:|---|---|
| 10 | [aulas/SEMANA10](aulas/SEMANA10/README.md) | TDD (RED → GREEN → REFACTOR, um commit por etapa, com evidência) nos 4 projetos: `bubblesort-tdd`, `carrinho-compras-tdd`, `conta-bancaria-tdd-maven`, `estoque-tdd`. Inclui teste de mutação (PIT) | 25 + 26 + 17 + 19 = **87** | **100%** em instruções, linhas, desvios, métodos e classes nos 4 projetos. Mutação: 100%, 100%, 83%* e 85%* | ✅ Concluída |
| 11 | [aulas/SEMANA11/exercicios_integracao_springboot](aulas/SEMANA11/exercicios_integracao_springboot/README.md) | Testes de integração Spring Boot sem mocks: CT01–CT05 (Service + Repository + H2 e MockMvc), complementares e variante com HTTP real. `src/main` intocado; achados documentados | **34** | **100%** de linhas e desvios em service, controller e model | ✅ Concluída |
| 14 | [aulas/SEMANA14/teste-usuario-final](aulas/SEMANA14/teste-usuario-final/README.md) | Plano, roteiro, planilha de observação, SUS, termo de consentimento, relatório e **analisador automatizado** dos dados | **76** (analisador) | **100%** (analisador) | ⏳ Preparação concluída; **sessões com usuários reais pendentes** |

\* Os mutantes sobreviventes são equivalentes e estão justificados em [aulas/SEMANA10/README.md](aulas/SEMANA10/README.md#resultados).

A rastreabilidade (requisito → teste → evidência) está em [RASTREABILIDADE.md](RASTREABILIDADE.md).

## Requisitos para executar

- Java **17+** (os projetos compilam para Java 17)
- Maven **3.9+**

## Rodar tudo

```bash
for p in $(find aulas -name pom.xml -not -path '*/target/*' -exec dirname {} \;); do (cd "$p" && mvn -q verify) || echo "FALHOU: $p"; done
```

Por projeto:

```bash
mvn test                 # testes (e relatório JaCoCo em target/site/jacoco)
mvn verify               # testes + checagem de cobertura
mvn -Pmutacao verify     # (Semana 10) + teste de mutação PIT em target/pit-reports
```

A integração contínua ([.github/workflows/testes.yml](.github/workflows/testes.yml)) roda `mvn -B verify` em cada projeto a cada push ou PR,
com Java 17 (Temurin). Ela publica os relatórios JaCoCo e Surefire como artefatos e roda a mutação PIT num job separado, não bloqueante.

## Atividades anteriores (Prova 01)

| Pasta | Atividade |
|---|---|
| [atividade1/](atividade1/) | Plano de teste e casos de teste |
| [atividade2/](atividade2/) | Teste funcional com Playwright |
| [central-pedidos/](central-pedidos/) | Teste estrutural / unitário com JUnit |
| [Exercícios na sala sobre GFC - Revisão/](Exercícios%20na%20sala%20sobre%20GFC%20-%20Revisão/) | Grafos de fluxo de controle |
