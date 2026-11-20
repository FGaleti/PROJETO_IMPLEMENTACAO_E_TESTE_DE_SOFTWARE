package br.edu.exemplo;

import br.edu.exemplo.model.Produto;
import br.edu.exemplo.repository.ProdutoRepository;
import br.edu.exemplo.service.ProdutoService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Casos complementares (além de CT01–CT03) de integração Service + Repository + H2,
 * sem mocks. Fecham as partições de entrada e 100% dos desvios de ProdutoService.
 * Cada teste roda em transação revertida ao final (@Transactional).
 */
@SpringBootTest
@Transactional
@DisplayName("ProdutoService + H2 — casos complementares")
class ProdutoServiceIntegrationComplementarTest {

    private static final double DELTA = 0.001;

    @Autowired ProdutoService service;
    @Autowired ProdutoRepository repository;
    @PersistenceContext EntityManager entityManager;

    private void sincronizarComBanco() {
        entityManager.flush();
        entityManager.clear();
    }

    /** ID garantidamente inexistente, sem depender de valor fixo: cria, apaga e devolve o ID. */
    private Long idInexistente() {
        Long id = repository.save(new Produto("Temporário", 1.0)).getId();
        repository.deleteById(id);
        sincronizarComBanco();
        return id;
    }

    // ------------------------------------------------------------ cadastrar

    @ParameterizedTest(name = "nome [{0}] é rejeitado")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "   "})
    @DisplayName("Nome nulo, vazio ou só com espaços → 'Nome obrigatório' e nada persistido")
    void nomeInvalidoNaoPersiste(String nome) {
        // Arrange
        long antes = repository.count();

        // Act + Assert
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> service.cadastrar(nome, 80.0));
        assertEquals("Nome obrigatório", erro.getMessage());
        sincronizarComBanco();
        assertEquals(antes, repository.count());
    }

    @ParameterizedTest(name = "preço {0} é rejeitado")
    @ValueSource(doubles = {0.0, -0.01, -80.0})
    @DisplayName("Fronteira preco <= 0 → 'Preço deve ser positivo' e nada persistido")
    void precoNaoPositivoNaoPersiste(double preco) {
        // Arrange
        long antes = repository.count();

        // Act + Assert
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> service.cadastrar("Teclado", preco));
        assertEquals("Preço deve ser positivo", erro.getMessage());
        sincronizarComBanco();
        assertEquals(antes, repository.count());
    }

    @Test
    @DisplayName("Fronteira: preço 0,01 é o menor válido e é persistido")
    void menorPrecoValidoEhPersistido() {
        // Arrange
        long antes = repository.count();

        // Act
        Long id = service.cadastrar("Adesivo", 0.01).getId();
        sincronizarComBanco();

        // Assert
        assertEquals(antes + 1, repository.count());
        assertEquals(0.01, repository.findById(id).orElseThrow().getPreco(), DELTA);
    }

    // --------------------------------------------------------------- buscar

    @Test
    @DisplayName("buscar(id) existente devolve os dados lidos do banco")
    void buscarExistente() {
        // Arrange
        Long id = service.cadastrar("Monitor", 900.0).getId();
        sincronizarComBanco();

        // Act
        Produto encontrado = service.buscar(id);

        // Assert
        assertEquals(id, encontrado.getId());
        assertEquals("Monitor", encontrado.getNome());
        assertEquals(900.0, encontrado.getPreco(), DELTA);
    }

    @Test
    @DisplayName("buscar(id) inexistente → 'Produto não encontrado'")
    void buscarInexistente() {
        // Arrange
        Long id = idInexistente();

        // Act + Assert
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> service.buscar(id));
        assertEquals("Produto não encontrado", erro.getMessage());
    }

    // -------------------------------------------------------- atualizarPreco

    @ParameterizedTest(name = "novo preço {0} é rejeitado")
    @ValueSource(doubles = {0.0, -0.01, -95.0})
    @DisplayName("atualizarPreco com preço inválido lança exceção e mantém o preço antigo no banco")
    void atualizarComPrecoInvalidoMantemPrecoAntigo(double novoPreco) {
        // Arrange
        Long id = service.cadastrar("Mouse", 80.0).getId();
        sincronizarComBanco();

        // Act
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> service.atualizarPreco(id, novoPreco));
        sincronizarComBanco();

        // Assert
        assertEquals("Preço deve ser positivo", erro.getMessage());
        assertEquals(80.0, repository.findById(id).orElseThrow().getPreco(), DELTA);
    }

    @Test
    @DisplayName("atualizarPreco de ID inexistente → 'Produto não encontrado' e nada é criado")
    void atualizarInexistente() {
        // Arrange
        Long id = idInexistente();
        long antes = repository.count();

        // Act
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> service.atualizarPreco(id, 95.0));
        sincronizarComBanco();

        // Assert
        assertEquals("Produto não encontrado", erro.getMessage());
        assertEquals(antes, repository.count());
        assertFalse(repository.existsById(id));
    }

    @Test
    @DisplayName("atualizarPreco devolve o produto com o novo preço e não cria registro novo")
    void atualizarNaoDuplicaRegistro() {
        // Arrange
        Long id = service.cadastrar("Headset", 200.0).getId();
        sincronizarComBanco();
        long antes = repository.count();

        // Act
        Produto atualizado = service.atualizarPreco(id, 180.0);
        sincronizarComBanco();

        // Assert
        assertEquals(id, atualizado.getId());
        assertEquals(180.0, atualizado.getPreco(), DELTA);
        assertEquals(antes, repository.count());
    }
}
