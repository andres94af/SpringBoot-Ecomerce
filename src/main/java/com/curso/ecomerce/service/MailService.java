package com.curso.ecomerce.service;

import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.curso.ecomerce.model.Orden;
import com.curso.ecomerce.model.Usuario;

@Service
public class MailService {

	private final Properties properties = new Properties();
	private Session session;
	String rutaSistema = System.getProperty("user.dir");
	String nombreArchivo = "/OrdenesPDF/Orden 001-";
	
	@Value("${spring.mail.username}")
	private String username;
	
	@Value("${spring.mail.password}")
	private String password;

	private void inicializarSesion() {
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.setProperty("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.port", "587");
		properties.setProperty("mail.smtp.port", "587");
		properties.put("mail.smtp.user", username);
		properties.setProperty("mail.smtp.auth", "true");
		session = Session.getDefaultInstance(properties);
	}
	

	public void enviarMailOrdenCreada(Orden orden) {
		String rutaDePdf = rutaSistema + nombreArchivo + orden.getNumero() + ".pdf";
		BodyPart mensajeOrden = new MimeBodyPart();
		BodyPart archivo = new MimeBodyPart();
		MimeMultipart adjunto = new MimeMultipart();
		try {
			mensajeOrden.setText(orden.getUsuario().getUsername() + ". Adjuntamos comprobante de la Orden nº001-" + orden.getNumero());
			archivo.setDataHandler(new DataHandler(new FileDataSource(rutaDePdf)));
			archivo.setFileName(new FileDataSource(rutaDePdf).getName());
			adjunto.addBodyPart(mensajeOrden);
			adjunto.addBodyPart(archivo);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		inicializarSesion();
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(orden.getUsuario().getEmail()));
			message.setSubject("Spring eCommerce - Orden nº001-"+orden.getNumero());
			message.setContent(adjunto);
			Transport t = session.getTransport("smtp");
			t.connect(username, password);
			t.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
			t.close();
		} catch (MessagingException me) {
			me.printStackTrace();
		}
	}
	
	
	public void enviarMailRegistro(Usuario usuario) {
		String mensajeRegistro = 
				"Hola " + usuario.getUsername()
				+ "<br/><br/>Usted se ha registrado de manera correcta en el sitio."
				+ "<br/>"
				+ "<br/>Saludos y disfrute la web!"
				+ "<br/><br/>Ir al sitio: <a href='http://localhost:8080/'>www.springecommerce.es<a/>";
		inicializarSesion();
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(usuario.getEmail()));
			message.setSubject("Bienvenid@ a Spring eCommerce");
			message.setText(mensajeRegistro, "ISO-8859-1", "html");
			Transport t = session.getTransport("smtp");
			t.connect(username, password);
			t.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
			t.close();
		} catch (MessagingException me) {
			me.printStackTrace();
		}
	}
}