// Função para criar transportes
document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("form-transporte");
    const saveButton = document.querySelector(".btn");

    saveButton.addEventListener("click", (e) => {
        e.preventDefault(); // Evita o comportamento padrão de recarregar a página

        // Captura os valores do formulário
        const categoria = document.getElementById("categoria").value;
        const placa = document.getElementById("placa").value;
        const quilometragem = document.getElementById("quilometragem").value;
        const nomeMotorista = document.getElementById("nomeMotorista").value;
        const horaSaida = document.getElementById("horaSaida").value;
        const horaChegada = document.getElementById("horaChegada").value;

        // Criação do objeto com os dados do transporte
        const transporteData = {
            categoria: categoria,
            placa: placa,
            quilometragem: quilometragem,
            nomeMotorista: nomeMotorista,
            horaSaida: horaSaida,
            horaChegada: horaChegada,
        };

        // Envia os dados para a API
        fetch("http://localhost:8080/transportes", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(transporteData),
        })
            .then((response) => {
                if (response.ok) {
                    alert("Transporte criado com sucesso!");
                    window.location.reload(); // Recarrega a página
                    return response.json();
                } else {
                    throw new Error("Erro ao criar o transporte");
                }
            })
            .catch((error) => {
                console.error("Erro:", error);
                alert("Erro ao criar o transporte.");
            });
    });
});

// Função para obter transportes
async function getTransportes() {
    try {
        const response = await fetch("http://localhost:8080/transportes");
        if (!response.ok) {
            throw new Error(`Erro ao buscar transportes: ${response.status}`);
        }
        const transportes = await response.json();
        return transportes;
    } catch (error) {
        console.error(error);
        alert("Erro ao carregar transportes. Tente novamente mais tarde.");
        return [];
    }
}

// Função para exibir transportes na página
function exibirTransportes(transportes) {
    const eventsContainer = document.querySelector(".events-container");
    eventsContainer.innerHTML = ""; // Limpa a lista existente

    if (transportes.length === 0) {
        eventsContainer.innerHTML = "<p>Nenhum transporte encontrado.</p>";
        return;
    }

    transportes.forEach((transporte) => {
        const eventCard = document.createElement("div");
        eventCard.classList.add("event-card");
        eventCard.innerHTML = `
            <div class="event-details">
                <p><strong>Categoria:</strong> <span id="categoria-display-${transporte.id}">${transporte.categoria}</span>
                <input type="text" id="categoria-${transporte.id}" value="${transporte.categoria}" style="display:none;" /></p>

                <p><strong>Placa:</strong> <span id="placa-display-${transporte.id}">${transporte.placa}</span>
                <input type="text" id="placa-${transporte.id}" value="${transporte.placa}" style="display:none;" /></p>

                <p><strong>Quilometragem:</strong> <span id="quilometragem-display-${transporte.id}">${transporte.quilometragem}</span>
                <input type="text" id="quilometragem-${transporte.id}" value="${transporte.quilometragem}" style="display:none;" /></p>

                <p><strong>Nome do Motorista:</strong> <span id="nomeMotorista-display-${transporte.id}">${transporte.nomeMotorista}</span>
                <input type="text" id="nomeMotorista-${transporte.id}" value="${transporte.nomeMotorista}" style="display:none;" /></p>

                <p><strong>Hora de Saída:</strong> <span id="horaSaida-display-${transporte.id}">${transporte.horaSaida}</span>
                <input type="text" id="horaSaida-${transporte.id}" value="${transporte.horaSaida}" style="display:none;" /></p>

                <p><strong>Hora de Chegada:</strong> <span id="horaChegada-display-${transporte.id}">${transporte.horaChegada}</span>
                <input type="text" id="horaChegada-${transporte.id}" value="${transporte.horaChegada}" style="display:none;" /></p>
            </div>
            <br>
            <button onclick="deletarTransporte(${transporte.id})">🗑️ Deletar</button>
            <button onclick="toggleEditAll(${transporte.id})">🖋️Editar </button>
            <button id="atualizar-${transporte.id}" style="display:none;" onclick="atualizarTransporte(${transporte.id})">Atualizar</button>
        `;
        eventsContainer.appendChild(eventCard);
    });

    // Adiciona o evento de clique aos botões de deletar
    document.querySelectorAll(".delete-button").forEach((button) => {
        button.addEventListener("click", function (event) {
            const transporteId = event.target.getAttribute("data-id");
            console.log(`ID do transporte clicado: ${transporteId}`);
            deletarTransporte(transporteId);
        });
    });
}

// Chama a função para obter transportes e exibi-los na página
getTransportes().then((transportes) => {
    exibirTransportes(transportes);
});


// Função para alternar entre editar e exibir valores de todos os campos ao mesmo tempo
function toggleEditAll(id) {
    const fields = ['categoria', 'placa', 'quilometragem', 'nomeMotorista', 'horaSaida', 'horaChegada'];

    fields.forEach(field => {
        const inputField = document.getElementById(`${field}-${id}`);
        const displayField = document.getElementById(`${field}-display-${id}`);
        const atualizarButton = document.getElementById(`atualizar-${id}`);

        if (inputField.style.display === "none") {
            inputField.style.display = "inline";
            inputField.value = displayField.textContent; // Preenche o input com o valor atual
            displayField.style.display = "none"; // Oculta o valor exibido
        } else {
            inputField.style.display = "none";
            displayField.style.display = "inline"; // Mostra o valor exibido
        }

        atualizarButton.style.display = "inline"; // Mostra o botão de atualizar
    });
}


// Função para atualizar todos os atributos do transporte
async function atualizarTransporte(id) {
    const transporteData = {
        id: id,
        categoria: document.getElementById(`categoria-${id}`).value.trim(),
        placa: document.getElementById(`placa-${id}`).value.trim(),
        quilometragem: document.getElementById(`quilometragem-${id}`).value.trim(),
        nomeMotorista: document.getElementById(`nomeMotorista-${id}`).value.trim(),
        horaSaida: document.getElementById(`horaSaida-${id}`).value.trim(),
        horaChegada: document.getElementById(`horaChegada-${id}`).value.trim()
    };

    // Validação dos campos obrigatórios
    if (!transporteData.categoria || !transporteData.placa || !transporteData.quilometragem || !transporteData.nomeMotorista || !transporteData.horaSaida || !transporteData.horaChegada) {
        alert("Por favor, preencha todos os campos.");
        return;
    }

    try {
        // Realiza a atualização via PUT
        const response = await fetch(`http://localhost:8080/transportes`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(transporteData)
        });

        if (!response.ok) throw new Error("Erro ao atualizar transporte.");

        alert("Transporte atualizado com sucesso!");
        const transporteAtualizado = await getTransportes();
        exibirTransportes(transporteAtualizado); // Atualiza a lista de transportes após a atualização
    } catch (error) {
        console.error(error);
        alert("Erro ao atualizar transporte.");
    }
}


// Função para deletar transporte
async function deletarTransporte(transporteId) {
    // Confirmação antes de deletar
    if (window.confirm("Tem certeza que deseja deletar este transporte?")) {
        try {
            console.log(`Tentando deletar o transporte com ID: ${transporteId}`);
            const response = await fetch(
                `http://localhost:8080/transportes/deletar/${transporteId}`,
                {
                    method: "DELETE",
                }
            );
            console.log("Resposta da requisição:", response);
            if (!response.ok) {
                throw new Error(`Erro ao deletar transporte: ${response.status}`);
            }
            console.log(`Transporte ${transporteId} deletado com sucesso.`);
            alert("Transporte deletado com sucesso!");
            window.location.reload();
        } catch (error) {
            console.error(`Erro: ${error.message}`);
            alert("Erro ao deletar o transporte. Tente novamente mais tarde.");
        }
    }
}
