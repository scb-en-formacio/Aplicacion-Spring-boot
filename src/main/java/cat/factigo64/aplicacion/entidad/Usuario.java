package cat.factigo64.aplicacion.entidad;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Usuario implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5171404643032430588L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="native")
	@GenericGenerator(name="native", strategy="native")
	private Long id;

	@Column
	@NotBlank
	@Size(min = 2, max = 25, message = "no se cumplen las reglas de tamaño de campo")
	private String nombre;

	@Column
	@NotBlank	// si el campo es numerico: int, byte, ... se usa NotNull 
	@Size(min = 2, max = 50)
	private String apellidos;

	@Column(unique = true)
	@NotBlank
	@Email
	private String email;

	@Column(unique = true)
	@NotBlank
	@Size(min = 6, max = 15)
	private String username;

	@Column
	@NotBlank
	@Size(min = 6, message = "ojo con el tamaño de contraseña")
	private String contrasenya;

	//ya que esta no será una columna de la bbdd, se anota con:
	@Transient
	private String confirmaContrasenya;
	
	//este campo se anota para enlazarse con la entidad de enlace y se especifican los campos para ello
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "usuario_roles",
			joinColumns = @JoinColumn(name = "usuario_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getContrasenya() {
		return contrasenya;
	}

	public void setContrasenya(String contrasenya) {
		this.contrasenya = contrasenya;
	}

	public String getConfirmaContrasenya() {
		return confirmaContrasenya;
	}

	public void setConfirmaContrasenya(String confirmaContrasenya) {
		this.confirmaContrasenya = confirmaContrasenya;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nombre=" + nombre + ", apellidos=" + apellidos + ", email=" + email
				+ ", username=" + username + ", contrasenya=" + contrasenya + ", confirmaContrasenya="
				+ confirmaContrasenya + ", roles=" + roles + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((apellidos == null) ? 0 : apellidos.hashCode());
		result = prime * result + ((confirmaContrasenya == null) ? 0 : confirmaContrasenya.hashCode());
		result = prime * result + ((contrasenya == null) ? 0 : contrasenya.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + ((roles == null) ? 0 : roles.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (apellidos == null) {
			if (other.apellidos != null)
				return false;
		} else if (!apellidos.equals(other.apellidos))
			return false;
		if (confirmaContrasenya == null) {
			if (other.confirmaContrasenya != null)
				return false;
		} else if (!confirmaContrasenya.equals(other.confirmaContrasenya))
			return false;
		if (contrasenya == null) {
			if (other.contrasenya != null)
				return false;
		} else if (!contrasenya.equals(other.contrasenya))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (roles == null) {
			if (other.roles != null)
				return false;
		} else if (!roles.equals(other.roles))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	
	
}
