// Função para listar alunos
async function listarAlunos() {
    try {
        const response = await fetch("http://localhost:8080/alunos");
        if (!response.ok) throw new Error("Erro ao buscar alunos.");

        const alunos = await response.json();
        const alunosContainer = document.getElementById("alunos-container");

        alunosContainer.innerHTML = ""; // Limpa o container antes de adicionar novos dados

        alunos.forEach((aluno) => {
            const alunoDiv = document.createElement("div");
            alunoDiv.classList.add("aluno");

            alunoDiv.innerHTML = `
                <div class="aluno-item">
                    <p><strong>Nome:</strong> ${aluno.nome}</p>
                    <p><strong>Matrícula:</strong> ${aluno.matricula}</p>
                    <p><strong>CPF:</strong> ${aluno.cpf}</p>
                    <p><strong>RG:</strong> ${aluno.rg}</p>
                    <p><strong>Data de Nascimento:</strong> ${aluno.dataNasc}</p>
                    <p><strong>Telefone:</strong> ${aluno.telefone}</p>
                    <p><strong>Email:</strong> ${aluno.email}</p>
                </div>
            `;

            // Adiciona o div do aluno no container
            alunosContainer.appendChild(alunoDiv);
        });
    } catch (error) {
        console.error(error);
        alert("Erro ao carregar alunos.");
    }
}

// Verifica em qual página o script está sendo executado
document.addEventListener("DOMContentLoaded", () => {
    const submitButton = document.getElementById("submit-aluno");
    const alunosContainer = document.getElementById("alunos-container");

    // Se o botão de cadastro existir, estamos na página de cadastro
    if (submitButton) {
        submitButton.addEventListener("click", cadastrarAluno);
    }

    // Se o container de alunos existir, estamos na página de listagem
    if (alunosContainer) {
        listarAlunos();
    }
});
