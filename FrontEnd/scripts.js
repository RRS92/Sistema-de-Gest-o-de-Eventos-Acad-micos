document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("form-events");
    const saveButton = document.querySelector(".btn");

    saveButton.addEventListener("click", (e) => {
        e.preventDefault();  // Evita o comportamento padrão de recarregar a página
        
        // Captura os valores do formulário
        const nome = document.getElementById("nome").value;
        const descricao = document.getElementById("descricao").value;
        const data = document.getElementById("data").value;
        const local = document.getElementById("local").value;
        const tipo = document.getElementById("tipo").value;

        // Criação do objeto com os dados do evento
        const eventoData = {
            nome: nome,
            descricao: descricao,
            data: data,
            local: local,
            tipo: tipo
        };

        // Envia os dados para a API
        fetch("http://localhost:8080/eventos", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(eventoData)
        })
        .then(response => {
            if (response.ok) {
                alert("Evento criado com sucesso!");
                window.location.reload(); // Recarrega a página
                return response.json();
            } else {
                throw new Error("Erro ao criar o evento");
            }
        })
        .catch(error => {
            console.error("Erro:", error);
            alert("Erro ao criar o evento.");
        });
    });
});
