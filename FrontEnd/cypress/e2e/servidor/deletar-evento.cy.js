describe('Teste de deletar eventos', () => {
  it('Deve deletar um evento se o botão de deletar existir, caso contrário, permanece na página de eventos', () => {
    cy.loginServidor("leo", "1234");
    cy.get('.btn').contains('Eventos Disponíveis').click();
    cy.wait(1000);
    cy.get('body').then(($body) => {
      if ($body.find('.delete-button').length > 0) {
        cy.log('Botão de deletar encontrado. Deletando o último evento...');
        cy.get('.delete-button', { timeout: 10000 }).should('exist')
        cy.get('.delete-button').last().click();
        cy.get('.event-item').should('have.length', 0); 
      } else {
        cy.log('Botão de deletar não encontrado.');
        cy.url().should('include', '/Entidades/Evento/lista-evento-para-servidor.html');
        cy.contains('h2', 'Eventos Acadêmicos').should('exist');
      }
    });
  });
});