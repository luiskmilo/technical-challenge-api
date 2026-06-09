package technical.challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * Punto de entrada de la aplicación Spring Boot.
 */
@SpringBootApplication
public class ChallengeApplication {

    /**
     * Método principal que arranca el contexto de Spring Boot.
     *
     * @param args argumentos de línea de comandos (no requeridos)
     */
	public static void main(String[] args) {
		SpringApplication.run(ChallengeApplication.class, args);
	}

}
