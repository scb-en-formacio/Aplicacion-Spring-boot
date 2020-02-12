package cat.factigo64.aplicacion.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CambiaContrasenyaForm {

	@NotNull
	private Long id;
	@NotBlank(message="Contraseña actual es obligatoria")
	private String currentPassword;

	@NotBlank(message="Contraseña nueva es obligatoria")
	private String newPassword;

	@NotBlank(message="Confirma contraseña es obligatoria")
	private String confirmPassword;

	public CambiaContrasenyaForm() { }
	public CambiaContrasenyaForm(Long id) {this.id = id;}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCurrentPassword() {
		return currentPassword;
	}
	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

}
