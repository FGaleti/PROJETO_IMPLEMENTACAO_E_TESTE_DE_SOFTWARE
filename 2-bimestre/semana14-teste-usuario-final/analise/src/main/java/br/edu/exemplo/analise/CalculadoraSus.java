package br.edu.exemplo.analise;

import java.util.List;

/**
 * System Usability Scale (Brooke, 1996).
 * Itens ímpares (positivos): nota − 1. Itens pares (negativos): 5 − nota.
 * Escore = soma × 2,5, entre 0 e 100.
 */
public final class CalculadoraSus {

    /** Média de referência de Sauro (2011): escores abaixo de 68 estão abaixo da média. */
    public static final double MEDIA_REFERENCIA = 68.0;

    private CalculadoraSus() {
    }

    public static double escore(List<Integer> respostas) {
        if (respostas.size() != RespostaSus.ITENS) {
            throw new IllegalArgumentException(
                    "O SUS exige exatamente 10 respostas; recebidas: " + respostas.size());
        }
        int soma = 0;
        for (int i = 0; i < RespostaSus.ITENS; i++) {
            int nota = respostas.get(i);
            int item = i + 1;
            validarNota(nota, item);
            soma += (item % 2 == 1) ? nota - 1 : 5 - nota;
        }
        return soma * 2.5;
    }

    static void validarNota(int nota, int item) {
        if (nota < 1 || nota > 5) {
            throw new IllegalArgumentException("Q" + item + " = " + nota + " fora da escala 1–5");
        }
    }

    /**
     * Faixas de interpretação (Sauro, 2011; Bangor, Kortum e Miller, 2009):
     * abaixo de 51 inaceitável; de 51 a 68 marginal (abaixo da média);
     * de 68 a 80,3 bom (acima da média); a partir de 80,3 excelente.
     */
    public static String interpretar(double escore) {
        if (escore < 51) {
            return "Inaceitável";
        }
        if (escore < MEDIA_REFERENCIA) {
            return "Marginal (abaixo da média)";
        }
        if (escore < 80.3) {
            return "Bom (acima da média)";
        }
        return "Excelente";
    }
}
