describe('Teste de criação de evento', () => {

  it('Deve fazer login, redirecionar para a página de perfil do servidor e clicar no botão "criar evento"', () => {
    cy.loginServidor("leo", "1234");
    cy.get('.btn').contains('Criar Evento').click();
  });

  it('Deve preencher o formulário com dados corretos e salvar com sucesso', () => {
    cy.loginServidor("leo", "1234");
    cy.visit('/Entidades/Evento/cadastro-evento.html');
    cy.get('#nome').type('Evento de Teste');
    cy.get('#descricao').type('Descrição do evento de teste');
    cy.get('#data').type('2024-12-31'); // Data futura (válida)
    cy.get('#local').type('Auditório Principal');
    cy.get('#tipo').type('Palestra');
    cy.get('button.btn').contains('Salvar').click();
    cy.url().should('include', '/Entidades/Evento/lista-evento-para-servidor.html');
  });
});
