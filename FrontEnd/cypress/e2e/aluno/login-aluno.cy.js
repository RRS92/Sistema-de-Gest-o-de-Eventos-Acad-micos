describe('Testes de login do aluno', () => {
  it('Deve clicar no botão "começar" e redirecionar para a página de usuários', () => {
    cy.visit('/index.html');
    cy.get('.btn').contains('Começar').click();
    cy.url().should('include', '/usuarios.html'); 
  });
  it('Deve clicar no botão "aluno" e redirecionar para a página de login do aluno', () => {
    cy.visit('/usuarios.html'); 
    cy.get('#aluno').click();
    cy.url().should('include', '/Entidades/Utilizador/login-aluno.html'); 
  });
  it('Deve exibir erro ao tentar fazer login com dados incorretos', () => {
    cy.visit('/Entidades/Utilizador/login-aluno.html'); 
    cy.get('#login').type('usuario_incorreto');
    cy.get('#senha').type('senha_incorreta');
    cy.get('button[type="submit"]').click();
    cy.on('window:alert', (text) => {
      expect(text).to.contains('Ocorreu um erro. Tente novamente mais tarde.');
    });
  });
  it('Deve fazer login com sucesso e redirecionar para a página de perfil do aluno', () => {
    cy.visit('/Entidades/Utilizador/login-aluno.html');
    cy.intercept("POST", "http://localhost:8080/login").as("loginRequest");
    cy.get("#login").type("everton");
    cy.get("#senha").type("1234");
    cy.get('button[type="submit"]').click();
    cy.wait("@loginRequest").its("response.statusCode").should("eq", 200);
  });
});
