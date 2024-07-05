package com.ccettour.literalura;

import com.ccettour.literalura.principal.Principal;
import com.ccettour.literalura.repository.LiteraluraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	@Autowired
	private LiteraluraRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		var consumoApi = new ConsumirAPI();
//		var json = consumoApi.obtenerDatos("https://gutendex.com/books/?search=Quijote");
//		ConvertirDatos conversor = new ConvertirDatos();
//		var datosConvertidos = conversor.obtenerDatos(json, ApiData.class);
//		System.out.println(datosConvertidos);
		Principal principal = new Principal(repository);
		principal.showMenu();
	}
}
