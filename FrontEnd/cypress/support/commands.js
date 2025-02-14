Cypress.Commands.add('loginServidor', (username, password) => {
    // Navega para a página inicial e clica no botão "Começar"
    cy.visit('/index.html');
    cy.get('.btn').contains('Começar').click();
    cy.url().should('include', '/usuarios.html');
  
    // Navega para a página de usuários e clica no botão "servidor"
    cy.visit('/usuarios.html');
    cy.get('#servidor').click();
    cy.url().should('include', '/Entidades/Utilizador/login-servidor.html');
  
    // Faz login como servidor
    cy.visit('/Entidades/Utilizador/login-servidor.html');
    cy.intercept("POST", "http://localhost:8080/login").as("loginRequest");
    cy.get("#login").type(username);
    cy.get("#senha").type(password);
    cy.get('button[type="submit"]').click();
    cy.wait(1000);
    cy.wait("@loginRequest").its("response.statusCode").should("eq", 200);
  });