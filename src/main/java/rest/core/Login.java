package rest.core;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

public class Login {

	static String token; // Declarando a variável token como global para poder ser ultilizada em todos os
							// testes

	public static String login() {
		// .body("{\"email\", \"eduardo@teste\"}, {\"senha\", \"@teste\"}") // Outra
		// forma de enviar os dados , agora pelo própio body
		Map<String, String> login = new HashMap<>(); // Usando o Map para armazenar chave e valor que serão enviados no
														// body
		login.put("email", "edu@testerest.com.br"); // atravez da variável login que foi criada com o Map é possível
													// passar email e senha
		login.put("senha", "123456");

		token = given() // Armazema o tokem após realizar o login na variável token
				// .contentType(ContentType.JSON)
				.body(login) // Enviando os dados de login no body passando a varível login
				.when().post("/signin") // Rota para fazer o login
				.then().statusCode(200) // verifica o status da requisição
				.extract().path("token"); // Extrai o token após realizar o login
		return token;
	}

}
