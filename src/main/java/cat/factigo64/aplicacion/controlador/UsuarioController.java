package cat.factigo64.aplicacion.controlador;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cat.factigo64.aplicacion.Excepcion.ErrorValidacionCampo;
import cat.factigo64.aplicacion.Excepcion.UsernameOrIdNoEncontrado;
import cat.factigo64.aplicacion.dto.CambiaContrasenyaForm;
import cat.factigo64.aplicacion.entidad.ReseteaContrasenyaToken;
import cat.factigo64.aplicacion.entidad.Role;
import cat.factigo64.aplicacion.entidad.Usuario;
import cat.factigo64.aplicacion.repositorio.ContrasenyaTokenRepositorio;
import cat.factigo64.aplicacion.repositorio.RoleRepository;
import cat.factigo64.aplicacion.repositorio.UsuarioRepository;
import cat.factigo64.aplicacion.servicio.EmailServicio;
import cat.factigo64.aplicacion.servicio.ReseteaContrasenyaTokenServicio;
import cat.factigo64.aplicacion.servicio.UsuarioService;

@Controller
public class UsuarioController {
	
	@Autowired
	RoleRepository rolRepo;
	
	@Autowired
	UsuarioService usuServ;
	
	@Autowired
	UsuarioRepository usuRepo;
	
	@Autowired
	private EmailServicio emailService;

	@Autowired
	ReseteaContrasenyaTokenServicio tokenServ;
	
	//para poder tener más de un valor posible, se ponen las {}
	@GetMapping({"/", "/login"})
	public String index(HttpSession session) {
        session.setAttribute("textoRecuerdo", "Recordar contraseña?");
			//ModelAndView model) {
        //ModelAndView model = new ModelAndView("/index"); <--no
		//model.addObject("textoRecuerdo", "texto pepito enviado");

		//model.addAttribute("textoRecuerdo", "texto pepito enviado");
		return "index";
	}
	
	/* para agregar el registro de nuevos, se crea la bandera 'signup' y para tener un 
	 * subconj de los roles, agregamos una funcion especifica en el repositorio
	 */
	@GetMapping("/registro")
	public String registroNuevo(Model model) {
		Role rol = rolRepo.findByNombre("USUARIO");
		List<Role> roles = Arrays.asList(rol); //esta es la forma corta
		/*List<Role> roles = new ArrayList<Role>();
		roles.add(rol);*/
		model.addAttribute("usuarioForm", new Usuario());
		model.addAttribute("roles", roles);
		model.addAttribute("signup", true);
		
		return "usuario-form/usuario-registro";
	}
	
	//para la respuesta usare el mismo metodo
	@PostMapping("/registro")
	public String postRegistro(@Valid @ModelAttribute("usuarioForm")Usuario user, 
			BindingResult result, ModelMap model) {
		boolean resultado = false;
		Role rol = rolRepo.findByNombre("USUARIO");
		List<Role> roles = Arrays.asList(rol);
		model.addAttribute("usuarioForm", user);
		model.addAttribute("roles", roles);
		model.addAttribute("signup", true);
		
		if(result.hasErrors()) {
			return "usuario-form/usuario-registro";
		} else {
			try {
				resultado  = usuServ.creaUsuario(user);
			} catch (ErrorValidacionCampo evc) {
				result.rejectValue( evc.getNombreCampo(), null, evc.getMessage());
			} catch (Exception e) {
				model.addAttribute("formErrorMessage", e.getMessage());
			}
		}
		model.addAttribute("usuarioList", usuServ.getAllUsuarios());
		model.addAttribute("roles", rolRepo.findAll());

		return "index"; //index();
	}
	
	/*La manera de pasar valores a nuestro HTML es agregandolos al Map del modelo.
	En este caso agregaremos 4:

    userForm: Lo utilizar el formulario de creación de usuario.
    roles: Mostrara la lista de roles disponibles en el formulario.
    userList: Lo utilizaremos para mostrar la lista de usuario en el DataTable
    listTab: Indica que la pestaña list sera la que este activa.
	 */
	@GetMapping("/usuarioForm")
	public String getUsuarioForm(Model modelo) {
		/* si quisieramos que con un usuario se fuera a un sitio diferente, se podría 
		 * agregar la complobacion de admin, por ejemplo:
		 * if(usuServ.isLoggedUserADMIN()) {
			model.addAttribute("usuarioForm", new User());
			model.addAttribute("usuarioList", userService.getAllUsers());
			model.addAttribute("roles",roleRepository.findAll());
			model.addAttribute("listTab","active");
			return "user-form/user-view";
		}else {
			model.addAttribute("datoParaClient1","valor1");
			model.addAttribute("datoParaClient2","valor2");
			return "pagina para cliente.";
		}
		 */
		modelo.addAttribute("usuarioForm", new Usuario());
		modelo.addAttribute("roles", rolRepo.findAll());
		modelo.addAttribute("usuarioList", usuServ.getAllUsuarios());
		modelo.addAttribute("listTab", "active");
		
		return "usuario-form/usuario-view";
	}
	
	/*esta vez recibiremos 2 parametros mas, uno que es el usuario mapeado desde el formulario
	y el otro sera el resultado de ese mapeo
	@PostMapping: Indica para este metodo debe ser un llamado POST y no GET, la ruta debe ser /userForm
    @Valid: Spring verifica los atributos del entity, en este caso @NotBlank y @Email.
    @ModelAttribute: El constr de esta anot recibe el nombre del form html y lo conv a obj Java.
    BindingResult: Contendra la inf del result entre el mapeo del form html y el obj Java User.
	*/
	//@RequestMapping(method="post") <-- notacion antigua
	@PostMapping("/usuarioForm")
	public String postUserForm(@Valid @ModelAttribute("usuarioForm")Usuario user, 
			BindingResult result, ModelMap model) {
		boolean resultado = false;
		if(result.hasErrors()) {
			model.addAttribute("usuarioForm", user);
			model.addAttribute("formTab", "active");
		} else {
			try {
				resultado  = usuServ.creaUsuario(user);
				//una vez creado, para no mostar los dato anteriores, se pone un nuevo Usuario
				model.addAttribute("usuarioForm", new Usuario());
				//luego que pestaña mostar
				model.addAttribute("listTab", "active");
			} catch (ErrorValidacionCampo evc) {//aqui se utiliza el Error personalizado
				result.rejectValue( evc.getNombreCampo(), null, evc.getMessage());
				
				model.addAttribute("usuarioForm", user);
				model.addAttribute("formTab", "active");
				model.addAttribute("usuarioList", usuServ.getAllUsuarios());
				model.addAttribute("roles", rolRepo.findAll());
			} catch (Exception e) {
				model.addAttribute("formErrorMessage", e.getMessage());
				model.addAttribute("usuarioForm", user);
				model.addAttribute("formTab", "active");
				model.addAttribute("usuarioList", usuServ.getAllUsuarios());
				model.addAttribute("roles", rolRepo.findAll());
			}
		}
		model.addAttribute("usuarioList", usuServ.getAllUsuarios());
		model.addAttribute("roles", rolRepo.findAll());

		return "usuario-form/usuario-view";
	}
	
	@GetMapping("/editUsuario/{id}")
	public String getEditUsuarioForm(Model modelo, @PathVariable(name="id") Long id) throws Exception {
		Usuario usuario = usuServ.getUsuarioById(id);
		modelo.addAttribute("usuarioList", usuServ.getAllUsuarios());
		modelo.addAttribute("roles",rolRepo.findAll());
		modelo.addAttribute("usuarioForm", usuario);
		modelo.addAttribute("formTab","active");//Activa el tab del formulario.
		
		modelo.addAttribute("editMode",true);//es la bandera indicadora
		modelo.addAttribute("passwordForm", new CambiaContrasenyaForm(id));
		return "usuario-form/usuario-view";
	}
	@PostMapping("/editUsuario")
	public String postEditUsuarioForm(@Valid @ModelAttribute("usuarioForm")Usuario user, 
			BindingResult result, ModelMap model) {
		boolean resultado = false;
		if(result.hasErrors()) {
			model.addAttribute("usuarioForm", user);
			model.addAttribute("formTab", "active");
			model.addAttribute("editMode",true);
			model.addAttribute("passwordForm",new CambiaContrasenyaForm(user.getId()));
		} else {
			try {
				resultado  = usuServ.modificaUsuario(user);
				if (resultado) {
					//si sale todo bien, se pone un nuevo Usuario
					model.addAttribute("usuarioForm", new Usuario());
					//luego que pestaña mostar
					model.addAttribute("listTab", "active");
				} else {
					
				}
			} catch (Exception e) {
				if(usuServ.isLoggedUserADMIN()) {
					model.addAttribute("formErrorMessage", e.getCause().getCause().getMessage());
				} else {
					model.addAttribute("formErrorMessage", e.getMessage());
				}
				model.addAttribute("usuarioForm", user);
				model.addAttribute("formTab", "active");
				model.addAttribute("usuarioList", usuServ.getAllUsuarios());
				model.addAttribute("roles", rolRepo.findAll());
				model.addAttribute("editMode",true);
				model.addAttribute("passwordForm",new CambiaContrasenyaForm(user.getId()));
			}
		}
		model.addAttribute("usuarioList", usuServ.getAllUsuarios());
		model.addAttribute("roles", rolRepo.findAll());
		return "usuario-form/usuario-view";
	}

	// acción Cancelar, es donde se envia desde el form.. th:href="@...
	@GetMapping("/editUsuario/cancela")
	public String cancelEditUsuario(ModelMap model){
		return "redirect:/usuarioForm"; //esta redireccion es a la acción inicial
	}
	
	@GetMapping("/deleteUsuario/{id}")
	public String eliminaUsuario(Model model, @PathVariable(name="id") Long id) {
		try {
			usuServ.eliminaUsuario(id);
		} catch (UsernameOrIdNoEncontrado e){
			model.addAttribute("listErrorMessage", e.getMessage());
		} catch (Exception e){
			model.addAttribute("deleteError", "No se pudo eliminar el usuario "+ id);
		}
		return getUsuarioForm(model);
	}

	// Para entregar una resp asíncrona, utilizaremos como objeto de retorno
	// ResponseEntity<?>, al cual le podremos asignar codigo http de resp y
	// un mensaje.
	// Si hay error cambiando el password o alguno identificado por la anot @Valid,
	//lo concatenamos en un string y lo regresamos en el cuerpo de un bad request
	//**Aqui arbitrariamente asignamos esta ruta porque cambiamos desde el 'editar'
	@PostMapping("/editUsuario/cambiaContrasenya")
	public ResponseEntity postEditUsuarioChangePassword(
			@Valid @RequestBody CambiaContrasenyaForm cambia, Errors errors) {
		try {
			//If error, just return a 400 bad request, along with the error message
	        if (errors.hasErrors()) {
	            String result = errors.getAllErrors()
	                        .stream().map(x -> x.getDefaultMessage())
	                        .collect(Collectors.joining(""));

	            throw new Exception(result);
	        }
			usuServ.cambiaContrasenya(cambia);
		} catch (Exception e) {
			String mens;
			if(usuServ.isLoggedUserADMIN()) {
				mens = "\n ****causa**\n"+ e.getCause().getCause().getMessage();
			} else {
				mens = "";
			}
			return ResponseEntity.badRequest().body(e.getMessage()+ mens);
		}
		return ResponseEntity.ok("triunfo!");
	}
	
	@GetMapping("/olvideContrasenya")
	public String muestraOlvideContrasenya(Model model) {
		
		//model.addAttribute("usuarioForm", new Usuario());
		//model.addAttribute("roles", roles);
		//model.addAttribute("signup", true);
		
		return "usuario-form/olvide-contrasenya";
	}
	
	@PostMapping("/olvideContrasenya") // "/forgot"
	public ModelAndView processForgotPasswordForm(ModelAndView modelAndView,
			@RequestParam("email") String userEmail, HttpServletRequest request) {

		Usuario user = null;
		try {
			user = usuServ.getUsuarioByEmail(userEmail);
		} catch (Exception e1) {
			// e1.printStackTrace();
			modelAndView.addObject("formErrorMessage", "No se ha encontrado una cuenta con este e-mail.");
			modelAndView.setViewName("usuario-form/olvide-contrasenya");
		}
		if (user == null) {
			modelAndView.addObject("formErrorMessage", "No se ha encontrado una cuenta con este e-mail.");
		} else {
			// Generate random 36-character string token for reset password
			String token = UUID.randomUUID().toString();
			ReseteaContrasenyaToken resetToken = new ReseteaContrasenyaToken(user.getId(), token, userEmail);

			try {
				tokenServ.save(resetToken);
			} catch (Exception e){
				modelAndView.addObject("formErrorMessage", "Error gravando Token ");
				modelAndView.setViewName("usuario-form/olvide-contrasenya");
			}
			StringBuffer serv = new StringBuffer(request.getServerName());
			if (serv.toString().equals("localhost"))
				serv.append(":8080");
			String appUrl = request.getScheme() + "://" + serv;
			
			SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
			passwordResetEmail.setFrom("support@fastigo64.cat");
			passwordResetEmail.setTo(user.getEmail());
			passwordResetEmail.setSubject("Password Reset Request / Restaura contraseña");
			passwordResetEmail.setText("Para restaurar la contraseña, haga click en el siguiente enlace:\n" 
					+ appUrl + "/restaura?token=" + token);
			
			emailService.sendEmail(passwordResetEmail);

			// Add success message to view
			modelAndView.addObject("successMessage", "Enviado correo de restauración de contraseña a: " + userEmail);
		}
		modelAndView.setViewName("index");//usuario-form/olvide-contrasenya
		return modelAndView;
	}
	
	// Display form to reset password
	@GetMapping("/restaura")
	public ModelAndView displayResetPasswordPage(ModelAndView modelAndView, @RequestParam("token") String token) {
		
		try {
			ReseteaContrasenyaToken resetToken = tokenServ.getReseteaContrasenyaTokenByToken(token);
			modelAndView.addObject("restauraForm", resetToken);
		} catch (Exception e1) {
			// e1.printStackTrace();
			modelAndView.addObject("formErrorMessage", "Token no encontrado ...\n"+ e1.getMessage());
			//modelAndView.addObject("errorMessage", "Oops!  This is an invalid password reset link.");
		} finally {
			modelAndView.setViewName("usuario-form/restaura-contrasenya");
			//return modelAndView;
		}
		return modelAndView;
	}
	
	// Process reset password form
	@PostMapping("/restaura")
	public ModelAndView setNewPassword(ModelAndView modelAndView, 
			@RequestParam Map<String, String> requestParams, RedirectAttributes redir) {
		
		String rutaOlvida = "usuario-form/restaura-contrasenya";
		if( requestParams.get("confirmaContrasenya").isEmpty() || requestParams.get("contrasenya").isEmpty()) {
			modelAndView.addObject("formErrorMessage", "deben llenarse los dos campos!");
		} else if( !requestParams.get("confirmaContrasenya").equals(requestParams.get("contrasenya"))) {
			modelAndView.addObject("formErrorMessage", "Contraseña nueva y Confirma contraseña no son iguales!");
		} else {
			// Find the user associated with the reset token
			try {
				ReseteaContrasenyaToken resetToken = tokenServ.getReseteaContrasenyaTokenByToken(requestParams.get("token"));
				if (resetToken != null) {
					try {
						Usuario user = usuServ.getUsuarioByEmail(resetToken.getEmail());
						// pone la nueva contraseña
						user.setContrasenya(usuServ.codifica (requestParams.get("contrasenya")));
						usuRepo.save(user);
				            
						// elimina el token de la tabla
						tokenServ.delete(resetToken);
	
						// In order to set a model attribute on a redirect, we must use
						// RedirectAttributes
						redir.addFlashAttribute("successMessage", "Contraseña actualizada con exito..  Puede ir al login.");
	
						modelAndView.setViewName("redirect:login");
						rutaOlvida = "index";
					} catch (Exception e1) {
						// e1.printStackTrace();
						modelAndView.addObject("formErrorMessage", "No se ha encontrado una cuenta con este e-mail.");
						modelAndView.setViewName(rutaOlvida);
					}
				}
			} catch (Exception e1) {
				// e1.printStackTrace();
				modelAndView.addObject("formErrorMessage", "Token invalido ...");
				//modelAndView.addObject("errorMessage", "Oops!  This is an invalid password reset link.");
			} finally {
				modelAndView.setViewName(rutaOlvida);
			}
		}
		modelAndView.setViewName(rutaOlvida);

		return modelAndView;
   }
   
    // Going to reset page without a token redirects to login page
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ModelAndView handleMissingParams(MissingServletRequestParameterException ex) {
		return new ModelAndView("redirect:login");
	}
}
