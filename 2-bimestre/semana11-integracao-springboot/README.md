# Semana 11: teste de integração no projeto Spring Boot

| Item | Onde |
|---|---|
| Projeto (código-base do professor + nossos testes) | [exercicios_integracao_springboot/](exercicios_integracao_springboot/) |
| **Relatório da semana**: CT01–CT05, complementares, cobertura, discussões, isolamento e achados | [exercicios_integracao_springboot/README.md](exercicios_integracao_springboot/README.md) |
| Enunciado original | [lista_exercicios_integracao_springboot.md](lista_exercicios_integracao_springboot.md) e [ENUNCIADO.md](exercicios_integracao_springboot/ENUNCIADO.md) |
| Evidências (`mvn test`, `mvn verify`, verificação de sanidade) | [exercicios_integracao_springboot/evidencias/](exercicios_integracao_springboot/evidencias/) |

```bash
cd exercicios_integracao_springboot
./mvnw test      # 34 testes (ou: mvn test)
./mvnw verify    # + JaCoCo: 100% de linhas e desvios em service, controller e model
```
