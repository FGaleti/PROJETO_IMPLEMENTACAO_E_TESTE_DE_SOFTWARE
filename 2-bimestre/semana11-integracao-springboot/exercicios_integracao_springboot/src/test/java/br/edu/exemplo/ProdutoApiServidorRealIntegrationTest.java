package br.edu.exemplo;

import br.edu.exemplo.repository.ProdutoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Variante opcional de CT04/CT05 sobre HTTP REAL: o Tomcat embutido sobe numa
 * porta aleatória e o TestRestTemplate faz requisições de verdade, para
 * contrastar com o MockMvc.
 *
 * Aqui NÃO há @Transactional: a requisição é atendida em outra thread (a do
 * Tomcat), com transação própria, e o commit é real. Por isso a limpeza é
 * explícita com deleteAll() em @AfterEach.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("API /produtos sobre HTTP real (Tomcat + TestRestTemplate)")
class ProdutoApiServidorRealIntegrationTest {

    @Autowired TestRestTemplate http;
    @Autowired ObjectMapper objectMapper;
    @Autowired ProdutoRepository repository;

    @AfterEach
    void limparBanco() {
        repository.deleteAll();
    }

    private ResponseEntity<String> postJson(String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return http.postForEntity("/produtos", new HttpEntity<>(json, headers), String.class);
    }

    @Test
    @DisplayName("CT04 (HTTP real) — POST /produtos devolve 200 com id, nome e preço")
    void deveCadastrarPorHttpReal() throws Exception {
        // Arrange
        long antes = repository.count();

        // Act
        ResponseEntity<String> resposta = postJson("{\"nome\":\"Mouse\",\"preco\":80.0}");

        // Assert
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        JsonNode corpo = objectMapper.readTree(resposta.getBody());
        assertTrue(corpo.get("id").isNumber());
        assertEquals("Mouse", corpo.get("nome").asText());
        assertEquals(80.0, corpo.get("preco").asDouble(), 0.001);
        assertEquals(antes + 1, repository.count(), "com HTTP real o registro é efetivamente commitado");
        assertTrue(repository.existsById(corpo.get("id").asLong()));
    }

    @Test
    @DisplayName("CT05 (HTTP real) — GET /produtos/{id} devolve o mesmo JSON do cadastro")
    void deveBuscarPorHttpReal() throws Exception {
        // Arrange
        JsonNode cadastrado = objectMapper.readTree(postJson("{\"nome\":\"Mouse\",\"preco\":80.0}").getBody());
        long id = cadastrado.get("id").asLong();

        // Act
        ResponseEntity<String> resposta = http.getForEntity("/produtos/{id}", String.class, id);

        // Assert
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(cadastrado, objectMapper.readTree(resposta.getBody()));
    }

    @Test
    @DisplayName("Achado (HTTP real) — nome em branco responde 500 em vez de 400")
    void nomeEmBrancoRespondeErroInterno() {
        // Arrange
        long antes = repository.count();

        // Act
        ResponseEntity<String> resposta = postJson("{\"nome\":\"   \",\"preco\":80.0}");

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resposta.getStatusCode());
        assertEquals(antes, repository.count());
    }

    @Test
    @DisplayName("Achado (HTTP real) — ID inexistente responde 500 em vez de 404")
    void idInexistenteRespondeErroInterno() throws Exception {
        // Arrange: ID que existiu e foi apagado (não depende de valor fixo)
        long id = objectMapper.readTree(postJson("{\"nome\":\"Temp\",\"preco\":1.0}").getBody()).get("id").asLong();
        repository.deleteById(id);

        // Act
        ResponseEntity<String> resposta = http.getForEntity("/produtos/{id}", String.class, id);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resposta.getStatusCode());
    }
}
