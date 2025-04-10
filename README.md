# 🧪 Testes Automatizados de API REST com REST Assured

Este projeto tem como objetivo realizar **testes automatizados** de uma API REST utilizando a biblioteca **REST Assured** integrada ao **JUnit**, com geração de massa dinâmica, boas práticas de organização e integração com relatórios.

---

## 🚀 Tecnologias e Ferramentas

- **Java 11+**
- **REST Assured**
- **JUnit 4**
- **Allure Reports**
- **Maven**
- **Git**
- **Geração de Massa Dinâmica**
- **Integração com token JWT**
- **Testes com validação de status codes, body e erros**

---

## 📁 Estrutura de Diretórios


---

## ✅ Funcionalidades Testadas

- ✅ Acesso à API sem token (esperado 401)
- ✅ Criação de conta com sucesso
- ✅ Alteração de conta
- ✅ Validação de nome duplicado
- ✅ Inserção de movimentação válida
- ✅ Validação de campos obrigatórios
- ✅ Validação de data futura
- ✅ Validação de saldo
- ❌ Exclusão de movimentação (comentado)

---

## 🔐 Autenticação

A autenticação é feita via **JWT Token**, obtido com a classe `Login.java`. O token é incluído nos headers de todas as requisições autenticadas.

---

## ▶️ Executando os Testes

1. Clone o projeto:

```bash
git clone git@github.com:eduh351/testes_com_rest_assured_v4.git
