package br.edu.exemplo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Casos adicionais ao BubbleSortTest do professor (que não é alterado):
 * fronteiras de tamanho, partições de valores, in-place, contrato para null
 * e teste de oráculo contra Arrays.sort.
 */
@DisplayName("BubbleSort — casos adicionais")
class BubbleSortCasosAdicionaisTest {

    static Stream<Arguments> particoesDeEntrada() {
        return Stream.of(
                Arguments.of("vetor vazio", new int[]{}, new int[]{}),
                Arguments.of("um elemento", new int[]{7}, new int[]{7}),
                Arguments.of("dois elementos já ordenados", new int[]{1, 2}, new int[]{1, 2}),
                Arguments.of("dois elementos invertidos", new int[]{2, 1}, new int[]{1, 2}),
                Arguments.of("ordem totalmente decrescente", new int[]{9, 7, 5, 3, 1}, new int[]{1, 3, 5, 7, 9}),
                Arguments.of("todos iguais", new int[]{4, 4, 4, 4}, new int[]{4, 4, 4, 4}),
                Arguments.of("somente negativos", new int[]{-1, -5, -3}, new int[]{-5, -3, -1}),
                Arguments.of("negativos, zero e positivos", new int[]{3, -2, 0, -7, 5}, new int[]{-7, -2, 0, 3, 5}),
                Arguments.of("extremos de int",
                        new int[]{Integer.MAX_VALUE, 0, Integer.MIN_VALUE, -1, 1},
                        new int[]{Integer.MIN_VALUE, -1, 0, 1, Integer.MAX_VALUE}),
                Arguments.of("menor elemento no fim", new int[]{2, 3, 4, 5, 1}, new int[]{1, 2, 3, 4, 5})
        );
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("particoesDeEntrada")
    @DisplayName("Deve ordenar em ordem crescente cada partição de entrada")
    void deveOrdenarParticoesDeEntrada(String descricao, int[] entrada, int[] esperado) {
        // Arrange
        BubbleSort bubbleSort = new BubbleSort(entrada);

        // Act
        bubbleSort.ordenar();

        // Assert
        assertArrayEquals(esperado, bubbleSort.getNumeros(), descricao);
    }

    @Test
    @DisplayName("Deve ordenar in-place: getNumeros() devolve a mesma referência recebida")
    void deveOrdenarNoProprioVetor() {
        // Arrange
        int[] vetor = {3, 2, 1};
        BubbleSort bubbleSort = new BubbleSort(vetor);

        // Act
        bubbleSort.ordenar();

        // Assert
        assertSame(vetor, bubbleSort.getNumeros());
        assertArrayEquals(new int[]{1, 2, 3}, vetor);
    }

    @Test
    @DisplayName("Vetor null: ordenar() não lança exceção e o vetor continua null (contrato definido)")
    void deveIgnorarVetorNulo() {
        // Arrange
        BubbleSort bubbleSort = new BubbleSort(null);

        // Act + Assert
        assertDoesNotThrow(bubbleSort::ordenar);
        assertNull(bubbleSort.getNumeros());
    }

    @Test
    @DisplayName("Oráculo: 200 vetores aleatórios (semente 42) devem coincidir com Arrays.sort")
    void deveCoincidirComArraysSortEmVetoresAleatorios() {
        Random random = new Random(42);
        for (int caso = 0; caso < 200; caso++) {
            // Arrange
            int tamanho = random.nextInt(31); // 0..30, inclui vazio e unitário
            int[] entrada = new int[tamanho];
            for (int k = 0; k < tamanho; k++) {
                entrada[k] = random.nextInt(201) - 100; // -100..100, força repetidos
            }
            int[] esperado = entrada.clone();
            Arrays.sort(esperado);
            BubbleSort bubbleSort = new BubbleSort(entrada);

            // Act
            bubbleSort.ordenar();

            // Assert
            assertArrayEquals(esperado, bubbleSort.getNumeros(),
                    "caso " + caso + ": " + Arrays.toString(esperado));
        }
    }
}
