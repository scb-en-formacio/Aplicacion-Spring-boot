package cat.factigo64.aplicacion.servicio;

import org.springframework.mail.SimpleMailMessage;

public interface EmailServicio {

	public void sendEmail(SimpleMailMessage email);
}
