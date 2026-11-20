# Projeto, Implementação e Teste de Software (2026)

| Aluno | RA |
|---|---|
| Breno Bertaglia Nosima | 24113673-2 |
| Felipe Galeti Gôngora | 24036480-2 |
| Henrique Kendi Ikeda | 24039456-2 |

**Disciplina:** Projeto, Implementação e Teste de Software (2026), prof. João Choma · **Curso:** Engenharia de Software, 6º semestre ·
**Repositório oficial:** https://github.com/JoaoChoma/teste_software_2026

| Prova | Onde está |
|---|---|
| **Prova 01** (1º bimestre) | [1-bimestre/](1-bimestre/README.md) |
| **Prova 02** (2º bimestre: Semanas 10, 11 e 14) | [2-bimestre/](2-bimestre/README.md) |

## Índice: um clique para cada atividade

| Prova | Atividade | Projeto / pasta | Testes | Cobertura | Status |
|---|---|---|---:|---|---|
| 02 | Semana 10: TDD | [bubblesort-tdd](2-bimestre/semana10-tdd/bubblesort-tdd/) | 25 | 100% · mutação 20/20 | ✅ |
| 02 | Semana 10: TDD | [carrinho-compras-tdd](2-bimestre/semana10-tdd/carrinho-compras-tdd/) | 26 | 100% · mutação 21/21 | ✅ |
| 02 | Semana 10: TDD | [conta-bancaria-tdd-maven](2-bimestre/semana10-tdd/conta-bancaria-tdd-maven/) | 17 | 100% · mutação 10/12* | ✅ |
| 02 | Semana 10: TDD | [estoque-tdd](2-bimestre/semana10-tdd/estoque-tdd/) | 19 | 100% · mutação 11/13* | ✅ |
| 02 | Semana 10: relatório, ciclos e evidências | [semana10-tdd/README.md](2-bimestre/semana10-tdd/README.md) · [evidencias/](2-bimestre/semana10-tdd/evidencias/) | | | ✅ |
| 02 | Semana 11: integração Spring Boot | [exercicios_integracao_springboot](2-bimestre/semana11-integracao-springboot/exercicios_integracao_springboot/README.md) | 34 | 100% linhas/desvios (service, controller, model) | ✅ |
| 02 | Semana 14: teste de usuário final | [semana14-teste-usuario-final](2-bimestre/semana14-teste-usuario-final/README.md) · [kit rápido](2-bimestre/semana14-teste-usuario-final/KIT-SESSAO-RAPIDA.md) · [analisador](2-bimestre/semana14-teste-usuario-final/analise/) | 82 | 100% (analisador) | ⏳ sessões com usuários reais pendentes |
| 01 | Plano e casos de teste | [atividade01-plano-e-casos-de-teste](1-bimestre/atividade01-plano-e-casos-de-teste/) | | | ✅ |
| 01 | Playwright | [atividade02-playwright](1-bimestre/atividade02-playwright/) | | | ✅ |
| 01 | JUnit (central-pedidos) | [atividade03-junit-central-pedidos](1-bimestre/atividade03-junit-central-pedidos/) | 54 | | ✅ |
| 01 | Grafos de fluxo de controle | [atividade04-gfc-revisao](1-bimestre/atividade04-gfc-revisao/) | | | ✅ |

\* Os mutantes sobreviventes são equivalentes e estão justificados em [2-bimestre/semana10-tdd/README.md](2-bimestre/semana10-tdd/README.md#resultados).

- **Rastreabilidade** (requisito → teste → evidência): [RASTREABILIDADE.md](RASTREABILIDADE.md)
- **Validação independente** (clone limpo, testes originais do professor, Linux e Windows, histórico TDD): [2-bimestre/evidencias-validacao/](2-bimestre/evidencias-validacao/)

## Como executar

Requisitos: **Java 17+**. O Maven é opcional: cada projeto traz o **Maven Wrapper** (`./mvnw`, ou `mvnw.cmd` no Windows), que baixa o Maven 3.9.9 sozinho.

```bash
cd <pasta-do-projeto>
./mvnw test              # testes
./mvnw verify            # testes + checagem de cobertura (JaCoCo)
./mvnw -Pmutacao verify  # (Semana 10) + teste de mutação PIT
```

Rodar tudo (Linux, macOS ou Git Bash):

```bash
for p in $(find 1-bimestre 2-bimestre -name pom.xml -not -path '*/target/*' -exec dirname {} \;); do (cd "$p" && ./mvnw -q verify) || echo "FALHOU: $p"; done
```

A integração contínua ([.github/workflows/testes.yml](.github/workflows/testes.yml)) roda `./mvnw verify` em todos os projetos, em **Ubuntu e Windows**, com Java 17 (Temurin),
publica os relatórios JaCoCo e Surefire e roda a mutação PIT num job separado.
