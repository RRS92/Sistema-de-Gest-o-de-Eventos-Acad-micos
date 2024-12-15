// Função para criar eventos
document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("form-event");
    const saveButton = document.querySelector(".btn");

    saveButton.addEventListener("click", (e) => {
        e.preventDefault(); // Evita o comportamento padrão de recarregar a página

        // Captura os valores do formulário
        const nome = document.getElementById("nome").value;
        const descricao = document.getElementById("descricao").value;
        const data = document.getElementById("data").value;
        const local = document.getElementById("local").value;
        const tipo = document.getElementById("tipo").value;

        // Criação do objeto com os dados do evento
        const eventoData = {
            nome: nome,
            descricao: descricao,
            data: data,
            local: local,
            tipo: tipo
        };

        // Envia os dados para a API
        fetch("http://localhost:8080/eventos", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": localStorage.getItem("token")
            },
            body: JSON.stringify(eventoData),
        })
            .then((response) => {
                if (response.ok) {
                    alert("Evento criado com sucesso!");
                    window.location.href = "lista-evento.html";

                } else {
                    throw new Error("Erro ao criar o evento");
                }
            })
            .catch((error) => {
                console.error("Erro:", error);
                alert("Erro ao criar o evento.");
            });
    });
});


// Função para obter eventos
async function getEventos() {
    try {
        const response = await fetch("http://localhost:8080/eventos", 
            {
            method: "GET",
            headers: {
                'Accept': 'application/json',
                "Authorization": localStorage.getItem("token")
            }});
        if (!response.ok) {
            throw new Error(`Erro ao buscar eventos: ${response.status}`);
        }
        const eventos = await response.json();
        return eventos;
    } catch (error) {
        console.error(error);
        alert("Erro ao carregar eventos. Tente novamente mais tarde.");
        return [];
    }
}

// Função para exibir eventos na página
function exibirEventos(eventos) {
    const eventsContainer = document.querySelector(".events-container");
    eventsContainer.innerHTML = ""; // Limpa a lista existente

    if (eventos.length === 0) {
        eventsContainer.innerHTML = "<p>Nenhum evento encontrado.</p>";
        return;
    }

    eventos.forEach((evento) => {
        const eventCard = document.createElement("div");
        eventCard.classList.add("event-card");
        eventCard.innerHTML = `
            <h3>${evento.nome}</h3>
            <div class="event-details">
                <p><strong>Nome:</strong> <span id="nome-display-${evento.id}">${evento.nome}</span>
                <input type="text" id="nome-${evento.id}" value="${evento.nome}" style="display:none;" /></p>

                <p><strong>Descrição:</strong> <span id="descricao-display-${evento.id}">${evento.descricao}</span>
                <input type="text" id="descricao-${evento.id}" value="${evento.descricao}" style="display:none;" /></p>

                <p><strong>Data:</strong> <span id="data-display-${evento.id}">${evento.data}</span>
                <input type="text" id="data-${evento.id}" value="${evento.data}" style="display:none;" /></p>

                <p><strong>Local:</strong> <span id="local-display-${evento.id}">${evento.local}</span>
                <input type="text" id="local-${evento.id}" value="${evento.local}" style="display:none;" /></p>

                <p><strong>Tipo:</strong> <span id="tipo-display-${evento.id}">${evento.tipo}</span>
                <input type="text" id="tipo-${evento.id}" value="${evento.tipo}" style="display:none;" /></p>
            </div>
            <br>
            <button class="certificate-button" data-eventoId="${evento.id}">Criar Certificado</button>
            <button class="transport-button" data-eventoId="${evento.id}">Cadastrar Transporte</button>

            <button class="evaluateEvent-button" data-eventoId="${evento.id}">Avaliar Evento</button>
            <button class="seeReviews-button" data-eventoId="${evento.id}">Ver Avaliações</button>

            <button class="seeTransportes-button" data-eventoId="${evento.id}">Ver Transportes</button>
            <button class="seeCertificados-button" data-eventoId="${evento.id}">Ver Certificados</button>

            <button class="edit-button" data-eventoId="${evento.id}">Editar ✏️</button>
            <button class="delete-button" data-eventoId="${evento.id}">Deletar 🗑️</button>  
            <button class="update-button" id="atualizar-${evento.id}" style="display:none;" onclick="atualizarEvento(${evento.id})">Atualizar ✏️</button>
            <button class="cancel-edit-button" style="display:none;" data-eventoId="${evento.id}">Cancelar ✖️</button>

        `;
        eventsContainer.appendChild(eventCard);
    });

     // Botão de Criar Certificado
     document.querySelectorAll('.certificate-button').forEach(button => {
        button.addEventListener('click', function() { 
            const idEvento = this.getAttribute('data-eventoId'); // Captura o ID do evento
            const nomeEvento = this.closest('.event-card').querySelector('h3').textContent; // Captura o nome do evento
            localStorage.setItem('idEventoSelecionado', idEvento); // Salva o ID do evento
            localStorage.setItem('nomeEventoSelecionado', nomeEvento); // Salva o nome do evento
            window.location.href = 'cadastro-certificado.html';
        });
    });

    // Botão de Cadastrar Transporte
    document.querySelectorAll('.transport-button').forEach(button => {
        button.addEventListener('click', function() {
            const idEvento = this.getAttribute('data-eventoId'); // Captura o ID do evento
            const nomeEvento = this.closest('.event-card').querySelector('h3').textContent; // Captura o nome do evento
            localStorage.setItem('idEventoSelecionado', idEvento); // Salva o ID do evento
            localStorage.setItem('nomeEventoSelecionado', nomeEvento); // Salva o nome do evento
            window.location.href = 'cadastro-transporte.html';
        });
    });

    // Botão de Avaliar Evento
    document.querySelectorAll('.evaluateEvent-button').forEach(button => {
        button.addEventListener('click', function() {
            const idEvento = this.getAttribute('data-eventoId'); // Captura o ID do evento
            const nomeEvento = this.closest('.event-card').querySelector('h3').textContent; // Captura o nome do evento
            localStorage.setItem('idEventoSelecionado', idEvento); // Salva o ID do evento
            localStorage.setItem('nomeEventoSelecionado', nomeEvento); // Salva o nome do evento
            window.location.href = 'Avaliar-evento.html'; // Redireciona para a página de avaliação
        });
    });

    // Botão de Ver Avaliações
    document.querySelectorAll('.seeReviews-button').forEach(button => {
        button.addEventListener('click', function() {
            const idEvento = this.getAttribute('data-eventoId');
            const nomeEvento = this.closest('.event-card').querySelector('h3').textContent; // Captura o nome do evento
            localStorage.setItem('idEventoSelecionado', idEvento);  // Salva o ID do evento no localStorage
            localStorage.setItem('nomeEventoSelecionado', nomeEvento); // Salva o nome do evento
            window.location.href = 'lista-avaliacoes.html';  // Redireciona para a página de listagem de avaliações
        });
    });

    // Botão de Ver transportes
    document.querySelectorAll('.seeTransportes-button').forEach(button => {
        button.addEventListener('click', function() {
            const idEvento = this.getAttribute('data-eventoId');
            const nomeEvento = this.closest('.event-card').querySelector('h3').textContent; // Captura o nome do evento
            localStorage.setItem('idEventoSelecionado', idEvento);  // Salva o ID do evento no localStorage
            localStorage.setItem('nomeEventoSelecionado', nomeEvento); // Salva o nome do evento
            window.location.href = 'lista-transporte-novo.html';  // Redireciona para a página de listagem de transportes
        });
    });

    // Botão de Ver certificados
    document.querySelectorAll('.seeCertificados-button').forEach(button => {
        button.addEventListener('click', function() {
            const idEvento = this.getAttribute('data-eventoId');
            const nomeEvento = this.closest('.event-card').querySelector('h3').textContent; // Captura o nome do evento
            localStorage.setItem('idEventoSelecionado', idEvento);  // Salva o ID do evento no localStorage
            localStorage.setItem('nomeEventoSelecionado', nomeEvento); // Salva o nome do evento
            window.location.href = 'lista-certificado-novo.html';  // Redireciona para a página de listagem de transportes
        });
    });

    // Adiciona o evento de clique aos botões de editar
    document.querySelectorAll(".edit-button").forEach((button) => {
        button.addEventListener("click", function(event) {
            const eventoId = event.target.getAttribute("data-eventoId");
            toggleEditAll(eventoId); // Chama a função para alternar o modo de edição
        });
    });

    // Adiciona o evento de clique aos botões de deletar
    document.querySelectorAll(".delete-button").forEach((button) => {
        button.addEventListener("click", function(event) {
            const eventoId = event.target.getAttribute("data-eventoId");
            console.log(`ID do evento clicado: ${eventoId}`);
            deletarEvento(eventoId);
        });
    });

    // Adiciona evento de clique ao botão de cancelar edição
    document.querySelectorAll(".cancel-edit-button").forEach((button) => {
        button.addEventListener("click", function () {
            location.reload(); // Recarrega a página
        });
    });

}

// Chama a função para obter eventos e exibi-los na página
getEventos().then((eventos) => {
    exibirEventos(eventos);
});


// Função para alternar entre editar e exibir valores de todos os campos ao mesmo tempo
function toggleEditAll(id) {
    const fields = ['nome', 'descricao', 'data', 'local', 'tipo'];

    // Seleciona os botões relacionados ao evento
    const editButton = document.querySelector(`.edit-button[data-eventoId="${id}"]`);
    const deleteButton = document.querySelector(`.delete-button[data-eventoId="${id}"]`);
    const evaluateEventButton = document.querySelector(`.evaluateEvent-button[data-eventoId="${id}"]`);
    const seeReviewsButton = document.querySelector(`.seeReviews-button[data-eventoId="${id}"]`);
    const certificateButton = document.querySelector(`.certificate-button[data-eventoId="${id}"]`);
    const transportButton = document.querySelector(`.transport-button[data-eventoId="${id}"]`);

    const atualizarButton = document.getElementById(`atualizar-${id}`);
    const cancelEditButton = document.querySelector(`.cancel-edit-button[data-eventoId="${id}"]`);

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
        evaluateEventButton.style.display = "none"
        seeReviewsButton.style.display = "none"
        certificateButton.style.display = "none"
        transportButton.style.display = "none"
        atualizarButton.style.display = "inline";
        cancelEditButton.style.display = "inline";
    } else {
        // Exibe os botões de "Editar" e "Deletar", e oculta o botão de "Atualizar"
        editButton.style.display = "inline";
        deleteButton.style.display = "inline";
        evaluateEventButton.style.display = "inline"
        seeReviewsButton.style.display = "inline"
        certificateButton.style.display = "inline"
        transportButton.style.display = "inline"
        atualizarButton.style.display = "none";
        cancelEditButton.style.display = "none";
    }
}


// Função para atualizar todos os atributos do evento
async function atualizarEvento(id) {
    const eventoData = {
        id: id,
        nome: document.getElementById(`nome-${id}`).value.trim(),
        descricao: document.getElementById(`descricao-${id}`).value.trim(),
        data: document.getElementById(`data-${id}`).value.trim(),
        local: document.getElementById(`local-${id}`).value.trim(),
        tipo: document.getElementById(`tipo-${id}`).value.trim()
    };

    // Validação dos campos obrigatórios
    if (!eventoData.nome || !eventoData.descricao || !eventoData.data || !eventoData.local || !eventoData.tipo) {
        alert("Por favor, preencha todos os campos.");
        return;
    }

    try {
        // Realiza a atualização via PUT
        const response = await fetch(`http://localhost:8080/eventos`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": localStorage.getItem("token")
            },
            body: JSON.stringify(eventoData)
        });

        if (!response.ok) throw new Error("Erro ao atualizar evento.");

        alert("Evento atualizado com sucesso!");
        const eventoAtualizado = await getEventos();
        exibirEventos(eventoAtualizado); // Atualiza a lista de eventos após a atualização
    } catch (error) {
        console.error(error);
        alert("Erro ao atualizar evento.");
    }
}

// Função para deletar evento
async function deletarEvento(eventoId) {
    // Confirmação antes de deletar
    if (window.confirm("Tem certeza que deseja deletar este evento?")) {
        try {
            console.log(`Tentando deletar o evento com ID: ${eventoId}`);
            const response = await fetch(`http://localhost:8080/eventos/deletar/${eventoId}`, {
                    method: "DELETE",
                    headers: {
                        "Authorization": `Bearer ${localStorage.getItem("token")}`
                    }
                }
            );
            console.log("Resposta da requisição:", response);
            if (!response.ok) {
                throw new Error(`Erro ao deletar evento: ${response.status}`);
            }
            console.log(`Evento ${eventoId} deletado com sucesso.`);
            alert("Evento deletado com sucesso!");
            window.location.reload();
        } catch (error) {
            console.error(`Erro: ${error.message}`);
            alert("Erro ao deletar o evento. Tente novamente mais tarde.");
        }
    }
}