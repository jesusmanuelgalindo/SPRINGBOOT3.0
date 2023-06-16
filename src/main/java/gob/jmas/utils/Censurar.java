package gob.jmas.utils;

import org.springframework.stereotype.Component;

@Component
public class Censurar {

    public String censurarEmail(String email) {
        int arrobaIndex = email.indexOf('@');

        if (arrobaIndex < 3) {
            return email;
        }

        String nombre = email.substring(0, 2);
        String dominio = email.substring(arrobaIndex - 2);

        StringBuilder censurado = new StringBuilder();
        censurado.append(nombre);

        int numAsteriscos = arrobaIndex - 4;
        StringBuilder asteriscos = new StringBuilder();
        for (int i = 0; i < numAsteriscos; i++) {
            asteriscos.append('*');
        }
        censurado.append(asteriscos);
        censurado.append(dominio);

        return censurado.toString();
    }
}
