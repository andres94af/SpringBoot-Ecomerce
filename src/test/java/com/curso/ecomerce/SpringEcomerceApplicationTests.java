package com.curso.ecomerce;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.curso.ecomerce.model.Orden;
import com.curso.ecomerce.service.MailService;

@SpringBootTest
class SpringEcomerceApplicationTests {
	
	@Autowired
	private MailService mailService;
	
	Orden orden = new Orden();

	@Test
	void contextLoads() {
		mailService.enviarMailOrdenCreada(orden);
	}

}
