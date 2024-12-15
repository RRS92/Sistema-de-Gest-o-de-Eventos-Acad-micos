//Função para criar certificados
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
                "Authorization": localStorage.getItem("token")
            },
            body: JSON.stringify(certificadoData)
        })
        .then(response => {
            if (response.ok) {
                alert("Certificado criado com sucesso!");
                window.location.href = "perfil-servidor.html";

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
        const response = await fetch('http://localhost:8080/certificados', 
            {
            method: "GET",
            headers: {
                'Accept': 'application/json',
                "Authorization": localStorage.getItem("token")
            }}); // URL do seu backend
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
            <p><strong>Carga Horária:</strong> <span id="cargaHoraria-display-${certificado.id}">${certificado.cargaHoraria}</span>
            <input type="text" id="cargaHoraria-${certificado.id}" value="${certificado.cargaHoraria}" style="display:none;" /></p>

            <p><strong>Descrição:</strong> <span id="descricao-display-${certificado.id}">${certificado.descricao}</span>
            <input type="text" id="descricao-${certificado.id}" value="${certificado.descricao}" style="display:none;" /></p>
            </div>
            <br>
           <button class="edit-button" data-certificadoId="${certificado.id}">Editar ✏️</button>  
            <button class="delete-button" data-certificadoId="${certificado.id}">Deletar 🗑️</button>
            <button class="update-button" id="atualizar-${certificado.id}" style="display:none;" onclick="atualizarCertificado(${certificado.id})">Atualizar ✏️</button>
        `;
        eventsContainer.appendChild(eventCard);
    });

    // Adiciona o evento de clique aos botões de editar
    document.querySelectorAll(".edit-button").forEach((button) => {
        button.addEventListener("click", function(event) {
            const certificadoId = event.target.getAttribute("data-certificadoId");
            toggleEditAll(certificadoId); // Chama a função para alternar o modo de edição
        });
    });

    // Adiciona o evento de clique aos botões de deletar
    document.querySelectorAll(".delete-button").forEach((button) => {
        button.addEventListener("click", function(event) {
            const certificadoId = event.target.getAttribute("data-certificadoId");
            console.log(`ID do certificado clicado: ${certificadoId}`);
            deletarCertificado(certificadoId);
        });
    });
}

// Chama a função para obter certificados e exibi-los na página
getCertificados().then(certificados => {
    exibirCertificados(certificados); // Exibe os certificados na página
});

// Função para alternar entre editar e exibir valores de todos os campos ao mesmo tempo
function toggleEditAll(id) {
    const fields = ['cargaHoraria', 'descricao'];

    // Seleciona os botões relacionados ao certificado
    const editButton = document.querySelector(`.edit-button[data-certificadoId="${id}"]`);
    const deleteButton = document.querySelector(`.delete-button[data-certificadoId="${id}"]`);
    const atualizarButton = document.getElementById(`atualizar-${id}`);

    // Alterna entre o modo de edição e visualização
    let isEditing = atualizarButton.style.display === "inline";

    fields.forEach(field => {
        const inputField = document.getElementById(`${field}-${id}`);
        const displayField = document.getElementById(`${field}-display-${id}`);

        if (!isEditing) {
            // Modo de edição: mostra inputs e oculta texto
            inputField.style.display = "inline";
            inputField.value = displayField.textContent; // Preenche o input com o valor atual
            displayField.style.display = "none";
        } else {
            // Modo de visualização: oculta inputs e mostra texto
            inputField.style.display = "none";
            displayField.style.display = "inline";
        }
    });

    if (!isEditing) {
        // Oculta os botões de "Editar" e "Deletar", e exibe o botão de "Atualizar"
        editButton.style.display = "none";
        deleteButton.style.display = "none";
        atualizarButton.style.display = "inline";
    } else {
        // Exibe os botões de "Editar" e "Deletar", e oculta o botão de "Atualizar"
        editButton.style.display = "inline";
        deleteButton.style.display = "inline";
        atualizarButton.style.display = "none";
    }
}

// Função para atualizar todos os atributos do certificado
async function atualizarCertificado(id) {
    const certificadoData = {
        id: id,
        cargaHoraria: document.getElementById(`cargaHoraria-${id}`).value.trim(),
        descricao: document.getElementById(`descricao-${id}`).value.trim()
    };

    // Validação dos campos obrigatórios
    if (!certificadoData.cargaHoraria || !certificadoData.descricao) {
        alert("Por favor, preencha todos os campos.");
        return;
    }

    try {
        // Realiza a atualização via PUT
        const response = await fetch(`http://localhost:8080/certificados`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": localStorage.getItem("token")
            },
            body: JSON.stringify(certificadoData)
        });

        if (!response.ok) throw new Error("Erro ao atualizar certificado.");

        alert("Certificado atualizado com sucesso!");
        const certificadoAtualizado = await getCertificados();
        exibirCertificados(certificadoAtualizado); // Atualiza a lista de certificados após a atualização
    } catch (error) {
        console.error(error);
        alert("Erro ao atualizar certificado.");
    }
}


// Função para deletar certificado
async function deletarCertificado(certificadoId) {
    // Confirmação antes de deletar
    if (window.confirm("Tem certeza que deseja deletar este certificado?")) {
        try {
            console.log(`Tentando deletar o certificado com ID: ${certificadoId}`);
            const response = await fetch(`http://localhost:8080/certificados/deletar/${certificadoId}`, {
                method: 'DELETE',
                headers: {
                    "Authorization": `Bearer ${localStorage.getItem("token")}`
                }
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
