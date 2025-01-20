function verificar() {
    var token = localStorage.getItem("token");
    if (token == null) {
        window.location.href = "index.html";
    }
}

verificar();

// Função para buscar os dados do aluno com base no ID
async function getAluno(id) {
    try {
        const response = await fetch(`http://localhost:8080/alunos/${id}`, { // Usando o ID fornecido
            method: "GET",
            headers: {
                'Accept': 'application/json',
                'Authorization': localStorage.getItem("token") // Token de autorização
            }
        });

        if (!response.ok) {
            throw new Error(`Erro ao buscar aluno: ${response.status}`);
        }
        const aluno = await response.json();
        return aluno;
    } catch (error) {
        console.error(error);
        alert("Erro ao carregar aluno. Tente novamente mais tarde.");
        return null;
    }
}

// Função para carregar os dados ao abrir a página
async function carregarPerfil() {
    // Usar o ID fixo (no caso, ID 5)
    const idUser = localStorage.getItem("userIdUsuario"); // Jair Ajudou


    // Buscar os dados do aluno
    const aluno = await getAluno(idUser);

    if (aluno) {
        // Preencher as informações no HTML
        document.querySelectorAll('.nome').forEach(element => {
            element.textContent = aluno.nome;
        });
        document.getElementById('matricula').textContent = aluno.matricula;
        document.getElementById('curso').textContent = aluno.curso;
        document.getElementById('email').textContent = aluno.email;
        
        document.getElementById('profile-picture').src = aluno.foto || 'images/foto-aluno.png';
    }
}

// Carregar os dados do aluno assim que a página carregar
window.onload = carregarPerfil;
