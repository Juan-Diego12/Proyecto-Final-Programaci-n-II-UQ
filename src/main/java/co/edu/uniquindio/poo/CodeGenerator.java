package co.edu.uniquindio.poo;

import java.util.Random;

/**
 * La clase CodeGenerator se utiliza para generar códigos aleatorios.
 */
public class CodeGenerator {

    /**
     * Genera un código aleatorio de 6 caracteres.
     * El código puede contener letras mayúsculas y dígitos.
     *
     * @return Un código aleatorio de 6 caracteres.
     */
    public static String generateCode() {
        int length = 6;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder code = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }
        return code.toString();
    }
}
