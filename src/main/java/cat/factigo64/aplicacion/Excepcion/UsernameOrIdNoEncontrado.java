package cat.factigo64.aplicacion.Excepcion;

public class UsernameOrIdNoEncontrado extends Exception {

	/** se le agrega el 'serial version' generado, porque el IDE lo recomienda
	 * 
	 */
	private static final long serialVersionUID = -722121580863731695L;
	
	public UsernameOrIdNoEncontrado() {
		super("Usuario o Id no encontrados");
	}
	
	public UsernameOrIdNoEncontrado(String mens) {
		super(mens);
	}
}
