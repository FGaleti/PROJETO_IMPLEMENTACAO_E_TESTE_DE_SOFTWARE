package br.edu.exemplo.analise;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Leitor de CSV (RFC 4180) sem dependências externas.
 * <ul>
 *   <li>Separador detectado na primeira linha: ';' (Excel pt-BR) ou ','.</li>
 *   <li>Campos entre aspas, com aspas escapadas ("") e quebras de linha internas.</li>
 *   <li>BOM UTF-8 ignorado; linhas totalmente vazias ignoradas.</li>
 *   <li>Linhas cujo primeiro campo começa com '#' são comentários.</li>
 * </ul>
 * Cada registro é um mapa "nome da coluna → valor aparado".
 */
public final class LeitorCsv {

    private static final char ASPAS = '"';

    private LeitorCsv() {
    }

    public static List<Map<String, String>> ler(Path arquivo) throws IOException {
        return ler(Files.readString(arquivo, StandardCharsets.UTF_8));
    }

    public static List<Map<String, String>> ler(String conteudo) {
        String texto = conteudo.startsWith("﻿") ? conteudo.substring(1) : conteudo;
        List<List<String>> linhas = new ArrayList<>();
        for (List<String> linha : separarLinhas(texto, detectarSeparador(texto))) {
            if (!ehComentario(linha) && !ehVazia(linha)) {
                linhas.add(linha);
            }
        }
        List<Map<String, String>> registros = new ArrayList<>();
        if (linhas.isEmpty()) {
            return registros;
        }
        List<String> cabecalho = linhas.get(0);
        for (List<String> campos : linhas.subList(1, linhas.size())) {
            Map<String, String> registro = new LinkedHashMap<>();
            for (int c = 0; c < cabecalho.size(); c++) {
                String valor = c < campos.size() ? campos.get(c) : "";
                registro.put(cabecalho.get(c).trim(), valor.trim());
            }
            registros.add(registro);
        }
        return registros;
    }

    /** Usa a primeira linha que não é vazia nem comentário (o cabeçalho); padrão ','. */
    static char detectarSeparador(String texto) {
        for (String linha : texto.split("\n")) {
            String conteudo = linha.trim();
            if (!conteudo.isEmpty() && !conteudo.startsWith("#")) {
                return contar(conteudo, ';') > contar(conteudo, ',') ? ';' : ',';
            }
        }
        return ',';
    }

    static List<List<String>> separarLinhas(String texto, char separador) {
        List<List<String>> linhas = new ArrayList<>();
        List<String> atual = new ArrayList<>();
        StringBuilder campo = new StringBuilder();
        boolean entreAspas = false;
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (entreAspas) {
                if (c != ASPAS) {
                    campo.append(c);
                } else if (i + 1 < texto.length() && texto.charAt(i + 1) == ASPAS) {
                    campo.append(ASPAS);
                    i++;
                } else {
                    entreAspas = false;
                }
            } else if (c == ASPAS) {
                entreAspas = true;
            } else if (c == separador) {
                atual.add(campo.toString());
                campo.setLength(0);
            } else if (c == '\n') {
                atual.add(campo.toString());
                campo.setLength(0);
                linhas.add(atual);
                atual = new ArrayList<>();
            } else if (c != '\r') {
                campo.append(c);
            }
        }
        if (campo.length() > 0 || !atual.isEmpty()) {
            atual.add(campo.toString());
            linhas.add(atual);
        }
        return linhas;
    }

    private static boolean ehComentario(List<String> linha) {
        return linha.get(0).trim().startsWith("#");
    }

    private static boolean ehVazia(List<String> linha) {
        return linha.stream().allMatch(String::isBlank);
    }

    private static int contar(String texto, char alvo) {
        return (int) texto.chars().filter(ch -> ch == alvo).count();
    }
}
