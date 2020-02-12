package cat.factigo64.aplicacion.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServicioImpl implements EmailServicio{

	@Autowired
	private JavaMailSender mailSender;

	@Async
	public void sendEmail(SimpleMailMessage email) {
		mailSender.send(email);
	}

}
