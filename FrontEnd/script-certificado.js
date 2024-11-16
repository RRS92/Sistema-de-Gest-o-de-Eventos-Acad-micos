//Função para criar eventos
document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("form-certificados");
    const saveButton = document.querySelector(".btn");

    saveButton.addEventListener("click", (e) => {
        e.preventDefault();  // Evita o comportamento padrão de recarregar a página
        
        // Captura os valores do formulário
        const cargaHoraria = document.getElementById("cargaHoraria").value;
        const descricao = document.getElementById("descricao").value;

        // Criação do objeto com os dados do evento
        const certificadoData = {
            cargaHoraria: cargaHoraria,
            descricao: descricao
        };

        // Envia os dados para a API
        fetch("http://localhost:8080/certificados", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(certificadoData)
        })
        .then(response => {
            if (response.ok) {
                alert("Certificado criado com sucesso!");
                window.location.reload(); // Recarrega a página
                return response.json();
            } else {
                throw new Error("Erro ao criar o certificado");
            }
        })
        .catch(error => {
            console.error("Erro:", error);
            alert("Erro ao criar o certificado.");
        });
    });
});

//Função para obter eventos
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

// Função para deletar certificado
async function deletarCertificado(certificadoId) {
    // Confirmação antes de deletar
    if (window.confirm("Tem certeza que deseja deletar este certificado?")) {
        try {
            console.log(`Tentando deletar o certificado com ID: ${certificadoId}`);
            const response = await fetch(`http://localhost:8080/certificados/${certificadoId}`, {
                method: 'DELETE',
            });
            console.log('Resposta da requisição:', response); // Log da resposta
            if (!response.ok) {
                throw new Error(`Erro ao deletar certificado: ${response.status}`);
            }
            console.log(`Certificado ${certificadoId} deletado com sucesso.`);
            // Recarrega a página após a exclusão bem-sucedida
            alert("Certificado deletado com sucesso!"); // Alerta de sucesso antes de recarregar
            window.location.reload(); // Recarrega a página para atualizar a lista de eventos
        } catch (error) {
            console.error(`Erro: ${error.message}`);
            alert("Erro ao deletar o certificado. Tente novamente mais tarde."); // Mensagem ao usuário
        }
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
		<p>Carga Horaria: ${certificado.cargaHoraria}</p>
                <p>Descrição: ${certificado.descricao}</p>
            </div>
            <button class="delete-button" data-id="${certificado.id}">Deletar certificado</button>
        `;
        eventsContainer.appendChild(eventCard);
    });

    // Adiciona o certificado de clique aos botões de deletar
    document.querySelectorAll('.delete-button').forEach(button => {
        button.addEventListener('click', function(event) {
            const certificadoId = event.target.getAttribute('data-id');
            console.log(`ID do certificado clicado: ${certificadoId}`);
            deletarEvento(certificadoId);
        });
    });
}

// Chama a função para obter certificados e exibi-los na página
getCertificados().then(certificados => {
    exibirCertificados(certificados); // Exibe os certificados na página
});
