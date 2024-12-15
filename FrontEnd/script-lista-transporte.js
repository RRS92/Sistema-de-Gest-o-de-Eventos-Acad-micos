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

        // Função para buscar as transportes do evento
        async function listarTransporte() {
            try {
                const response = await fetch(`http://localhost:8080/transportes/evento?eventoId=${idEvento}`, {
                    method: 'GET',
                    headers: {
                        "Authorization": `Bearer ${localStorage.getItem("token")}`,
                        "Content-Type": "application/json" // Adiciona cabeçalho para JSON se necessário
                    }
                });
                const transportes = await response.json();
        
                const transportesContainer = document.querySelector('.transportes-container');
                transportesContainer.innerHTML = ''; // Limpa a lista existente
        
                if (transportes.length === 0) {
                    transportesContainer.innerHTML = '<p>Nenhum transporte encontrado para este evento.</p>';
                    return;
                }
        
                // Exibe os transportes
                transportes.forEach(transporte => {
                    const transporteCard = document.createElement('div');
                    transporteCard.classList.add('transporte-card');
                    transporteCard.innerHTML = `
                    <div class="event-details">
                        <p><strong>Categoria:</strong> ${transporte.categoria}</p>
                        <p><strong>Placa:</strong> ${transporte.placa}</p>
                        <p><strong>Quilometragem:</strong> ${transporte.quilometragem}</p>
                        <p><strong>Nome do Motorista:</strong> ${transporte.nomeMotorista}</p>
                        <p><strong>Hora de Saída:</strong> ${transporte.horaSaida}</p>
                        <p><strong>Hora de Chegada:</strong> ${transporte.horaChegada}</p>
                    </div>
                    `;
                    transportesContainer.appendChild(transporteCard);
                });
            } catch (error) {
                alert('Erro ao carregar transportes.');
                console.error(error);
            }
        }
        
        // Chama a função para listar os transportes
        listarTransporte();
        