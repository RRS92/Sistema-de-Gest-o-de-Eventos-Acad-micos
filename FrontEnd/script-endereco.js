// Função para obter enderecos
async function getEnderecos() {
    try {
        const response = await fetch("http://localhost:8080/enderecos", 
            {
            method: "GET",
            headers: {
                'Accept': 'application/json',
                "Authorization": localStorage.getItem("token")
            }});
        if (!response.ok) {
            throw new Error(`Erro ao buscar endereços: ${response.status}`);
        }
        const enderecos = await response.json();
        return enderecos;
    } catch (error) {
        console.error(error);
        alert("Erro ao carregar endereços. Tente novamente mais tarde.");
        return [];
    }
}

// Função para exibir endereços na página
function exibirEnderecos(enderecos) {
    const eventsContainer = document.querySelector(".events-container");
    eventsContainer.innerHTML = ""; // Limpa a lista existente

    if (enderecos.length === 0) {
        eventsContainer.innerHTML = "<p>Nenhum endereço encontrado.</p>";
        return;
    }

    enderecos.forEach((endereco) => {
        const eventCard = document.createElement("div");
        eventCard.classList.add("event-card");
        eventCard.innerHTML = `
            <div class="event-details">
                <p><strong>Rua:</strong> <span id="rua-display-${endereco.id}">${endereco.rua}</span>
                <input type="text" id="rua-${endereco.id}" value="${endereco.rua}" style="display:none;" /></p>

                <p><strong>Número:</strong> <span id="numero-display-${endereco.id}">${endereco.numero}</span>
                <input type="text" id="numero-${endereco.id}" value="${endereco.numero}" style="display:none;" /></p>

                <p><strong>Bairro:</strong> <span id="bairro-display-${endereco.id}">${endereco.bairro}</span>
                <input type="text" id="bairro-${endereco.id}" value="${endereco.bairro}" style="display:none;" /></p>

                <p><strong>Cidade:</strong> <span id="cidade-display-${endereco.id}">${endereco.cidade}</span>
                <input type="text" id="cidade-${endereco.id}" value="${endereco.cidade}" style="display:none;" /></p>

                <p><strong>Estado:</strong> <span id="estado-display-${endereco.id}">${endereco.estado}</span>
                <input type="text" id="estado-${endereco.id}" value="${endereco.estado}" style="display:none;" /></p>

                <p><strong>CEP:</strong> <span id="cep-display-${endereco.id}">${endereco.cep}</span>
                <input type="text" id="cep-${endereco.id}" value="${endereco.cep}" style="display:none;" /></p>

                <p><strong>Complemento:</strong> <span id="complemento-display-${endereco.id}">${endereco.complemento}</span>
                <input type="text" id="complemento-${endereco.id}" value="${endereco.complemento}" style="display:none;" /></p>
            </div>
            <br>
            <button class="edit-button" data-enderecoId="${endereco.id}">Editar ✏️</button>  
            <button class="update-button" id="atualizar-${endereco.id}" style="display:none;" onclick="atualizarEndereco(${endereco.id})">Atualizar ✏️</button>
        `;
        eventsContainer.appendChild(eventCard);
    });

    // Adiciona o evento de clique aos botões de editar
    document.querySelectorAll(".edit-button").forEach((button) => {
        button.addEventListener("click", function(event) {
            const enderecoId = event.target.getAttribute("data-enderecoId");
            toggleEditAll(enderecoId); // Chama a função para alternar o modo de edição
        });
    });

    // Adiciona o evento de clique aos botões de deletar
    document.querySelectorAll(".delete-button").forEach((button) => {
        button.addEventListener("click", function(event) {
            const enderecoId = event.target.getAttribute("data-enderecoId");
            console.log(`ID do endereco clicado: ${enderecoId}`);
            deletarEndereco(enderecoId);
        });
    });
}

// Chama a função para obter endereços e exibi-los na página
getEnderecos().then((enderecos) => {
    exibirEnderecos(enderecos);
});


// Função para alternar entre editar e exibir valores de todos os campos ao mesmo tempo
function toggleEditAll(id) {
    const fields = ['rua', 'numero', 'bairro', 'cidade', 'estado', 'cep', 'complemento'];

    // Seleciona os botões relacionados ao banco
    const editButton = document.querySelector(`.edit-button[data-enderecoId="${id}"]`);
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
        atualizarButton.style.display = "inline";
    } else {
        // Exibe os botões de "Editar" e "Deletar", e oculta o botão de "Atualizar"
        editButton.style.display = "inline";
        atualizarButton.style.display = "none";
    }
}


// Função para atualizar todos os atributos do endereço
async function atualizarEndereco(id) {
    const enderecoData = {
        id: id,
        rua: document.getElementById(`rua-${id}`).value.trim(),
        numero: document.getElementById(`numero-${id}`).value.trim(),
        bairro: document.getElementById(`bairro-${id}`).value.trim(),
        cidade: document.getElementById(`cidade-${id}`).value.trim(),
        estado: document.getElementById(`estado-${id}`).value.trim(),
        cep: document.getElementById(`cep-${id}`).value.trim(),
        complemento: document.getElementById(`complemento-${id}`).value.trim()
    };

    // Validação dos campos obrigatórios
    if (!enderecoData.rua || !enderecoData.numero || !enderecoData.bairro || !enderecoData.cidade || !enderecoData.estado || !enderecoData.cep || !enderecoData.complemento) {
        alert("Por favor, preencha todos os campos.");
        return;
    }

    try {
        // Realiza a atualização via PUT
        const response = await fetch(`http://localhost:8080/enderecos`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": localStorage.getItem("token")
            },
            body: JSON.stringify(enderecoData)
        });

        if (!response.ok) throw new Error("Erro ao atualizar endereço.");

        alert("Endereço atualizado com sucesso!");
        const enderecoAtualizado = await getEnderecos();
        exibirEnderecos(enderecoAtualizado); // Atualiza a lista de endereços após a atualização
    } catch (error) {
        console.error(error);
        alert("Erro ao atualizar endereço.");
    }
}

// Função para deletar endereço
async function deletarEndereco(enderecoId) {
    // Confirmação antes de deletar
    if (window.confirm("Tem certeza que deseja deletar este endereço?")) {
        try {
            console.log(`Tentando deletar o endereço com ID: ${enderecoId}`);
            const response = await fetch(`http://localhost:8080/enderecos/deletar/${enderecoId}`, {
                    method: "DELETE",
                    headers: {
                        "Authorization": `Bearer ${localStorage.getItem("token")}`
                    }
                }
            );
            console.log("Resposta da requisição:", response);
            if (!response.ok) {
                throw new Error(`Erro ao deletar endereço: ${response.status}`);
            }
            console.log(`Endereço ${enderecoId} deletado com sucesso.`);
            alert("Endereço deletado com sucesso!");
            window.location.reload();
        } catch (error) {
            console.error(`Erro: ${error.message}`);
            alert("Erro ao deletar o endereço. Tente novamente mais tarde.");
        }
    }
}