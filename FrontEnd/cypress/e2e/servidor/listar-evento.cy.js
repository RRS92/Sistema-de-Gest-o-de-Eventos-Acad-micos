describe('Teste de listar eventos', () => {
    it('Deve fazer login, redirecionar para a página de perfil do servidor e clicar no botão "eventos disponíveis"', () => {
      cy.loginServidor("leo", "1234");
      cy.get('.btn').contains('Eventos Disponíveis').click();
    });
});
  