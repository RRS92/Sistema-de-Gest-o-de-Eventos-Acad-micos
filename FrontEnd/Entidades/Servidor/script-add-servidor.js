function verificar() {
    var token = localStorage.getItem("token");
    if (token == null) {
        window.location.href = "/index.html";
    }
}

verificar();


const userId = localStorage.getItem('userIdUtilizador');

// Função para cadastrar servidor
async function cadastrarServidor() {
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
            headers: { "Content-Type": "application/json" ,
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
            headers: { "Content-Type": "application/json",
                "Authorization": localStorage.getItem("token")
 },
            body: JSON.stringify(enderecoData)
        });

        if (!enderecoResponse.ok) throw new Error("Erro ao salvar endereço.");
        const endereco = await enderecoResponse.json();
        const enderecoId = endereco.id;

        // 3. Cadastra o servidor com os IDs de banco e endereço
        const servidorData = {
            nome: document.getElementById("nome-servidor").value,
            cpf: document.getElementById("cpf").value,
            rg: document.getElementById("rg").value,
            dataNasc: document.getElementById("dataNasc").value,
            telefone: document.getElementById("telefone").value,
            email: document.getElementById("email").value,
            siape: document.getElementById("siape").value,
            cargo: document.getElementById("cargo").value,
            banco: { id: bancoId },
            endereco: { id: enderecoId },
            utilizador: { id: userId }
        };

        const servidorResponse = await fetch("http://localhost:8080/servidores", {
            method: "POST",
            headers: { "Content-Type": "application/json" ,
                "Authorization": localStorage.getItem("token")
},
            body: JSON.stringify(servidorData)
        });

        if (!servidorResponse.ok) throw new Error("Erro ao salvar servidor.");
        const servidor = await servidorResponse.json();
        const servidorId = servidor.id;
       
        alert("Servidor cadastrado com sucesso!");
        localStorage.setItem('userIdUsuario', servidorId);

        // Redireciona para outra página
        window.location.href = "perfil-servidor.html";  
        

    } catch (error) {
        console.error(error);
        alert("Erro ao cadastrar. Tente novamente.");
    }
}
