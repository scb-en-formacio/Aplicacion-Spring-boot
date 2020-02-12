package cat.factigo64.aplicacion.servicio;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.factigo64.aplicacion.entidad.ReseteaContrasenyaToken;
import cat.factigo64.aplicacion.repositorio.ContrasenyaTokenRepositorio;

@Service
public class ReseteaContrasenyaTokenServicioImpl implements ReseteaContrasenyaTokenServicio {

	private static final int EXPIRATION = 60 * 24;
	
	@Autowired
	private ContrasenyaTokenRepositorio tokenRepo;

	@Override
	public ReseteaContrasenyaToken getReseteaContrasenyaTokenByEmail(String mail) throws Exception {
		/*Para evitar algo como:
		List<Document> documentList =
				 documentRepository.findByNameOrderByVersionDesc("mydoc.doc");
				 Document document = documentList.get(0);*/
		ReseteaContrasenyaToken token = tokenRepo.findTopByEmail(mail);
		return token;
	}
	@Override
	public ReseteaContrasenyaToken getReseteaContrasenyaTokenByToken(String token) throws Exception {
		ReseteaContrasenyaToken resToken = tokenRepo.findByToken(token);
		return resToken;
	}

	@Override
	public ReseteaContrasenyaToken save(ReseteaContrasenyaToken token) throws Exception {
		try {
			ReseteaContrasenyaToken myToken = getReseteaContrasenyaTokenByEmail(token.getEmail());
			
			//oken.setToken(myToken.getToken());
			token.setId(myToken.getId());
		} finally {
			
		}
	    //= new ReseteaContrasenyaToken(token, user.getEmail());
		//le pone fecha de caducidad
		//Date expiryDate = new Date();
		Calendar calendar = Calendar.getInstance();
	    calendar.add(Calendar.MINUTE, EXPIRATION);
		token.setExpiryDate(calendar.getTime());
		return tokenRepo.save(token);
	}
	
	@Override
	public void delete(ReseteaContrasenyaToken token) throws Exception {
		
		tokenRepo.delete(token);
	}


}
