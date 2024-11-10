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
                <p>
                    <strong>Nome:</strong> 
                    <span id="nome-display-${aluno.id}">${aluno.nome}</span>
                    <button onclick="toggleEdit('nome', ${aluno.id})">🖋️</button>
                    <input type="text" id="nome-${aluno.id}" placeholder="Novo nome" style="display: none;" />
                    <button onclick="atualizarAtributo('nome', ${aluno.id})" style="display: none;" id="atualizar-nome-${aluno.id}">Atualizar</button>
                </p>
                <p>
                    <strong>Matrícula:</strong> 
                    <span id="matricula-display-${aluno.id}">${aluno.matricula}</span>
                    <button onclick="toggleEdit('matricula', ${aluno.id})">🖋️</button>
                    <input type="text" id="matricula-${aluno.id}" placeholder="Nova matrícula" style="display: none;" />
                    <button onclick="atualizarAtributo('matricula', ${aluno.id})" style="display: none;" id="atualizar-matricula-${aluno.id}">Atualizar</button>
                </p>
                <p>
                    <strong>CPF:</strong> 
                    <span id="cpf-display-${aluno.id}">${aluno.cpf}</span>   
                </p>
                <p>
                    <strong>RG:</strong> 
                    <span id="rg-display-${aluno.id}">${aluno.rg}</span>
                </p>
                <p>
                    <strong>Data de Nascimento:</strong> 
                    <span id="dataNasc-display-${aluno.id}">${aluno.dataNasc}</span>
                    <button onclick="toggleEdit('dataNasc', ${aluno.id})">🖋️</button>
                    <input type="date" id="dataNasc-${aluno.id}" style="display: none;" />
                    <button onclick="atualizarAtributo('dataNasc', ${aluno.id})" style="display: none;" id="atualizar-dataNasc-${aluno.id}">Atualizar</button>
                </p>
                <p>
                    <strong>Telefone:</strong> 
                    <span id="telefone-display-${aluno.id}">${aluno.telefone}</span>
                    <button onclick="toggleEdit('telefone', ${aluno.id})">🖋️</button>
                    <input type="text" id="telefone-${aluno.id}" placeholder="Novo telefone" style="display: none;" />
                    <button onclick="atualizarAtributo('telefone', ${aluno.id})" style="display: none;" id="atualizar-telefone-${aluno.id}">Atualizar</button>
                </p>
                <p>
                    <strong>Email:</strong> 
                    <span id="email-display-${aluno.id}">${aluno.email}</span>
                    <button onclick="toggleEdit('email', ${aluno.id})">🖋️</button>
                    <input type="email" id="email-${aluno.id}" placeholder="Novo email" style="display: none;" />
                    <button onclick="atualizarAtributo('email', ${aluno.id})" style="display: none;" id="atualizar-email-${aluno.id}">Atualizar</button>
                </p>
                </br>
                <button onclick="deletarAluno(${aluno.id})">🗑️ Deletar</button>
                <hr>
            `;

            alunosContainer.appendChild(alunoDiv);
        });
    } catch (error) {
        console.error(error);
        alert("Erro ao carregar alunos.");
    }
}

function toggleEdit(field, id) {
    const inputField = document.getElementById(`${field}-${id}`);
    const displayField = document.getElementById(`${field}-display-${id}`);
    const atualizarButton = document.getElementById(`atualizar-${field}-${id}`);

    if (inputField.style.display === "none") {
        inputField.style.display = "inline";
        inputField.value = displayField.textContent; // Preenche o input com o valor atual
        displayField.style.display = "none"; // Oculta o valor exibido
        atualizarButton.style.display = "inline"; // Mostra o botão de atualizar
    } else {
        inputField.style.display = "none";
        displayField.style.display = "inline"; // Mostra o valor exibido
        atualizarButton.style.display = "none"; // Oculta o botão de atualizar
    }
}

async function atualizarAtributo(field, id) {
    const novoValor = document.getElementById(`${field}-${id}`).value.trim();

    if (!novoValor) {
        alert("Por favor, insira um valor válido.");
        return;
    }

    const alunos = await fetch("http://localhost:8080/alunos");
    const alunosList = await alunos.json();
    const alunoAtual = alunosList.find(aluno => aluno.id === id);

    const dadosAtualizados = {
        id: alunoAtual.id,
        nome: field === 'nome' ? novoValor : alunoAtual.nome,
        matricula: field === 'matricula' ? novoValor : alunoAtual.matricula,
        cpf: alunoAtual.cpf,
        rg: alunoAtual.rg,
        dataNasc: field === 'dataNasc' ? novoValor : alunoAtual.dataNasc,
        telefone: field === 'telefone' ? novoValor : alunoAtual.telefone,
        email: field === 'email' ? novoValor : alunoAtual.email
    };

    try {
        const response = await fetch(`http://localhost:8080/alunos`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(dadosAtualizados)
        });

        if (!response.ok) throw new Error("Erro ao atualizar aluno.");

        alert("Aluno atualizado com sucesso!");
        listarAlunos();
    } catch (error) {
        console.error(error);
        alert("Erro ao atualizar aluno.");
    }
}

async function deletarAluno(id) {
    const confirmacao = confirm("Tem certeza de que deseja deletar este aluno?");
    if (!confirmacao) return;

    try {
        const response = await fetch(`http://localhost:8080/alunos/deletar/${id}`, {
            method: "DELETE",
        });

        if (!response.ok) throw new Error("Erro ao deletar aluno.");

        alert("Aluno deletado com sucesso!");
        listarAlunos();
    } catch (error) {
        console.error(error);
        alert("Erro ao deletar aluno.");
    }
}

// Chama a função ao carregar a página
listarAlunos();
