#!/usr/bin/env bash
# Verifica todos os links relativos dos arquivos Markdown versionados.
# Para cada link [texto](destino) que não seja http(s)/mailto/âncora pura,
# resolve o caminho a partir da pasta do .md (decodificando %XX) e falha se
# o destino não existir. Uso: scripts/verificar-links.sh   (na raiz do repositório)
set -u
cd "$(git rev-parse --show-toplevel)" || exit 2

total=0
quebrados=0
while IFS= read -r md; do
  dir=$(dirname "$md")
  # remove blocos de código cercados por ``` antes de extrair os links
  while IFS= read -r alvo; do
    case "$alvo" in
      http://*|https://*|mailto:*|\#*|"") continue ;;
    esac
    caminho="${alvo%%#*}"                                   # tira a âncora
    caminho=$(printf '%b' "$(printf '%s' "$caminho" | sed 's/%\([0-9A-Fa-f][0-9A-Fa-f]\)/\\x\1/g')")
    total=$((total + 1))
    if [ -e "$dir/$caminho" ]; then
      echo "OK        $md -> $alvo"
    else
      echo "QUEBRADO  $md -> $alvo"
      quebrados=$((quebrados + 1))
    fi
  done < <(awk '/^```/{dentro=!dentro; next} !dentro' "$md" | grep -oE '\]\([^)]+\)' | sed -E 's/^\]\(//; s/\)$//; s/[[:space:]]+"[^"]*"$//')
done < <(git ls-files '*.md')

echo
echo "Links relativos verificados: $total | quebrados: $quebrados"
if [ "$quebrados" -gt 0 ]; then
  echo "RESULTADO: FALHOU"
  exit 1
fi
echo "RESULTADO: PASSOU"
