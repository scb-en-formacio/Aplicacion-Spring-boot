package cat.factigo64.aplicacion;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MetodoSecurityConfig extends GlobalMethodSecurityConfiguration{

	/*para que funcionen las expresiones, hay que agregar la libreria de seguridad de
	 * Thymeleaf al pom
	 */
}
