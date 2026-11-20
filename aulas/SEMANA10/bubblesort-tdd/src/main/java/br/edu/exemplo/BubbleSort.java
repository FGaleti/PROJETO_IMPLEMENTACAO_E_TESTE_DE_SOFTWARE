package br.edu.exemplo;

public class BubbleSort {

    private int[] numeros;

    public BubbleSort(int[] numeros) {
        this.numeros = numeros;
    }

    public void ordenar() {
        for (int fim = numeros.length - 1; fim > 0; fim--) {
            for (int i = 0; i < fim; i++) {
                if (numeros[i] > numeros[i + 1]) {
                    int temp = numeros[i];
                    numeros[i] = numeros[i + 1];
                    numeros[i + 1] = temp;
                }
            }
        }
    }

    public int[] getNumeros() {
        return numeros;
    }
}
