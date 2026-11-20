package br.edu.exemplo.analise;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Observacao — conversão de uma linha da planilha")
class ObservacaoTest {

    private static Map<String, String> linha(String tempo, String sucesso, String erros, String ajuda) {
        Map<String, String> m = new HashMap<>();
        m.put("participante", "F01");
        m.put("Tarefa", "Aplicar filtros");
        m.put("Tempo (s)", tempo);
        m.put("Sucesso (S/N)", sucesso);
        m.put("Erros", erros);
        m.put("com_ajuda", ajuda);
        m.put("Observações", "fictício");
        return m;
    }

    @Test
    @DisplayName("Linha completa é convertida (tempo com vírgula decimal)")
    void linhaCompleta() {
        Observacao o = Observacao.de(linha("12,5", "S", "2", "N")).orElseThrow();
        assertEquals(new Observacao("F01", "Aplicar filtros", 12.5, true, 2, false, "fictício"), o);
    }

    @Test
    @DisplayName("Sucesso em branco = linha ainda não coletada → ignorada")
    void linhaNaoColetada() {
        assertTrue(Observacao.de(linha("", "", "", "")).isEmpty());
        assertTrue(Observacao.de(Map.of()).isEmpty());
    }

    @Test
    @DisplayName("Tempo, erros e com_ajuda em branco assumem: sem tempo, 0 erros, sem ajuda")
    void valoresPadrao() {
        Observacao o = Observacao.de(linha("", "s", "", "")).orElseThrow();
        assertNull(o.tempoSegundos());
        assertEquals(0, o.erros());
        assertFalse(o.comAjuda());
        assertTrue(o.sucesso());
    }

    @ParameterizedTest(name = "sucesso={0}, ajuda={1} → sucesso sem ajuda = {2}")
    @CsvSource({"S,N,true", "S,S,false", "N,N,false", "N,S,false"})
    @DisplayName("Sucesso só conta se for sem ajuda")
    void sucessoSemAjuda(String sucesso, String ajuda, boolean esperado) {
        assertEquals(esperado, Observacao.de(linha("10", sucesso, "0", ajuda)).orElseThrow().sucessoSemAjuda());
    }

    @ParameterizedTest(name = "[{index}] {4}")
    @CsvSource({
            "10,  X, 0,   N,      'Sucesso (S/N)' = 'X'",
            "10,  S, 0,   talvez, 'com_ajuda' = 'talvez'",
            "-1,  S, 0,   N,      'Tempo (s)' = '-1'",
            "abc, S, 0,   N,      'Tempo (s)' = 'abc'",
            "10,  S, -1,  N,      'Erros' = '-1'",
            "10,  S, 1.5, N,      'Erros' = '1.5'",
            "10,  S, dois,N,      'Erros' = 'dois'"
    })
    @DisplayName("Valores inválidos geram mensagem com coluna, valor, participante e tarefa")
    void valoresInvalidos(String tempo, String sucesso, String erros, String ajuda, String trecho) {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> Observacao.de(linha(tempo, sucesso, erros, ajuda)));
        assertTrue(e.getMessage().contains(trecho), e.getMessage());
        assertTrue(e.getMessage().contains("[participante F01, tarefa Aplicar filtros]"), e.getMessage());
    }

    @ParameterizedTest(name = "coluna {0} vazia")
    @CsvSource({"participante", "Tarefa"})
    @DisplayName("Participante e Tarefa são obrigatórios quando a linha foi coletada")
    void colunasObrigatorias(String coluna) {
        Map<String, String> m = linha("10", "S", "0", "N");
        m.put(coluna, " ");
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> Observacao.de(m));
        assertEquals("Coluna '" + coluna + "' obrigatória em linha com Sucesso preenchido", e.getMessage());
    }
}
