# Relatório de validação independente da entrega

Cinco verificações independentes foram executadas por agentes separados. Cada um partiu de um **clone novo do GitHub** (branch `develop`), não da cópia local da equipe,
e salvou a **saída real** dos comandos no arquivo indicado. As falhas da 1ª rodada foram corrigidas, e os agentes que falharam rodaram de novo.
Os relatórios guardam as duas rodadas.

## Resumo

| Agente | Missão | 1ª rodada | Rodada final | Relatório |
|---|---|---|---|---|
| A | "Script ingênuo": `find pom.xml` → `mvn test` e `mvn verify`; `clean test` com repositório Maven **vazio**; **JDK 17 e JDK 21** | PASSOU | **PASSOU** | [agente-a-script-ingenuo.txt](agente-a-script-ingenuo.txt) |
| B | Testes **originais do professor** copiados por cima dos nossos; teste "às cegas" do carrinho a partir só do `REQUISITOS.md`; Semana 11 com o `src/main` **do professor**; `diff -r` | FALHOU¹ | **PASSOU** | [agente-b-testes-do-professor.txt](agente-b-testes-do-professor.txt) |
| C | Corretor humano com rubrica: checklist de cada enunciado, rubrica da Semana 11, 5 perguntas de discussão, `desafios.md` | FALHOU² | **PASSOU** | [agente-c-corretor-rubrica.txt](agente-c-corretor-rubrica.txt) |
| D | TDD e histórico: RED antes de GREEN, evidências RED com falha real, `git log --follow` após `git mv`, autoria | FALHOU³ | **PASSOU** | [agente-d-tdd-historico.txt](agente-d-tdd-historico.txt) |
| E | Robustez: repositório público, `./mvnw` **sem Maven instalado** (inclusive `mvnw.cmd`), `.gitattributes`, CI **Ubuntu + Windows** | FALHOU⁴ | **PASSOU** | [agente-e-robustez-ambiente.txt](agente-e-robustez-ambiente.txt) |
| — | Links relativos de todos os `.md` ([scripts/verificar-links.sh](../../scripts/verificar-links.sh)) | PASSOU | **PASSOU** (130 links, 0 quebrados) | [links.txt](links.txt) |

## O que falhou e como foi corrigido

1. **B:** o critério escrito na instrução do agente tratava como "teste do professor alterado" os **esqueletos** que o enunciado manda preencher
   (`CarrinhoTest`, vazio, e os `TODO` da Semana 11). Reavaliado com o critério correto: classe, nomes de métodos e anotações originais mantidos.
   Os testes que já vinham **prontos** (`BubbleSortTest`, `ContaBancariaTest`, `ProdutoTest`) estão idênticos e passam 100%.
   O teste às cegas do carrinho passou 9/9, sem atrito de API.
2. **C:** (a) CI vermelha (veja o item 4); (b) a RASTREABILIDADE citava "seção do PR" em vez de arquivos reais: passou a apontar para os relatórios;
   (c) a Semana 14 citava `evidencias/analise.txt`, que ainda não existe: agora o arquivo aparece descrito como gerado após a coleta, e foi incluída a evidência real
   `evidencias/analise-verify.txt` (82 testes, cobertura 100%); (d) ajustes de texto: limiar de mutação, papel do avaliador e decisão do R5.
3. **D:** o README da Semana 10 omitia, no ciclo 1 RED do estoque, que R4 também falhava. Corrigido, junto com uma nota sobre o rótulo `feat(...)`
   dos RED de regressão e sobre os caminhos antigos (`aulas/SEMANA10`) dentro das evidências.
4. **E:** o job `verify (estoque-tdd, ubuntu-latest)` falhou antes de compilar, enquanto o mesmo código passou no Windows e na run anterior.
   O log exige login de administrador, então a causa não foi confirmada; a mais provável é uma falha transitória no download do Maven pelo wrapper.
   Correção: um passo que pré-aquece o wrapper com até 3 tentativas e `actions/checkout`/`setup-java` v5. Run seguinte: **18/18 jobs com sucesso**.

## Números confirmados de forma independente

| Projeto | Testes | Situação |
|---|---:|---|
| semana10-tdd/bubblesort-tdd | 25 | test/verify OK em JDK 17 e 21, com cache vazio, Ubuntu e Windows |
| semana10-tdd/carrinho-compras-tdd | 26 | idem |
| semana10-tdd/conta-bancaria-tdd-maven | 17 | idem |
| semana10-tdd/estoque-tdd | 19 | idem |
| semana11-integracao-springboot/exercicios_integracao_springboot | 34 | idem; também com o `src/main` original do professor |
| semana14-teste-usuario-final/analise | 82 | idem |
| 1-bimestre/atividade03-junit-central-pedidos | 54 | idem |

## Pendências que dependem só da equipe

- **Merge do PR `develop → main`.** Hoje a branch padrão (`main`) ainda não contém a Prova 02.
- **Sessões da Semana 14 com pessoas reais.** Não podem ser simuladas. Veja o [kit de sessão rápida](../semana14-teste-usuario-final/KIT-SESSAO-RAPIDA.md).

## Observações registradas e aceitas

- A equipe tem três integrantes, e o enunciado da Prova 02 fala em "dupla". O ponto foi apenas relatado.
- Há identidades git duplicadas por autor (e-mails diferentes). A autoria não foi alterada (`.mailmap` não foi criado), por decisão da equipe.
- O relatório do Agente E cita, na linha 31, uma URL do Maven com um segmento faltando. É erro de digitação do texto do relatório; a URL real usada pelo wrapper (`.mvn/wrapper/maven-wrapper.properties`) responde normalmente.
