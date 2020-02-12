package cat.factigo64.aplicacion.repositorio;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cat.factigo64.aplicacion.entidad.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long>{

	//gracias al jpa se puede trabajar con el sistema: convención sobre configuracion
	public Optional<Usuario> findByUsername(String usuari);
	public Optional<Usuario> findByUsernameOrEmail(String usuari, String mail);
	public Optional<Usuario> findByEmail(String mail);
	//public Set<Usuario> findByUsername(String usuari);
	//public Usuario findByUsername(String usuari);
	public Usuario findByIdAndContrasenya(Long id, String clave);
	//como ejercicio para mostrar las capacidades delspring, supongamos un met, para usuarios activos
	//public Iterable<Usuario> findAllByStatus(String estado); //necesitaria el campo status
}
