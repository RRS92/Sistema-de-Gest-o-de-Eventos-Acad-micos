async function getMatriculas() {
    try {
        const response = await fetch("http://localhost:8080/matriculas", {
            method: "GET",
            headers: {
                'Accept': 'application/json',
                "Authorization": localStorage.getItem("token")
            }
        });

        if (!response.ok) {
            throw new Error(`Erro ao buscar matrículas: ${response.status}`);
        }

        const matriculas = await response.json();

        if (Array.isArray(matriculas) && matriculas.length > 0 && matriculas[0].curso) {
            const idCurso = matriculas[0].curso.id; // Pegando o ID do curso dentro do objeto curso
            localStorage.setItem("idCurso", idCurso);
        }

        return matriculas;
    } catch (error) {
        console.error(error);
        alert("Erro ao carregar matrículas. Tente novamente mais tarde.");
        return [];
    }
}

const idCursos = JSON.parse(localStorage.getItem("idCursos"));


// Função para exibir matrículas na página
function exibirMatriculas(matriculas) {
    const eventsContainer = document.querySelector(".events-container");
    eventsContainer.innerHTML = ""; // Limpa a lista existente

    if (matriculas.length === 0) {
        eventsContainer.innerHTML = "<p>Nenhuma matrícula encontrada.</p>";
        return;
    }

    matriculas.forEach((matricula) => {
        const eventCard = document.createElement("div");
        eventCard.classList.add("event-card");
        eventCard.innerHTML = `
            <div class="event-details">
                <p><strong>Matrícula:</strong> <span id="numMatricula-display-${matricula.id}">${matricula.numMatricula}</span>
                <input type="text" id="numMatricula-${matricula.id}" value="${matricula.numMatricula}" style="display:none;" /></p>

                <p><strong>Período de Ingresso:</strong> <span id="periodoIngresso-display-${matricula.id}">${matricula.periodoIngresso}</span>
                <input type="text" id="periodoIngresso-${matricula.id}" value="${matricula.periodoIngresso}" style="display:none;" /></p>

                <p><strong>Turno:</strong> <span id="turno-display-${matricula.id}">${matricula.turno}</span>
                <input type="text" id="turno-${matricula.id}" value="${matricula.turno}" style="display:none;" /></p>

                <p><strong>Curso:</strong> <span id="nomeCurso-display-${matricula.id}">${matricula.nomeCurso}</span>
                <input type="text" id="nomeCurso-${matricula.id}" value="${matricula.nomeCurso}" style="display:none;" /></p>

                <p><strong>Modalidade:</strong> <span id="modalidade-display-${matricula.id}">${matricula.modalidade}</span>
                <input type="text" id="modalidade-${matricula.id}" value="${matricula.modalidade}" style="display:none;" /></p>


            </div>
            <br>
            <button class="edit-button" data-matriculaId="${matricula.id}">Editar ✏️</button>  
            <button class="delete-button" data-matriculaId="${matricula.id}">Deletar 🗑️</button>
            <button class="update-button" id="atualizar-${matricula.id}" style="display:none;" onclick="atualizarMatricula(${matricula.id})">Atualizar ✏️</button>
            <button class="cancel-edit-button" style="display:none;" data-matriculaId="${matricula.id}">Cancelar ✖️</button>
        `;
        eventsContainer.appendChild(eventCard);
    });

    // Adiciona o evento de clique aos botões de editar
    document.querySelectorAll(".edit-button").forEach((button) => {
        button.addEventListener("click", function(event) {
            const matriculaId = event.target.getAttribute("data-matriculaId");
            toggleEditAll(matriculaId); // Chama a função para alternar o modo de edição
        });
    });

    // Adiciona o evento de clique aos botões de deletar
    document.querySelectorAll(".delete-button").forEach((button) => {
        button.addEventListener("click", function(event) {
            const matriculaId = event.target.getAttribute("data-matriculaId");
            console.log(`ID da matrícula clicado: ${matriculaId}`);
            deletarMatricula(matriculaId);
        });
    });

    // Adiciona evento de clique ao botão de cancelar edição
    document.querySelectorAll(".cancel-edit-button").forEach((button) => {
        button.addEventListener("click", function () {
            location.reload(); // Recarrega a página
        });
    });
}

// Chama a função para obter matriculas e exibi-las na página
getMatriculas().then((matriculas) => {
    exibirMatriculas(matriculas);
});


// Função para alternar entre editar e exibir valores de todos os campos ao mesmo tempo
function toggleEditAll(id) {
    const fields = ['numMatricula', 'periodoIngresso', 'turno', 'nomeCurso', 'modalidade'];

    // Seleciona os botões relacionados a matrícula
    const editButton = document.querySelector(`.edit-button[data-matriculaId="${id}"]`);
    const deleteButton = document.querySelector(`.delete-button[data-matriculaId="${id}"]`);

    const atualizarButton = document.getElementById(`atualizar-${id}`);
    const cancelEditButton = document.querySelector(`.cancel-edit-button[data-matriculaId="${id}"]`);

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
        cancelEditButton.style.display = "inline";
    } else {
        // Exibe os botões de "Editar" e "Deletar", e oculta o botão de "Atualizar"
        editButton.style.display = "inline";
        deleteButton.style.display = "inline";

        atualizarButton.style.display = "none";
        cancelEditButton.style.display = "none";
    }
}


// Função para atualizar todos os atributos do transporte
async function atualizarMatricula(id) {
    const matriculaData = {
        id: id,
        numMatricula: document.getElementById(`numMatricula-${id}`).value.trim(),
        periodoIngresso: document.getElementById(`periodoIngresso-${id}`).value.trim(),
        turno: document.getElementById(`turno-${id}`).value.trim(),
        nomeCurso:document.getElementById(`nomeCurso-${id}`).value.trim(),
        modalidade:document.getElementById(`modalidade-${id}`).value.trim()
    };

    // Validação dos campos obrigatórios
    if (!matriculaData.numMatricula || !matriculaData.periodoIngresso || !matriculaData.turno || !matriculaData.nomeCurso || !matriculaData.modalidade) {
        alert("Por favor, preencha todos os campos.");
        return;
    }

    try {
        // Realiza a atualização via PUT
        const response = await fetch(`http://localhost:8080/matriculas`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": localStorage.getItem("token")
            },
            body: JSON.stringify(matriculaData)
        });

        if (!response.ok) throw new Error("Erro ao atualizar matrícula.");

        alert("Matrícula atualizada com sucesso!");
        const matriculaAtualizada = await getMatriculas();
        exibirMatriculas(matriculaAtualizada); // Atualiza a lista de matrículas após a atualização
    } catch (error) {
        console.error(error);
        alert("Erro ao atualizar matrícula.");
    }
}

// Função para deletar matricula
async function deletarMatricula(matriculaId) {
    // Confirmação antes de deletar
    if (window.confirm("Tem certeza que deseja deletar esta matrícula?")) {
        try {
            console.log(`Tentando deletar a matrícula com ID: ${matriculaId}`);
            const response = await fetch(`http://localhost:8080/matriculas/deletar/${matriculaId}`, {
                    method: "DELETE",
                    headers: {
                        "Authorization": `Bearer ${localStorage.getItem("token")}`
                    }
                }
            );
            console.log("Resposta da requisição:", response);
            if (!response.ok) {
                throw new Error(`Erro ao deletar matrícula: ${response.status}`);
            }
            console.log(`Matrícula ${matriculaId} deletada com sucesso.`);
            alert("Matrícula deletada com sucesso!");
            window.location.reload();
        } catch (error) {
            console.error(`Erro: ${error.message}`);
            alert("Erro ao deletar a matrícula. Tente novamente mais tarde.");
        }
    }
}