function criarUsuario() {
    const login = document.getElementById('login').value;
    const senha = document.getElementById('senha').value;
    const confirmarSenha = document.getElementById('confirmar-senha').value;

    // Verifica se as senhas coincidem
    if (senha !== confirmarSenha) {
        alert('As senhas não coincidem. Por favor, tente novamente.');
        return;
    }

    const dadosUsuario = {
        login: login,
        senha: senha
    };

    fetch('http://localhost:8080/login/cadastrarServidor', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(dadosUsuario)
    })
    .then(response => {
        if (response.ok) {
            alert('Usuário cadastrado com sucesso!');
            window.location.href = 'login-servidor.html'; // Redireciona para a página de login
        } else {
            throw new Error('Erro ao cadastrar usuário. Tente novamente.');
        }
    })
    .catch(error => {
        console.error('Erro na requisição:', error);
        alert('Ocorreu um erro. Por favor, tente novamente mais tarde.');
    });
}

// Impede o envio do formulário padrão e chama a função de cadastro
document.querySelector('form').addEventListener('submit', function(e) {
    e.preventDefault();  // Impede o envio padrão do formulário
    criarUsuario();      // Chama a função de criação de usuário
});
