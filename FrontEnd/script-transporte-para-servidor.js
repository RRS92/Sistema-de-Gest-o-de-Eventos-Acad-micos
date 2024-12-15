
// Função para obter transportes
async function getTransportes() {
    try {
        const response = await fetch("http://localhost:8080/transportes", 
            {
            method: "GET",
            headers: {
                'Accept': 'application/json',
                "Authorization": localStorage.getItem("token")
            }});
        if (!response.ok) {
            throw new Error(`Erro ao buscar transportes: ${response.status}`);
        }
        const transportes = await response.json();
        return transportes;
    } catch (error) {
        console.error(error);
        alert("Erro ao carregar transportes. Tente novamente mais tarde.");
        return [];
    }
}

// Função para exibir transportes na página
function exibirTransportes(transportes) {
    const eventsContainer = document.querySelector(".events-container");
    eventsContainer.innerHTML = ""; // Limpa a lista existente

    if (transportes.length === 0) {
        eventsContainer.innerHTML = "<p>Nenhum transporte encontrado.</p>";
        return;
    }

    transportes.forEach((transporte) => {
        const eventCard = document.createElement("div");
        eventCard.classList.add("event-card");
        eventCard.innerHTML = `
            <div class="event-details">
                <p><strong>Categoria:</strong> <span id="categoria-display-${transporte.id}">${transporte.categoria}</span>
                <input type="text" id="categoria-${transporte.id}" value="${transporte.categoria}" style="display:none;" /></p>

                <p><strong>Placa:</strong> <span id="placa-display-${transporte.id}">${transporte.placa}</span>
                <input type="text" id="placa-${transporte.id}" value="${transporte.placa}" style="display:none;" /></p>

                <p><strong>Quilometragem:</strong> <span id="quilometragem-display-${transporte.id}">${transporte.quilometragem}</span>
                <input type="text" id="quilometragem-${transporte.id}" value="${transporte.quilometragem}" style="display:none;" /></p>

                <p><strong>Nome do Motorista:</strong> <span id="nomeMotorista-display-${transporte.id}">${transporte.nomeMotorista}</span>
                <input type="text" id="nomeMotorista-${transporte.id}" value="${transporte.nomeMotorista}" style="display:none;" /></p>

                <p><strong>Hora de Saída:</strong> <span id="horaSaida-display-${transporte.id}">${transporte.horaSaida}</span>
                <input type="text" id="horaSaida-${transporte.id}" value="${transporte.horaSaida}" style="display:none;" /></p>

                <p><strong>Hora de Chegada:</strong> <span id="horaChegada-display-${transporte.id}">${transporte.horaChegada}</span>
                <input type="text" id="horaChegada-${transporte.id}" value="${transporte.horaChegada}" style="display:none;" /></p>
            </div>
                 `;
        eventsContainer.appendChild(eventCard);
    });

}

// Chama a função para obter transportes e exibi-los na página
getTransportes().then((transportes) => {
    exibirTransportes(transportes);
});

