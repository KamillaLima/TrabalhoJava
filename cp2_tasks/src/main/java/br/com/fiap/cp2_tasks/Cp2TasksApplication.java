package br.com.fiap.cp2_tasks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Cp2TasksApplication {

	public static void main(String[] args) {
		SpringApplication.run(Cp2TasksApplication.class, args);
		openSwaggerInBrowser();
	}

	private static void openSwaggerInBrowser() {
        try {
            // Abre o Swagger no navegador padrão
            Runtime.getRuntime().exec("cmd /c start http://localhost:8080/swagger-ui.html");
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

}
