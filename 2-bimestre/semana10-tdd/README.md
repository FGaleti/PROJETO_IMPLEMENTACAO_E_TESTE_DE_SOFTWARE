# Semana 10: TDD nos quatro projetos

Enunciado: *"implementar TDD nos quatro projetos disponíveis até a implementação e cobertura total"*
([aulas/SEMANA10 no repositório oficial](https://github.com/JoaoChoma/teste_software_2026/tree/main/aulas/SEMANA10)).

Os quatro projetos partem do código-base original do professor (commit `chore: importa código-base original…`).
**Nenhum teste fornecido foi alterado.** Os testes novos estão em arquivos novos (`*CasosAdicionaisTest`,
`BubbleSortParadaAntecipadaTest`). No carrinho, que não tinha testes, eles foram escritos dentro de `CarrinhoTest`, como pede o enunciado.

## Como executar

Pré-requisitos: Java 17+. Rode os comandos dentro da pasta de cada projeto. Use `mvn` (Maven 3.9+ instalado) ou o Maven Wrapper incluído (`./mvnw`, ou `mvnw.cmd` no Windows), que tem o mesmo efeito.

| Comando | O que faz |
|---|---|
| `mvn test` | Roda os testes e gera o relatório JaCoCo em `target/site/jacoco/index.html` |
| `mvn verify` | Faz o mesmo e **falha se a cobertura for menor que 100%** (instruções, linhas, desvios, métodos e classes) |
| `mvn -Pmutacao verify` | Faz o mesmo e roda o **teste de mutação (PIT)**, com relatório em `target/pit-reports/index.html` |

O `jacoco:check` fica ligado à fase `verify`, nunca à `test`. Assim, um problema de cobertura não derruba um `mvn test`.

## Resultados

| Projeto | Testes do professor | Testes adicionais | Total executado | Instruções | Linhas | Desvios | Métodos | Mutação (PIT, STRONGER) |
|---|---:|---:|---:|---:|---:|---:|---:|---:|
| `bubblesort-tdd` | 3 | 22 | **25** | 86/86 (100%) | 22/22 (100%) | 10/10 (100%) | 5/5 (100%) | **20/20 (100%)** |
| `carrinho-compras-tdd` | 0 (vazio) | 26 | **26** | 102/102 (100%) | 29/29 (100%) | 8/8 (100%) | 12/12 (100%) | **21/21 (100%)** |
| `conta-bancaria-tdd-maven` | 4 | 13 | **17** | 36/36 (100%) | 10/10 (100%) | 6/6 (100%) | 4/4 (100%) | 10/12 (83%), 2 equivalentes* |
| `estoque-tdd` | 6 | 13 | **19** | 37/37 (100%) | 12/12 (100%) | 6/6 (100%) | 5/5 (100%) | 11/13 (85%), 2 equivalentes* |

"Total executado" conta cada caso de um `@ParameterizedTest` como um teste, do mesmo jeito que o Surefire.
Evidências: `evidencias/<projeto>-final-verify.txt` e `evidencias/<projeto>-final-mutacao.txt`.

\* **Mutantes equivalentes sobreviventes.** São quatro, todos do mesmo tipo:

| Projeto | Local | Mutação | Por que é equivalente |
|---|---|---|---|
| conta | `depositar`: `if (valor > 0)` | `>` vira `>=` | Só muda o caso `valor == 0`, e `saldo += 0.0` não altera o saldo |
| conta | `sacar`: `valor > 0 && …` | `>` vira `>=` | Só muda o caso `valor == 0`, e `saldo -= 0.0` não altera o saldo |
| estoque | `adicionarEstoque`: `if (quantidade > 0)` | `>` vira `>=` | Somar 0 unidades não altera o estoque |
| estoque | `retirarEstoque`: `quantidade > 0 && …` | `>` vira `>=` | Retirar 0 unidades não altera o estoque |

Nenhum teste observável consegue distinguir esses mutantes do original. Seria preciso mudar a assinatura `void` dos métodos do professor
(por exemplo, para retornar `boolean`), e isso não é permitido. Os testes de valor zero existem e documentam a regra
(R6 do estoque diz *"negativa ou igual a zero"*), mas o comportamento é o mesmo. O limiar do profile `mutacao`
nesses dois `pom.xml` foi fixado no valor alcançado (83 e 84). Se um teste regredir, o build falha.

## Ciclos TDD e evidências

Cada linha corresponde a um commit, na ordem do histórico. Cada arquivo de evidência é a saída real de `mvn -B test`.

> **Notas de leitura do histórico.** (1) Nas linhas "RED (regressão)", o teste que falha **já existia** (é do professor). Quem provoca o RED é o commit de implementação mínima, por isso ele aparece como `feat(...)` e não como `test(...)`; a correção vem no commit `fix(...)` seguinte. (2) As evidências foram geradas quando os projetos ficavam em `aulas/SEMANA10/`, antes da reorganização por bimestre, e por isso mostram esses caminhos antigos. A pasta foi movida com `git mv`; use `git log --follow -- <arquivo>` para ver o histórico completo de cada arquivo.

### bubblesort-tdd

| Ciclo | Etapa | O que aconteceu | Evidência |
|---|---|---|---|
| 0 | RED | Os 3 testes do professor, antes de qualquer implementação: 2 falhas. `deveManterVetorJaOrdenado` **passa por acidente**, porque o método vazio não altera um vetor que já está ordenado | [ciclo0-red](evidencias/bubblesort-tdd-ciclo0-red.txt) |
| 1 | GREEN | Bubble Sort mínimo com trocas adjacentes | [ciclo1-green](evidencias/bubblesort-tdd-ciclo1-green.txt) |
| 1 | REFACTOR | Extrai o método `trocar(i, j)` | [ciclo1-refactor](evidencias/bubblesort-tdd-ciclo1-refactor.txt) |
| 2 | RED | Casos adicionais e oráculo: só o caso `null` falha (`NullPointerException`). Os demais já passavam e funcionam como testes de caracterização | [ciclo2-red](evidencias/bubblesort-tdd-ciclo2-red.txt) |
| 2 | GREEN | Guarda para vetor `null` | [ciclo2-green](evidencias/bubblesort-tdd-ciclo2-green.txt) |
| 3 | RED | Parada antecipada: o teste não compila porque `getPassagens()` ainda não existe | [ciclo3-red](evidencias/bubblesort-tdd-ciclo3-red.txt) |
| 3 | GREEN | Parada antecipada quando uma passagem não faz troca, com contagem de passagens | [ciclo3-green](evidencias/bubblesort-tdd-ciclo3-green.txt) |

### carrinho-compras-tdd (um ciclo por requisito)

| Ciclo | Requisito | RED | GREEN | REFACTOR |
|---|---|---|---|---|
| 1 | R1: carrinho vazio com total 0,00 | [red](evidencias/carrinho-compras-tdd-ciclo1-red.txt): `getTotal()` não existe | [green](evidencias/carrinho-compras-tdd-ciclo1-green.txt): `return 0.0` (*fake it*) | n/a |
| 2 | R2: adicionar Teclado de 100,00 | [red](evidencias/carrinho-compras-tdd-ciclo2-red.txt): `adicionar()` não existe | [green](evidencias/carrinho-compras-tdd-ciclo2-green.txt): `total = preço` (mínimo) | n/a |
| 3 | R3: Teclado + Mouse = 150,00 | [red](evidencias/carrinho-compras-tdd-ciclo3-red.txt): *esperado 150, obtido 50* (triangulação) | [green](evidencias/carrinho-compras-tdd-ciclo3-green.txt): `total += preço` | n/a |
| 4 | R4: remover (incluindo inexistente, vazio e ocorrência única) | [red](evidencias/carrinho-compras-tdd-ciclo4-red.txt): `remover()` não existe | [green](evidencias/carrinho-compras-tdd-ciclo4-green.txt): lista de produtos | n/a |
| 5 | R5: preço negativo e produto `null` | [red](evidencias/carrinho-compras-tdd-ciclo5-red.txt): 5 falhas | [green](evidencias/carrinho-compras-tdd-ciclo5-green.txt) | [refactor](evidencias/carrinho-compras-tdd-ciclo5-refactor.txt): `validar()` |
| 6 | R6: desconto de 10% a partir de 500,00 | [red](evidencias/carrinho-compras-tdd-ciclo6-red.txt): `getSubtotal()`/`getDesconto()` não existem | [green](evidencias/carrinho-compras-tdd-ciclo6-green.txt): literais 500/0,10 | [refactor](evidencias/carrinho-compras-tdd-ciclo6-refactor.txt): constantes nomeadas |
| 7 | `getProdutos()` imutável | [red](evidencias/carrinho-compras-tdd-ciclo7-red.txt) | [green](evidencias/carrinho-compras-tdd-ciclo7-green.txt) | n/a |

Depois do ciclo 7, o `mvn verify` falhou na cobertura porque `Produto.getNome()` nunca era chamado.
O teste de listagem passou a conferir os nomes dos produtos (commit `test(carrinho): verifica nomes na listagem`).

### conta-bancaria-tdd-maven

| Ciclo | Etapa | O que aconteceu | Evidência |
|---|---|---|---|
| 0 | RED | Os 4 testes do professor falham | [ciclo0-red](evidencias/conta-bancaria-tdd-maven-ciclo0-red.txt) |
| 1 | GREEN (depósito) | `saldo += valor`: os 2 testes de depósito passam. `deveSacarValorDaConta` continua falhando (é do ciclo 2). `naoDevePermitirSaqueMaiorQueSaldo` **passa por acidente**, porque `sacar()` está vazio | [ciclo1-green](evidencias/conta-bancaria-tdd-maven-ciclo1-green.txt) |
| 2 | RED (regressão) | `saldo -= valor`: o saque passa, mas o teste que passava por acidente **quebra** (saldo -50). A suíte detectou a regressão | [ciclo2-red](evidencias/conta-bancaria-tdd-maven-ciclo2-red.txt) |
| 2 | GREEN | Saque só acontece se `valor <= saldo` | [ciclo2-green](evidencias/conta-bancaria-tdd-maven-ciclo2-green.txt) |
| 3 | RED | Casos adicionais: um depósito negativo **reduz** o saldo e um saque negativo **aumenta** o saldo | [ciclo3-red](evidencias/conta-bancaria-tdd-maven-ciclo3-red.txt) |
| 3 | GREEN | Ignora valor menor ou igual a zero | [ciclo3-green](evidencias/conta-bancaria-tdd-maven-ciclo3-green.txt) |

### estoque-tdd

| Ciclo | Etapa | O que aconteceu | Evidência |
|---|---|---|---|
| 0 | RED | 4 falhas e **2 testes passando por acidente** (veja a seção "Testes que passam por acidente") | [ciclo0-red](evidencias/estoque-tdd-ciclo0-red.txt) |
| 1 | RED (regressão) | `quantidade += q`: R2 e R3 passam, mas R6 (`naoDeveAdicionarQuantidadeNegativa`) **quebra** (estoque -10). `deveRetirarProdutoDoEstoque` (R4) continua falhando, porque é do ciclo 2 | [ciclo1-red](evidencias/estoque-tdd-ciclo1-red.txt) |
| 1 | GREEN | Ignora entrada menor ou igual a zero. Fica pendente só o R4 (próximo ciclo). R5 passa por acidente porque `retirarEstoque()` está vazio | [ciclo1-green](evidencias/estoque-tdd-ciclo1-green.txt) |
| 2 | RED (regressão) | `quantidade -= q`: R4 passa, mas R5 **quebra** (estoque -5) | [ciclo2-red](evidencias/estoque-tdd-ciclo2-red.txt) |
| 2 | GREEN | Retirada só acontece se `q <= disponível`. Os 6 testes do professor passam | [ciclo2-green](evidencias/estoque-tdd-ciclo2-green.txt) |
| 3 | RED | Casos adicionais: retirar uma quantidade negativa **aumenta** o estoque | [ciclo3-red](evidencias/estoque-tdd-ciclo3-red.txt) |
| 3 | GREEN | Ignora retirada menor ou igual a zero | [ciclo3-green](evidencias/estoque-tdd-ciclo3-green.txt) |

## Decisões de projeto

### Carrinho, R5: ignorar ou lançar exceção? (resposta à "discussão com o professor")

Decidimos **lançar `IllegalArgumentException`**, e o carrinho **permanece inalterado**.

- Ignorar a operação em silêncio esconderia um erro de cadastro: quem chama o método não saberia que o item não entrou no carrinho.
  A exceção torna a falha explícita e fácil de testar (`assertThrows` e conferência da mensagem).
- **Preço zero é válido** (brinde, item promocional). A fronteira testada é -0,01 (rejeitado), 0,00 e 0,01 (aceitos).
- **Produto `null`** também lança `IllegalArgumentException("Produto não pode ser nulo")`.
- A validação fica no `Carrinho`. O `Produto` do professor **não foi alterado**.

Outros pontos do carrinho:

- `remover(Produto)` retorna `boolean` (`false` se o produto não estava no carrinho) e remove **uma** ocorrência.
  A comparação é por **identidade**, porque `Produto` não define `equals` e não pode ser alterado.
- Desconto: subtotal **maior ou igual a 500,00** recebe 10%. As constantes nomeadas são `VALOR_MINIMO_PARA_DESCONTO` e `PERCENTUAL_DESCONTO`.
  O desconto é calculado sobre a soma dos itens (300 + 200 = 500 vira 450).
  Também há um teste de regressão R4×R6: remover um item e cair abaixo de 500 faz o desconto desaparecer.
- `getProdutos()` devolve uma visão **imutável** da lista. Tentar modificá-la lança `UnsupportedOperationException`.

### Conta bancária e estoque: contrato "operação inválida é ignorada"

Os testes do professor `naoDevePermitirSaqueMaiorQueSaldo` e `naoDeveRetirarQuantidadeMaiorQueEstoque`/`naoDeveAdicionarQuantidadeNegativa`
exigem que o valor **continue o mesmo** e não esperam exceção. Seguimos esse contrato de forma consistente:
depósito, saque, entrada ou retirada com valor menor ou igual a zero, e saque ou retirada acima do disponível, são **ignorados**.
Sacar ou retirar **exatamente** o disponível é permitido e zera o saldo ou o estoque.

### Bubble Sort com vetor `null`

Contrato definido: `ordenar()` **não lança exceção** e `getNumeros()` continua devolvendo `null`.
O construtor e as assinaturas do professor não mudaram. A ordenação é feita no próprio vetor (*in-place*): `getNumeros()` devolve a mesma referência recebida.

### Parada antecipada observável

Uma passagem sem nenhuma troca encerra o algoritmo. Para que os testes consigam **garantir** isso, e não só passar pela linha,
`BubbleSort` registra quantas passagens fez em `getPassagens()`, com visibilidade de pacote (não faz parte da API pública).
Sem isso, remover a otimização não mudaria o resultado, e o mutante sobreviveria.
Os testes cobrem os dois lados do desvio: vetor ordenado faz 1 passagem, `[2,1,3,4,5]` faz 2, e vetor decrescente de 5 elementos faz 4.

## Testes que passam por acidente

- **estoque-tdd:** `produtoDeveIniciarComEstoqueZero` e `naoDeveAdicionarQuantidadeNegativa` passam **antes** de qualquer
  implementação. O campo já nasce com 0 e o método vazio não altera nada. O segundo **quebrou** assim que a soma ingênua foi
  implementada (ciclo 1). Isso mostra que um teste verde no início não prova que a regra existe.
- O mesmo aconteceu com `deveManterVetorJaOrdenado` (bubblesort) e, entre ciclos, com `naoDevePermitirSaqueMaiorQueSaldo`
  (conta) e `naoDeveRetirarQuantidadeMaiorQueEstoque` (estoque).
- Os testes de valor **zero** (depósito, saque, entrada e retirada) especificam a regra, mas não distinguem a implementação.
  Por isso aparecem como mutantes equivalentes na tabela acima.

## Observações de qualidade

- **100% de cobertura não significa ausência de defeitos.** A cobertura mostra que uma linha foi *executada*, não que um defeito nela
  seria *detectado*. Por isso usamos também: (1) **teste de mutação**, que mostra que os testes falham quando o código é adulterado;
  (2) **fronteiras e partições de equivalência** com `@ParameterizedTest` (499,99/500,00/500,01; -0,01/0,00/0,01; saldo ± 0,01);
  (3) **oráculo**: 200 vetores aleatórios com `new Random(42)` comparados com `Arrays.sort`.
- **`double` para dinheiro** foi mantido por fidelidade ao código-base. Todas as comparações usam delta `0.001`
  (por exemplo, 0,10 + 0,20 = 0,30). Em produção, o correto seria usar `BigDecimal`.
- Todos os testes novos usam `@DisplayName` em português, o padrão **Arrange / Act / Assert** e verificam um comportamento por teste.
