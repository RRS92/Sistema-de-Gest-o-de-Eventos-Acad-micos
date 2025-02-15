describe('Teste de criação de transporte', () => {

    beforeEach(() => {
        cy.loginServidor("leo", "1234");
    });
    
    it('Deve fazer login, redirecionar para a página de perfil do servidor e clicar no botão "criar evento"', () => {
        cy.get('.btn').contains('Eventos Disponíveis').click();
        cy.get('.menu-button').click({ force: true }); // Abre o menu
        cy.get('.menu-item[data-action="transporte"]').should('be.visible').click(); // Clica em "Cadastrar Transporte"
        cy.get('#categoria').type('Busao');
        cy.get('#placa').type('PGME35');
        cy.get('#quilometragem').type('10000'); 
        cy.get('#nomeMotorista').type('Andinho');
        cy.get('#horaSaida').type('12:12');
        cy.get('#horaChegada').type('14:44');
        cy.get('button.btn').contains('Salvar').click();
    });

    // Listar os transportes
    it('Deve fazer login, redirecionar para a página de perfil do servidor e clicar no botão "listar transporte"', () => {
        cy.get('.btn').contains('Eventos Disponíveis').click();
        cy.get('.menu-button').click({ force: true }); // Abre o menu
        cy.wait(1000);
        cy.get('.menu-item[data-action="ver-transportes"]').should('be.visible').click(); // Clica em "Ver Transportes"
        cy.get('header nav a').contains('Voltar').should('be.visible').click();

    });

  
 });
  