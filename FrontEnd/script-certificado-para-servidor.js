
async function getCertificados() {
    try {
        const response = await fetch('http://localhost:8080/certificados'); // URL do seu backend
        if (!response.ok) {
            throw new Error(`Erro ao buscar certificados: ${response.status}`);
        }
        const certificados = await response.json(); // Parse do JSON retornado
        return certificados; // Retorna a lista de eventos
    } catch (error) {
        console.error(error); // Log de erros
        alert("Erro ao carregar eventos. Tente novamente mais tarde."); // Mensagem ao usuário
        return []; // Retorna um array vazio em caso de erro
    }
}


// Função para exibir certificados na página
function exibirCertificados(certificados) {
    const eventsContainer = document.querySelector('.events-container');
    eventsContainer.innerHTML = ''; // Limpa a lista existente

    if (certificados.length === 0) {
        eventsContainer.innerHTML = '<p>Nenhum certificado encontrado.</p>';
        return;
    }

    certificados.forEach(certificado => {
        const eventCard = document.createElement('div');
        eventCard.classList.add('event-card');
        eventCard.innerHTML = `
            <div class="event-details">
            <p><strong>Carga Horaria:</strong> <span id="cargaHoraria-display-${certificado.id}">${certificado.cargaHoraria}</span>
            <input type="text" id="cargaHoraria-${certificado.id}" value="${certificado.cargaHoraria}" style="display:none;" /></p>

            <p><strong>Descricao:</strong> <span id="descricao-display-${certificado.id}">${certificado.descricao}</span>
            <input type="text" id="descricao-${certificado.id}" value="${certificado.descricao}" style="display:none;" /></p>
            </div>
           `;
        eventsContainer.appendChild(eventCard);
    });

}

// Chama a função para obter certificados e exibi-los na página
getCertificados().then(certificados => {
    exibirCertificados(certificados); // Exibe os certificados na página
});


