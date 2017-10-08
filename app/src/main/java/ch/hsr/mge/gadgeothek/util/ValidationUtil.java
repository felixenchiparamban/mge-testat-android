package ch.hsr.mge.gadgeothek.util;

public class ValidationUtil {
    public static boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    public static boolean isEmailValid(String email){
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
 }