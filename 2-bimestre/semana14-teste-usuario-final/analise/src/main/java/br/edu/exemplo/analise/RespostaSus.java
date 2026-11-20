package br.edu.exemplo.analise;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Respostas de um participante ao questionário SUS (colunas Q1..Q10, escala 1–5).
 *
 * @param respostas notas na ordem Q1..Q10; vazia quando o participante não respondeu
 */
public record RespostaSus(String participante, List<Integer> respostas) {

    public static final String COL_PARTICIPANTE = "participante";
    public static final int ITENS = 10;

    public RespostaSus {
        respostas = List.copyOf(respostas);
    }

    public boolean semResposta() {
        return respostas.isEmpty();
    }

    /** Escore SUS (0–100); só pode ser chamado se houver resposta. */
    public double escore() {
        return CalculadoraSus.escore(respostas);
    }

    /**
     * Converte um registro do CSV. Linha sem participante e sem respostas é
     * ignorada (Optional.empty). Participante sem nenhuma resposta vira
     * "sem resposta". Respostas parciais ou fora de 1–5 são rejeitadas.
     */
    public static Optional<RespostaSus> de(Map<String, String> registro) {
        String participante = registro.getOrDefault(COL_PARTICIPANTE, "").trim();
        List<String> valores = new ArrayList<>();
        for (int i = 1; i <= ITENS; i++) {
            valores.add(registro.getOrDefault("Q" + i, "").trim());
        }
        boolean nenhuma = valores.stream().allMatch(String::isEmpty);
        if (participante.isEmpty()) {
            if (nenhuma) {
                return Optional.empty();
            }
            throw new IllegalArgumentException("Respostas SUS sem identificação de participante");
        }
        if (nenhuma) {
            return Optional.of(new RespostaSus(participante, Collections.emptyList()));
        }
        List<Integer> notas = new ArrayList<>();
        for (int i = 0; i < ITENS; i++) {
            notas.add(nota(valores.get(i), i + 1, participante));
        }
        return Optional.of(new RespostaSus(participante, notas));
    }

    private static int nota(String v, int item, String participante) {
        try {
            int nota = Integer.parseInt(v);
            CalculadoraSus.validarNota(nota, item);
            return nota;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format(
                    "SUS do participante %s: Q%d = '%s' não é uma nota de 1 a 5 (questionário incompleto?)",
                    participante, item, v));
        }
    }
}
