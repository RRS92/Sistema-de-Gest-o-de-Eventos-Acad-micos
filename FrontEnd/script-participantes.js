document.addEventListener("DOMContentLoaded", async () => {
    const eventId = localStorage.getItem("idEventoSelecionado");
    const eventName = localStorage.getItem("nomeEventoSelecionado");
    const participantsContainer = document.querySelector(".participants-container");
    const title = document.querySelector(".event-title");

    if (!eventId) {
        alert("Nenhum evento selecionado.");
        window.location.href = "lista-evento-para-servidor.html";
        return;
    }

    title.textContent = `Participantes do Evento: ${eventName}`;

    async function fetchParticipants() {
        try {
            const response = await fetch(`http://localhost:8080/eventos/${eventId}/participantes`, {
                method: "GET",
                headers: {
                    "Accept": "application/json",
                    "Authorization": localStorage.getItem("token")
                }
            });

            if (!response.ok) {
                throw new Error(`Erro ao buscar participantes: ${response.status}`);
            }

            const participantes = await response.json();
            displayParticipants(participantes);
        } catch (error) {
            console.error(error);
            alert("Erro ao carregar participantes. Tente novamente mais tarde.");
        }
    }

    function displayParticipants(participantes) {
        participantsContainer.innerHTML = "";

        if (participantes.length === 0) {
            participantsContainer.innerHTML = "<p>Nenhum participante encontrado.</p>";
            return;
        }

        participantes.forEach(participante => {
            const participantCard = document.createElement("div");
            participantCard.classList.add("participant-card");
            participantCard.innerHTML = `
                <p><strong>Nome:</strong> ${participante.nome}</p>
                <p><strong>Email:</strong> ${participante.email}</p>
            `;
            participantsContainer.appendChild(participantCard);
        });
    }

    fetchParticipants();
});
