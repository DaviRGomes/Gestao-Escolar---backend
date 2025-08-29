// Configuração da API
const API_BASE_URL = 'http://localhost:8082/api';

// Elementos do DOM
const resultsDiv = document.getElementById('results');

// Função para exibir resultados
function displayResult(data, isError = false) {
    const resultClass = isError ? 'error' : 'success';
    const formattedData = typeof data === 'object' ? JSON.stringify(data, null, 2) : data;
    
    resultsDiv.innerHTML = `<div class="${resultClass}">${formattedData}</div>`;
    resultsDiv.scrollIntoView({ behavior: 'smooth' });
}

// Função para exibir loading
function displayLoading(message = 'Carregando...') {
    resultsDiv.innerHTML = `<div class="loading">${message}</div>`;
}

// Função genérica para fazer requisições
async function makeRequest(url, options = {}) {
    try {
        displayLoading();
        
        const defaultOptions = {
            headers: {
                'Content-Type': 'application/json',
            },
        };
        
        const response = await fetch(url, { ...defaultOptions, ...options });
        
        if (!response.ok) {
            throw new Error(`Erro ${response.status}: ${response.statusText}`);
        }
        
        const data = await response.json();
        displayResult(data);
        return data;
    } catch (error) {
        displayResult(`Erro: ${error.message}`, true);
        console.error('Erro na requisição:', error);
    }
}

// ==================== FUNÇÕES DE USUÁRIO ====================

// Criar usuário
document.getElementById('createUserForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const userData = {
        email: document.getElementById('userEmail').value,
        senha: document.getElementById('userPassword').value,
        perfil: document.getElementById('userPerfil').value
    };
    
    await makeRequest(`${API_BASE_URL}/usuarios`, {
        method: 'POST',
        body: JSON.stringify(userData)
    });
    
    // Limpar formulário
    e.target.reset();
});

// Buscar usuário por ID
async function searchUser() {
    const userId = document.getElementById('searchUserId').value;
    
    if (!userId) {
        displayResult('Por favor, insira um ID válido.', true);
        return;
    }
    
    await makeRequest(`${API_BASE_URL}/usuarios/${userId}`);
}

// Buscar usuário por email
async function searchUserByEmail() {
    const userEmail = document.getElementById('searchUserEmail').value;
    
    if (!userEmail) {
        displayResult('Por favor, insira um email válido.', true);
        return;
    }
    
    await makeRequest(`${API_BASE_URL}/usuarios/email/${encodeURIComponent(userEmail)}`);
}

// Listar todos os usuários
async function listAllUsers() {
    await makeRequest(`${API_BASE_URL}/usuarios`);
}

// ==================== FUNÇÕES DE PROFESSOR ====================

// Criar professor
document.getElementById('createProfessorForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const professorData = {
        email: document.getElementById('profEmail').value,
        senha: document.getElementById('profPassword').value,
        nome: document.getElementById('profNome').value,
        cpf: document.getElementById('profCpf').value,
        formacao: document.getElementById('profFormacao').value || null,
        telefone: document.getElementById('profTelefone').value || null
    };
    
    await makeRequest(`${API_BASE_URL}/professores`, {
        method: 'POST',
        body: JSON.stringify(professorData)
    });
    
    // Limpar formulário
    e.target.reset();
});

// Buscar professor por ID
async function searchProfessor() {
    const professorId = document.getElementById('searchProfId').value;
    
    if (!professorId) {
        displayResult('Por favor, insira um ID válido.', true);
        return;
    }
    
    await makeRequest(`${API_BASE_URL}/professores/${professorId}`);
}

// Buscar professor por CPF
async function searchProfessorByCpf() {
    const professorCpf = document.getElementById('searchProfCpf').value;
    
    if (!professorCpf) {
        displayResult('Por favor, insira um CPF válido.', true);
        return;
    }
    
    await makeRequest(`${API_BASE_URL}/professores/cpf/${encodeURIComponent(professorCpf)}`);
}

// Buscar professor por email
async function searchProfessorByEmail() {
    const professorEmail = document.getElementById('searchProfEmail').value;
    
    if (!professorEmail) {
        displayResult('Por favor, insira um email válido.', true);
        return;
    }
    
    await makeRequest(`${API_BASE_URL}/professores/email/${encodeURIComponent(professorEmail)}`);
}

// Listar todos os professores
async function listAllProfessors() {
    await makeRequest(`${API_BASE_URL}/professores`);
}

// ==================== FUNÇÕES UTILITÁRIAS ====================

// Função para formatar CPF (opcional)
function formatCPF(cpf) {
    return cpf.replace(/\D/g, '')
              .replace(/(\d{3})(\d)/, '$1.$2')
              .replace(/(\d{3})(\d)/, '$1.$2')
              .replace(/(\d{3})(\d{1,2})$/, '$1-$2');
}

// Adicionar formatação automática de CPF
document.getElementById('profCpf').addEventListener('input', function(e) {
    e.target.value = formatCPF(e.target.value);
});

document.getElementById('searchProfCpf').addEventListener('input', function(e) {
    e.target.value = formatCPF(e.target.value);
});

// Função para validar email
function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

// Adicionar validação de email em tempo real
function addEmailValidation(inputId) {
    const input = document.getElementById(inputId);
    input.addEventListener('blur', function() {
        if (this.value && !isValidEmail(this.value)) {
            this.style.borderColor = '#e53e3e';
            this.style.backgroundColor = '#fed7d7';
        } else {
            this.style.borderColor = '#e2e8f0';
            this.style.backgroundColor = 'white';
        }
    });
}

// Aplicar validação de email aos campos
addEmailValidation('userEmail');
addEmailValidation('profEmail');
addEmailValidation('searchUserEmail');
addEmailValidation('searchProfEmail');

// ==================== INICIALIZAÇÃO ====================

// Verificar se a API está disponível ao carregar a página
window.addEventListener('load', async () => {
    try {
        displayLoading('Verificando conexão com a API...');
        
        const response = await fetch(`${API_BASE_URL}/usuarios`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        });
        
        if (response.ok) {
            displayResult('✅ Conexão com a API estabelecida com sucesso!\n\nVocê pode começar a usar o sistema.');
        } else {
            throw new Error(`Status: ${response.status}`);
        }
    } catch (error) {
        displayResult(
            `❌ Erro ao conectar com a API em ${API_BASE_URL}\n\n` +
            `Verifique se:\n` +
            `1. O servidor Spring Boot está rodando\n` +
            `2. A aplicação está rodando na porta 8082\n` +
            `3. Não há problemas de CORS\n\n` +
            `Erro: ${error.message}`,
            true
        );
    }
});

// Adicionar funcionalidade de limpar resultados
function clearResults() {
    resultsDiv.innerHTML = '<p>Os resultados das operações aparecerão aqui...</p>';
}

// Adicionar botão para limpar resultados (opcional)
const clearButton = document.createElement('button');
clearButton.textContent = 'Limpar Resultados';
clearButton.onclick = clearResults;
clearButton.style.marginBottom = '20px';
resultsDiv.parentNode.insertBefore(clearButton, resultsDiv);

console.log('🚀 Frontend Template carregado com sucesso!');
console.log('📡 API Base URL:', API_BASE_URL);
console.log('📋 Endpoints disponíveis:');
console.log('   - GET/POST /usuarios');
console.log('   - GET /usuarios/{id}');
console.log('   - GET /usuarios/email/{email}');
console.log('   - GET/POST /professores');
console.log('   - GET /professores/{id}');
console.log('   - GET /professores/cpf/{cpf}');
console.log('   - GET /professores/email/{email}');