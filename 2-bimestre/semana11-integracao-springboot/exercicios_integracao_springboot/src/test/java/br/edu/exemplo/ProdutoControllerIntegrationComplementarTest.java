package br.edu.exemplo;

import br.edu.exemplo.model.Produto;
import br.edu.exemplo.repository.ProdutoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Casos complementares (além de CT04–CT05) do fluxo HTTP simulado
 * MockMvc → DispatcherServlet → Controller → Service → Repository → H2.
 *
 * A aplicação não tem @ControllerAdvice. Quando o serviço lança
 * IllegalArgumentException, nenhum HandlerExceptionResolver a converte em
 * resposta HTTP, e o MockMvc a repropaga embrulhada em ServletException.
 * Estes testes documentam esse comportamento (ver "Achados" no README).
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("API /produtos via MockMvc — casos complementares")
class ProdutoControllerIntegrationComplementarTest {

    private static final double DELTA = 0.001;

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired ProdutoRepository repository;
    @PersistenceContext EntityManager entityManager;

    private void sincronizarComBanco() {
        entityManager.flush();
        entityManager.clear();
    }

    private Long idInexistente() {
        Long id = repository.save(new Produto("Temporário", 1.0)).getId();
        repository.deleteById(id);
        sincronizarComBanco();
        return id;
    }

    /** Verifica que a causa raiz da ServletException é IllegalArgumentException com a mensagem esperada. */
    private static void assertCausaRaiz(ServletException erro, String mensagemEsperada) {
        Throwable raiz = NestedExceptionUtils.getMostSpecificCause(erro);
        assertInstanceOf(IllegalArgumentException.class, raiz);
        assertEquals(mensagemEsperada, raiz.getMessage());
    }

    @Test
    @DisplayName("POST válido persiste de fato no H2 (lido pelo repositório após flush/clear)")
    void postPersisteNoBanco() throws Exception {
        // Arrange
        long antes = repository.count();

        // Act
        String corpo = mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Teclado\",\"preco\":150.0}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        long id = objectMapper.readTree(corpo).get("id").asLong();
        sincronizarComBanco();

        // Assert
        assertEquals(antes + 1, repository.count());
        Produto doBanco = repository.findById(id).orElseThrow();
        assertEquals("Teclado", doBanco.getNome());
        assertEquals(150.0, doBanco.getPreco(), DELTA);
    }

    @Test
    @DisplayName("POST com nome em branco: exceção repropagada (deveria ser 400) e nada persistido")
    void postComNomeEmBranco() {
        // Arrange
        long antes = repository.count();

        // Act
        ServletException erro = assertThrows(ServletException.class, () -> mockMvc.perform(post("/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"   \",\"preco\":80.0}")));

        // Assert
        assertCausaRaiz(erro, "Nome obrigatório");
        sincronizarComBanco();
        assertEquals(antes, repository.count());
    }

    @Test
    @DisplayName("POST com preço zero: exceção repropagada (deveria ser 400) e nada persistido")
    void postComPrecoZero() {
        // Arrange
        long antes = repository.count();

        // Act
        ServletException erro = assertThrows(ServletException.class, () -> mockMvc.perform(post("/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Mouse\",\"preco\":0.0}")));

        // Assert
        assertCausaRaiz(erro, "Preço deve ser positivo");
        sincronizarComBanco();
        assertEquals(antes, repository.count());
    }

    @Test
    @DisplayName("POST sem o campo preco: o record recebe 0.0 e a regra de preço é acionada (achado)")
    void postSemCampoPreco() {
        // Arrange
        long antes = repository.count();

        // Act
        ServletException erro = assertThrows(ServletException.class, () -> mockMvc.perform(post("/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Mouse\"}")));

        // Assert
        assertCausaRaiz(erro, "Preço deve ser positivo");
        sincronizarComBanco();
        assertEquals(antes, repository.count());
    }

    @Test
    @DisplayName("GET de ID inexistente: exceção repropagada (deveria ser 404)")
    void getInexistente() {
        // Arrange
        Long id = idInexistente();

        // Act
        ServletException erro = assertThrows(ServletException.class,
                () -> mockMvc.perform(get("/produtos/{id}", id)));

        // Assert
        assertCausaRaiz(erro, "Produto não encontrado");
    }

    @Test
    @DisplayName("GET com ID não numérico → 400 (conversão do @PathVariable falha)")
    void getComIdNaoNumerico() throws Exception {
        mockMvc.perform(get("/produtos/{id}", "abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST sem Content-Type → 415 e nada persistido")
    void postSemContentType() throws Exception {
        // Arrange
        long antes = repository.count();

        // Act + Assert
        mockMvc.perform(post("/produtos").content("{\"nome\":\"Mouse\",\"preco\":80.0}"))
                .andExpect(status().isUnsupportedMediaType());
        sincronizarComBanco();
        assertEquals(antes, repository.count());
    }

    @Test
    @DisplayName("POST com JSON malformado → 400 e nada persistido")
    void postComJsonMalformado() throws Exception {
        // Arrange
        long antes = repository.count();

        // Act + Assert
        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Mouse\",\"preco\":"))
                .andExpect(status().isBadRequest());
        sincronizarComBanco();
        assertEquals(antes, repository.count());
    }

    @Test
    @DisplayName("POST com preço não numérico → 400 e nada persistido")
    void postComPrecoNaoNumerico() throws Exception {
        // Arrange
        long antes = repository.count();

        // Act + Assert
        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Mouse\",\"preco\":\"oitenta\"}"))
                .andExpect(status().isBadRequest());
        sincronizarComBanco();
        assertEquals(antes, repository.count());
    }
}
