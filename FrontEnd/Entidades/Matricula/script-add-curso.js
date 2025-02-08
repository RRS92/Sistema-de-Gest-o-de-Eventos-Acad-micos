// Função para cadastrar Matricula
const IdUsuario = localStorage.getItem('userIdUsuario');

async function cadastrarMatricula() {
    try {
        
        // 3. Cadastra matricula
        const matriculaData = {
            numMatricula : document.getElementById("numMatricula").value,
            periodoIngresso : document.getElementById("periodoIngresso").value,
            turno : document.getElementById("turno").value,
            nomeCurso : document.getElementById("nomeCurso").value,
            modalidade : document.getElementById("modalidade").value,
            aluno: { id: IdUsuario}
        };

        const matriculaResponse = await fetch("http://localhost:8080/matriculas", {
            method: "POST",
            headers: { "Content-Type": "application/json" ,
                "Authorization": localStorage.getItem("token")},
            body: JSON.stringify(matriculaData)
        });

        if (!matriculaResponse.ok) throw new Error("Erro ao salvar matricula.");
        alert("Matricula cadastrada com sucesso!");

        // Redireciona para outra página
        window.location.href = "perfil-aluno.html";

    } catch (error) {
        console.error(error);
        alert("Erro ao cadastrar. Tente novamente.");
    }
}

// Verifica em qual página o script está sendo executado
document.addEventListener("DOMContentLoaded", () => {
    const submitButton = document.getElementById("submit-curso");

    // Se o botão de cadastro existir, estamos na página de cadastro
    if (submitButton) {
        submitButton.addEventListener("click", cadastrarMatricula);
    }

});