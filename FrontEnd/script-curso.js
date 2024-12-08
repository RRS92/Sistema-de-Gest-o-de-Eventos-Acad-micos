// Função para obter cursos
async function getCursos() {
    try {
        const response = await fetch("http://localhost:8080/cursos");
        if (!response.ok) {
            throw new Error(`Erro ao buscar cursos: ${response.status}`);
        }
        const cursos = await response.json();
        return cursos;
    } catch (error) {
        console.error(error);
        alert("Erro ao carregar cursos. Tente novamente mais tarde.");
        return [];
    }
}

// Função para exibir cursos na página
function exibirCursos(cursos) {
    const eventsContainer = document.querySelector(".events-container");
    eventsContainer.innerHTML = ""; // Limpa a lista existente

    if (cursos.length === 0) {
        eventsContainer.innerHTML = "<p>Nenhum curso encontrado.</p>";
        return;
    }

    cursos.forEach((curso) => {
        const eventCard = document.createElement("div");
        eventCard.classList.add("event-card");
        eventCard.innerHTML = `
            <div class="event-details">
                <p><strong>Nome:</strong> <span id="nome-display-${curso.id}">${curso.nome}</span>
                <input type="text" id="nome-${curso.id}" value="${curso.nome}" style="display:none;" /></p>

                <p><strong>Modalidade:</strong> <span id="modalidade-display-${curso.id}">${curso.modalidade}</span>
                <input type="text" id="modalidade-${curso.id}" value="${curso.modalidade}" style="display:none;" /></p>

            </div>
            <br>
            <button class="edit-button" data-cursoId="${curso.id}">Editar ✏️</button>  
            <button class="delete-button" data-cursoId="${curso.id}">Deletar 🗑️</button>
            <button class="update-button" id="atualizar-${curso.id}" style="display:none;" onclick="atualizarCurso(${curso.id})">Atualizar ✏️</button>
        `;
        eventsContainer.appendChild(eventCard);
    });

    // Adiciona o evento de clique aos botões de editar
    document.querySelectorAll(".edit-button").forEach((button) => {
        button.addEventListener("click", function(event) {
            const cursoId = event.target.getAttribute("data-cursoId");
            toggleEditAll(cursoId); // Chama a função para alternar o modo de edição
        });
    });

    // Adiciona o evento de clique aos botões de deletar
    document.querySelectorAll(".delete-button").forEach((button) => {
        button.addEventListener("click", function(event) {
            const cursoId = event.target.getAttribute("data-cursoId");
            console.log(`ID do curso clicado: ${cursoId}`);
            deletarCurso(cursoId);
        });
    });
}

// Chama a função para obter cursos e exibi-los na página
getCursos().then((cursos) => {
    exibirCursos(cursos);
});


// Função para alternar entre editar e exibir valores de todos os campos ao mesmo tempo
function toggleEditAll(id) {
    const fields = ['nome', 'modalidade'];

    // Seleciona os botões relacionados ao curso
    const editButton = document.querySelector(`.edit-button[data-cursoId="${id}"]`);
    const deleteButton = document.querySelector(`.delete-button[data-cursoId="${id}"]`);
    const atualizarButton = document.getElementById(`atualizar-${id}`);

    // Alterna entre o modo de edição e visualização
    let isEditing = atualizarButton.style.display === "inline";

    fields.forEach(field => {
        const inputField = document.getElementById(`${field}-${id}`);
        const displayField = document.getElementById(`${field}-display-${id}`);

        if (!isEditing) {
            // Modo de edição: mostra inputs e oculta texto
            inputField.style.display = "inline";
            inputField.value = displayField.textContent; // Preenche o input com o valor atual
            displayField.style.display = "none";
        } else {
            // Modo de visualização: oculta inputs e mostra texto
            inputField.style.display = "none";
            displayField.style.display = "inline";
        }
    });

    if (!isEditing) {
        // Oculta os botões de "Editar" e "Deletar", e exibe o botão de "Atualizar"
        editButton.style.display = "none";
        deleteButton.style.display = "none";
        atualizarButton.style.display = "inline";
    } else {
        // Exibe os botões de "Editar" e "Deletar", e oculta o botão de "Atualizar"
        editButton.style.display = "inline";
        deleteButton.style.display = "inline";
        atualizarButton.style.display = "none";
    }
}


// Função para atualizar todos os atributos do curso
async function atualizarCurso(id) {
    const cursoData = {
        id: id,
        nome: document.getElementById(`nome-${id}`).value.trim(),
        modalidade: document.getElementById(`modalidade-${id}`).value.trim()
    };

    // Validação dos campos obrigatórios
    if (!cursoData.nome || !cursoData.modalidade) {
        alert("Por favor, preencha todos os campos.");
        return;
    }

    try {
        // Realiza a atualização via PUT
        const response = await fetch(`http://localhost:8080/cursos`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(cursoData)
        });

        if (!response.ok) throw new Error("Erro ao atualizar curso.");

        alert("Curso atualizado com sucesso!");
        const cursoAtualizado = await getCursos();
        exibirCursos(cursoAtualizado); // Atualiza a lista de cursos após a atualização
    } catch (error) {
        console.error(error);
        alert("Erro ao atualizar curso.");
    }
}

// Função para deletar curso
async function deletarCurso(cursoId) {
    // Confirmação antes de deletar
    if (window.confirm("Tem certeza que deseja deletar este curso?")) {
        try {
            console.log(`Tentando deletar o transporte com ID: ${cursoId}`);
            const response = await fetch(`http://localhost:8080/cursos/deletar/${cursoId}`, {
                    method: "DELETE",
                }
            );
            console.log("Resposta da requisição:", response);
            if (!response.ok) {
                throw new Error(`Erro ao deletar curso: ${response.status}`);
            }
            console.log(`Curso ${cursoId} deletado com sucesso.`);
            alert("Curso deletado com sucesso!");
            window.location.reload();
        } catch (error) {
            console.error(`Erro: ${error.message}`);
            alert("Erro ao deletar o curso. Tente novamente mais tarde.");
        }
    }
}
