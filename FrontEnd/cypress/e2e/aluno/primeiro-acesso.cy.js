describe('Testes de primeiro acesso do aluno', () => {
    let userCount = Math.floor(Math.random() * 10000); // Gera um número aleatório para evitar conflitos
    let username = `usuario_teste${userCount}`;
  
    it('Deve clicar no botão "começar" e redirecionar para a página de usuários', () => {
      cy.visit('/index.html');
      cy.get('.btn').contains('Começar').click();
      cy.url().should('include', '/usuarios.html'); 
    });
  
    it('Deve clicar no botão "servidor" e redirecionar para a página de login do aluno', () => {
      cy.visit('/usuarios.html'); 
      cy.get('#aluno').click();
      cy.url().should('include', '/Entidades/Utilizador/login-aluno.html');
    });
  
    it('Deve clicar no botão "primeiro acesso" e redirecionar para a página de primeiro acesso do aluno', () => {
      cy.visit('/Entidades/Utilizador/login-aluno.html'); 
      cy.get('a').contains('Primeiro Acesso').click();
      cy.url().should('include', '/Entidades/Utilizador/primeiro-acesso-aluno.html'); 
    });
  
    it('Deve exibir erro ao preencher senhas diferentes', () => {
      cy.visit('/Entidades/Utilizador/primeiro-acesso-aluno.html');
      cy.get('#login').type(username);
      cy.get('#senha').type('Senha123');
      cy.get('#confirmar-senha').type('Senha456'); 
      cy.get('button.btn').contains('Salvar').click();
      cy.on('window:alert', (text) => {
        expect(text).to.contains('As senhas não coincidem. Por favor, tente novamente.');
      });
    });
  
    it('Deve cadastrar o usuário com sucesso e logar pela primeira vez', () => {
      cy.visit('/Entidades/Utilizador/primeiro-acesso-aluno.html');
      cy.get('#login').type(username);
      cy.get('#senha').type('Senha123');
      cy.get('#confirmar-senha').type('Senha123'); 
      cy.get('button.btn').contains('Salvar').click();
      cy.on('window:alert', (text) => {
        expect(text).to.contains('Usuário cadastrado com sucesso!');
      });
      cy.url().should('include', '/Entidades/Utilizador/login-aluno.html');
  
      cy.intercept("POST", "http://localhost:8080/login").as("loginRequest");
      cy.get("#login").type(username);
      cy.get("#senha").type('Senha123');
      cy.get('button[type="submit"]').click();
      cy.wait("@loginRequest").its("response.statusCode").should("eq", 200);
    });
  
    it('Deve preencher todos os campos do formulário de cadastro do aluno', () => {
      cy.visit('/Entidades/Utilizador/login-aluno.html');
      cy.intercept("POST", "http://localhost:8080/login").as("loginRequest");
      cy.get("#login").type(username);
      cy.get("#senha").type('Senha123');
      cy.get('button[type="submit"]').click();
      cy.wait("@loginRequest").its("response.statusCode").should("eq", 200);
  
      cy.visit('/Entidades/Aluno/cadastro-aluno.html');
  
      cy.get('#nomeBanco').type('Banco do Brasil');
      cy.get('#numConta').type('123456789');
      cy.get('#agencia').type('1234');
      cy.get('#operacao').type('001');
  
      cy.get('#rua').type('Rua das Flores');
      cy.get('#numero').type('123');
      cy.get('#bairro').type('Centro');
      cy.get('#cidade').type('Recife');
      cy.get('#estado').type('PE');
      cy.get('#cep').type('50000000');
      cy.get('#complemento').type('Apartamento 101');
  
      cy.get('#nome-aluno').type('Everton Juan');
      cy.get('#cpf').type('12345678901');
      cy.get('#rg').type('1234567');
      cy.get('#dataNasc').type('1990-01-01');
      cy.get('#telefone').type('81987654321');
      cy.get('#email').type('everton.juan@example.com');
      cy.get('#matricula').type('20222');

      cy.get('#periodoIngresso').type('20222');
      cy.get('#turno').type('manha');
      cy.get('#nomeCurso').type('ads');
      cy.get('#modalidade').type('superior');
  
      cy.intercept("POST", "http://localhost:8080/alunos").as("cadastroRequest");
  
      cy.get('#submit-aluno').click();
      
      cy.url().should('include', '/Entidades/Aluno/perfil-aluno.html');
    });
  });