package cat.factigo64.aplicacion.servicio;

import cat.factigo64.aplicacion.entidad.ReseteaContrasenyaToken;

public interface ReseteaContrasenyaTokenServicio {

	public ReseteaContrasenyaToken getReseteaContrasenyaTokenByEmail(String mail) throws Exception;
	public ReseteaContrasenyaToken getReseteaContrasenyaTokenByToken(String token) throws Exception;
	public ReseteaContrasenyaToken save(ReseteaContrasenyaToken token) throws Exception;
	public void delete(ReseteaContrasenyaToken token) throws Exception;

}
