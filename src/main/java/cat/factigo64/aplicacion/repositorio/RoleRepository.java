package cat.factigo64.aplicacion.repositorio;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cat.factigo64.aplicacion.entidad.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long>{

	public Role findByNombre(String nom);
	
}
