// Função para obter eventos
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
                <button class="btnAvaliar" data-id="${evento.id}">Avaliar Evento</button>
                <button class="btnVerAvaliacoes" data-id="${evento.id}">Ver Avaliações</button>
            </div>
        `;
        eventsContainer.appendChild(eventCard);
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
}

// Chama a função para obter eventos e exibi-los na página
getEventos().then(eventos => {
    exibirEventos(eventos); // Exibe os eventos na página
});
