package cat.factigo64.aplicacion.entidad;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ReseteaContrasenyaToken implements Serializable {

	/**
	 */
	private static final long serialVersionUID = 3882985830484139699L;
	  
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private Long id_usuario;
  
    private String token;
  /*
    @OneToOne(targetEntity = Usuario.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "id")//"user_id"
    private Usuario user;*/
    
    private String email;
  
    private Date expiryDate;

	public ReseteaContrasenyaToken() {
		super();
	}

	public ReseteaContrasenyaToken(String token, String mail) {
		super();
		this.token = token;
		this.email = mail;
	}

	public ReseteaContrasenyaToken(Long id_usuario, String token, String email) {
		super();
		this.id_usuario = id_usuario;
		this.token = token;
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId_usuario() {
		return id_usuario;
	}

	public void setId_usuario(Long id_usuario) {
		this.id_usuario = id_usuario;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
    
}
