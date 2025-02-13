# **Sistema de Gestão de Eventos Acadêmicos**  

O **Sistema de Gestão de Eventos Acadêmicos** tem como principal objetivo facilitar a seleção e participação de alunos e servidores em eventos acadêmicos, como projetos de pesquisa e extensão, viagens pedagógicas, entre outras. Além disso, o sistema contará com perfis para cada usuário, permitindo um melhor gerenciamento de suas informações.  

## **Garantia de Qualidade com Testes Unitários**  

Para assegurar a confiabilidade e a integridade do sistema, foram implementados **testes unitários** abrangentes. Esses testes têm o papel de validar o comportamento esperado de cada funcionalidade, garantindo que o sistema opere corretamente e evitando regressões em futuras atualizações.  

Os testes unitários foram desenvolvidos utilizando **JUnit** (para a camada de backend com Spring Boot) e outras ferramentas apropriadas para cada tecnologia utilizada no projeto. Eles cobrem aspectos essenciais como:

- **Autenticação e autorização**: Validação de login, controle de acesso e permissões.  
- **Cadastro e gerenciamento de eventos**: Verificação da criação, edição e exclusão de eventos acadêmicos.  
- **Gerenciamento de usuários**: Testes para criação, atualização e exclusão de perfis de alunos e servidores.  
- **Regras de negócio**: Garantia de que todas as validações, como prazos de inscrição e requisitos de participação, sejam respeitadas.  
- **Integração com o banco de dados**: Testes para assegurar que as operações com o banco de dados sejam realizadas corretamente.  

A implementação desses testes melhora a **manutenção do código**, reduz a probabilidade de **erros em produção** e facilita a adição de novas funcionalidades ao sistema sem comprometer a estabilidade da aplicação.  

## **Formatação e Estrutura dos Testes Unitários**

Os testes unitários foram organizados para garantir a integridade do sistema em diversas camadas da aplicação. Para uma boa manutenção e padronização do código, seguem os pontos principais de formatação e estrutura dos testes:

### **1. Organização dos Testes**
- **Pasta de Testes**: Os testes unitários estão localizados na pasta `src/test/java` do projeto.
  - Testes de integração, controle e serviços são organizados em subpastas correspondentes aos pacotes da aplicação principal.

### **2. Convenções de Nomenclatura**
- **Classe de Teste**: Cada classe de teste corresponde a uma classe do sistema, com sufixo `Test`. Exemplo:
  - Para a classe `EventoService.java`, o arquivo de teste será `EventoServiceTest.java`.
  
- **Métodos de Teste**: O nome dos métodos de teste segue o padrão `metodo_esperado_comportamento`. Exemplo:
  - Para testar o cadastro de um evento, o método seria `cadastroEvento_Sucesso()`.
  
### **3. Framework de Teste**

Os testes unitários utilizam o **JUnit 5** como framework para execução e validação. As principais anotações são:
  
- `@Test`: Indica que o método é um teste.
- `@BeforeEach`: Executa o método antes de cada teste, sendo útil para configurar pré-condições.
- `@AfterEach`: Executa o método após cada teste, para limpeza de recursos.
  
Exemplo de estrutura de teste com JUnit 5:
  
```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EventoServiceTest {

    @Test
    void cadastroEvento_Sucesso() {
        Evento evento = new Evento("Palestra", "2025-03-15");
        EventoService service = new EventoService();
        
        boolean resultado = service.cadastrarEvento(evento);
        
        assertTrue(resultado);
    }
}
