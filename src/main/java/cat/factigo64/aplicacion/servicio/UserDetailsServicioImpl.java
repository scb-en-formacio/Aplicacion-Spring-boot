package cat.factigo64.aplicacion.servicio;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cat.factigo64.aplicacion.entidad.Role;
import cat.factigo64.aplicacion.entidad.Usuario;
import cat.factigo64.aplicacion.repositorio.UsuarioRepository;

@Service
@Transactional
public class UserDetailsServicioImpl implements UserDetailsService{

	@Autowired
    UsuarioRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		//Buscar nombre de usuario en nuestra base de datos
		Usuario appUser = userRepository.findByUsername(username)
	          .orElseThrow(() -> new UsernameNotFoundException("Usuario inexistente!"));
		//notese que aqui no se puede utilizar: new Exception(...
		//porque con el springsecurity se esta obteniendo usernameNotfound...
	    Set<GrantedAuthority> grantList = new HashSet<GrantedAuthority>(); 
	    
	    //Crear la lista de los roles/accessos que tienen el usuarios
	    for (Role role: appUser.getRoles()) {
	        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getDescripcion());
	            grantList.add(grantedAuthority);
	    }
			
	    //Crear y retornar Objeto de usuario soportado por Spring Security
	    UserDetails user = (UserDetails) new User(appUser.getUsername(), appUser.getContrasenya(), grantList);
	    return user;
	}
	
}