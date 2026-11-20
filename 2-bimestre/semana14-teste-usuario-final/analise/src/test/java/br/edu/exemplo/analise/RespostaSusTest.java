package br.edu.exemplo.analise;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("RespostaSus — conversão de uma linha do questionário")
class RespostaSusTest {

    private static Map<String, String> linha(String participante, String... notas) {
        Map<String, String> m = new HashMap<>();
        m.put("participante", participante);
        for (int i = 0; i < notas.length; i++) {
            m.put("Q" + (i + 1), notas[i]);
        }
        return m;
    }

    @Test
    @DisplayName("Respostas completas → escore calculado")
    void respostasCompletas() {
        RespostaSus r = RespostaSus.de(linha("F01", "4", "2", "4", "2", "4", "2", "4", "2", "4", "2")).orElseThrow();
        assertFalse(r.semResposta());
        assertEquals(75.0, r.escore(), 0.001);
    }

    @Test
    @DisplayName("Participante sem nenhuma resposta → 'sem resposta' (não entra na média)")
    void participanteSemResposta() {
        RespostaSus r = RespostaSus.de(linha("F03")).orElseThrow();
        assertTrue(r.semResposta());
        assertEquals("F03", r.participante());
    }

    @Test
    @DisplayName("Linha totalmente vazia → ignorada")
    void linhaVazia() {
        assertTrue(RespostaSus.de(linha(" ")).isEmpty());
    }

    @Test
    @DisplayName("Respostas sem participante → exceção")
    void respostasSemParticipante() {
        assertThrows(IllegalArgumentException.class, () -> RespostaSus.de(linha("", "3")));
    }

    @Test
    @DisplayName("Questionário incompleto → exceção indicando o item")
    void questionarioIncompleto() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> RespostaSus.de(linha("F01", "3", "3", "3")));
        assertTrue(e.getMessage().contains("Q4 = ''"), e.getMessage());
    }

    @Test
    @DisplayName("Nota fora da escala ou não numérica → exceção")
    void notaInvalida() {
        assertThrows(IllegalArgumentException.class,
                () -> RespostaSus.de(linha("F01", "3", "3", "3", "3", "3", "3", "3", "3", "3", "6")));
        assertThrows(IllegalArgumentException.class,
                () -> RespostaSus.de(linha("F01", "3", "3", "3", "3", "3", "3", "3", "3", "3", "três")));
    }

    @Test
    @DisplayName("Lista de respostas é copiada (imutável)")
    void listaImutavel() {
        List<Integer> notas = new ArrayList<>(List.of(3, 3, 3, 3, 3, 3, 3, 3, 3, 3));
        RespostaSus r = new RespostaSus("F01", notas);
        notas.set(0, 5);
        assertEquals(3, r.respostas().get(0));
        assertThrows(UnsupportedOperationException.class, () -> r.respostas().add(1));
    }
}
