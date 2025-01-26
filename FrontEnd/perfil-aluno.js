function verificar() {
    const token = localStorage.getItem("token");
    if (!token) {
        window.location.href = "index.html";
    }
}

verificar();

// Função para buscar os dados do aluno com base no ID
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
        alert("Erro ao carregar aluno. Tente novamente mais tarde.");
        return null;
    }
}

// Função para carregar os dados ao abrir a página
async function carregarPerfil() {
    const idUser = localStorage.getItem("userIdUsuario");

    if (!idUser) {
        alert("ID do usuário não encontrado. Faça login novamente.");
        window.location.href = "index.html";
        return;
    }

    const aluno = await getAluno(idUser);

    if (aluno) {
        // Preencher as informações no HTML
        document.querySelectorAll('.nome').forEach(element => {
            element.textContent = aluno.nome;
        });

        // Preencher outros campos, validando se os elementos existem
        const matriculaElement = document.getElementById('matricula');
        if (matriculaElement) matriculaElement.textContent = aluno.matricula;

        const cursoElement = document.getElementById('curso');
        if (cursoElement) cursoElement.textContent = aluno.curso;

        const emailElement = document.getElementById('email');
        if (emailElement) emailElement.textContent = aluno.email;

        const profilePictureElement = document.getElementById('profile-picture');
        if (profilePictureElement) {
            profilePictureElement.src = aluno.foto || 'images/foto-aluno.png';
        } else {
            console.error("Elemento de imagem do perfil não encontrado!");
        }
    }
}

// Carregar os dados do aluno assim que a página carregar
window.onload = carregarPerfil;
