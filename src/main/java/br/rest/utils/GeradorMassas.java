package br.rest.utils;

import java.util.Random;

public class GeradorMassas {
	// Método para gerar o nome da pessoa física 
    public static String nomeConta() {
    	Random random = new Random();
    	// Gera um número aleatório entre 10000 e 90000
        int numeroAleatorio = 10000 + random.nextInt(90000);
    	String nome = "Pessoa ";
    	String nomeGerado;
    	nomeGerado = nome + numeroAleatorio;
    	
    	return nomeGerado;
    }
     
    }
    
    
