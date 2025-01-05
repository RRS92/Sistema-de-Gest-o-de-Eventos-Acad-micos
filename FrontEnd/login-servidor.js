// Código JS (login-servidor.js)

function efetuarLogin() {
    const login = document.getElementById('login').value;
    const senha = document.getElementById('senha').value;

    const dadosLogin = {
        login: login,
        senha: senha
    };

    fetch('http://localhost:8080/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(dadosLogin)
    })
        .then(response => {
            if (response.ok) {
                return response.json();  // Extraímos o JSON da resposta
            } else {
                throw new Error('Credenciais inválidas');
            }
        })
        .then(data => {
            const token = data.token;
            const id = data.id;  // Adiciona a captura do id do usuário
            const idUser = data.idUser; // Jair Ajudou
            const acessLevel = data.acessLevel;
            if (token && acessLevel == "SERVIDOR") {
                localStorage.setItem('token', token);  // Armazena o token no localStorage
                localStorage.setItem('userIdUtilizador', id);  // Armazena o id do usuário no localStorage

                if (idUser == null) {
                    localStorage.setItem('userIdUsuario', idUser);  // Jair Ajudou
                    window.location.href = 'cadastro-servidor.html';  // Jair Ajudou

                }
                else {
                    localStorage.setItem('userIdUsuario', idUser);  // Jair Ajudou
                    window.location.href = 'perfil-servidor.html';  // Jair Ajudou
                }

            } else {
                alert('Acesso negado!');
            }
        })
        .catch(error => {
            console.error('Erro na requisição:', error);
            alert('Ocorreu um erro. Tente novamente mais tarde.');
        });
}

// Impede o envio do formulário e chama a função de login
document.getElementById('loginForm').addEventListener('submit', function (e) {
    e.preventDefault();  // Impede o envio do formulário
    efetuarLogin();      // Chama a função de login
});
