import { test, expect } from '@playwright/test';

// Regra: uma pessoa com credenciais válidas e conta ativa deve acessar
// sua área. Credenciais inválidas devem ser rejeitadas sem revelar
// qual dado está incorreto.

test('permite login com credenciais válidas', async ({ page }) => {
  await page.goto('/login');

  await page.getByLabel('E-mail').fill('ana@exemplo.com');
  await page.getByLabel('Senha').fill('SenhaSegura123!');
  await page.getByRole('button', { name: 'Entrar' }).click();

  await expect(page).toHaveURL(/\/conta$/);
  await expect(
    page.getByRole('heading', { name: 'Minha conta' })
  ).toBeVisible();
});

test('nega login com senha inválida', async ({ page }) => {
  await page.goto('/login');

  await page.getByLabel('E-mail').fill('ana@exemplo.com');
  await page.getByLabel('Senha').fill('senha-incorreta');
  await page.getByRole('button', { name: 'Entrar' }).click();

  await expect(page.getByRole('alert')).toHaveText(
    'E-mail ou senha inválidos'
  );
  await expect(page).toHaveURL(/\/login$/);
});
