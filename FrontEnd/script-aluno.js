function verificar() {
    var token = localStorage.getItem("token");
    if (token == null) {
        window.location.href = "index.html";
    }
}

verificar();


const userId = localStorage.getItem('userIdUtilizador');

// Função para cadastrar aluno
async function cadastrarAluno() {
    try {
        // 1. Cadastra o banco e obtém o ID
        const bancoData = {
            nomeBanco: document.getElementById("nomeBanco").value,
            numConta: document.getElementById("numConta").value,
            agencia: document.getElementById("agencia").value,
            operacao: document.getElementById("operacao").value
        };

        const bancoResponse = await fetch("http://localhost:8080/bancos", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": localStorage.getItem("token")
            },
            body: JSON.stringify(bancoData)
        });

        if (!bancoResponse.ok) throw new Error("Erro ao salvar banco.");
        const banco = await bancoResponse.json();
        const bancoId = banco.id;

        // 2. Cadastra o endereço e obtém o ID
        const enderecoData = {
            rua: document.getElementById("rua").value,
            numero: document.getElementById("numero").value,
            bairro: document.getElementById("bairro").value,
            cidade: document.getElementById("cidade").value,
            estado: document.getElementById("estado").value,
            cep: document.getElementById("cep").value,
            complemento: document.getElementById("complemento").value
        };

        const enderecoResponse = await fetch("http://localhost:8080/enderecos", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": localStorage.getItem("token")
            },
            body: JSON.stringify(enderecoData)
        });

        if (!enderecoResponse.ok) throw new Error("Erro ao salvar endereço.");
        const endereco = await enderecoResponse.json();
        const enderecoId = endereco.id;

        // 3. Cadastra o aluno com os IDs de banco e endereço
        const alunoData = {
            matricula: document.getElementById("matricula").value,
            nome: document.getElementById("nome-aluno").value,
            cpf: document.getElementById("cpf").value,
            rg: document.getElementById("rg").value,
            dataNasc: document.getElementById("dataNasc").value,
            telefone: document.getElementById("telefone").value,
            email: document.getElementById("email").value,
            banco: { id: bancoId },
            endereco: { id: enderecoId },
            utilizador: { id: userId }

        };

        const alunoResponse = await fetch("http://localhost:8080/alunos", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": localStorage.getItem("token")
            },
            body: JSON.stringify(alunoData)
        });

        if (!alunoResponse.ok) throw new Error("Erro ao salvar aluno.");
        const aluno = await alunoResponse.json();
        const alunoId = aluno.id;


        const matriculaData = {
            periodoIngresso : document.getElementById("periodoIngresso").value,
            turno : document.getElementById("turno").value,
            nomeCurso : document.getElementById("nomeCurso").value,
            modalidade : document.getElementById("modalidade").value,
            aluno: { id: alunoId}
        };

        const matriculaResponse = await fetch("http://localhost:8080/matriculas", {
            method: "POST",
            headers: { "Content-Type": "application/json" ,
                "Authorization": localStorage.getItem("token")},
            body: JSON.stringify(matriculaData)
        });

        if (!matriculaResponse.ok) throw new Error("Erro ao salvar matricula.");
        alert("Aluno cadastrado com sucesso!");

        localStorage.setItem('userIdUsuario', alunoId);
        // Redireciona para outra página
        window.location.href = "perfil-aluno.html";

    } catch (error) {
        console.error(error);
        alert("Erro ao cadastrar. Tente novamente.");
    }
}

// Função para obter alunos
async function getAlunos() {
    try {
        const idUser = localStorage.getItem("userIdUsuario");

        const response = await fetch(`http://localhost:8080/alunos/${idUser}`,

            {
                method: "GET",
                headers: {
                    'Accept': 'application/json',
                    "Authorization": localStorage.getItem("token")

                }
            });
        if (!response.ok) {
            throw new Error(`Erro ao buscar alunos: ${response.status}`);
        }
        const alunos = await response.json();
        return alunos;
    } catch (error) {
        console.error(error);
        alert("Erro ao carregar alunos. Tente novamente mais tarde.");
        return null;
    }
}

function formatarData(data) {
    return new Intl.DateTimeFormat('pt-BR', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
    }).format(new Date(data));
}

function formatarCPF(cpf) {
    // Remove qualquer caractere que não seja número
    cpf = cpf.replace(/[^\d]/g, '');

    // Aplica o formato XXX.XXX.XXX-XX
    if (cpf.length === 11) {
        return cpf.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
    }

    return cpf; // Retorna o CPF sem formatação se não tiver o tamanho correto
}

function formatarTelefone(telefone) {
    // Remove qualquer caractere que não seja número
    telefone = telefone.replace(/[^\d]/g, '');

    // Aplica o formato (XX) XXXXX-XXXX
    if (telefone.length === 11) {
        return telefone.replace(/(\d{2})(\d{5})(\d{4})/, '($1) $2-$3');
    }

    return telefone; // Retorna o telefone sem formatação se não tiver o tamanho correto
}

// Função para exibir alunos na página
function exibirAlunos(aluno) {


    
    const eventsContainer = document.querySelector(".events-container");
    eventsContainer.innerHTML = ""; // Limpa a lista existente

    if (!aluno) {
        eventsContainer.innerHTML = "<p>Nenhum aluno encontrado.</p>";
        return;
    }

    const eventCard = document.createElement("div");
    eventCard.classList.add("event-card");
    eventCard.innerHTML = `
           <button class="menu-button">&#8942;</button>
            <div class="menu">
                <ul>
                    <li><a href="lista-banco-aluno.html">Info Bancárias</a></li>
                    <li><a href="lista-endereco-aluno.html">Info Residenciais</a></li>
                    <li><a href="lista-matricula.html">Info Acadêmicas</a></li>
                </ul>
            </div>

            <div class="event-details">


                <p><strong>Nome:</strong> <span id="nome-display-${aluno.id}">${aluno.nome}</span>
                <input type="text" id="nome-${aluno.id}" value="${aluno.nome}" style="display:none;" /></p>

                
                <p><strong>CPF:</strong> <span id="cpf-display-${aluno.id}">${formatarCPF(aluno.cpf)}</span>
                <input type="text" id="cpf-${aluno.id}" value="${aluno.cpf}" style="display:none;" /></p>


                <p><strong>RG:</strong> <span id="rg-display-${aluno.id}">${aluno.rg}</span>
                <input type="text" id="rg-${aluno.id}" value="${aluno.rg}" style="display:none;" /></p>

                <p><strong>Data de Nascimento:</strong> 
                <span id="dataNasc-display-${aluno.id}">${aluno.dataNasc}</span>
                <input type="date" id="dataNasc-${aluno.id}" value="${aluno.dataNasc}" style="display:none;" /></p>
    
                <p><strong>Telefone:</strong> <span id="telefone-display-${aluno.id}">${formatarTelefone(aluno.telefone)}</span>
                <input type="text" id="telefone-${aluno.id}" value="${aluno.telefone}" style="display:none;" /></p>

                <p><strong>Email:</strong> <span id="email-display-${aluno.id}">${aluno.email}</span>
                <input type="text" id="email-${aluno.id}" value="${aluno.email}" style="display:none;" /></p>

                <p><strong>matricula:</strong> <span id="matricula-display-${aluno.id}">${aluno.matricula}</span>
                <input type="text" id="matricula-${aluno.id}" value="${aluno.matricula}" style="display:none;" /></p>
            </div>
            <br>
            <button class="edit-center-button" data-alunoId="${aluno.id}">Editar ✏️</button>
            <button class="update-button" id="atualizar-${aluno.id}" style="display:none;" onclick="atualizarAluno(${aluno.id})">Atualizar ✏️</button>
            <button class="cancel-edit-button" style="display:none;" data-alunoId="${aluno.id}">Cancelar ✖️</button>
        `;
    eventsContainer.appendChild(eventCard);
    ;

    // Adiciona o evento de clique aos botões de editar
    document.querySelectorAll(".edit-center-button").forEach((button) => {
        button.addEventListener("click", function (event) {
            const alunoId = event.target.getAttribute("data-alunoId");
            toggleEditAll(alunoId); // Chama a função para alternar o modo de edição
        });
    });

    // Adiciona evento de clique ao botão de cancelar edição
    document.querySelectorAll(".cancel-edit-button").forEach((button) => {
        button.addEventListener("click", function () {
            location.reload(); // Recarrega a página
        });
    });

    // Adiciona a funcionalidade do botão de menu
    document.querySelectorAll('.menu-button').forEach((button, index) => {
        button.addEventListener('click', function(event) {
            event.stopPropagation(); // Impede que o clique se propague para o documento
            const menu = button.nextElementSibling; // O menu é o próximo irmão do botão
            menu.style.display = menu.style.display === 'block' ? 'none' : 'block';
        });
    });

    // Fechar o menu ao clicar fora dele
    document.addEventListener('click', function(event) {
        document.querySelectorAll('.menu').forEach(menu => {
            if (!menu.contains(event.target)) {
                menu.style.display = 'none';
            }
        });
    });
}

document.addEventListener("DOMContentLoaded", function () {
    if (!window.location.pathname.includes("cadastro-aluno.html")) {
        getAlunos().then((alunos) => {
            exibirAlunos(alunos);
        }).catch(error => console.error("Erro ao exibir alunos:", error));
    }
});


// Função para alternar entre editar e exibir valores de todos os campos ao mesmo tempo
function toggleEditAll(id) {
    const fields = ['nome', 'matricula', 'dataNasc', 'telefone', 'email'];

    // Seleciona os botões relacionados ao aluno
    const editButton = document.querySelector(`.edit-center-button[data-alunoId="${id}"]`);
    const atualizarButton = document.getElementById(`atualizar-${id}`);
    const cancelEditButton = document.querySelector(`.cancel-edit-button[data-alunoId="${id}"]`);


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
        atualizarButton.style.display = "inline";
        cancelEditButton.style.display = "inline";
    } else {
        // Exibe os botões de "Editar" e "Deletar", e oculta o botão de "Atualizar"
        editButton.style.display = "inline";
        atualizarButton.style.display = "none";
        cancelEditButton.style.display = "none";
    }
}


// Função para atualizar todos os atributos do aluno
async function atualizarAluno(id) {
    const alunoData = {
        id: id,
        matricula: document.getElementById(`matricula-${id}`).value.trim(),
        nome: document.getElementById(`nome-${id}`).value.trim(),
        cpf: document.getElementById(`cpf-${id}`).value.trim(),
        rg: document.getElementById(`rg-${id}`).value.trim(),
        dataNasc: document.getElementById(`dataNasc-${id}`).value.trim(),
        telefone: document.getElementById(`telefone-${id}`).value.trim(),
        email: document.getElementById(`email-${id}`).value.trim()
    };

    // Validação dos campos obrigatórios
    if (!alunoData.matricula || !alunoData.nome ||  !alunoData.cpf || !alunoData.rg || !alunoData.dataNasc || !alunoData.telefone || !alunoData.email) {
        alert("Por favor, preencha todos os campos.");
        return;
    }

    try {
        // Realiza a atualização via PUT
        const response = await fetch(`http://localhost:8080/alunos`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": localStorage.getItem("token")
            },
            body: JSON.stringify(alunoData)
        });

        if (!response.ok) throw new Error("Erro ao atualizar aluno.");

        alert("Aluno atualizado com sucesso!");
        const alunosAtualizados = await getAlunos();
        exibirAlunos(alunosAtualizados); // Atualiza a lista de alunos após a atualização
    } catch (error) {
        console.error(error);
        alert("Erro ao atualizar aluno.");
    }
}


async function deletarAluno(alunoId) {
    // Confirmação antes de deletar
    if (window.confirm("Tem certeza que deseja deletar este aluno?")) {
        try {
            console.log(`Tentando deletar o aluno com ID: ${alunoId}`);
            const response = await fetch(`http://localhost:8080/alunos/deletar/${alunoId}`, {
                method: "DELETE",
            });

            console.log("Resposta da requisição:", response);
            if (!response.ok) {
                throw new Error(`Erro ao deletar aluno: ${response.status}`);
            }
            console.log(`Aluno ${alunoId} deletado com sucesso.`);
            alert("Aluno deletado com sucesso!");
            window.location.reload();
        } catch (error) {
            console.error(`Erro: ${error.message}`);
            alert("Erro ao deletar o aluno. Tente novamente mais tarde.");
        }
    }
}

// Verifica em qual página o script está sendo executado
document.addEventListener("DOMContentLoaded", () => {
    const submitButton = document.getElementById("submit-aluno");
    const alunosContainer = document.getElementById("alunos-container");

    // Se o botão de cadastro existir, estamos na página de cadastro
    if (submitButton) {
        submitButton.addEventListener("click", cadastrarAluno);
    }

    // Se o container de alunos existir, estamos na página de listagem
    if (alunosContainer) {
        exibirAlunos(alunoss);
    }
});