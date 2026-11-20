# Matriz de rastreabilidade: Prova 02

Cada requisito dos enunciados oficiais aponta para os testes que o cobrem e para a evidência de execução.
Fontes relidas: `REQUISITOS.md` (bubblesort e carrinho), `README.md` (conta e estoque), `requisitos_estoque_tdd.md`,
`lista_exercicios_integracao_springboot.md`, `README.md` do projeto da Semana 11 e `TESTE USUARIO FINAL.pdf`.

Convenção: `Classe#método`. Testes do professor marcados com **(prof.)**. Os caminhos de evidência são relativos a cada pasta de semana.

## Semana 10: bubblesort-tdd (`REQUISITOS.md`)

| Requisito | Testes | Evidência |
|---|---|---|
| R1: ordenar vetor desordenado `[5,3,1,4,2]` | `BubbleSortTest#deveOrdenarVetorDesordenado` **(prof.)**; `BubbleSortCasosAdicionaisTest#deveOrdenarParticoesDeEntrada` (decrescente, negativos, MIN/MAX…); `#deveCoincidirComArraysSortEmVetoresAleatorios` (oráculo, 200 vetores) | `evidencias/bubblesort-tdd-ciclo0-red.txt` → `ciclo1-green.txt` |
| R2: manter vetor já ordenado | `BubbleSortTest#deveManterVetorJaOrdenado` **(prof.)**; partições "dois já ordenados", "um elemento", "vazio"; `BubbleSortParadaAntecipadaTest` (já ordenado faz 1 passagem) | `ciclo1-green.txt`, `ciclo3-green.txt` |
| R3: valores repetidos `[3,1,3,2,1]` | `BubbleSortTest#deveOrdenarVetorComValoresRepetidos` **(prof.)**; partição "todos iguais"; oráculo (valores −100..100 forçam repetição) | `ciclo1-green.txt` |
| Algoritmo **Bubble Sort** (trocas adjacentes) | `BubbleSortParadaAntecipadaTest#deveExecutarSomenteAsPassagensNecessarias` (decrescente faz n−1 passagens) | `ciclo3-green.txt` |
| Restrição: não alterar os testes fornecidos | `git diff` do `BubbleSortTest.java` contra o commit-base é vazio | Seção "Verificação final" do PR |
| Extra: `null`, in-place, parada antecipada | `#deveIgnorarVetorNulo`, `#deveOrdenarNoProprioVetor`, `BubbleSortParadaAntecipadaTest` (7 + 2) | `ciclo2-red/green.txt`, `ciclo3-red/green.txt` |
| Cobertura total | JaCoCo 100% (5 contadores); PIT 20/20 | `bubblesort-tdd-final-verify.txt`, `-final-mutacao.txt` |

## Semana 10: carrinho-compras-tdd (`REQUISITOS.md`)

| Requisito | Testes (`CarrinhoTest`) | Evidência (RED → GREEN) |
|---|---|---|
| R1: carrinho vazio com total R$ 0,00 | `r1CarrinhoNovoDeveTerTotalZero`; `r6CarrinhoVazioNaoTemDesconto` | `carrinho-compras-tdd-ciclo1-red.txt` → `ciclo1-green.txt` |
| R2: adicionar Teclado R$ 100 → 100 | `r2DeveAdicionarUmProduto` | `ciclo2-red.txt` → `ciclo2-green.txt` |
| R3: Teclado + Mouse → 150 | `r3DeveSomarVariosProdutos`; `r3DeveSomarValoresComCentavos` | `ciclo3-red.txt` → `ciclo3-green.txt` |
| R4: remover Mouse → 100 | `r4DeveRemoverProduto`; `r4RemoverProdutoInexistenteNaoAlteraTotal`; `r4RemoverDeCarrinhoVazio`; `r4RemoverRetiraApenasUmaOcorrencia`; `r4RemoverTodosZeraOTotal` | `ciclo4-red.txt` → `ciclo4-green.txt` |
| R5: preço negativo não permitido (decisão: exceção) | `r5ProdutoComPrecoNegativoDeveLancarExcecao`; `r5ProdutoInvalidoNaoAlteraCarrinho`; `r5PrecoNaoNegativoNaFronteiraEhAceito` (0,00/0,01); `r5PrecoNegativoNaFronteiraEhRejeitado` (−0,01/−100); `r5ProdutoNuloDeveLancarExcecao` | `ciclo5-red.txt` → `ciclo5-green.txt` → `ciclo5-refactor.txt` |
| R6: ≥ R$ 500 recebe 10% (600 → 540; fronteiras 499,99/500,00/500,01) | `r6CompraDe600DeveCustar540`; `r6FronteiraDoDesconto` (4 casos); `r6DescontoSobreASomaDeVariosItens`; `r6DescontoSomeAoRemoverItemEFicarAbaixoDaFronteira` (R4×R6) | `ciclo6-red.txt` → `ciclo6-green.txt` → `ciclo6-refactor.txt` |
| Relação Carrinho → Produto (armazenar e manipular) | `deveListarProdutosNaOrdemDeInclusao`; `listaDeProdutosDeveSerImutavel` | `ciclo7-red.txt` → `ciclo7-green.txt` |
| Cobertura total | JaCoCo 100%; PIT 21/21 | `carrinho-compras-tdd-final-verify.txt`, `-final-mutacao.txt` |

## Semana 10: conta-bancaria-tdd-maven (`README.md`)

| Comportamento | Testes | Evidência |
|---|---|---|
| Depositar | `ContaBancariaTest#deveDepositarValorNaConta` **(prof.)**; `ContaBancariaCasosAdicionaisTest#valoresComCentavos` | `conta-bancaria-tdd-maven-ciclo0-red.txt` → `ciclo1-green.txt` |
| Acumular depósitos | `ContaBancariaTest#deveAcumularDepositos` **(prof.)** | `ciclo1-green.txt` |
| Sacar | `ContaBancariaTest#deveSacarValorDaConta` **(prof.)**; `#fronteiraDoSaque` (99,99 / 100,00 / 100,01) | `ciclo2-red.txt` → `ciclo2-green.txt` |
| Saque maior que o saldo não permitido (ignorado) | `ContaBancariaTest#naoDevePermitirSaqueMaiorQueSaldo` **(prof.)**; `#saqueEmContaZeradaDeveSerRecusado` | `ciclo2-red.txt` (regressão) → `ciclo2-green.txt` |
| Extra: saldo inicial, valores não positivos, sequência | `#contaNovaDeveTerSaldoZero`; `#depositoNaoPositivoDeveSerIgnorado`; `#saqueNaoPositivoDeveSerIgnorado`; `#sequenciaDeOperacoes` | `ciclo3-red.txt` → `ciclo3-green.txt` |
| Cobertura total | JaCoCo 100%; PIT 10/12 (2 equivalentes justificados) | `conta-bancaria-tdd-maven-final-*.txt` |

## Semana 10: estoque-tdd (`requisitos_estoque_tdd.md`)

| Requisito | Testes | Evidência |
|---|---|---|
| R1: estoque inicial zero | `ProdutoTest#produtoDeveIniciarComEstoqueZero` **(prof.)** | `estoque-tdd-ciclo0-red.txt` (passa por acidente) |
| R2: adicionar 10 → 10 | `ProdutoTest#deveAdicionarProdutoAoEstoque` **(prof.)**; `ProdutoCasosAdicionaisTest#fronteiraDaEntrada` (1 → +1) | `ciclo1-red.txt` → `ciclo1-green.txt` |
| R3: acumular 10 + 5 → 15 | `ProdutoTest#deveAcumularQuantidadeEmEstoque` **(prof.)**; `#entradasESaidasIntercaladas` | `ciclo1-green.txt` |
| R4: retirar 3 de 10 → 7 | `ProdutoTest#deveRetirarProdutoDoEstoque` **(prof.)**; `#fronteiraDaRetirada` (9 / 10 = tudo) | `ciclo2-red.txt` → `ciclo2-green.txt` |
| R5: retirada maior que o estoque não altera | `ProdutoTest#naoDeveRetirarQuantidadeMaiorQueEstoque` **(prof.)**; `#fronteiraDaRetirada` (11); `#retirarDeEstoqueZerado` | `ciclo2-red.txt` (regressão) → `ciclo2-green.txt` |
| R6: entrada **negativa ou igual a zero** não altera | `ProdutoTest#naoDeveAdicionarQuantidadeNegativa` **(prof., só negativo)**; `#adicionarZeroDeveSerIgnorado`; `#fronteiraDaEntrada` (−1 / 0) | `ciclo1-red.txt` (regressão) → `ciclo1-green.txt` |
| Extra: nome, retirada não positiva | `#deveGuardarONome`; `#retirarNaoPositivoDeveSerIgnorado` | `ciclo3-red.txt` → `ciclo3-green.txt` |
| Cobertura total | JaCoCo 100%; PIT 11/13 (2 equivalentes justificados) | `estoque-tdd-final-*.txt` |

## Semana 11: exercicios_integracao_springboot (lista de exercícios)

| Requisito / rubrica | Testes | Evidência |
|---|---|---|
| CT01: cadastro válido (Teclado 150,0; ID; encontrado no repositório) | `ProdutoServiceIntegrationTest#deveCadastrarERecuperarProduto` | `evidencias/mvn-test.txt`; sabotagem 1 detectada (`sanidade-sabotagem.txt`) |
| CT02: nome em branco (exceção; contagem não muda) | `ProdutoServiceIntegrationTest#naoDevePersistirProdutoComNomeEmBranco`; `ProdutoServiceIntegrationComplementarTest#nomeInvalidoNaoPersiste` (5 casos) | `mvn-test.txt`; sabotagem 2 detectada |
| CT03: atualização 80 → 95 consultada no repositório | `ProdutoServiceIntegrationTest#deveAtualizarPrecoPersistido`; complementares `atualizarComPrecoInvalidoMantemPrecoAntigo`, `atualizarInexistente`, `atualizarNaoDuplicaRegistro` | `mvn-test.txt` |
| CT04: POST /produtos → 200, id numérico, nome, preço | `ProdutoControllerIntegrationTest#deveCadastrarProdutoPelaApi`; `ProdutoApiServidorRealIntegrationTest#deveCadastrarPorHttpReal` | `mvn-test.txt` |
| CT05: POST + GET /produtos/{id} com o mesmo conteúdo | `ProdutoControllerIntegrationTest#deveBuscarProdutoPelaApi`; `ProdutoApiServidorRealIntegrationTest#deveBuscarPorHttpReal` | `mvn-test.txt` |
| Sem mocks para Repository/Service | Todas as classes usam `@SpringBootTest` com beans reais (nenhum `@MockBean`/`Mockito`) | `grep` no PR |
| `mvn test` e registro do resultado | n/a | `evidencias/mvn-test.txt`, `mvn-verify.txt` |
| README com evidências e explicação do isolamento (1,0) | n/a | `README.md`, seções "Resultado", "Isolamento entre testes", "Respostas às discussões" e "Achados" |
| Discussões do Ex. 1 e Ex. 2 | n/a | `README.md`, "Respostas às discussões do enunciado" (5 respostas) |

## Semana 14: teste de usuário final (`TESTE USUARIO FINAL.pdf`)

| Item do enunciado | Artefato / teste | Evidência / status |
|---|---|---|
| 1. Objetivo | `01-plano-de-teste.md` §1 | Pronto |
| 2. Cenário de uso | `01` §2; `02-roteiro-sessao.md` §B (leitura padronizada) | Pronto |
| 3. Tarefas 1–7 | `01` §3 (8 tarefas com critério operacional); `02` §C; `03-planilha-observacao.csv` (linhas) | Pronto |
| (lacunas 4 e 5 do PDF) | `01` §4 (perfil e nº ≥ 5) e §5 (procedimento) | Pronto |
| 6. Dados coletados: tempo, taxa de sucesso sem ajuda, erros | `03-planilha-observacao.csv/.md`; `AnalisadorUsabilidadeTest#tempos`, `#sucessoPorTarefa`, `#sucessoGlobal`, `#erros` | Analisador testado; **dados pendentes de coleta** |
| 7. Critério: 90% adicionam ao carrinho | `AnalisadorUsabilidade#criterioCarrinho`; testes `criteriosAtendidos`, `criteriosNaoAtendidos`, `fronteirasDosCriterios` (9/10 = 90%) | Analisador testado; **resultado pendente** |
| 7. Critério: tempo médio ≤ 4 min | `AnalisadorUsabilidade#criterioTempo`; os mesmos testes (240 s / 241 s) | Analisador testado; **resultado pendente** |
| 8. Papéis e ferramentas (inclui formulário SUS) | `01` §8; `04-questionario-sus.csv/.md`; `CalculadoraSusTest` (50 / 100 / 0) | Pronto |
| 9. Planilha de observação (colunas do modelo) | `03-planilha-observacao.csv` (colunas exatas + `participante`, `com_ajuda`) | Pronto |
| Executar o teste com usuários | Sessões reais (≥ 5) | ⏳ **Pendente**, depende da equipe |
