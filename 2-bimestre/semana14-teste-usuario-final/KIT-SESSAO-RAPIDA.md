# Kit de sessão rápida (~8 min por participante, SUS incluído)

Uma página para o avaliador. É a versão enxuta do [roteiro completo](02-roteiro-sessao.md) e segue as mesmas regras.

## Antes (30 s)
- No celular ou no computador **do participante**: abrir uma **janela anônima** em branco.
- No **seu** celular: cronômetro e a planilha simplificada aberta no Google Sheets ([como montar](#planilha-do-avaliador-google-sheets)).
- Código do participante: P01, P02… (nunca o nome).

## Briefing (30 s, leia em voz alta)
> "Vamos avaliar o **site do Mercado Livre**, não você. Faça como faria em casa e **pense em voz alta**.
> Você **não** vai fazer login, **não** vai digitar dados pessoais e **não** vai pagar nada: se pedir login ou pagamento, paramos ali.
> Os dados são anônimos e você pode parar quando quiser. Topa?" *(Só segue se ouvir "sim".)*
>
> "Cenário: você quer um **fone de ouvido sem fio** para o dia a dia, **bem avaliado**, e vai simular a compra até o carrinho."

## Tarefas (~5 min): leia uma por vez, cronometre e anote
| Linha | Diga | Concluída quando… |
|:-:|---|---|
| 1 | "Acesse o site do Mercado Livre." | a página inicial carregou |
| 2 | "Pesquise **fone de ouvido Bluetooth**." | a lista de resultados apareceu |
| 3 | "Mostre só entrega **Full**." … "Agora ordene pelos **melhores avaliados**." | os dois estão aplicados (**um tempo só** para as duas) |
| 4 | "Abra os detalhes de um produto." | a página do anúncio abriu |
| 5 | "Adicione ao carrinho." | a confirmação apareceu ou o item está no carrinho |
| 6 | "Comece a compra e **pare antes de login ou pagamento**." | chegou à tela de login ou do checkout |
| 7 | "Volte à página inicial." | a página inicial apareceu |

**Regras rápidas:** silêncio por 10 s → "O que você está pensando?". Travado por 60 s → **uma** dica mínima e `ajuda = Sim`.
3 min na mesma tarefa → `ok = Não` e passa para a próxima. **Erro** = clique errado, voltar por estar perdido, filtro ou ordenação errados.

## SUS (~2 min)
Mande o link do Google Forms ([texto pronto](04b-sus-google-forms.txt)) ou entregue o papel ([04-questionario-sus.md](04-questionario-sus.md)).
O participante responde sozinho e digita o próprio código (P01…).

## Depois (30 s)
Feche a janela anônima, agradeça e peça que não conte as tarefas a ninguém. Se for o caso, tire o print, **sem dados pessoais**, e salve em `evidencias/`.

---

## Planilha do avaliador (Google Sheets)
Crie uma planilha com esta primeira linha (o modelo está em [03b-planilha-simplificada.csv](03b-planilha-simplificada.csv)):

```
participante,tarefa,segundos,ok,erros,ajuda,obs
```

Uma linha por tarefa: `P01,3,75,Sim,2,Sim,aplicou Frete grátis antes do Full`.
- `tarefa` = número da linha da tabela acima (1–7)
- `ok` e `ajuda` = Sim/Não (ou S/N)
- `erros` em branco = 0

## Depois de todas as sessões
1. Google Sheets: **Arquivo → Fazer download → CSV**. Salve por cima de `03-planilha-observacao.csv`.
2. Google Forms: **Respostas → ⋮ → Fazer download das respostas (.csv)**. Descompacte e salve por cima de `04-questionario-sus.csv`.
   O analisador aceita os dois formatos **direto**, sem editar nada.
3. Rode o analisador (ou peça ao Claude Code):
   ```bash
   cd analise
   ./mvnw -q compile exec:java > ../evidencias/analise.txt
   ```
   Com menos de 5 sessões, o próprio relatório avisa que os resultados são **parciais**.
