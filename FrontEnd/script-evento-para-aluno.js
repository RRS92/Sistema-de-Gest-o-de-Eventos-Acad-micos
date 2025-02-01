// Obtém o ID do usuário do localStorage
const IdUsuario = localStorage.getItem('userIdUsuario');

// Função para obter eventos do backend
async function getEventos() {
    try {
        const response = await fetch('http://localhost:8080/eventos', {
            method: "GET",
            headers: {
                'Accept': 'application/json',
                "Authorization": localStorage.getItem("token")
            }
        });
        if (!response.ok) {
            throw new Error(`Erro ao buscar eventos: ${response.status}`);
        }
        return await response.json(); // Retorna os eventos como JSON
    } catch (error) {
        console.error(error);
        alert("Erro ao carregar eventos. Tente novamente mais tarde.");
        return []; // Retorna um array vazio em caso de erro
    }
}

// Função para obter os participantes do backend
async function verificarParticipacao() {
    try {
        const response = await fetch('http://localhost:8080/participantes', {
            method: "GET",
            headers: {
                'Accept': 'application/json',
                "Authorization": localStorage.getItem("token")
            }
        });
        if (!response.ok) {
            throw new Error(`Erro ao buscar participantes: ${response.status}`);
        }
        return await response.json(); // Retorna os participantes como JSON
    } catch (error) {
        console.error(error);
        alert("Erro ao verificar participação. Tente novamente mais tarde.");
        return []; // Retorna um array vazio em caso de erro
    }
}
function formatarData(data) {
    return new Intl.DateTimeFormat('pt-BR', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
    }).format(new Date(data));
}

// Função para exibir os eventos na página
async function exibirEventos(eventos) {
    const eventsContainer = document.querySelector('.events-container');
    eventsContainer.innerHTML = ''; // Limpa o conteúdo existente

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
                <p>Data: ${formatarData(evento.data)}</p>
                <p>Local: ${evento.local}</p>
                <p>Tipo: ${evento.tipo}</p>
            </div>
            <div class="buttons-container">
                <button class="partipate-button" data-id="${evento.id}">Participar</button>
            </div>
        `;
        eventsContainer.appendChild(eventCard);
    });

    // Atualiza os botões de participação
    await atualizarBotoesParticipacao();
}

// Função para atualizar os botões de participação/desinscrição
async function atualizarBotoesParticipacao() {
    const participantes = await verificarParticipacao();
    const userId = parseInt(IdUsuario);

    // Filtra as participações do usuário
    const participacoesUsuario = participantes.filter(part => part.aluno?.id === userId);

    // Atualiza o texto e estilo dos botões com base nas participações
    const buttons = document.querySelectorAll('.partipate-button');
    buttons.forEach(button => {
        const eventoId = parseInt(button.getAttribute('data-id'));
        const participacao = participacoesUsuario.find(part => part.evento?.id === eventoId);

        if (participacao) {
            button.textContent = "Cancelar Inscrição";
            button.classList.add('desinscrever');
        } else {
            button.textContent = "Participar";
            button.classList.remove('desinscrever');
        }
    });
}

// Gerencia os cliques nos botões de participar/desinscrever
document.querySelector('.events-container').addEventListener('click', async (event) => {
    const button = event.target.closest('.partipate-button');
    if (!button) return;

    const eventoId = parseInt(button.getAttribute('data-id'));
    const userId = parseInt(IdUsuario);
    const participantes = await verificarParticipacao();

    const participacao = participantes.find(part => part.aluno?.id === userId && part.evento?.id === eventoId);

    if (button.classList.contains('desinscrever')) {
        // Desinscrição do evento
        try {
            const response = await fetch(`http://localhost:8080/participantes/deletar/${participacao.id}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': localStorage.getItem("token")
                }
            });
            if (!response.ok) throw new Error(`Erro ao cancelar inscrição: ${response.status}`);
            alert('Você cancelou sua inscrição com sucesso!');
            button.textContent = "Participar";
            button.classList.remove('desinscrever');
        } catch (error) {
            console.error(error);
            alert("Erro ao desinscrever. Tente novamente mais tarde.");
        }
    } else {
        // Inscrição no evento
        try {
            const response = await fetch('http://localhost:8080/participantes', {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': localStorage.getItem("token")
                },
                body: JSON.stringify({
                    evento: { id: eventoId },
                    aluno: { id: userId }
                })
            });
            if (!response.ok) throw new Error(`Erro ao participar do evento: ${response.status}`);
            alert('Você se inscreveu no evento com sucesso!');
            button.textContent = "Cancelar Inscrição";
            button.classList.add('desinscrever');
        } catch (error) {
            console.error(error);
            alert("Erro ao participar do evento. Tente novamente mais tarde.");
        }
    }
});

// Inicialização da página
async function inicializar() {
    const eventos = await getEventos();
    await exibirEventos(eventos);
}

// Chama a função de inicialização ao carregar a página
inicializar();
