// Função para carregar os dados ao abrir a página
async function carregarPerfil() {
    const idUser = localStorage.getItem("userIdUsuario");
    const userId = localStorage.getItem('userIdUtilizador');

    if (!idUser) {
        alert("ID do usuário não encontrado. Faça login novamente.");
        window.location.href = "../../index.html";
        return;
    }

    // Busca os dados do aluno
    const aluno = await getAluno(idUser);
    
    // Busca os dados do usuário (para pegar a foto)
    const usuario = await getUsuario(userId);

    if (aluno) {
        // Preencher as informações no HTML
        document.querySelectorAll('.nome').forEach(element => {
            element.textContent = aluno.nome;
        });

        const matriculaElement = document.getElementById('matricula');
        if (matriculaElement) matriculaElement.textContent = aluno.matricula;

        const cursoElement = document.getElementById('curso');
        if (cursoElement) cursoElement.textContent = aluno.curso;

        const emailElement = document.getElementById('email');
        if (emailElement) emailElement.textContent = aluno.email;
    }

    // Exibição da foto
    const profilePictureElement = document.querySelector('.profile-picture');
    if (usuario && usuario.fotoBase64) {
        profilePictureElement.src = `data:image/png;base64,${usuario.fotoBase64}`;
    } else {
        profilePictureElement.src = '../../images/foto-aluno.png'; // Imagem padrão
    }
}

// Função para pegar os dados do aluno
async function getAluno(id) {
    try {
        const response = await fetch(`http://localhost:8080/alunos/${id}`, {
            method: "GET",
            headers: {
                'Accept': 'application/json',
                'Authorization': localStorage.getItem("token")
            }
        });

        if (!response.ok) {
            throw new Error(`Erro ao buscar aluno: ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        console.error(error);
        alert("Erro ao carregar dados do aluno. Tente novamente mais tarde.");
        return null;
    }
}

// Função para pegar os dados do usuário (foto)
async function getUsuario(id) {
    try {
        const response = await fetch(`http://localhost:8080/login/${id}`, {
            method: "GET",
            headers: {
                'Accept': 'application/json',
                'Authorization': localStorage.getItem("token")
            }
        });

        if (!response.ok) {
            throw new Error(`Erro ao buscar usuário: ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        console.error(error);
        alert("Erro ao carregar dados do usuário. Tente novamente mais tarde.");
        return null;
    }
}

// Evento para detectar quando o usuário seleciona uma nova foto
document.getElementById('uploadFoto').addEventListener('change', async function (event) {
    const file = event.target.files[0];

    if (file) {
        const base64String = await convertToBase64(file);
        await putUsuarioFoto(base64String);
    }
});

// Função para converter imagem em Base64
function convertToBase64(file) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => {
            const base64String = reader.result.split(',')[1]; // Remove o prefixo "data:image/png;base64,"
            resolve(base64String);
        };
        reader.onerror = (error) => reject(error);
    });
}

// Função para enviar a foto para o backend
async function putUsuarioFoto(fotoBase64) {
    const userId = localStorage.getItem('userIdUtilizador');

    if (!userId) {
        alert("Usuário não encontrado. Faça login novamente.");
        return;
    }

    const payload = {
        id: userId,
        fotoBase64: fotoBase64
    };

    try {
        const response = await fetch(`http://localhost:8080/login`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": localStorage.getItem("token")
            },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            throw new Error(`Erro ao atualizar foto: ${response.status}`);
        }

        alert("Foto atualizada com sucesso!");
        location.reload(); // Recarrega a página para atualizar a foto no perfil

    } catch (error) {
        console.error(error);
        alert("Erro ao atualizar foto. Tente novamente mais tarde.");
    }
}

// Evento de clique para o botão "Encerrar Sessão"
document.getElementById('encerrarSessao').addEventListener('click', function(event) {
    event.preventDefault();  // Previne a navegação imediata para index.html

    // Limpa o localStorage
    localStorage.clear();

    // Redireciona para a página inicial (index.html)
    window.location.href = '../../index.html';
});

// Chama a função para carregar os dados quando a página for carregada
document.addEventListener('DOMContentLoaded', carregarPerfil);
