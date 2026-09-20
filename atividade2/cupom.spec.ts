import { test, expect } from '@playwright/test';
import { aplicarCupom } from '../app/cupom';

// Regra: um cupom concede 10% de desconto quando está ativo, dentro da
// validade, ainda não foi usado pelo cliente e o subtotal é de pelo
// menos R$ 100. Casos de teste conforme o plano de teste da atividade
// (plano-de-teste-cupom-desconto.md).

test('CT-01 - cupom válido no limite mínimo do subtotal', () => {
  const resultado = aplicarCupom({
    cupomAtivo: true,
    dentroDaValidade: true,
    jaUsadoPeloCliente: false,
    subtotal: 100,
  });

  expect(resultado.aprovado).toBe(true);
  expect(resultado.desconto).toBe(10);
  expect(resultado.subtotalFinal).toBe(90);
});

test('CT-02 - subtotal abaixo do mínimo (limite)', () => {
  const resultado = aplicarCupom({
    cupomAtivo: true,
    dentroDaValidade: true,
    jaUsadoPeloCliente: false,
    subtotal: 99.99,
  });

  expect(resultado.aprovado).toBe(false);
  expect(resultado.motivo).toBe('subtotal abaixo do mínimo');
  expect(resultado.desconto).toBe(0);
  expect(resultado.subtotalFinal).toBe(99.99);
});

test('CT-03 - cupom fora da validade (expirado)', () => {
  const resultado = aplicarCupom({
    cupomAtivo: true,
    dentroDaValidade: false,
    jaUsadoPeloCliente: false,
    subtotal: 150,
  });

  expect(resultado.aprovado).toBe(false);
  expect(resultado.motivo).toBe('cupom expirado');
  expect(resultado.desconto).toBe(0);
  expect(resultado.subtotalFinal).toBe(150);
});

test('CT-04 - cupom já usado pelo mesmo cliente', () => {
  const resultado = aplicarCupom({
    cupomAtivo: true,
    dentroDaValidade: true,
    jaUsadoPeloCliente: true,
    subtotal: 150,
  });

  expect(resultado.aprovado).toBe(false);
  expect(resultado.motivo).toBe('cupom já utilizado');
  expect(resultado.desconto).toBe(0);
  expect(resultado.subtotalFinal).toBe(150);
});
