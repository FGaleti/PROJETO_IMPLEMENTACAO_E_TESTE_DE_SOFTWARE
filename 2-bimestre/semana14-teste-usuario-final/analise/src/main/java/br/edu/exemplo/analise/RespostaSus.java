package br.edu.exemplo.analise;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Respostas de um participante ao questionário SUS (colunas Q1..Q10, escala 1–5).
 *
 * @param respostas notas na ordem Q1..Q10; vazia quando o participante não respondeu
 */
public record RespostaSus(String participante, List<Integer> respostas) {

    public static final String COL_PARTICIPANTE = "participante";
    public static final int ITENS = 10;

    private static final Pattern ITEM = Pattern.compile("^\\s*[Qq]?\\s*(\\d{1,2})(?!\\d)");

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
    public static Optional<RespostaSus> de(Map<String, String> original) {
        Map<String, String> registro = normalizar(original);
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

    /**
     * Aceita também o CSV exportado do Google Forms: a coluna cujo título contém
     * "participante" vira {@code participante}; títulos que começam com o número do
     * item ("Q1 - …", "1. …", "Q10) …") viram Q1..Q10. Demais colunas (por exemplo,
     * "Carimbo de data/hora") são ignoradas.
     */
    static Map<String, String> normalizar(Map<String, String> registro) {
        Map<String, String> normalizado = new HashMap<>();
        registro.forEach((coluna, v) -> {
            Matcher item = ITEM.matcher(coluna);
            if (coluna.toLowerCase(Locale.ROOT).contains(COL_PARTICIPANTE)) {
                normalizado.putIfAbsent(COL_PARTICIPANTE, v);
            } else if (item.find()) {
                normalizado.put("Q" + Integer.parseInt(item.group(1)), v);
            }
        });
        return normalizado;
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
