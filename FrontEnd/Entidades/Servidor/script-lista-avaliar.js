        // Recupera o ID do evento selecionado do localStorage
        const idEvento = localStorage.getItem('idEventoSelecionado');

        if (idEvento) {
            document.getElementById('idEvento').value = idEvento;

            // Recupera o nome do evento (você pode armazená-lo no localStorage também)
            const nomeEvento = localStorage.getItem('nomeEventoSelecionado');
            
            // Preenche o nome do evento para exibição
            if (nomeEvento) {
                document.getElementById('nomeEvento').innerText = nomeEvento;
            }
        }

        // Função para buscar as avaliações do evento
        async function listarAvaliacoes() {
            try {
                const response = await fetch(`http://localhost:8080/avaliacoes/evento?eventoId=${idEvento}`, {
                    method: 'GET',
                    headers: {
                        "Authorization": `Bearer ${localStorage.getItem("token")}`,
                        "Content-Type": "application/json" // Adiciona cabeçalho para JSON se necessário
                    }
                });
                const avaliacoes = await response.json();
        
                const avaliacoesContainer = document.querySelector('.avaliacoes-container');
                avaliacoesContainer.innerHTML = ''; // Limpa a lista existente
        
                if (avaliacoes.length === 0) {
                    avaliacoesContainer.innerHTML = '<p>Nenhuma avaliação encontrada para este evento.</p>';
                    return;
                }
        
                // Exibe as avaliações
                avaliacoes.forEach(avaliacao => {
                    const avaliacaoCard = document.createElement('div');
                    avaliacaoCard.classList.add('avaliacao-card');
                    avaliacaoCard.innerHTML = `
                    <div class="event-details">
                        <p><strong>Nota:</strong> ${avaliacao.nota}</p>
                        <p><strong>Comentário:</strong> ${avaliacao.comentario}</p>
                        <p><strong>Participante:</strong> ${avaliacao.idParticipante}</p>
                    </div>
                    `;
                    avaliacoesContainer.appendChild(avaliacaoCard);
                });
            } catch (error) {
                alert('Erro ao carregar avaliações.');
                console.error(error);
            }
        }
        
        // Chama a função para listar as avaliações
        listarAvaliacoes();
        