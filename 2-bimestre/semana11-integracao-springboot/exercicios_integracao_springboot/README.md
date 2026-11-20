# Semana 11: testes de integração com Spring Boot e JUnit 5

Projeto-base: `exercicios_integracao_springboot` (Spring Boot 3.3.13, Spring Data JPA, H2 em memória, MockMvc).
O enunciado e a rubrica originais estão em [ENUNCIADO.md](ENUNCIADO.md) e em [../lista_exercicios_integracao_springboot.md](../lista_exercicios_integracao_springboot.md).

Restrições respeitadas:

- **Sem mocks.** `ProdutoService` e `ProdutoRepository` são os beans reais, e o banco é o H2.
- **`src/main` não foi alterado.** O projeto-base é o objeto de teste. Os defeitos encontrados estão em [Achados](#achados).
- Os `TODO` foram preenchidos **dentro dos métodos originais**, com os mesmos nomes. Os casos complementares estão em classes novas.

## Como executar

```bash
mvn test     # 34 testes
mvn verify   # também exige 100% de linhas e desvios em service, controller e model (JaCoCo)
```

Relatório de cobertura: `target/site/jacoco/index.html`.

## Resultado

Saída real: [evidencias/mvn-test.txt](evidencias/mvn-test.txt) e [evidencias/mvn-verify.txt](evidencias/mvn-verify.txt).

```
[INFO] Tests run: 4,  Failures: 0, Errors: 0, Skipped: 0 -- in ProdutoApiServidorRealIntegrationTest
[INFO] Tests run: 9,  Failures: 0, Errors: 0, Skipped: 0 -- in ProdutoControllerIntegrationComplementarTest
[INFO] Tests run: 2,  Failures: 0, Errors: 0, Skipped: 0 -- in ProdutoControllerIntegrationTest
[INFO] Tests run: 16, Failures: 0, Errors: 0, Skipped: 0 -- in ProdutoServiceIntegrationComplementarTest
[INFO] Tests run: 3,  Failures: 0, Errors: 0, Skipped: 0 -- in ProdutoServiceIntegrationTest
[INFO] Tests run: 34, Failures: 0, Errors: 0, Skipped: 0
[INFO] All coverage checks have been met.
[INFO] BUILD SUCCESS
```

### Cobertura (JaCoCo)

| Classe | Instruções | Desvios | Linhas | Métodos |
|---|---:|---:|---:|---:|
| `service.ProdutoService` | 70/70 | **8/8** | 7/7 | 5/5 |
| `controller.ProdutoController` | 19/19 | n/a | 3/3 | 3/3 |
| `controller.ProdutoRequest` | 9/9 | n/a | 1/1 | 1/1 |
| `model.Produto` | 25/25 | n/a | 6/6 | 6/6 |
| `Aplicacao` (fora da regra) | 3/8 | n/a | 1/1 | 1/2 |

`Aplicacao.main` não é executado nos testes, porque o contexto é criado pelo `@SpringBootTest`. Por isso a classe fica fora da verificação.

### Os testes falham quando deveriam? (verificação de sanidade)

Testes que passam de primeira precisam provar que também detectam defeitos. Alteramos o `ProdutoService` de propósito, rodamos o teste
e revertemos com `git checkout`. Nada disso foi commitado. Evidência: [evidencias/sanidade-sabotagem.txt](evidencias/sanidade-sabotagem.txt).

| Defeito introduzido | Teste | Resultado |
|---|---|---|
| `cadastrar()` não chama `repository.save` | CT01 | **Falhou** (`expected: not <null>` no ID) |
| A validação aceita nome em branco (`nome==null` apenas) | CT02 | **Falhou** (`Expected IllegalArgumentException… nothing was thrown`) |

## Casos de teste

### Exercício 1: Service + Repository + H2 (`ProdutoServiceIntegrationTest`)

| Caso | Entrada | Verificação | Resultado |
|---|---|---|---|
| **CT01** `deveCadastrarERecuperarProduto` | `Teclado`, 150.0 | `assertNotNull(id)`; após `flush`+`clear`, `repository.findById(id)` encontra **outra instância** (`assertNotSame`), lida do H2, com nome e preço corretos | ✅ |
| **CT02** `naoDevePersistirProdutoComNomeEmBranco` | `"   "`, 80.0 | `assertThrows(IllegalArgumentException)` com mensagem `Nome obrigatório`; `repository.count()` antes == depois | ✅ |
| **CT03** `deveAtualizarPrecoPersistido` (desafio) | `Mouse` 80.0, depois `atualizarPreco` para 95.0 | Nova consulta ao **banco** retorna 95.0 e o nome continua `Mouse` | ✅ |

### Exercício 2: HTTP via MockMvc (`ProdutoControllerIntegrationTest`)

| Caso | Requisição | Verificação | Resultado |
|---|---|---|---|
| **CT04** `deveCadastrarProdutoPelaApi` | `POST /produtos` `{"nome":"Mouse","preco":80.0}`, `contentType(APPLICATION_JSON)` | `status().isOk()`, conteúdo JSON, `$.id` numérico, `$.nome == "Mouse"`, `$.preco == 80.0` | ✅ |
| **CT05** `deveBuscarProdutoPelaApi` | POST, extração do ID com `ObjectMapper.readTree`, depois `GET /produtos/{id}` | 200; `$.id`, `$.nome`, `$.preco`; **JSON do GET idêntico** ao do POST (`JsonNode.equals`) | ✅ |

### Complementares: serviço (`ProdutoServiceIntegrationComplementarTest`, 16 execuções)

| Partição / caso | Entrada | Verificação |
|---|---|---|
| Nome inválido | `null`, `""`, `" "`, `"\t"`, `"   "` | `Nome obrigatório`; contagem inalterada |
| Fronteira `preco <= 0` | 0.0, -0.01, -80.0 | `Preço deve ser positivo`; contagem inalterada |
| Menor preço válido | 0.01 | Persistido (+1 registro, preço lido do banco) |
| `buscar` existente | ID cadastrado | Devolve id, nome e preço do banco |
| `buscar` inexistente | ID criado e depois apagado | `Produto não encontrado` |
| `atualizarPreco` com preço inválido | 0.0, -0.01, -95.0 | Exceção **e** o preço no banco continua 80.0 |
| `atualizarPreco` de ID inexistente | ID apagado | `Produto não encontrado`; nenhum registro criado |
| `atualizarPreco` não duplica | 200, depois 180 | Mesmo ID, novo preço, contagem inalterada |

Com isso, os **8 desvios** de `ProdutoService` ficam cobertos: `nome==null`, `isBlank`, `preco<=0` (cadastrar), `orElseThrow` e `novoPreco<=0`, cada um nos dois sentidos.

### Complementares: API via MockMvc (`ProdutoControllerIntegrationComplementarTest`, 9 testes)

| Caso | Requisição | Verificação |
|---|---|---|
| POST persiste de fato | `{"nome":"Teclado","preco":150.0}` | +1 registro; `findById` (após `flush`/`clear`) traz os dados |
| POST com nome em branco | `{"nome":"   ","preco":80.0}` | `assertThrows(ServletException)`; causa raiz `IllegalArgumentException("Nome obrigatório")`; nada persistido |
| POST com preço zero | `{"nome":"Mouse","preco":0.0}` | `ServletException`; causa raiz `Preço deve ser positivo`; nada persistido |
| POST sem o campo `preco` | `{"nome":"Mouse"}` | O campo ausente vira `0.0` no record, e a regra de preço é acionada (veja os Achados) |
| GET de ID inexistente | `GET /produtos/{id apagado}` | `ServletException`; causa raiz `Produto não encontrado` |
| GET com ID não numérico | `GET /produtos/abc` | **400** |
| POST sem `Content-Type` | corpo JSON sem cabeçalho | **415**; nada persistido |
| POST com JSON malformado | `{"nome":"Mouse","preco":` | **400**; nada persistido |
| POST com preço não numérico | `"preco":"oitenta"` | **400**; nada persistido |

### Extra: HTTP real (`ProdutoApiServidorRealIntegrationTest`, 4 testes)

`@SpringBootTest(webEnvironment = RANDOM_PORT)` com `TestRestTemplate`. Repete CT04 e CT05 sobre **Tomcat real**,
sem `@Transactional` (a limpeza é feita com `repository.deleteAll()` em `@AfterEach`). Também confirma os achados como resposta HTTP de verdade:
nome em branco e ID inexistente respondem **500**.

## Respostas às discussões do enunciado

### 1. Por que `repository.findById()` é uma evidência diferente do objeto retornado pelo serviço?

O objeto que `service.cadastrar()` devolve é o mesmo que estava em memória. Ele prova que o serviço montou o produto,
mas não prova que o produto foi **gravado**. Se o `save` fosse esquecido, o objeto ainda teria nome e preço (a sabotagem 1 só foi detectada porque o ID ficou nulo).
Consultar o repositório verifica o **efeito colateral persistido**: o registro existe na tabela, com aquele ID e aqueles valores.

Existe uma armadilha nesse ponto. Dentro da mesma transação, o `findById` devolveria a **mesma instância** guardada no cache de primeiro nível
(o *persistence context*) do JPA, sem ir ao banco. Por isso os testes chamam `entityManager.flush()`, que envia os `INSERT`/`UPDATE` pendentes ao H2,
e `entityManager.clear()`, que esvazia o cache, antes de consultar. Assim o `findById` executa um `SELECT` real.
O CT01 confirma isso com `assertNotSame(cadastrado, doBanco)`.

### 2. O que `@Transactional` faz ao fim de cada teste?

Em uma classe de teste, o Spring abre uma transação antes de cada método e faz **rollback** ao final, por padrão (`@Rollback(true)`).
Tudo o que o teste gravou é desfeito, e o próximo teste encontra o banco como estava antes. O `flush()` envia os comandos SQL ao H2
**dentro** da transação, o que permite consultá-los, mas o rollback no final descarta tudo do mesmo jeito.

### 3. Quais componentes o MockMvc exercita?

O fluxo completo da aplicação, sem rede: `DispatcherServlet` → mapeamento (`@RequestMapping("/produtos")`, `@PostMapping`, `@GetMapping("/{id}")`) →
conversão do corpo JSON para `ProdutoRequest` pelo Jackson (`HttpMessageConverter`) → `ProdutoController` → `ProdutoService` (validações) →
`ProdutoRepository` (Spring Data JPA/Hibernate) → **H2** → serialização da resposta em JSON. Também passa pelos resolvedores de exceção padrão do Spring MVC,
como mostram os casos 400 e 415.

### 4. Por que o MockMvc não precisa de um servidor HTTP real?

O MockMvc chama o `DispatcherServlet` **diretamente**, com objetos `MockHttpServletRequest` e `MockHttpServletResponse` em memória.
Não há Tomcat, porta, socket nem serialização em rede, e a requisição roda na **mesma thread** do teste.
Por isso ela participa da transação do teste, que é revertida no final. Com servidor real (`RANDOM_PORT`), a requisição roda em outra thread,
com transação própria e commit real. Por esse motivo, `ProdutoApiServidorRealIntegrationTest` não usa `@Transactional` e precisa limpar o banco manualmente.

### 5. Qual é a diferença para um teste unitário do controller com mocks?

Em um teste unitário (`@WebMvcTest(ProdutoController.class)` com `@MockBean ProdutoService`), só a camada web é carregada.
O serviço é um dublê programado (`when(service.cadastrar(...)).thenReturn(...)`), e não há JPA nem banco.
Esse teste verifica o **contrato HTTP do controller isoladamente** e é rápido, mas **não detecta** erros de validação real, mapeamento JPA, SQL ou transação.
Os testes desta entrega usam `@SpringBootTest` com todos os beans reais, então exercitam a **integração** entre as camadas.
Em troca, são mais lentos e dependem do contexto completo.

## Isolamento entre testes

- **Rollback por teste.** As classes com `@Transactional` desfazem tudo o que gravaram ao final de cada método.
- **H2 em memória com `ddl-auto=create-drop`.** O esquema é criado quando o contexto sobe e descartado quando ele é encerrado. Nada persiste entre execuções do `mvn test`.
- **Nenhuma dependência de ID fixo nem de ordem de execução.** Os IDs são sempre lidos da resposta ou do objeto salvo. Um "ID inexistente" é obtido
  criando um registro e apagando-o em seguida. As contagens são sempre **relativas** (`antes`/`depois`), nunca absolutas.
- **Teste com servidor real.** Como não há rollback (o commit é real), cada teste limpa o banco com `deleteAll()` em `@AfterEach`.

## Achados

O projeto-base não foi corrigido, porque é o objeto de teste. Os comportamentos abaixo ficam documentados como achados.

| # | Achado | Evidência | Esperado | Severidade |
|---|---|---|---|---|
| A1 | **Falta tratamento de exceções** (`@ControllerAdvice`/`@ExceptionHandler`). Um erro de regra (`IllegalArgumentException`) sai do controller sem tratamento. No MockMvc ele é repropagado como `ServletException`. Sobre HTTP real, o cliente recebe **500 Internal Server Error** | `postComNomeEmBranco`, `postComPrecoZero`, `nomeEmBrancoRespondeErroInterno` | **400 Bad Request** com a mensagem de validação | Média |
| A2 | **ID inexistente responde 500** em vez de 404, pelo mesmo motivo de A1 | `getInexistente`, `idInexistenteRespondeErroInterno` | **404 Not Found** | Média |
| A3 | **Campo `preco` ausente vira 0.0.** `ProdutoRequest` usa o primitivo `double`, então o Jackson preenche o valor padrão 0.0, e o erro aparece como "Preço deve ser positivo" em vez de "preço obrigatório" | `postSemCampoPreco` | Rejeitar o campo ausente (`Double` + `@NotNull`/`@Valid`) com 400 | Baixa |
| A4 | Não há `PUT`/`PATCH` na API para `atualizarPreco`. A regra existe só no serviço | Leitura do `ProdutoController` | Expor o endpoint, se for requisito | Informativo |
