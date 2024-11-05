async function listarServidores() {
    try {
        const response = await fetch("http://localhost:8080/servidores");
        if (!response.ok) throw new Error("Erro ao buscar servidores.");

        const servidores = await response.json();
        const servidoresContainer = document.getElementById("servidores-container");

        servidoresContainer.innerHTML = ""; // Limpa o container antes de adicionar novos dados

        servidores.forEach((servidor) => {
            const servidorDiv = document.createElement("div");
            servidorDiv.classList.add("servidor");

            servidorDiv.innerHTML = `
                <p>
                    <strong>Nome:</strong> 
                    <span id="nome-display-${servidor.id}">${servidor.nome}</span>
                    <button onclick="toggleEdit('nome', ${servidor.id})">🖋️</button>
                    <input type="text" id="nome-${servidor.id}" placeholder="Novo nome" style="display: none;" />
                    <button onclick="atualizarAtributo('nome', ${servidor.id})" style="display: none;" id="atualizar-nome-${servidor.id}">Atualizar</button>
                </p>
                <p>
                    <strong>SIAPE:</strong> 
                    <span id="siape-display-${servidor.id}">${servidor.siape}</span>
                    <button onclick="toggleEdit('siape', ${servidor.id})">🖋️</button>
                    <input type="text" id="siape-${servidor.id}" placeholder="Novo SIAPE" style="display: none;" />
                    <button onclick="atualizarAtributo('siape', ${servidor.id})" style="display: none;" id="atualizar-siape-${servidor.id}">Atualizar</button>
                </p>
                <p>
                    <strong>Cargo:</strong> 
                    <span id="cargo-display-${servidor.id}">${servidor.cargo}</span>
                    <button onclick="toggleEdit('cargo', ${servidor.id})">🖋️</button>
                    <input type="text" id="cargo-${servidor.id}" placeholder="Novo cargo" style="display: none;" />
                    <button onclick="atualizarAtributo('cargo', ${servidor.id})" style="display: none;" id="atualizar-cargo-${servidor.id}">Atualizar</button>
                </p>
                <p>
                    <strong>CPF:</strong> 
                    <span id="cpf-display-${servidor.id}">${servidor.cpf}</span>
                    <button onclick="toggleEdit('cpf', ${servidor.id})">🖋️</button>
                    <input type="text" id="cpf-${servidor.id}" placeholder="Novo CPF" style="display: none;" />
                    <button onclick="atualizarAtributo('cpf', ${servidor.id})" style="display: none;" id="atualizar-cpf-${servidor.id}">Atualizar</button>
                </p>
                <p>
                    <strong>RG:</strong> 
                    <span id="rg-display-${servidor.id}">${servidor.rg}</span>
                    <button onclick="toggleEdit('rg', ${servidor.id})">🖋️</button>
                    <input type="text" id="rg-${servidor.id}" placeholder="Novo RG" style="display: none;" />
                    <button onclick="atualizarAtributo('rg', ${servidor.id})" style="display: none;" id="atualizar-rg-${servidor.id}">Atualizar</button>
                </p>
                <p>
                    <strong>Data de Nascimento:</strong> 
                    <span id="dataNasc-display-${servidor.id}">${servidor.dataNasc}</span>
                    <button onclick="toggleEdit('dataNasc', ${servidor.id})">🖋️</button>
                    <input type="date" id="dataNasc-${servidor.id}" style="display: none;" />
                    <button onclick="atualizarAtributo('dataNasc', ${servidor.id})" style="display: none;" id="atualizar-dataNasc-${servidor.id}">Atualizar</button>
                </p>
                <p>
                    <strong>Telefone:</strong> 
                    <span id="telefone-display-${servidor.id}">${servidor.telefone}</span>
                    <button onclick="toggleEdit('telefone', ${servidor.id})">🖋️</button>
                    <input type="text" id="telefone-${servidor.id}" placeholder="Novo telefone" style="display: none;" />
                    <button onclick="atualizarAtributo('telefone', ${servidor.id})" style="display: none;" id="atualizar-telefone-${servidor.id}">Atualizar</button>
                </p>
                <p>
                    <strong>Email:</strong> 
                    <span id="email-display-${servidor.id}">${servidor.email}</span>
                    <button onclick="toggleEdit('email', ${servidor.id})">🖋️</button>
                    <input type="email" id="email-${servidor.id}" placeholder="Novo email" style="display: none;" />
                    <button onclick="atualizarAtributo('email', ${servidor.id})" style="display: none;" id="atualizar-email-${servidor.id}">Atualizar</button>
                </p>
                </br>
                <button onclick="deletarServidor(${servidor.id})">🗑️ Deletar</button>
                <hr>
            `;

            servidoresContainer.appendChild(servidorDiv);
        });
    } catch (error) {
        console.error(error);
        alert("Erro ao carregar servidores.");
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

    const servidores = await fetch("http://localhost:8080/servidores");
    const servidoresList = await servidores.json();
    const servidorAtual = servidoresList.find(servidor => servidor.id === id);

    const dadosAtualizados = {
        id: servidorAtual.id,
        nome: field === 'nome' ? novoValor : servidorAtual.nome,
        siape: field === 'siape' ? novoValor : servidorAtual.siape,
        cargo: field === 'cargo' ? novoValor : servidorAtual.cargo,
        cpf: field === 'cpf' ? novoValor : servidorAtual.cpf,
        rg: field === 'rg' ? novoValor : servidorAtual.rg,
        dataNasc: field === 'dataNasc' ? novoValor : servidorAtual.dataNasc,
        telefone: field === 'telefone' ? novoValor : servidorAtual.telefone,
        email: servidorAtual.email
    };

    try {
        const response = await fetch(`http://localhost:8080/servidores`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(dadosAtualizados)
        });

        if (!response.ok) throw new Error("Erro ao atualizar servidor.");

        alert("Servidor atualizado com sucesso!");
        listarServidores();
    } catch (error) {
        console.error(error);
        alert("Erro ao atualizar servidor.");
    }
}

async function deletarServidor(id) {
    const confirmacao = confirm("Tem certeza de que deseja deletar este servidor?");
    if (!confirmacao) return;

    try {
        const response = await fetch(`http://localhost:8080/servidores/${id}`, {
            method: "DELETE",
        });

        if (!response.ok) throw new Error("Erro ao deletar servidor.");

        alert("Servidor deletado com sucesso!");
        listarServidores();
    } catch (error) {
        console.error(error);
        alert("Erro ao deletar servidor.");
    }
}

// Chama a função ao carregar a página
listarServidores();