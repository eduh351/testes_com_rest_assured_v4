package rest.cadastrarContas;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import rest.core.BaseTest;
import rest.core.GeradorMassas;
import rest.core.Login;

public class CenariosTestes extends BaseTest { // Classe Cenários foi extendida á classe BaseTest

	String nomeConta = GeradorMassas.nomeContaCadastro();
	String nomeContaAlteracao = GeradorMassas.nomeContaAlteracao();
	int id;

	@Test
	public void naoDevoAcessarAPISemToken() { // Cenário que tenta acessar a rota contas sem estar autenticado na API
		given().when().get("/contas") // Realiza um get na rota contas
				.then().statusCode(401) // Verifica a resposta da requisição , 401 não autorizado
		;

	}

	@Test
	public void incluirContaComSucesso() { // Cenário para incluir uma nova conta
		id = given().header("Authorization", "JWT " + Login.login()) // Envia o token que f0i extraído
				.body("{\"nome\": \"" + nomeConta + "\"}") // Envia o nome da conta que
															// será criada
				.when().post("/contas") // Rota para fazer o post
				.then().statusCode(201) // Verifica que a resposta da requisição , conta criada com sucesso 201 Created
				.log().all().extract().path("id")

		;

	}
	/*
	 * Parte do teste se fosse alterar após o cadastro
	 * given().header("Authorization", "JWT " + Login.login()) // Envia o token que
	 * f0i extraído .body("{\"nome\": \"" + nomeContaAlteracao + "\"}") // Envia o
	 * nome da conta que será criada .when().put("/contas/" + id) // Rota para fazer
	 * o post .then().statusCode(200) // Verifica que a resposta da requisição ,
	 * conta criada com sucesso 200 .body("nome", is(nomeContaAlteracao)) //
	 * verifica o nome da conta que foi alterada
	 * 
	 * ;
	 * 
	 * }
	 */

	@Test
	public void alterandoContaComSucesso() { // Cenário para alterar uma conta

		given().header("Authorization", "JWT " + Login.login()) // Envia o token que f0i extraído
				.body("{\"nome\": \"" + nomeContaAlteracao + "\"}") // Envia o nome da conta que será criada
				.when().put("/contas/2381880") // Rota para fazer o post
				.then().statusCode(200) // Verifica que a resposta da requisição , conta criada com sucesso 200
				.body("nome", is(nomeContaAlteracao)) // verifica o nome da conta que foi alterada
		;

	}

	@Test
	public void naoDevoIncluirContaComMesmoNome() { // Cenário para tentar incluir uma conta com mesmo nome

		given().header("Authorization", "JWT " + Login.login()) // Envia o token que f0i extraído
				.body("{\"nome\":\"Conta para teste de alteração\"}") // Envia o nome da conta que será criada
				.when().post("/contas") // Rota para fazer o post
				.then().statusCode(400) // Verifica que a resposta da requisição , conta criada com sucesso 201 Created
				.body("error", is("Já existe uma conta com esse nome!")) // verifica a mensagem de erro que existe na
																			// chave "error"
		;

	}

	@Test
	public void inserindoMovimetacaoComSucesso() { // Cenário para incluir uma movimentação
		Movimentacao mov = movimetacaoValida(); // Montando um objeto de movimentação
		given().header("Authorization", "JWT " + Login.login()) // Envia o token que f0i extraído
				.body(mov) // Envia o nome da conta que será criada
				.when().post("/transacoes") // Rota para fazer o post
				.then().statusCode(201) // Verifica que a resposta da requisição , conta criada com sucesso 201 Created

		;

	}

	@Test
	public void validarCamposObrigatoros() { // Cenário para incluir uma movimentação

		given().header("Authorization", "JWT " + Login.login()) // Envia o token que f0i extraído
				.body("{}") // Envia o body vazio
				.when().post("/transacoes") // Rota para fazer o post
				.then().statusCode(400) // Verifica que a resposta da requisição , conta criada com sucesso 201 Created
				.body("$", hasSize(8)) // verifica que o tamanho da lista filhos tem 8 mensagens
				.body("msg",
						hasItems("Data da Movimentação é obrigatório", "Data do pagamento é obrigatório",
								"Descrição é obrigatório", // verificando as mensagens
								"Interessado é obrigatório", "Valor é obrigatório", "Valor deve ser um número",
								"Conta é obrigatório", "Situação é obrigatório"))

		;

	}

	@Test
	public void naoDevoInserirMovimetacaoComDataFutura() { // Cenário para incluir uma movimentação
		Movimentacao mov = movimetacaoValida(); // Montando um objeto de movimentação
		mov.setData_transacao("10/01/2030");
		given().header("Authorization", "JWT " + Login.login()) // Envia o token que f0i extraído
				.body(mov) // Envia o nome da conta que será criada
				.when().post("/transacoes") // Rota para fazer o post
				.then().statusCode(400) // Verifica que a resposta da requisição , conta criada com sucesso 201 Created
				.body("msg", hasItem("Data da Movimentação deve ser menor ou igual à data atual")) // Verifica a mensagem de erro

		;

	}
	
	
	@Test
	public void naoDevoRemoverCobtaComMovimentaca() { // Cenário para incluir uma movimentação
		given().header("Authorization", "JWT " + Login.login()) // Envia o token que f0i extraído
				.when().delete("/contas/2381880") // Rota para fazer o post
				.then().statusCode(400) // Verifica que a resposta da requisição , conta criada com sucesso 201 Created
				.body("msg", hasItem("Data da Movimentação deve ser menor ou igual à data atual")) // Verifica a mensagem de erro

		;

	}

	// Método para gerar um amovimentação válida
	private Movimentacao movimetacaoValida() {
		Movimentacao mov = new Movimentacao();
		mov.setConta_id(2381880); // Enviando o ID da conta que será incluída a movimentação
		// mov.setUsuario_id(usuraio_id);
		mov.setDescricao("Movimentação Suspeita");
		mov.setEnvolvido("Possoa teste");
		mov.setTipo("REC");
		mov.setData_transacao("12/01/2024");
		mov.setData_pagamento("12/03/2025");
		mov.setValor(100f);
		mov.setStatus(true);
		return mov;
	}

}
