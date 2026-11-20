package br.edu.exemplo.analise;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("CalculadoraSus — escore e interpretação")
class CalculadoraSusTest {

    private static final double DELTA = 0.001;

    @Test
    @DisplayName("Referência: todas as respostas 3 (neutro) → 50")
    void todasNeutrasValem50() {
        assertEquals(50.0, CalculadoraSus.escore(Collections.nCopies(10, 3)), DELTA);
    }

    @Test
    @DisplayName("Melhor caso: ímpares 5 e pares 1 → 100")
    void melhorCasoVale100() {
        assertEquals(100.0, CalculadoraSus.escore(List.of(5, 1, 5, 1, 5, 1, 5, 1, 5, 1)), DELTA);
    }

    @Test
    @DisplayName("Pior caso: ímpares 1 e pares 5 → 0")
    void piorCasoVale0() {
        assertEquals(0.0, CalculadoraSus.escore(List.of(1, 5, 1, 5, 1, 5, 1, 5, 1, 5)), DELTA);
    }

    @Test
    @DisplayName("Ímpares usam (nota − 1) e pares (5 − nota): 4/2 alternados → 75")
    void ponderacaoDeImparesEPares() {
        // ímpares: 4 − 1 = 3 (×5); pares: 5 − 2 = 3 (×5) → 30 × 2,5 = 75
        assertEquals(75.0, CalculadoraSus.escore(List.of(4, 2, 4, 2, 4, 2, 4, 2, 4, 2)), DELTA);
    }

    @Test
    @DisplayName("Inverter a polaridade dos itens muda o escore (ímpar ≠ par)")
    void polaridadeImporta() {
        // todas 5: ímpares 4×5 = 20, pares 0×5 = 0 → 50; todas 1: ímpares 0, pares 4×5 = 20 → 50
        assertEquals(50.0, CalculadoraSus.escore(Collections.nCopies(10, 5)), DELTA);
        assertEquals(50.0, CalculadoraSus.escore(Collections.nCopies(10, 1)), DELTA);
        // só Q1 = 5, demais neutras: +0,5 ponto bruto em relação a 50 → 55
        List<Integer> respostas = new ArrayList<>(Collections.nCopies(10, 3));
        respostas.set(0, 5);
        assertEquals(55.0, CalculadoraSus.escore(respostas), DELTA);
        respostas.set(0, 3);
        respostas.set(1, 5);
        assertEquals(45.0, CalculadoraSus.escore(respostas), DELTA);
    }

    @ParameterizedTest(name = "{0} respostas → exceção")
    @ValueSource(ints = {0, 9, 11})
    @DisplayName("Quantidade de respostas diferente de 10 é rejeitada")
    void quantidadeErradaDeRespostas(int quantidade) {
        List<Integer> respostas = Collections.nCopies(quantidade, 3);
        assertThrows(IllegalArgumentException.class, () -> CalculadoraSus.escore(respostas));
    }

    @ParameterizedTest(name = "nota {0} fora da escala")
    @ValueSource(ints = {0, 6, -1})
    @DisplayName("Notas fora de 1–5 são rejeitadas")
    void notaForaDaEscala(int nota) {
        List<Integer> respostas = new ArrayList<>(Collections.nCopies(10, 3));
        respostas.set(4, nota);
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> CalculadoraSus.escore(respostas));
        assertEquals("Q5 = " + nota + " fora da escala 1–5", erro.getMessage());
    }

    @ParameterizedTest(name = "{0} → {1}")
    @CsvSource({
            "0,     Inaceitável",
            "50.9,  Inaceitável",
            "51,    Marginal (abaixo da média)",
            "67.9,  Marginal (abaixo da média)",
            "68,    Bom (acima da média)",
            "80.2,  Bom (acima da média)",
            "80.3,  Excelente",
            "100,   Excelente"
    })
    @DisplayName("Faixas de interpretação nas fronteiras 51, 68 e 80,3")
    void faixasDeInterpretacao(double escore, String esperado) {
        assertEquals(esperado, CalculadoraSus.interpretar(escore));
    }
}
