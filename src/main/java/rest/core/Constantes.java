package rest.core;

import io.restassured.http.ContentType;

public interface Constantes {
	
	// Url  base dos testes 
	String BASE_URL = "https://barrigarest.wcaquino.me/";
	
	// tipo de requisição que a API trabalha 

	ContentType APP_CONTENT_TYPE = ContentType.JSON;
	
	// Tempo máximo  de resposta para cada requisição 
	long MAX_TIMEOUT = 5000; 
	

}
