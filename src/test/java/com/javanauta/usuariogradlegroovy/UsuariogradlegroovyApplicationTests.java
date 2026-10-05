package com.javanauta.usuariogradlegroovy;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

// Ativa o perfil "test": o Spring carrega também o application-test.properties
@ActiveProfiles("test")
// "properties" tem prioridade sobre o application.properties e sobre variáveis
// de ambiente, então o teste usa estes valores em qualquer máquina
@SpringBootTest(properties = {
		"jwt.secret=TesteChaveSecretaParaGithubActionsJwt1234567"
})
class UsuariogradlegroovyApplicationTests {
	// ...o resto continua igual (o método contextLoads)


	@Test
	void contextLoads() {
	}

}
