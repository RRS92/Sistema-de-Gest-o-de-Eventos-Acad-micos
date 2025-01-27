// Função para obter bancos
async function getBancos() {
    try {
        const response = await fetch("http://localhost:8080/bancos", 
            {
            method: "GET",
            headers: {
                'Accept': 'application/json',
                "Authorization": localStorage.getItem("token")
            }});
        if (!response.ok) {
            throw new Error(`Erro ao buscar bancos: ${response.status}`);
        }
        const bancos = await response.json();
        return bancos;
    } catch (error) {
        console.error(error);
        alert("Erro ao carregar bancos. Tente novamente mais tarde.");
        return [];
    }
}

// Função para exibir bancos na página
function exibirBancos(bancos) {
    const eventsContainer = document.querySelector(".events-container");
    eventsContainer.innerHTML = ""; // Limpa a lista existente

    if (bancos.length === 0) {
        eventsContainer.innerHTML = "<p>Nenhum banco encontrado.</p>";
        return;
    }

    bancos.forEach((banco) => {
        const eventCard = document.createElement("div");
        eventCard.classList.add("event-card");
        eventCard.innerHTML = `
            <div class="event-details">
                <p><strong>Banco:</strong> <span id="nomeBanco-display-${banco.id}">${banco.nomeBanco}</span>
                <input type="text" id="nomeBanco-${banco.id}" value="${banco.nomeBanco}" style="display:none;" /></p>

                <p><strong>Nº da Conta:</strong> <span id="numConta-display-${banco.id}">${banco.numConta}</span>
                <input type="text" id="numConta-${banco.id}" value="${banco.numConta}" style="display:none;" /></p>

                <p><strong>Agência:</strong> <span id="agencia-display-${banco.id}">${banco.agencia}</span>
                <input type="text" id="agencia-${banco.id}" value="${banco.agencia}" style="display:none;" /></p>

                <p><strong>Operação:</strong> <span id="operacao-display-${banco.id}">${banco.operacao}</span>
                <input type="text" id="operacao-${banco.id}" value="${banco.operacao}" style="display:none;" /></p>
            </div>
            <br>
            <button class="edit-center-button" data-bancoId="${banco.id}">Editar ✏️</button>  
            <button class="update-button" id="atualizar-${banco.id}" style="display:none;" onclick="atualizarBanco(${banco.id})">Atualizar ✏️</button>
            <button class="cancel-edit-button" style="display:none;" data-bancoId="${banco.id}">Cancelar ✖️</button>
        `;
        eventsContainer.appendChild(eventCard);
    });

    // Adiciona o evento de clique aos botões de editar
    document.querySelectorAll(".edit-center-button").forEach((button) => {
        button.addEventListener("click", function(event) {
            const bancoId = event.target.getAttribute("data-bancoId");
            toggleEditAll(bancoId); // Chama a função para alternar o modo de edição
        });
    });

    // Adiciona o evento de clique aos botões de deletar
    document.querySelectorAll(".delete-button").forEach((button) => {
        button.addEventListener("click", function(event) {
            const bancoId = event.target.getAttribute("data-bancoId");
            console.log(`ID do banco clicado: ${bancoId}`);
            deletarBanco(bancoId);
        });
    });

    // Adiciona evento de clique ao botão de cancelar edição
    document.querySelectorAll(".cancel-edit-button").forEach((button) => {
        button.addEventListener("click", function () {
            location.reload(); // Recarrega a página
        });
    });
}

// Chama a função para obter bancos e exibi-los na página
getBancos().then((bancos) => {
    exibirBancos(bancos);
});


// Função para alternar entre editar e exibir valores de todos os campos ao mesmo tempo
function toggleEditAll(id) {
    const fields = ['nomeBanco', 'numConta', 'agencia', 'operacao'];

    // Seleciona os botões relacionados ao banco
    const editButton = document.querySelector(`.edit-center-button[data-bancoId="${id}"]`);

    const atualizarButton = document.getElementById(`atualizar-${id}`);
    const cancelEditButton = document.querySelector(`.cancel-edit-button[data-bancoId="${id}"]`);

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

        atualizarButton.style.display = "inline";
        cancelEditButton.style.display = "inline";
    } else {
        // Exibe os botões de "Editar" e "Deletar", e oculta o botão de "Atualizar"
        editButton.style.display = "inline";

        atualizarButton.style.display = "none";
        cancelEditButton.style.display = "none";
    }
}


// Função para atualizar todos os atributos do banco
async function atualizarBanco(id) {
    const bancoData = {
        id: id,
        nomeBanco: document.getElementById(`nomeBanco-${id}`).value.trim(),
        numConta: document.getElementById(`numConta-${id}`).value.trim(),
        agencia: document.getElementById(`agencia-${id}`).value.trim(),
        operacao: document.getElementById(`operacao-${id}`).value.trim()
    };

    // Validação dos campos obrigatórios
    if (!bancoData.nomeBanco || !bancoData.numConta || !bancoData.agencia || !bancoData.operacao) {
        alert("Por favor, preencha todos os campos.");
        return;
    }

    try {
        // Realiza a atualização via PUT
        const response = await fetch(`http://localhost:8080/bancos`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": localStorage.getItem("token")
            },
            body: JSON.stringify(bancoData)
        });

        if (!response.ok) throw new Error("Erro ao atualizar banco.");

        alert("Banco atualizado com sucesso!");
        const bancoAtualizado = await getBancos();
        exibirBancos(bancoAtualizado); // Atualiza a lista de bancos após a atualização
    } catch (error) {
        console.error(error);
        alert("Erro ao atualizar banco.");
    }
}

// Função para deletar banco
async function deletarBanco(bancoId) {
    // Confirmação antes de deletar
    if (window.confirm("Tem certeza que deseja deletar este banco?")) {
        try {
            console.log(`Tentando deletar o banco com ID: ${bancoId}`);
            const response = await fetch(`http://localhost:8080/bancos/deletar/${bancoId}`, {
                    method: "DELETE",
                    headers: {
                        "Authorization": `Bearer ${localStorage.getItem("token")}`
                    }
                }
            );
            console.log("Resposta da requisição:", response);
            if (!response.ok) {
                throw new Error(`Erro ao deletar banco: ${response.status}`);
            }
            console.log(`Banco ${bancoId} deletado com sucesso.`);
            alert("Banco deletado com sucesso!");
            window.location.reload();
        } catch (error) {
            console.error(`Erro: ${error.message}`);
            alert("Erro ao deletar o banco. Tente novamente mais tarde.");
        }
    }
}