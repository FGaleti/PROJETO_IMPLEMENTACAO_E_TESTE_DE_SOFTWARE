package br.edu.exemplo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Garante a parada antecipada: o algoritmo encerra assim que uma passagem
 * completa não realiza nenhuma troca. Cobre os dois lados do desvio
 * (passagem com troca → continua; passagem sem troca → para).
 */
@DisplayName("BubbleSort — parada antecipada")
class BubbleSortParadaAntecipadaTest {

    static Stream<Arguments> passagensEsperadas() {
        return Stream.of(
                // sem troca na 1ª passagem → para imediatamente
                Arguments.of("já ordenado", new int[]{1, 2, 3, 4, 5}, 1),
                Arguments.of("todos iguais (iguais não são trocados)", new int[]{4, 4, 4}, 1),
                // troca na 1ª passagem, nenhuma na 2ª → para na 2ª
                Arguments.of("um par fora de ordem no início", new int[]{2, 1, 3, 4, 5}, 2),
                // troca em todas as passagens → percorre as n-1 passagens
                Arguments.of("totalmente decrescente", new int[]{5, 4, 3, 2, 1}, 4),
                Arguments.of("dois elementos invertidos", new int[]{2, 1}, 1),
                // nada a comparar
                Arguments.of("um elemento", new int[]{7}, 0),
                Arguments.of("vazio", new int[]{}, 0)
        );
    }

    @ParameterizedTest(name = "[{index}] {0} → {2} passagem(ns)")
    @MethodSource("passagensEsperadas")
    @DisplayName("Deve executar somente as passagens necessárias")
    void deveExecutarSomenteAsPassagensNecessarias(String descricao, int[] entrada, int passagens) {
        // Arrange
        BubbleSort bubbleSort = new BubbleSort(entrada);

        // Act
        bubbleSort.ordenar();

        // Assert
        assertEquals(passagens, bubbleSort.getPassagens(), descricao);
    }

    @Test
    @DisplayName("Vetor null não executa nenhuma passagem")
    void vetorNuloNaoExecutaPassagens() {
        // Arrange
        BubbleSort bubbleSort = new BubbleSort(null);

        // Act
        bubbleSort.ordenar();

        // Assert
        assertEquals(0, bubbleSort.getPassagens());
    }

    @Test
    @DisplayName("Reordenar um vetor já ordenado pelo próprio objeto custa uma única passagem")
    void segundaOrdenacaoDeveUsarUmaPassagem() {
        // Arrange
        BubbleSort bubbleSort = new BubbleSort(new int[]{3, 2, 1});
        bubbleSort.ordenar();

        // Act
        bubbleSort.ordenar();

        // Assert
        assertEquals(1, bubbleSort.getPassagens());
        assertArrayEquals(new int[]{1, 2, 3}, bubbleSort.getNumeros());
    }
}
