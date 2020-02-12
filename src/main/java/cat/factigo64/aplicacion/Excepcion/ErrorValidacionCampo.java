package cat.factigo64.aplicacion.Excepcion;

public class ErrorValidacionCampo extends Exception {

	/** se le agrega el 'serial version' generado, porque el IDE lo recomienda
	 * 
	 */
	private static final long serialVersionUID = 2219781413180122751L;
	private String nombreCampo;
	
	/*creando este Obj Error, se puede controlar el mens en un sitio diferente, en este caso
	 * ayuda a posicionar el mens error inmediatamente despues del campo en el form
	 */
	public ErrorValidacionCampo(String mens, String nomCampo) {
		super(mens);
		nombreCampo = nomCampo;
	}
	
	public String getNombreCampo() {
		return nombreCampo;
	}

	public void setNombreCampo(String nombreCampo) {
		this.nombreCampo = nombreCampo;
	}
	
}
