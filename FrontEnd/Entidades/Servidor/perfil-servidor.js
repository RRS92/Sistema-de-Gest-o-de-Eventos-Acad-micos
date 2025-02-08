// Verifica se o usuário está autenticado
function verificar() {
    var token = localStorage.getItem("token");
    if (!token) {
        window.location.href = "/index.html";
    }
}

verificar();

// Função para buscar os dados do servidor
async function getServidor(id) {
    try {
        const response = await fetch(`http://localhost:8080/servidores/${id}`, {
            method: "GET",
            headers: {
                'Accept': 'application/json',
                'Authorization': localStorage.getItem("token")
            }
        });

        if (!response.ok) {
            throw new Error(`Erro ao buscar servidor: ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        console.error(error);
        alert("Erro ao carregar servidor. Tente novamente mais tarde.");
        return null;
    }
}

// Função para buscar os dados do usuário (foto)
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

// Função para carregar os dados ao abrir a página
async function carregarPerfil() {
    const idUser = localStorage.getItem("userIdUsuario");
    const userId = localStorage.getItem('userIdUtilizador');

    if (!idUser) {
        alert("ID do usuário não encontrado. Faça login novamente.");
        window.location.href = "/index.html";
        return;
    }

    // Buscar os dados do servidor
    const servidor = await getServidor(idUser);
    const usuario = await getUsuario(userId);

    if (servidor) {
        // Preencher os campos corretamente
        document.querySelectorAll('.nome').forEach(element => {
            element.textContent = servidor.nome;
        });

        const siapeElement = document.getElementById('siape');
        if (siapeElement) siapeElement.textContent = servidor.siape;

        const cargoElement = document.getElementById('cargo');
        if (cargoElement) cargoElement.textContent = servidor.cargo;

        const emailElement = document.getElementById('email');
        if (emailElement) emailElement.textContent = servidor.email;
    }

    // Atualizar foto do servidor
    const profilePictureElement = document.querySelector('.profile-picture');
    if (usuario && usuario.fotoBase64) {
        profilePictureElement.src = `data:image/png;base64,${usuario.fotoBase64}`;
    } else {
        profilePictureElement.src = '../../images/foto-servidor.png'; // Imagem padrão
    }
}

// Evento para detectar quando o usuário seleciona uma nova foto
document.getElementById('uploadFoto')?.addEventListener('change', async function (event) {
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
    window.location.href = '/index.html';
});

// Carregar os dados do servidor assim que a página carregar
window.onload = carregarPerfil;
