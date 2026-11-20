package br.edu.exemplo.analise;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("LeitorCsv — leitura de CSV (RFC 4180)")
class LeitorCsvTest {

    static Path fixture(String nome) throws URISyntaxException {
        return Path.of(LeitorCsvTest.class.getResource("/fixtures/" + nome).toURI());
    }

    @ParameterizedTest(name = "[{index}] conteúdo vazio")
    @ValueSource(strings = {"", "\n\n", "# só comentário\n", "﻿"})
    @DisplayName("CSV vazio, só com linhas em branco, só comentário ou só BOM → nenhum registro")
    void csvVazio(String conteudo) {
        assertTrue(LeitorCsv.ler(conteudo).isEmpty());
    }

    @Test
    @DisplayName("CSV só com cabeçalho → nenhum registro")
    void somenteCabecalho() {
        assertTrue(LeitorCsv.ler("a,b,c\n").isEmpty());
    }

    @Test
    @DisplayName("Separador ',' e valores aparados")
    void separadorVirgula() {
        List<Map<String, String>> r = LeitorCsv.ler("nome , idade\n Ana , 30 \n");
        assertEquals(List.of(Map.of("nome", "Ana", "idade", "30")), r);
    }

    @Test
    @DisplayName("Separador ';' (Excel pt-BR) é detectado no cabeçalho, mesmo após comentário")
    void separadorPontoEVirgula() {
        List<Map<String, String>> r = LeitorCsv.ler("# comentário, com vírgula\nnome;nota\nAna;7,5\n");
        assertEquals(List.of(Map.of("nome", "Ana", "nota", "7,5")), r);
    }

    @Test
    @DisplayName("Empate de separadores ou nenhum separador → ','")
    void detectarSeparadorPadrao() {
        assertEquals(',', LeitorCsv.detectarSeparador("a;b,c"));
        assertEquals(',', LeitorCsv.detectarSeparador("coluna"));
        assertEquals(',', LeitorCsv.detectarSeparador("   \n# x;y;z\n"));
        assertEquals(';', LeitorCsv.detectarSeparador("\n   \n# x\na;b"));
    }

    @Test
    @DisplayName("Aspas: separador, aspas escapadas e quebra de linha dentro do campo")
    void camposEntreAspas() {
        String csv = "obs,n\n\"tem, vírgula e \"\"aspas\"\"\nem duas linhas\",1\n";
        List<Map<String, String>> r = LeitorCsv.ler(csv);
        assertEquals("tem, vírgula e \"aspas\"\nem duas linhas", r.get(0).get("obs"));
        assertEquals("1", r.get(0).get("n"));
    }

    @Test
    @DisplayName("Aspas de fechamento como último caractere do arquivo")
    void aspasNoFimDoArquivo() {
        assertEquals("x", LeitorCsv.ler("a\n\"x\"").get(0).get("a"));
    }

    @Test
    @DisplayName("CRLF, BOM, última linha sem quebra e linha terminando em separador")
    void finsDeLinhaVariados() {
        List<Map<String, String>> r = LeitorCsv.ler("﻿a,b\r\n1,2\r\n3,\r\n\r\n5,6");
        assertEquals(3, r.size());
        assertEquals(Map.of("a", "1", "b", "2"), r.get(0));
        assertEquals(Map.of("a", "3", "b", ""), r.get(1));
        assertEquals(Map.of("a", "5", "b", "6"), r.get(2));
    }

    @Test
    @DisplayName("Última linha termina em separador e sem quebra de linha → último campo vazio")
    void ultimaLinhaTerminaEmSeparador() {
        assertEquals(List.of(Map.of("a", "1", "b", "")), LeitorCsv.ler("a,b\n1,"));
    }

    @Test
    @DisplayName("Linha com menos campos que o cabeçalho completa com vazio")
    void camposFaltantes() {
        assertEquals(Map.of("a", "1", "b", "", "c", ""), LeitorCsv.ler("a,b,c\n1\n").get(0));
    }

    @Test
    @DisplayName("Lê arquivo do disco (fixture fictícia) ignorando comentário")
    void lerArquivo() throws IOException, URISyntaxException {
        List<Map<String, String>> r = LeitorCsv.ler(fixture("FICTICIO-sus.csv"));
        assertEquals(3, r.size(), "a linha totalmente vazia é descartada");
        assertEquals("F01", r.get(0).get("participante"));
        assertEquals("1", r.get(1).get("Q10"));
    }
}
