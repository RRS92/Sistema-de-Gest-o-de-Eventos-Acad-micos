// Função para cadastrar servidor
async function cadastrarServidor() {
    try {
        // 1. Cadastra o banco e obtém o ID
        const bancoData = {
            nomeBanco: document.getElementById("nomeBanco").value,
            numConta: document.getElementById("numConta").value,
            agencia: document.getElementById("agencia").value,
            operacao: document.getElementById("operacao").value
        };

        const bancoResponse = await fetch("http://localhost:8080/bancos", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(bancoData)
        });

        if (!bancoResponse.ok) throw new Error("Erro ao salvar banco.");
        const banco = await bancoResponse.json();
        const bancoId = banco.id;

        // 2. Cadastra o endereço e obtém o ID
        const enderecoData = {
            rua: document.getElementById("rua").value,
            numero: document.getElementById("numero").value,
            bairro: document.getElementById("bairro").value,
            cidade: document.getElementById("cidade").value,
            estado: document.getElementById("estado").value,
            cep: document.getElementById("cep").value,
            complemento: document.getElementById("complemento").value
        };

        const enderecoResponse = await fetch("http://localhost:8080/enderecos", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(enderecoData)
        });

        if (!enderecoResponse.ok) throw new Error("Erro ao salvar endereço.");
        const endereco = await enderecoResponse.json();
        const enderecoId = endereco.id;

        // 3. Cadastra o servidor com os IDs de banco e endereço
        const servidorData = {
            nome: document.getElementById("nome-servidor").value,
            cpf: document.getElementById("cpf").value,
            rg: document.getElementById("rg").value,
            dataNasc: document.getElementById("dataNasc").value,
            telefone: document.getElementById("telefone").value,
            email: document.getElementById("email").value,
            siape: document.getElementById("siape").value,
            cargo: document.getElementById("cargo").value,
            banco: { id: bancoId },
            endereco: { id: enderecoId }
        };

        const servidorResponse = await fetch("http://localhost:8080/servidores", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(servidorData)
        });

        if (!servidorResponse.ok) throw new Error("Erro ao salvar servidor.");
        alert("servidor cadastrado com sucesso!");

        // Redireciona para outra página
        window.location.href = "perfil-servidor.html";  
        

    } catch (error) {
        console.error(error);
        alert("Erro ao cadastrar. Tente novamente.");
    }
}


// Função para obter servidores
async function getServidores() {
    try {
        const response = await fetch("http://localhost:8080/servidores");
        if (!response.ok) {
            throw new Error(`Erro ao buscar servidores: ${response.status}`);
        }
        const servidores = await response.json();
        return servidores;
    } catch (error) {
        console.error(error);
        alert("Erro ao carregar servidores. Tente novamente mais tarde.");
        return [];
    }
}

// Função para exibir servidores na página
function exibirServidores(servidores) {
    const eventsContainer = document.querySelector(".events-container");
    eventsContainer.innerHTML = ""; // Limpa a lista existente

    if (servidores.length === 0) {
        eventsContainer.innerHTML = "<p>Nenhum servidor encontrado.</p>";
        return;
    }

    servidores.forEach((servidor) => {
        const eventCard = document.createElement("div");
        eventCard.classList.add("event-card");
        eventCard.innerHTML = `
            <div class="event-details">
                <p><strong>Nome:</strong> <span id="nome-display-${servidor.id}">${servidor.nome}</span>
                <input type="text" id="nome-${servidor.id}" value="${servidor.nome}" style="display:none;" /></p>

                <p><strong>Siape:</strong> <span id="siape-display-${servidor.id}">${servidor.siape}</span>
                <input type="text" id="siape-${servidor.id}" value="${servidor.siape}" style="display:none;" /></p>

                <p><strong>Cargo:</strong> <span id="cargo-display-${servidor.id}">${servidor.cargo}</span>
                <input type="text" id="cargo-${servidor.id}" value="${servidor.cargo}" style="display:none;" /></p>

                <p><strong>CPF:</strong> <span id="cpf-display-${servidor.id}">${servidor.cpf}</span>
                <input type="text" id="cpf-${servidor.id}" value="${servidor.cpf}" style="display:none;" /></p>

                <p><strong>RG:</strong> <span id="rg-display-${servidor.id}">${servidor.rg}</span>
                <input type="text" id="rg-${servidor.id}" value="${servidor.rg}" style="display:none;" /></p>

                <p><strong>Data de Nascimento:</strong> <span id="dataNasc-display-${servidor.id}">${servidor.dataNasc}</span>
                <input type="text" id="dataNasc-${servidor.id}" value="${servidor.dataNasc}" style="display:none;" /></p>

                <p><strong>Telefone:</strong> <span id="telefone-display-${servidor.id}">${servidor.telefone}</span>
                <input type="text" id="telefone-${servidor.id}" value="${servidor.telefone}" style="display:none;" /></p>

                <p><strong>Email:</strong> <span id="email-display-${servidor.id}">${servidor.email}</span>
                <input type="text" id="email-${servidor.id}" value="${servidor.email}" style="display:none;" /></p>
            </div>
            <br>
            <button class="edit-button" data-servidorId="${servidor.id}">Editar ✏️</button>  
            <button class="delete-button" data-servidorId="${servidor.id}">Deletar 🗑️</button>
            <button class="update-button" id="atualizar-${servidor.id}" style="display:none;" onclick="atualizarServidor(${servidor.id})">Atualizar ✏️</button>
        `;
        eventsContainer.appendChild(eventCard);
    });

    // Adiciona o evento de clique aos botões de editar
    document.querySelectorAll(".edit-button").forEach((button) => {
        button.addEventListener("click", function(event) {
            const servidorId = event.target.getAttribute("data-servidorId");
            toggleEditAll(servidorId); // Chama a função para alternar o modo de edição
            });
        });

    // Adiciona o evento de clique aos botões de deletar
    document.querySelectorAll(".delete-button").forEach((button) => {
        button.addEventListener("click", function(event) {
            const servidorId = event.target.getAttribute("data-servidorId");
            console.log(`ID do servidor clicado: ${servidorId}`);
            deletarServidor(servidorId);
        });
    });
}

// Chama a função para obter servidores e exibi-los na página
getServidores().then((servidores) => {
    exibirServidores(servidores);
});


// Função para alternar entre editar e exibir valores de todos os campos ao mesmo tempo
function toggleEditAll(id) {
    const fields = ['nome', 'siape', 'cargo', 'dataNasc', 'telefone', 'email'];

    // Seleciona os botões relacionados ao servidor
    const editButton = document.querySelector(`.edit-button[data-servidorId="${id}"]`);
    const deleteButton = document.querySelector(`.delete-button[data-servidorId="${id}"]`);
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


// Função para atualizar todos os atributos do servidor
async function atualizarServidor(id) {
    const servidorData = {
        id: id,
        nome: document.getElementById(`nome-${id}`).value.trim(),
        siape: document.getElementById(`siape-${id}`).value.trim(),
        cargo: document.getElementById(`cargo-${id}`).value.trim(),
        cpf: document.getElementById(`cpf-${id}`).value.trim(),
        rg: document.getElementById(`rg-${id}`).value.trim(),
        dataNasc: document.getElementById(`dataNasc-${id}`).value.trim(),
        telefone: document.getElementById(`telefone-${id}`).value.trim(),
        email: document.getElementById(`email-${id}`).value.trim()
    };

    // Validação dos campos obrigatórios
    if (!servidorData.nome || !servidorData.siape || !servidorData.cargo || !servidorData.cpf || !servidorData.rg || !servidorData.dataNasc || !servidorData.telefone || !servidorData.email) {
        alert("Por favor, preencha todos os campos.");
        return;
    }

    try {
        // Realiza a atualização via PUT
        const response = await fetch(`http://localhost:8080/servidores`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(servidorData)
        });

        if (!response.ok) throw new Error("Erro ao atualizar servidor.");

        alert("Servidor atualizado com sucesso!");
        const servidoresAtualizados = await getServidores();
        exibirServidores(servidoresAtualizados); // Atualiza a lista de servidores após a atualização
    } catch (error) {
        console.error(error);
        alert("Erro ao atualizar servidor.");
    }
}


async function deletarServidor(servidorId) {
    // Confirmação antes de deletar
    if (window.confirm("Tem certeza que deseja deletar este servidor?")) {
        try {
            console.log(`Tentando deletar o servidor com ID: ${servidorId}`);
            const response = await fetch(`http://localhost:8080/servidores/deletar/${servidorId}`, {
                    method: "DELETE",
                });

            console.log("Resposta da requisição:", response);
            if (!response.ok) {
                throw new Error(`Erro ao deletar servidor: ${response.status}`);
            }
            console.log(`Servidor ${servidorId} deletado com sucesso.`);
            alert("Servidor deletado com sucesso!");
            window.location.reload();
        } catch (error) {
            console.error(`Erro: ${error.message}`);
            alert("Erro ao deletar o servidor. Tente novamente mais tarde.");
        }
    }
}

// Verifica em qual página o script está sendo executado
document.addEventListener("DOMContentLoaded", () => {
    const submitButton = document.getElementById("submit-servidor");
    const servidoresContainer = document.getElementById("servidores-container");

    // Se o botão de cadastro existir, estamos na página de cadastro
    if (submitButton) {
        submitButton.addEventListener("click", cadastrarServidor);
    }

    // Se o container de servidores existir, estamos na página de listagem
    if (servidoresContainer) {
        exibirServidores(servidores)
    }
});
