// Função para obter eventos

const userId = localStorage.getItem('userIdUtilizador');

async function getEventos() {
    try {
        const response = await fetch('http://localhost:8080/eventos', 
            {
                method: "GET",
                headers: {
                    'Accept': 'application/json',
                    "Authorization": localStorage.getItem("token")
                }
            }); // URL do seu backend
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
                <button class="btnAvaliar" data-id="${evento.id}">Avaliar Evento</button>
                <button class="btnVerAvaliacoes" data-id="${evento.id}">Ver Avaliações</button>
                <button class="partipate-button" data-id="${evento.id}">Participar</button> 
            </div>
        `;
        eventsContainer.appendChild(eventCard);
    });

    // Botão de Avaliação
    document.querySelectorAll('.btnAvaliar').forEach(button => {
        button.addEventListener('click', function() {
            const idEvento = this.getAttribute('data-id');
            const nomeEvento = this.closest('.event-card').querySelector('h3').textContent;
            localStorage.setItem('idEventoSelecionado', idEvento);
            localStorage.setItem('nomeEventoSelecionado', nomeEvento);
            window.location.href = 'Avaliar-evento.html';
        });
    });

    // Botão de Ver Avaliações
    document.querySelectorAll('.btnVerAvaliacoes').forEach(button => {
        button.addEventListener('click', function() {
            const idEvento = this.getAttribute('data-id');
            const nomeEvento = this.closest('.event-card').querySelector('h3').textContent;
            localStorage.setItem('idEventoSelecionado', idEvento);
            localStorage.setItem('nomeEventoSelecionado', nomeEvento);
            window.location.href = 'lista-avaliacoes.html';
        });
    });

    // Botão de Participar
document.querySelectorAll('.partipate-button').forEach(button => {
    button.addEventListener('click', async function() {
        const idEvento = this.getAttribute('data-id');
        const eventoSelecionado = eventos.find(evento => evento.id === parseInt(idEvento));

        if (!eventoSelecionado) {
            alert('Erro: Evento não encontrado.');
            return;
        }

        try {
            const response = await fetch('http://localhost:8080/participantes', {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': localStorage.getItem("token")
                },
                body: JSON.stringify({
                    evento: eventoSelecionado, // Envia o evento completo
                    usuarioId: localStorage.getItem('userId') // ID do usuário
                })
            });
            if (!response.ok) {
                throw new Error(`Erro ao criar participante: ${response.status}`);
            }
            alert('Participação criada com sucesso!'); // Mensagem de sucesso
        } catch (error) {
            console.error(error);
            alert('Erro ao participar do evento. Tente novamente mais tarde.');
        }
    });
});
}

// Chama a função para obter eventos e exibi-los na página
getEventos().then(eventos => {
    exibirEventos(eventos);
});
