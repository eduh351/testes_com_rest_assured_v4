package br.rest.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import org.junit.AfterClass;
import org.junit.Test;

import br.rest.core.BaseTest;
import br.rest.utils.DataUtils;
import br.rest.utils.GeradorMassas;
import br.rest.utils.Login;

public class CenariosTestes extends BaseTest { // Classe Cenários foi extendida á classe BaseTest

	private static String nomeConta = GeradorMassas.nomeConta(); // criando a variável nomeConta estática e instanciando
																	// a classe para gerar o nome da conta
	private static Integer idConta; // criando uma variável estática assim ela não será zerada e poderá ser utilizada em outros testes 
	
	private static String nomeContaAlterada = nomeConta + "  Conta Alterada";

	@Test
	public void naoDevoAcessarAPISemToken() { // Cenário que tenta acessar a rota contas sem estar autenticado na API
		given().when().get("/contas").then().statusCode(401); // Verifica a resposta da requisição: 401 não autorizado
	}

	@Test
	public void incluirContaComSucesso() { // Cenário para incluir uma nova conta
		idConta = given().header("Authorization", "JWT " + Login.login()) // Envia o token que foi extraído
				.body("{\"nome\": \"" + nomeConta + "\"}") // Envia o nome da conta que será criada
				.when().post("/contas") // Rota para fazer o post
				.then().statusCode(201) // Verifica que a conta foi criada com sucesso: 201 Created
				.extract().path("id"); // Extrai o ID da conta criada para usar nos testes seguintes

	}

	@Test
	public void alterandoContaComSucesso() { // Cenário para alterar uma conta
		given().header("Authorization", "JWT " + Login.login()).body("{\"nome\": \"" + nomeContaAlterada + "\"}").when()
				.put("/contas/" + idConta).then().statusCode(200) // Verifica se a alteração da conta foi bem-sucedida:
																	// 200 OK
				.body("nome", is(nomeContaAlterada)); // Verifica se o nome da conta foi alterado corretamente
	}

	@Test
	public void naoDevoIncluirContaComMesmoNome() { // Cenário para tentar incluir uma conta com mesmo nome
		given().header("Authorization", "JWT " + Login.login()).body("{\"nome\": \"" + nomeConta + "\"}").when()
				.post("/contas").then().statusCode(400) // Verifica se o sistema rejeita a duplicidade: 400 Bad Request
				.body("error", is("Já existe uma conta com esse nome!")); // Verifica a mensagem de erro retornada
	}

	@Test
	public void inserindoMovimetacaoComSucesso() { // Cenário para incluir uma movimentação
		Movimentacao mov = movimetacaoValida();
				given().header("Authorization", "JWT " + Login.login()).body(mov).when().post("/transacoes").then()
				.statusCode(201) // Verifica se a movimentação foi criada com sucesso: 201 Created
				.extract().path("id"); // Extrai o ID da movimentação criada
	}

	@Test
	public void validarCamposObrigatoros() { // Cenário para incluir uma movimentação
		given().header("Authorization", "JWT " + Login.login()).body("{}") // Envia body vazio
				.when().post("/transacoes").then().statusCode(400) // Verifica se a API retorna erro de validação: 400
																	// Bad Request
				.body("$", hasSize(8)) // Verifica se existem 8 mensagens de erro (campos obrigatórios)
				.body("msg",
						hasItems("Data da Movimentação é obrigatório", "Data do pagamento é obrigatório",
								"Descrição é obrigatório", "Interessado é obrigatório", "Valor é obrigatório",
								"Valor deve ser um número", "Conta é obrigatório", "Situação é obrigatório")); // Verifica
																												// todas
																												// as
																												// mensagens
																												// de
																												// erro
																												// esperadas
	}

	@Test
	public void naoDevoInserirMovimetacaoComDataFutura() { // Cenário para incluir uma movimentação
		Movimentacao mov = movimetacaoValida();
		mov.setData_transacao(DataUtils.getDataDiferencaDias(5)); // passa o gerador de datas com um adicional de 5 dias
		given().header("Authorization", "JWT " + Login.login()).body(mov).when().post("/transacoes").then()
				.statusCode(400) // Verifica se a API rejeita movimentações com data futura
				.body("msg", hasItem("Data da Movimentação deve ser menor ou igual à data atual")); // Verifica mensagem
																									// de erro da API
	}

	@Test
	public void naoDevoRemoverCobtaComMovimentaca() { // Cenário para incluir uma movimentação
		given().header("Authorization", "JWT " + Login.login()).when().delete("/contas/" + idConta).then()
				.statusCode(500) // Verifica se a tentativa de exclusão da conta falha devido à movimentações
									// vinculadas
				.body("constraint", is("transacoes_conta_id_foreign")); // Verifica se a constraint de integridade é
																		// violada
	}

	@Test
	public void calcularSaldoConta() { // Cenário para incluir uma movimentação
		given().header("Authorization", "JWT " + Login.login()).when().get("/saldo").then().statusCode(200) // Verifica
																											// se a
																											// chamada
																											// ao
																											// endpoint
																											// de saldo
																											// foi
																											// bem-sucedida
				.body("saldo", hasItem("534.00")) // Verifica se o saldo da conta aparece corretamente
				.body("saldo", hasItem("-220.00")); // Verifica se saldo negativo também aparece corretamente
	}

	/*
	 * @Test public void removerMovimentacao() { // Cenário para remover uma
	 * movimentação given().header("Authorization", "JWT " + Login.login())
	 * .when().delete("/transacoes/" + idMov) .then() .statusCode(204); // Verifica
	 * se a movimentação foi removida com sucesso: 204 No Content }
	 */

	// Método para gerar uma movimentação válida
	private Movimentacao movimetacaoValida() {
		Movimentacao mov = new Movimentacao();
		mov.setConta_id(idConta); // Enviando o ID da conta que será incluída a movimentação
		mov.setDescricao("Movimentação Suspeita");
		mov.setEnvolvido("Possoa teste");
		mov.setTipo("REC");
		mov.setData_transacao(DataUtils.getDataDiferencaDias(-5)); // passa o gerador de datas com um diferencial de 5
																	// dias
		mov.setData_pagamento(DataUtils.getDataDiferencaDias(1)); // passa o gerador de datas com um adicional de 1 dia
		mov.setValor(100f);
		mov.setStatus(true);
		return mov;
	}

	// Esse método efetua o reset da aplicação apagando todos os dados ao final da
	// execução
	@AfterClass
	public static void encerrarExecucao() {
		given().header("Authorization", "JWT " + Login.login()).when().get("/reset").then().statusCode(200); // Verifica
																												// se o
																												// reset
																												// foi
																												// realizado
																												// com
																												// sucesso

		System.out.println("Todos os testes foram executados e a aplicação foi resetada.");
	}
}
