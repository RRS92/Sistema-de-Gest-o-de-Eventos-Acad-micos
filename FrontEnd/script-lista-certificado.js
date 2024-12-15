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

// Função para buscar as certificados do evento
async function listarCertificado() {
    try {
        const response = await fetch(`http://localhost:8080/certificados/evento?eventoId=${idEvento}`, {
            method: 'GET',
            headers: {
                "Authorization": `Bearer ${localStorage.getItem("token")}`,
                "Content-Type": "application/json" // Adiciona cabeçalho para JSON se necessário
            }
        });
        const certificados = await response.json();

        const certificadosContainer = document.querySelector('.certificados-container');
        certificadosContainer.innerHTML = ''; // Limpa a lista existente

        if (certificados.length === 0) {
            certificadosContainer.innerHTML = '<p>Nenhum certificado encontrado para este evento.</p>';
            return;
        }

        // Exibe os certificados
        certificados.forEach(certificado => {
            const certificadoCard = document.createElement('div');
            certificadoCard.classList.add('certificado-card');
            certificadoCard.innerHTML = `
            <div class="event-details">
                <p><strong>Carga Horaria:</strong> ${certificado.cargaHoraria}</p>
                <p><strong>Descrição:</strong> ${certificado.descricao}</p>
            </div>
            `;
            certificadosContainer.appendChild(certificadoCard);
        });
    } catch (error) {
        alert('Erro ao carregar certificados.');
        console.error(error);
    }
}

// Chama a função para listar os transportes
listarCertificado();
