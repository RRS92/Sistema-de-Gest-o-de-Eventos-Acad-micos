describe('Teste de editar eventos', () => {
    it('Deve editar um evento se o botão de editar existir e depois clicar no botão de "atualizar"', () => {
      cy.loginServidor("leo", "1234");
      cy.get('.btn').contains('Eventos Disponíveis').click();
      cy.wait(1000);
      cy.get('body').then(($body) => {
        if ($body.find('.edit-button').length > 0) {
          cy.log('Botão de editar encontrado. Editando o último evento...');
          cy.get('.edit-button', { timeout: 10000 }).should('exist')
          cy.get('.edit-button').last().click();
          cy.get('.update-button').last().click();
          cy.get('.event-item').should('have.length', 0); 
        } else {
          cy.log('Botão de editar não encontrado.');
          cy.url().should('include', '/Entidades/Evento/lista-evento-para-servidor.html');
          cy.contains('h2', 'Eventos Acadêmicos').should('exist');
        }
      });
    });

    it('Deve editar um evento se o botão de editar existir e depois clicar no botão de "cancelar edição"', () => {
        cy.loginServidor("leo", "1234");
        cy.get('.btn').contains('Eventos Disponíveis').click();
        cy.wait(1000);
        cy.get('body').then(($body) => {
          if ($body.find('.edit-button').length > 0) {
            cy.log('Botão de editar encontrado. Editando o último evento...');
            cy.get('.edit-button', { timeout: 10000 }).should('exist')
            cy.get('.edit-button').last().click();
            cy.get('.cancel-edit-button').last().click();
            cy.get('.event-item').should('have.length', 0); 
          } else {
            cy.log('Botão de editar não encontrado.');
            cy.url().should('include', '/Entidades/Evento/lista-evento-para-servidor.html');
            cy.contains('h2', 'Eventos Acadêmicos').should('exist');
          }
        });
      });
  });