document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("form-transport");
    const saveButton = document.querySelector(".btn");

    saveButton.addEventListener("click", () => {
        const idEvento = localStorage.getItem("idEventoSelecionado");
        if (!idEvento) {
            alert("Erro: idEventoSelecionado não encontrado no localStorage.");
            return;
        }

        // Captura os valores do formulário
        const transporteData = {
            idEvento: idEvento,
            categoria: document.getElementById("categoria").value,
            placa: document.getElementById("placa").value,
            quilometragem: document.getElementById("quilometragem").value,
            nomeMotorista: document.getElementById("nomeMotorista").value,
            horaSaida: document.getElementById("horaSaida").value,
            horaChegada: document.getElementById("horaChegada").value
        };

        // Envia os dados para a API
        fetch("http://localhost:8080/transportes", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": localStorage.getItem("token")
            },
            body: JSON.stringify(transporteData)
        })
        .then(response => {
            if (response.ok) {
                alert("Transporte cadastrado com sucesso!");
                window.location.href = "lista-evento.html";  
            } else {
                return response.json().then(errorData => {
                    throw new Error(errorData.message || "Erro ao cadastrar transporte.");
                });
            }
        })
        .catch(error => {
            console.error("Erro:", error);
            alert("Ocorreu um erro ao tentar salvar o transporte. Tente novamente.");
        });
    });
});
