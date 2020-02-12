package cat.factigo64.aplicacion.servicio;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import cat.factigo64.aplicacion.Excepcion.ErrorValidacionCampo;
import cat.factigo64.aplicacion.Excepcion.UsernameOrIdNoEncontrado;
import cat.factigo64.aplicacion.dto.CambiaContrasenyaForm;
import cat.factigo64.aplicacion.entidad.Usuario;
import cat.factigo64.aplicacion.repositorio.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService{

	@Autowired
	UsuarioRepository usuRepo;
	@Autowired
	BCryptPasswordEncoder bCryptCoder;
	
	@Override
	public Iterable getAllUsuarios() {
		
		//en caso de tener el met. find(activos)en la interface, aqui se podría usar
		//return usuRepo.findAllByStatus("activo");
		return usuRepo.findAll();
	}

	private boolean esUsernameLibre(Usuario user) throws Exception {
		//Usuario usuEncontrado = usuRepo.findByUsername(user.getUsername());
		//if (usuEncontrado.getId() >= 0) {
		Optional<Usuario> usuEncontrado = usuRepo.findByUsername(user.getUsername());
		if (usuEncontrado.isPresent()) {
			throw new ErrorValidacionCampo("Nombre de usuario no disponible", "username");
		}
		return true;
	}
	private boolean contrasenyaValida(Usuario user) throws Exception {
		if (user.getConfirmaContrasenya() == null || user.getConfirmaContrasenya().isEmpty()) {
			throw new ErrorValidacionCampo("Confirma contraseña es obligatorio", "ConfirmaContrasenya");
		}
		if (!user.getContrasenya().equals(user.getConfirmaContrasenya())) {
			throw new ErrorValidacionCampo("Contraseña y Confirma contraseña no son iguales", "contrasenya");
		}
		return true;
	}
	@Override
	public boolean creaUsuario(Usuario user) throws Exception {
		if (esUsernameLibre(user) && contrasenyaValida(user)) {
			String claveEncodif = codifica(user.getContrasenya());
			user.setContrasenya(claveEncodif);
			user = usuRepo.save(user);
			return true;
		}
		return false;
	}
	public String codifica(String contrasenya) {
		return bCryptCoder.encode(contrasenya);
	}

	/*Para dar accesso al metodo al usuario en sesion necesitas activar la anotacion:
	 *  @PreAuthorize y anotar dicho metodo. Utilizando la misma expression que en thymeleaf.
 		 Para activar la anot de seguridad se crea otra clase de config en el paquete raiz.
 		 MethodSecurityConfig.java
	*/
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USUARIO')")
	@Override
	public boolean modificaUsuario(Usuario usu1) throws Exception {
		Usuario usu2 = getUsuarioById(usu1.getId());
		mapUsuario(usu1, usu2);
		Usuario usu3 = usuRepo.save(usu2);
		if (usu3.getApellidos().equals(usu2.getApellidos())) {
			return true;
		}
		return false;
	}
	/**
	 * Map everythin but the password.
	 * @param from
	 * @param to
	 */
	protected void mapUsuario(Usuario from, Usuario to) {
		to.setUsername(from.getUsername());
		to.setNombre(from.getNombre());
		to.setApellidos(from.getApellidos());
		to.setEmail(from.getEmail());
		to.setRoles(from.getRoles());
		to.setConfirmaContrasenya("xxxxxx");
	}
	
	@Override
	public Usuario getUsuarioById(Long id) throws UsernameOrIdNoEncontrado {
		Usuario usuar = (Usuario)usuRepo.findById(id)
				// en lugar de usar 'Exception', se usa nuestra clase: 
				//UsernameOrIdNoEncontgrada
				.orElseThrow(() -> new UsernameOrIdNoEncontrado("el Id no existe: "+ this.getClass().getName()));
		return usuar;
	}
	
	public Usuario getUsuarioByEmail(String mail) throws Exception{
		Usuario usuar = (Usuario)usuRepo.findByEmail(mail)
				// en lugar de usar 'Exception', se usa nuestra clase: 
				//UsernameOrIdNoEncontgrada
				.orElseThrow(() -> new Exception("Email no registrado: "+ this.getClass().getName()));
		return usuar;
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@Override
	public boolean eliminaUsuario(Long id) throws UsernameOrIdNoEncontrado {
		Usuario usu2 = getUsuarioById(id);
		usuRepo.delete(usu2);

		return true;
	}

	@Override
	public Usuario cambiaContrasenya(CambiaContrasenyaForm form) throws Exception {
		Usuario usuar = (Usuario)usuRepo.findById(form.getId())
				.orElseThrow(() -> new Exception("el Usuario no existe: "+ this.getClass().getName()));
		String claveEncodif = codifica(form.getCurrentPassword());
		//esta validacion de rol se agrega junto con los controles equiv d los html
		if( !isLoggedUserADMIN() ) {
			if( !bCryptCoder.matches(form.getCurrentPassword(), usuar.getContrasenya())) {
				throw new Exception("Contraseña actual Incorrecta.");
			}
			if ( form.getCurrentPassword().equals(form.getNewPassword())) {
				throw new Exception("Contraseña nueva y actual no deben ser iguales!");
			}
		}
		if( !form.getNewPassword().equals(form.getConfirmPassword())) {
			throw new Exception("Contraseña nueva y Confirma contraseña no son iguales!");
		}
		//se agrega la codif de la contraseña
		claveEncodif = codifica(form.getNewPassword());
		usuar.setContrasenya(claveEncodif);
		return usuRepo.save(usuar);
	}

	//es la validacion de rol administrador
	public boolean isLoggedUserADMIN(){
		return loggedUserHasRole("ROLE_ADMIN");
	}

	public boolean loggedUserHasRole(String role) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails loggedUser = null;
		Object roles = null; 
		if (principal instanceof UserDetails) {
			loggedUser = (UserDetails) principal;
		
			roles = loggedUser.getAuthorities().stream()
					.filter(x -> role.equals(x.getAuthority() ))      
					.findFirst().orElse(null); //loggedUser = null;
		}
		return roles != null ?true :false;
	}
	
	private Usuario getLoggedUsuario() throws Exception {
		//Obtener el usuario logeado
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails loggedUser = null;

		//Verificar que ese objeto traido de sesion es el usuario
		if (principal instanceof UserDetails) {
			loggedUser = (UserDetails) principal;
		}
		Usuario myUser = usuRepo.findByUsername(
				loggedUser.getUsername()).orElseThrow(() -> new Exception("No se pudo obtener el usuario de sesion"));
		
		return myUser;
	}
	
}
