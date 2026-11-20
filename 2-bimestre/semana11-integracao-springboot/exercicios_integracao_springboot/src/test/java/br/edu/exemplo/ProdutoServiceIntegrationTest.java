package br.edu.exemplo;
import br.edu.exemplo.model.Produto;
import br.edu.exemplo.repository.ProdutoRepository;
import br.edu.exemplo.service.ProdutoService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest @Transactional
class ProdutoServiceIntegrationTest {
 private static final double DELTA = 0.001;
 @Autowired ProdutoService service;
 @Autowired ProdutoRepository repository;
 @PersistenceContext EntityManager entityManager;

 /**
  * Envia ao H2 o que está pendente (flush) e esvazia o cache de 1º nível do JPA
  * (clear). Assim, o próximo findById executa um SELECT real no banco, em vez de
  * devolver a mesma instância que o serviço acabou de manipular em memória.
  */
 private void sincronizarComBanco() {
  entityManager.flush();
  entityManager.clear();
 }

 @Test void deveCadastrarERecuperarProduto(){
  // CT01 — Arrange + Act: cadastrar Teclado por 150
  Produto cadastrado = service.cadastrar("Teclado", 150.0);
  sincronizarComBanco();

  // Assert: recebeu ID e o registro está no banco com os dados corretos
  assertNotNull(cadastrado.getId());
  Produto doBanco = repository.findById(cadastrado.getId()).orElseThrow();
  assertNotSame(cadastrado, doBanco, "a leitura deve vir do H2, não do cache do JPA");
  assertEquals("Teclado", doBanco.getNome());
  assertEquals(150.0, doBanco.getPreco(), DELTA);
 }

 @Test void naoDevePersistirProdutoComNomeEmBranco(){
  // CT02 — Arrange: contagem antes
  long antes = repository.count();

  // Act + Assert: nome em branco é rejeitado
  IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
          () -> service.cadastrar("   ", 80.0));
  assertEquals("Nome obrigatório", erro.getMessage());

  // Assert: nada foi persistido
  sincronizarComBanco();
  assertEquals(antes, repository.count());
 }

 @Test void deveAtualizarPrecoPersistido(){
  // CT03 (desafio) — Arrange: cadastrar Mouse por 80
  Long id = service.cadastrar("Mouse", 80.0).getId();
  sincronizarComBanco();

  // Act: atualizar para 95
  service.atualizarPreco(id, 95.0);
  sincronizarComBanco();

  // Assert: nova consulta ao banco traz 95 e o nome não muda
  Produto doBanco = repository.findById(id).orElseThrow();
  assertEquals(95.0, doBanco.getPreco(), DELTA);
  assertEquals("Mouse", doBanco.getNome());
 }
}
