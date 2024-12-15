// Função para obter alunos
async function getAlunos() {
    try {
        const response = await fetch("http://localhost:8080/alunos", 
            {
            method: "GET",
            headers: {
                'Accept': 'application/json',
                "Authorization": localStorage.getItem("token")
            }});
        if (!response.ok) {
            throw new Error(`Erro ao buscar alunos: ${response.status}`);
        }
        const alunos = await response.json();
        return alunos;
    } catch (error) {
        console.error(error);
        alert("Erro ao carregar alunos. Tente novamente mais tarde.");
        return [];
    }
}

// Função para exibir alunos na página
function exibirAlunos(alunos) {
    const eventsContainer = document.querySelector(".events-container");
    eventsContainer.innerHTML = ""; // Limpa a lista existente

    if (alunos.length === 0) {
        eventsContainer.innerHTML = "<p>Nenhum aluno encontrado.</p>";
        return;
    }

    alunos.forEach((aluno) => {
        const eventCard = document.createElement("div");
        eventCard.classList.add("event-card");
        eventCard.innerHTML = `
            <div class="event-details">
                <p><strong>Nome:</strong> <span id="nome-display-${aluno.id}">${aluno.nome}</span>
                <input type="text" id="nome-${aluno.id}" value="${aluno.nome}" style="display:none;" /></p>

                <p><strong>Matrícula:</strong> <span id="matricula-display-${aluno.id}">${aluno.matricula}</span>
                <input type="text" id="matricula-${aluno.id}" value="${aluno.matricula}" style="display:none;" /></p>

                <p><strong>CPF:</strong> <span id="cpf-display-${aluno.id}">${aluno.cpf}</span>
                <input type="text" id="cpf-${aluno.id}" value="${aluno.cpf}" style="display:none;" /></p>

                <p><strong>RG:</strong> <span id="rg-display-${aluno.id}">${aluno.rg}</span>
                <input type="text" id="rg-${aluno.id}" value="${aluno.rg}" style="display:none;" /></p>

                <p><strong>Data de Nascimento:</strong> <span id="dataNasc-display-${aluno.id}">${aluno.dataNasc}</span>
                <input type="text" id="dataNasc-${aluno.id}" value="${aluno.dataNasc}" style="display:none;" /></p>

                <p><strong>Telefone:</strong> <span id="telefone-display-${aluno.id}">${aluno.telefone}</span>
                <input type="text" id="telefone-${aluno.id}" value="${aluno.telefone}" style="display:none;" /></p>

                <p><strong>Email:</strong> <span id="email-display-${aluno.id}">${aluno.email}</span>
                <input type="text" id="email-${aluno.id}" value="${aluno.email}" style="display:none;" /></p>
            </div>
        `;
        eventsContainer.appendChild(eventCard);
    });
}

// Chama a função para obter alunos e exibi-los na página
getAlunos().then((alunos) => {
    exibirAlunos(alunos);
});

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
        exibirAlunos(alunos);
    }
});
