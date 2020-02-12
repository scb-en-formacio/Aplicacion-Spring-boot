package cat.factigo64.aplicacion.util;

import javax.swing.JOptionPane;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PassGenerator {

	public static void main(String ...args) {

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(4);
		
        String clave;
        do {
    		clave = JOptionPane.showInputDialog(null, "Introduce la contraseña");
	        //El String que mandamos al metodo encode es el password que queremos encriptar.
	        System.out.println(bCryptPasswordEncoder.encode(clave));
        } while(!clave.isEmpty());
       
        /*
         * Resultado("1234"): $2a$04$n6WIRDQlIByVFi.5rtQwEOTAzpzLPzIIG/O6quaxRKY2LlIHG8uty
         */
	}
	
}
