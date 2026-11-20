package br.edu.exemplo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Casos adicionais ao ContaBancariaTest do professor (que não é alterado).
 * Contrato (definido por naoDevePermitirSaqueMaiorQueSaldo): operação
 * inválida é IGNORADA — o saldo não muda e nenhuma exceção é lançada.
 */
@DisplayName("ContaBancaria — casos adicionais")
class ContaBancariaCasosAdicionaisTest {

    private static final double DELTA = 0.001;

    @Test
    @DisplayName("Conta nova deve ter saldo R$ 0,00")
    void contaNovaDeveTerSaldoZero() {
        // Arrange + Act
        ContaBancaria conta = new ContaBancaria();

        // Assert
        assertEquals(0.0, conta.getSaldo(), DELTA);
    }

    @ParameterizedTest(name = "depósito de {0} é ignorado")
    @ValueSource(doubles = {0.0, -0.01, -50.0})
    @DisplayName("Depósito zero ou negativo deve ser ignorado")
    void depositoNaoPositivoDeveSerIgnorado(double valor) {
        // Arrange
        ContaBancaria conta = new ContaBancaria();
        conta.depositar(100);

        // Act
        conta.depositar(valor);

        // Assert
        assertEquals(100.0, conta.getSaldo(), DELTA);
    }

    @ParameterizedTest(name = "saque de {0} é ignorado")
    @ValueSource(doubles = {0.0, -0.01, -50.0})
    @DisplayName("Saque zero ou negativo deve ser ignorado")
    void saqueNaoPositivoDeveSerIgnorado(double valor) {
        // Arrange
        ContaBancaria conta = new ContaBancaria();
        conta.depositar(100);

        // Act
        conta.sacar(valor);

        // Assert
        assertEquals(100.0, conta.getSaldo(), DELTA);
    }

    @ParameterizedTest(name = "saldo 100,00, saque {0} → saldo {1}")
    @CsvSource({
            "99.99,  0.01",    // logo abaixo do saldo: permitido
            "100.00, 0.00",    // exatamente o saldo: permitido, conta zera
            "100.01, 100.00"   // 0,01 acima do saldo: recusado
    })
    @DisplayName("Fronteira do saque em relação ao saldo")
    void fronteiraDoSaque(double saque, double saldoEsperado) {
        // Arrange
        ContaBancaria conta = new ContaBancaria();
        conta.depositar(100);

        // Act
        conta.sacar(saque);

        // Assert
        assertEquals(saldoEsperado, conta.getSaldo(), DELTA);
    }

    @Test
    @DisplayName("Saque em conta zerada deve ser recusado")
    void saqueEmContaZeradaDeveSerRecusado() {
        // Arrange
        ContaBancaria conta = new ContaBancaria();

        // Act
        conta.sacar(10);

        // Assert
        assertEquals(0.0, conta.getSaldo(), DELTA);
    }

    @Test
    @DisplayName("Sequência de operações válidas e inválidas mantém o saldo coerente")
    void sequenciaDeOperacoes() {
        // Arrange
        ContaBancaria conta = new ContaBancaria();

        // Act
        conta.depositar(200);   // 200
        conta.sacar(50);        // 150
        conta.sacar(500);       // recusado → 150
        conta.depositar(-30);   // ignorado → 150
        conta.depositar(25.5);  // 175.5
        conta.sacar(175.5);     // 0

        // Assert
        assertEquals(0.0, conta.getSaldo(), DELTA);
    }

    @Test
    @DisplayName("Valores com centavos: 0,10 + 0,20 = 0,30 (comparação com delta)")
    void valoresComCentavos() {
        // Arrange
        ContaBancaria conta = new ContaBancaria();

        // Act
        conta.depositar(0.10);
        conta.depositar(0.20);

        // Assert
        assertEquals(0.30, conta.getSaldo(), DELTA);
    }
}
