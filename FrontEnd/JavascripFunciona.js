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
        `;
        eventsContainer.appendChild(eventCard);
    });
}

// Chamada da função e manipulação da resposta
getEventos().then(eventos => {
    exibirEventos(eventos); // Exibe os eventos na página
});
