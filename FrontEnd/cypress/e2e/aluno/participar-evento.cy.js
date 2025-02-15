describe('Teste de participar dos eventos', () => {
    it('Deve participar de um evento se o botão de participar existir', () => {
      cy.loginAluno("everton", "1234");
      cy.get('.btn').contains('Eventos Disponíveis').click();
      cy.wait(1000);
      cy.get('body').then(($body) => {
        if ($body.find('.partipate-button').length > 0) {
          cy.log('Botão de participar encontrado. Participando do último evento...');
          cy.get('.partipate-button', { timeout: 10000 }).should('exist')
          cy.get('.partipate-button').last().click();
          cy.get('.event-item').should('have.length', 0); 
        } else {
          cy.log('Botão de participar não encontrado.');
          cy.url().should('include', '/Entidades/Evento/lista-evento-para-aluno.html');
          cy.contains('h2', 'Eventos Acadêmicos').should('exist');
        }
      });
    });

    it('Deve cancelar inscrição de um evento se o botão de cancelar inscrição existir', () => {
        cy.loginAluno("everton", "1234");
        cy.get('.btn').contains('Eventos Disponíveis').click();
        cy.wait(1000);
        cy.get('body').then(($body) => {
          if ($body.find('.cancelar-participacao').length > 0) {
            cy.log('Botão de cancelar participação encontrado. Cancelando inscrição no último evento...');
            cy.get('.cancelar-participacao', { timeout: 10000 }).should('exist')
            cy.get('.cancelar-participacao').last().click();
            cy.get('.event-item').should('have.length', 0); 
          } else {
            cy.log('Botão de cancelar edição não encontrado.');
            cy.url().should('include', '/Entidades/Evento/lista-evento-para-aluno.html');
            cy.contains('h2', 'Eventos Acadêmicos').should('exist');
          }
        });
      });
  });