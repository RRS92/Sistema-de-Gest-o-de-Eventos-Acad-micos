// Recupera o ID do evento salvo no localStorage
const idEvento = localStorage.getItem('idEventoSelecionado');

// Preenche automaticamente o campo ID do evento (agora oculto)
if (idEvento) {
    document.getElementById('idEvento').value = idEvento;

    // Recupera o nome do evento (você pode armazená-lo no localStorage também)
    const nomeEvento = localStorage.getItem('nomeEventoSelecionado');
    
    // Preenche o nome do evento para exibição
    if (nomeEvento) {
        document.getElementById('nomeEvento').innerText = nomeEvento;
    }
}

// Lógica para envio do formulário
document.getElementById('formTransporte').addEventListener('submit', async function(event) {
    event.preventDefault();
    const dados = {
        idEvento: document.getElementById('idEvento').value,
        cargaHoraria: document.getElementById('cargaHoraria').value,
        descricao: document.getElementById('descricao').value
    };

    try {
        const response = await fetch('http://localhost:8080/certificados', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json',
                "Authorization": localStorage.getItem("token") },
            body: JSON.stringify(dados)
        });

        if (response.ok) {
            alert('Certificado criado com sucesso!');
            window.location.href = '../Evento/lista-evento-para-servidor.html'; // Redirecionamento após sucesso
        } else {
            alert('Erro ao criar certificado.');
        }
    } catch (error) {
        alert('Erro de conexão.');
        console.error(error);
    }
});
