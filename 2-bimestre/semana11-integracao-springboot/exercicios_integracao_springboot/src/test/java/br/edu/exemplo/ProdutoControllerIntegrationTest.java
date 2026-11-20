package br.edu.exemplo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;
@SpringBootTest @AutoConfigureMockMvc @Transactional
class ProdutoControllerIntegrationTest {
 @Autowired MockMvc mockMvc;
 @Autowired ObjectMapper objectMapper;
 @Test void deveCadastrarProdutoPelaApi() throws Exception {
  // CT04 — POST /produtos com JSON {"nome":"Mouse","preco":80.0}
  mockMvc.perform(post("/produtos")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content("{\"nome\":\"Mouse\",\"preco\":80.0}"))
          .andExpect(status().isOk())
          .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.id").isNumber())
          .andExpect(jsonPath("$.nome").value("Mouse"))
          .andExpect(jsonPath("$.preco").value(80.0));
 }
 @Test void deveBuscarProdutoPelaApi() throws Exception {
  // CT05 — Arrange: cadastrar por POST e extrair o ID do corpo da resposta
  String corpoPost = mockMvc.perform(post("/produtos")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content("{\"nome\":\"Mouse\",\"preco\":80.0}"))
          .andExpect(status().isOk())
          .andReturn().getResponse().getContentAsString();
  JsonNode cadastrado = objectMapper.readTree(corpoPost);
  long id = cadastrado.get("id").asLong();

  // Act + Assert: GET /produtos/{id} devolve 200 e os mesmos dados
  String corpoGet = mockMvc.perform(get("/produtos/{id}", id))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(id))
          .andExpect(jsonPath("$.nome").value("Mouse"))
          .andExpect(jsonPath("$.preco").value(80.0))
          .andReturn().getResponse().getContentAsString();
  assertEquals(cadastrado, objectMapper.readTree(corpoGet), "JSON da consulta deve ser idêntico ao do cadastro");
 }
}
