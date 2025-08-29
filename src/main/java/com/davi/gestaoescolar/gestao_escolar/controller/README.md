# Documentação dos Controllers

Este documento descreve os endpoints REST disponíveis para gerenciamento de usuários e professores no sistema de gestão escolar.

## UsuarioController

**Base URL:** `/api/usuarios`

### Endpoints Disponíveis

#### 1. Criar Usuário
- **POST** `/api/usuarios`
- **Descrição:** Cria um novo usuário no sistema
- **Body:** Objeto Usuario (JSON)
- **Retorno:** Usuario criado ou mensagem de erro

#### 2. Atualizar Usuário
- **PUT** `/api/usuarios/{id}`
- **Descrição:** Atualiza um usuário existente
- **Parâmetros:** `id` (Long) - ID do usuário
- **Body:** Objeto Usuario (JSON)
- **Retorno:** Usuario atualizado ou mensagem de erro

#### 3. Buscar Usuário por ID
- **GET** `/api/usuarios/{id}`
- **Descrição:** Busca um usuário pelo ID
- **Parâmetros:** `id` (Long) - ID do usuário
- **Retorno:** Usuario encontrado ou 404

#### 4. Buscar Usuário por Email
- **GET** `/api/usuarios/email/{email}`
- **Descrição:** Busca um usuário pelo email
- **Parâmetros:** `email` (String) - Email do usuário
- **Retorno:** Usuario encontrado ou 404

#### 5. Listar Todos os Usuários
- **GET** `/api/usuarios`
- **Descrição:** Lista todos os usuários do sistema
- **Retorno:** Lista de usuários

#### 6. Listar Usuários por Perfil
- **GET** `/api/usuarios/perfil/{perfil}`
- **Descrição:** Lista usuários por perfil específico
- **Parâmetros:** `perfil` (String) - Perfil do usuário (ADMIN, PROFESSOR, ALUNO, RESPONSAVEL)
- **Retorno:** Lista de usuários do perfil especificado

#### 7. Listar Usuários Ativos
- **GET** `/api/usuarios/ativos`
- **Descrição:** Lista apenas usuários ativos
- **Retorno:** Lista de usuários ativos

#### 8. Listar Usuários Inativos
- **GET** `/api/usuarios/inativos`
- **Descrição:** Lista apenas usuários inativos
- **Retorno:** Lista de usuários inativos

#### 9. Listar por Perfil e Status
- **GET** `/api/usuarios/perfil/{perfil}/ativo/{ativo}`
- **Descrição:** Lista usuários por perfil e status ativo/inativo
- **Parâmetros:** 
  - `perfil` (String) - Perfil do usuário
  - `ativo` (Boolean) - Status ativo (true/false)
- **Retorno:** Lista de usuários filtrados

#### 10. Desativar Usuário
- **PATCH** `/api/usuarios/{id}/desativar`
- **Descrição:** Desativa um usuário (soft delete)
- **Parâmetros:** `id` (Long) - ID do usuário
- **Retorno:** Mensagem de sucesso ou erro

#### 11. Reativar Usuário
- **PATCH** `/api/usuarios/{id}/reativar`
- **Descrição:** Reativa um usuário desativado
- **Parâmetros:** `id` (Long) - ID do usuário
- **Retorno:** Mensagem de sucesso ou erro

#### 12. Alterar Senha
- **PATCH** `/api/usuarios/{id}/senha`
- **Descrição:** Altera a senha de um usuário
- **Parâmetros:** `id` (Long) - ID do usuário
- **Body:** `{"novaSenha": "nova_senha"}`
- **Retorno:** Mensagem de sucesso ou erro

#### 13. Atualizar Último Acesso
- **PATCH** `/api/usuarios/ultimo-acesso/{email}`
- **Descrição:** Atualiza o timestamp do último acesso
- **Parâmetros:** `email` (String) - Email do usuário
- **Retorno:** Mensagem de sucesso ou erro

#### 14. Verificar Email
- **GET** `/api/usuarios/verificar-email/{email}`
- **Descrição:** Verifica se um email já existe no sistema
- **Parâmetros:** `email` (String) - Email a verificar
- **Retorno:** `{"existe": true/false}`

#### 15. Deletar Usuário
- **DELETE** `/api/usuarios/{id}`
- **Descrição:** Remove permanentemente um usuário
- **Parâmetros:** `id` (Long) - ID do usuário
- **Retorno:** Mensagem de sucesso ou erro

---

## ProfessorController

**Base URL:** `/api/professores`

### Endpoints Disponíveis

#### 1. Criar Professor
- **POST** `/api/professores`
- **Descrição:** Cria um novo professor no sistema
- **Body:** Objeto Professor (JSON)
- **Retorno:** Professor criado ou mensagem de erro

#### 2. Atualizar Professor
- **PUT** `/api/professores/{id}`
- **Descrição:** Atualiza um professor existente
- **Parâmetros:** `id` (Long) - ID do professor
- **Body:** Objeto Professor (JSON)
- **Retorno:** Professor atualizado ou mensagem de erro

#### 3. Buscar Professor por ID
- **GET** `/api/professores/{id}`
- **Descrição:** Busca um professor pelo ID
- **Parâmetros:** `id` (Long) - ID do professor
- **Retorno:** Professor encontrado ou 404

#### 4. Buscar Professor por CPF
- **GET** `/api/professores/cpf/{cpf}`
- **Descrição:** Busca um professor pelo CPF
- **Parâmetros:** `cpf` (String) - CPF do professor
- **Retorno:** Professor encontrado ou 404

#### 5. Buscar Professor por Email
- **GET** `/api/professores/email/{email}`
- **Descrição:** Busca um professor pelo email
- **Parâmetros:** `email` (String) - Email do professor
- **Retorno:** Professor encontrado ou 404

#### 6. Listar Todos os Professores
- **GET** `/api/professores`
- **Descrição:** Lista todos os professores do sistema
- **Retorno:** Lista de professores

#### 7. Listar Professores Ativos
- **GET** `/api/professores/ativos`
- **Descrição:** Lista apenas professores ativos
- **Retorno:** Lista de professores ativos

#### 8. Listar Professores Inativos
- **GET** `/api/professores/inativos`
- **Descrição:** Lista apenas professores inativos
- **Retorno:** Lista de professores inativos

#### 9. Buscar por Nome
- **GET** `/api/professores/buscar/nome/{nome}`
- **Descrição:** Busca professores por nome (busca parcial)
- **Parâmetros:** `nome` (String) - Nome ou parte do nome
- **Retorno:** Lista de professores encontrados

#### 10. Buscar por Formação
- **GET** `/api/professores/buscar/formacao/{formacao}`
- **Descrição:** Busca professores por formação (busca parcial)
- **Parâmetros:** `formacao` (String) - Formação ou parte da formação
- **Retorno:** Lista de professores encontrados

#### 11. Buscar por Telefone
- **GET** `/api/professores/buscar/telefone/{telefone}`
- **Descrição:** Busca professores por telefone (busca parcial)
- **Parâmetros:** `telefone` (String) - Telefone ou parte do telefone
- **Retorno:** Lista de professores encontrados

#### 12. Desativar Professor
- **PATCH** `/api/professores/{id}/desativar`
- **Descrição:** Desativa um professor (soft delete)
- **Parâmetros:** `id` (Long) - ID do professor
- **Retorno:** Mensagem de sucesso ou erro

#### 13. Reativar Professor
- **PATCH** `/api/professores/{id}/reativar`
- **Descrição:** Reativa um professor desativado
- **Parâmetros:** `id` (Long) - ID do professor
- **Retorno:** Mensagem de sucesso ou erro

#### 14. Alterar Senha
- **PATCH** `/api/professores/{id}/senha`
- **Descrição:** Altera a senha de um professor
- **Parâmetros:** `id` (Long) - ID do professor
- **Body:** `{"novaSenha": "nova_senha"}`
- **Retorno:** Mensagem de sucesso ou erro

#### 15. Atualizar Último Acesso
- **PATCH** `/api/professores/ultimo-acesso/{email}`
- **Descrição:** Atualiza o timestamp do último acesso
- **Parâmetros:** `email` (String) - Email do professor
- **Retorno:** Mensagem de sucesso ou erro

#### 16. Verificar CPF
- **GET** `/api/professores/verificar-cpf/{cpf}`
- **Descrição:** Verifica se um CPF já existe no sistema
- **Parâmetros:** `cpf` (String) - CPF a verificar
- **Retorno:** `{"existe": true/false}`

#### 17. Formatar CPF
- **GET** `/api/professores/formatar-cpf/{cpf}`
- **Descrição:** Formata um CPF para exibição
- **Parâmetros:** `cpf` (String) - CPF a formatar
- **Retorno:** `{"cpfFormatado": "xxx.xxx.xxx-xx"}`

#### 18. Deletar Professor
- **DELETE** `/api/professores/{id}`
- **Descrição:** Remove permanentemente um professor
- **Parâmetros:** `id` (Long) - ID do professor
- **Retorno:** Mensagem de sucesso ou erro

#### 19. Busca Avançada
- **POST** `/api/professores/busca-avancada`
- **Descrição:** Realiza busca avançada com múltiplos filtros
- **Body:** 
```json
{
  "nome": "string (opcional)",
  "formacao": "string (opcional)",
  "ativo": "boolean (opcional)"
}
```
- **Retorno:** Lista de professores filtrados

---

## Tratamento de Erros

Todos os endpoints utilizam o `GlobalExceptionHandler` para tratamento padronizado de erros:

### Códigos de Status HTTP
- **200 OK:** Operação realizada com sucesso
- **201 Created:** Recurso criado com sucesso
- **400 Bad Request:** Dados inválidos ou erro de validação
- **404 Not Found:** Recurso não encontrado
- **409 Conflict:** Conflito de dados (ex: email/CPF duplicado)
- **500 Internal Server Error:** Erro interno do servidor

### Formato de Resposta de Erro
```json
{
  "timestamp": "2024-01-20T10:30:00",
  "status": 400,
  "error": "Erro de validação",
  "message": "Descrição específica do erro",
  "path": "/api/usuarios"
}
```

### Validações Implementadas
- **Email:** Formato válido e unicidade
- **CPF:** Formato válido, algoritmo de validação e unicidade
- **Senha:** Mínimo 6 caracteres, pelo menos uma letra e um número
- **Nome:** Mínimo 2 caracteres, apenas letras e espaços
- **Telefone:** Formato brasileiro válido
- **Perfil:** Valores permitidos (ADMIN, PROFESSOR, ALUNO, RESPONSAVEL)

---

## Observações Importantes

1. **Autenticação:** Os endpoints não implementam autenticação/autorização nesta versão
2. **CORS:** Configurado para aceitar requisições de qualquer origem (`*`)
3. **Soft Delete:** Operações de desativação não removem dados permanentemente
4. **Validações:** Utilizam a classe `ValidationUtils` para validações customizadas
5. **Encoding:** Senhas são automaticamente criptografadas antes do armazenamento
6. **Timestamps:** Último acesso é atualizado automaticamente em operações relevantes

---

## Exemplos de Uso

### Criar um Professor
```bash
curl -X POST http://localhost:8080/api/professores \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao.silva@escola.com",
    "senha": "senha123",
    "cpf": "123.456.789-00",
    "formacao": "Licenciatura em Matemática",
    "telefone": "(11) 99999-9999",
    "perfil": "PROFESSOR",
    "ativo": true
  }'
```

### Buscar Professor por Email
```bash
curl -X GET http://localhost:8080/api/professores/email/joao.silva@escola.com
```

### Listar Professores Ativos
```bash
curl -X GET http://localhost:8080/api/professores/ativos
```