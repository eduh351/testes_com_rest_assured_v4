package rest.core;

import org.hamcrest.Matchers;
import org.junit.BeforeClass;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;


// Essa classe ira ser iniciada antes de todos os testes
public class BaseTest implements Constantes {
	
	@BeforeClass
	public static void setup() {
		
		// sempre ira iniciar com a URL Base que foi definida na classe Constantes
		RestAssured.baseURI = BASE_URL;
		
		// Indica que todas as requisções estaram implicitamente sendo enviadas com o formato json
		RequestSpecBuilder recBuilder = new RequestSpecBuilder();
		recBuilder.setContentType(APP_CONTENT_TYPE);
		RestAssured.requestSpecification = recBuilder.build();
		
	
		// Define o tempo máximo de resposta de acordo com o configurado na classe Constantes
		ResponseSpecBuilder  resBuilder = new ResponseSpecBuilder();
		resBuilder.expectResponseTime(Matchers.lessThan(MAX_TIMEOUT));
		RestAssured.responseSpecification = resBuilder.build();
		
		// Habilita o loog apenas se der erro no teste 
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		
		
		
				
	}
	
}
