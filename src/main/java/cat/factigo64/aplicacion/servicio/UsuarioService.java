package cat.factigo64.aplicacion.servicio;

import java.lang.Iterable;
import java.util.Optional;

import cat.factigo64.aplicacion.Excepcion.UsernameOrIdNoEncontrado;
import cat.factigo64.aplicacion.dto.CambiaContrasenyaForm;
import cat.factigo64.aplicacion.entidad.Usuario;

public interface UsuarioService {

	public Iterable getAllUsuarios();
	public boolean creaUsuario(Usuario frmUsu) throws Exception;
	public boolean eliminaUsuario(Long id) throws UsernameOrIdNoEncontrado;
	public boolean modificaUsuario(Usuario frmUsu) throws Exception;
	public Usuario getUsuarioById(Long id) throws UsernameOrIdNoEncontrado;
	public Usuario cambiaContrasenya(CambiaContrasenyaForm cambia) throws Exception;
	public boolean isLoggedUserADMIN();
	public Usuario getUsuarioByEmail(String mail) throws Exception;
	//public void createPasswordResetTokenForUser(Usuario user, String token);

	public String codifica(String contrasenya);
}
