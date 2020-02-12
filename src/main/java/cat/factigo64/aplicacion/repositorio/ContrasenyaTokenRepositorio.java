package cat.factigo64.aplicacion.repositorio;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cat.factigo64.aplicacion.entidad.ReseteaContrasenyaToken;

@Repository
public interface ContrasenyaTokenRepositorio extends CrudRepository<ReseteaContrasenyaToken, Long> {

	public ReseteaContrasenyaToken findTopByEmail(String mail);
	public ReseteaContrasenyaToken findByToken(String token);
}
