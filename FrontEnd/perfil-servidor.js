function verificar() {
    var token = localStorage.getItem("token");
    if (token == null) {
        window.location.href = "index.html";
    }
}

verificar();

// Função para buscar os dados do servidor com base no ID
async function getServidor(id) {
    try {
        const response = await fetch(`http://localhost:8080/servidores/${id}`, { // Usando o ID fornecido
            method: "GET",
            headers: {
                'Accept': 'application/json',
                'Authorization': localStorage.getItem("token") // Token de autorização
            }
        });

        if (!response.ok) {
            throw new Error(`Erro ao buscar servidor: ${response.status}`);
        }
        const servidor = await response.json();
        return servidor;
    } catch (error) {
        console.error(error);
        alert("Erro ao carregar servidor. Tente novamente mais tarde.");
        return null;
    }
}

// Função para carregar os dados ao abrir a página
async function carregarPerfil() {
    // Usar o ID fixo (no caso, ID 5)
    const idUser = localStorage.getItem("userIdUsuario"); // Jair Ajudou


    // Buscar os dados do servidor
    const servidor = await getServidor(idUser);

    if (servidor) {
        // Preencher as informações no HTML
        document.querySelectorAll('.nome').forEach(element => {
            element.textContent = servidor.nome;
        });
        document.getElementById('siape').textContent = servidor.siape;
        document.getElementById('cargo').textContent = servidor.cargo;
        document.getElementById('email').textContent = servidor.email;
        document.getElementById('profile-picture').src = servidor.foto || 'images/foto-servidor.png';
    }
}

// Carregar os dados do servidor assim que a página carregar
window.onload = carregarPerfil;
