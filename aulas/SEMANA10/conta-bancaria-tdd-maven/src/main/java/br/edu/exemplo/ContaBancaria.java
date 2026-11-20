package br.edu.exemplo;

/**
 * Conta bancária simples. Contrato (definido pelos testes do professor):
 * operação inválida é ignorada — o saldo não muda e nenhuma exceção é lançada.
 * São inválidos: depósito ou saque com valor menor ou igual a zero e saque
 * maior que o saldo. Sacar exatamente o saldo é permitido.
 */
public class ContaBancaria {
    private double saldo;

    public ContaBancaria() {
        this.saldo = 0;
    }

    public double getSaldo() {
        return saldo;
    }

    public void depositar(double valor) {
        if (valor > 0) {
            saldo += valor;
        }
    }

    public void sacar(double valor) {
        if (valor > 0 && valor <= saldo) {
            saldo -= valor;
        }
    }
}
