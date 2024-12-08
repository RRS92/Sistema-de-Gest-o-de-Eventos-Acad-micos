//Função para criar eventos
document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("form-events");
    const saveButton = document.querySelector(".btn");

    saveButton.addEventListener("click", (e) => {
        e.preventDefault();  // Evita o comportamento padrão de recarregar a página
        
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
            },
            body: JSON.stringify(eventoData)
        })
        .then(response => {
            if (response.ok) {
                alert("Evento criado com sucesso!");
                window.location.href = "perfil-servidor.html";
            } else {
                throw new Error("Erro ao criar o evento");
            }
        })
        .catch(error => {
            console.error("Erro:", error);
            alert("Erro ao criar o evento.");
        });
    });
});


//Função para obter eventos
async function getEventos() {
    try {
        const response = await fetch('http://localhost:8080/eventos'); // URL do seu backend
        if (!response.ok) {
            throw new Error(`Erro ao buscar eventos: ${response.status}`);
        }
        const eventos = await response.json(); // Parse do JSON retornado
        return eventos; // Retorna a lista de eventos
    } catch (error) {
        console.error(error); // Log de erros
        alert("Erro ao carregar eventos. Tente novamente mais tarde."); // Mensagem ao usuário
        return []; // Retorna um array vazio em caso de erro
    }
}


// Função para exibir eventos na página
function exibirEventos(eventos) {
    const eventsContainer = document.querySelector('.events-container');
    eventsContainer.innerHTML = ''; // Limpa a lista existente

    if (eventos.length === 0) {
        eventsContainer.innerHTML = '<p>Nenhum evento encontrado.</p>';
        return;
    }

    eventos.forEach(evento => {
        const eventCard = document.createElement('div');
        eventCard.classList.add('event-card');
        eventCard.innerHTML = `
            <h3>${evento.nome}</h3>
            <div class="event-details">
                <p>Descrição: ${evento.descricao}</p>
                <p>Data: ${evento.data}</p>
                <p>Local: ${evento.local}</p>
                <p>Tipo: ${evento.tipo}</p>
            </div>
            <div class="buttons-container">
                <div class="top-buttons">
                    <button class="btnCertificado" data-id="${evento.id}">Criar Certificado</button>
                    <button class="btnTransporte" data-id="${evento.id}">Cadastrar Transporte</button>
                </div>
                <button class="btnAvaliar" data-id="${evento.id}">Avaliar Evento</button>
                <button class="btnVerAvaliacoes" data-id="${evento.id}">Ver Avaliações</button>
                <button class="delete-button" data-id="${evento.id}">Deletar Evento</button>
            </div>
        `;
        eventsContainer.appendChild(eventCard);
    });

    // Botão de Certificado
    document.querySelectorAll('.btnCertificado').forEach(button => {
        button.addEventListener('click', function() { 
            const idEvento = this.getAttribute('data-id'); // Captura o ID do evento
            const nomeEvento = this.closest('.event-card').querySelector('h3').textContent; // Captura o nome do evento
            localStorage.setItem('idEventoSelecionado', idEvento); // Salva o ID do evento
            localStorage.setItem('nomeEventoSelecionado', nomeEvento); // Salva o nome do evento
            window.location.href = 'cadastro-certificado.html';
        });
    });

    // Botão de Transporte
    document.querySelectorAll('.btnTransporte').forEach(button => {
        button.addEventListener('click', function() {
            const idEvento = this.getAttribute('data-id'); // Captura o ID do evento
            const nomeEvento = this.closest('.event-card').querySelector('h3').textContent; // Captura o nome do evento
            localStorage.setItem('idEventoSelecionado', idEvento); // Salva o ID do evento
            localStorage.setItem('nomeEventoSelecionado', nomeEvento); // Salva o nome do evento
            window.location.href = 'cadastro-transporte.html';
        });
    });

    // Botão de Avaliação
    document.querySelectorAll('.btnAvaliar').forEach(button => {
        button.addEventListener('click', function() {
            const idEvento = this.getAttribute('data-id'); // Captura o ID do evento
            const nomeEvento = this.closest('.event-card').querySelector('h3').textContent; // Captura o nome do evento
            localStorage.setItem('idEventoSelecionado', idEvento); // Salva o ID do evento
            localStorage.setItem('nomeEventoSelecionado', nomeEvento); // Salva o nome do evento
            window.location.href = 'Avaliar-evento.html'; // Redireciona para a página de avaliação
        });
    });

    // Botão de Ver Avaliações
    document.querySelectorAll('.btnVerAvaliacoes').forEach(button => {
        button.addEventListener('click', function() {
            const idEvento = this.getAttribute('data-id');
            const nomeEvento = this.closest('.event-card').querySelector('h3').textContent; // Captura o nome do evento
            localStorage.setItem('idEventoSelecionado', idEvento);  // Salva o ID do evento no localStorage
            localStorage.setItem('nomeEventoSelecionado', nomeEvento); // Salva o nome do evento
            window.location.href = 'lista-avaliacoes.html';  // Redireciona para a página de listagem de avaliações
        });
    });

        // Adiciona o evento de clique aos botões de deletar
        document.querySelectorAll('.delete-button').forEach(button => {
            button.addEventListener('click', function(event) {
                const eventoId = event.target.getAttribute('data-id');
                console.log(`ID do evento clicado: ${eventoId}`);
                deletarEvento(eventoId);
            });
        });
}


// Chama a função para obter eventos e exibi-los na página
getEventos().then(eventos => {
    exibirEventos(eventos); // Exibe os eventos na página
});


// Função para deletar evento
async function deletarEvento(eventoId) {
    // Confirmação antes de deletar
    if (window.confirm("Tem certeza que deseja deletar este evento?")) {
        try {
            console.log(`Tentando deletar o evento com ID: ${eventoId}`);
            const response = await fetch(`http://localhost:8080/eventos/deletar/${eventoId}`, {
                method: 'DELETE',
            });
            console.log('Resposta da requisição:', response); // Log da resposta
            if (!response.ok) {
                throw new Error(`Erro ao deletar evento: ${response.status}`);
            }
            console.log(`Evento ${eventoId} deletado com sucesso.`);
            // Recarrega a página após a exclusão bem-sucedida
            alert("Evento deletado com sucesso!"); // Alerta de sucesso antes de recarregar
            window.location.reload(); // Recarrega a página para atualizar a lista de eventos
        } catch (error) {
            console.error(`Erro: ${error.message}`);
            alert("Erro ao deletar o evento. Tente novamente mais tarde."); // Mensagem ao usuário
        }
    }
}