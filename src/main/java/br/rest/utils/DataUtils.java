package br.rest.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DataUtils {
	
	
	// Método para criar a instância de uma data com base na data atuar mais a quantidade de dias que será passada como parâmetro
	public static String getDataDiferencaDias(Integer qtdDias) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, qtdDias);
		return getDataformatada(cal.getTime());

	}

	// Método para formatar a data 
	public static String getDataformatada(Date data) {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		return format.format(data);
	}
}
