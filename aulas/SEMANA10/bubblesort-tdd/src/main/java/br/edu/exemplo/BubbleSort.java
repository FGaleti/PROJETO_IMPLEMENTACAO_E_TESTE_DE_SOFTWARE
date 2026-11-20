package br.edu.exemplo;

public class BubbleSort {

    private int[] numeros;
    private int passagens;

    public BubbleSort(int[] numeros) {
        this.numeros = numeros;
    }

    /**
     * Ordena o vetor em ordem crescente, no próprio vetor (in-place), por trocas
     * adjacentes. Encerra antecipadamente quando uma passagem não faz trocas.
     * Contrato para vetor null: não faz nada e não lança exceção.
     */
    public void ordenar() {
        passagens = 0;
        if (numeros == null) {
            return;
        }
        for (int fim = numeros.length - 1; fim > 0; fim--) {
            passagens++;
            boolean houveTroca = false;
            for (int i = 0; i < fim; i++) {
                if (numeros[i] > numeros[i + 1]) {
                    trocar(i, i + 1);
                    houveTroca = true;
                }
            }
            if (!houveTroca) {
                return;
            }
        }
    }

    public int[] getNumeros() {
        return numeros;
    }

    /** Quantidade de passagens feitas na última chamada a ordenar() (visível para testes). */
    int getPassagens() {
        return passagens;
    }

    private void trocar(int i, int j) {
        int temp = numeros[i];
        numeros[i] = numeros[j];
        numeros[j] = temp;
    }
}
