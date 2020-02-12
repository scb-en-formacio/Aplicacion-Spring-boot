package cat.factigo64.aplicacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import cat.factigo64.aplicacion.servicio.UserDetailsServicioImpl;

//Indica que esta clase es de configuracion y necesita ser cargada durante el inicio del server
@Configuration

//Indica que esta clase sobreescribira la implmentacion de seguridad web, sin esta clase, la
//validacion la hace spring solo para admin, para poder usar nuestros propios usuarios es que
//debe existir
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	/* *****
	 * lo último para complementar la conf es asegurarte que el formulario de
	 *  login tenga los siguientes atributos, en especial el metodo post.
	 *  <form class="col-12" th:action="@{/login}" ...
	 */
	String[] resources = new String[]{
            "/include/**","/css/**","/icons/**","/img/**","/js/**","/layer/**"
    };
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http
        .authorizeRequests()
        .antMatchers(resources).permitAll()  
        .antMatchers("/","/index", "/registro", "/restaura", "/olvideContrasenya").permitAll() // tendria q agregar  para q permitiera pasar
            .anyRequest().authenticated()
            .and()
        .formLogin()
            .loginPage("/login")	//significa que se se permite a todos
            .permitAll()			//los que se validan y etc
            .defaultSuccessUrl("/usuarioForm")
            .failureUrl("/login?error=true")
            .usernameParameter("nomUsuario") //nombre de los 2 campos en el html
            .passwordParameter("parol")
            .and()
            .csrf().disable()
        .logout()
            .permitAll()		//se le permite a cualquiera y si se realiza
            .logoutSuccessUrl("/login?logout");	//se redirecciona a esta ruta
    }
    
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
		bCryptPasswordEncoder = new BCryptPasswordEncoder(4); //este es el nivel de encript
        return bCryptPasswordEncoder;
    }
    
    @Autowired
    UserDetailsServicioImpl userDetailsService;
    /*gracias a este serv se puede llamar esta funcion, que es la que se encarga
     * d hacer el logueo, con el encriptador .
     * luego hay q controlar que ese form (index)tenga:
     * <form class="col-12" th:action="@{/login}" method="post">
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception { 
    	//Especificar el encargado del login y encriptacion del passwordEncoder
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
}
