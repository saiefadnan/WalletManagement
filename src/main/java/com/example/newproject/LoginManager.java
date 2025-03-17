package com.example.newproject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.prefs.Preferences;
public class LoginManager {
    private static Preferences prefs = Preferences.userNodeForPackage(LoginManager.class);
    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = md.digest(password.getBytes());
        StringBuilder hexString = new StringBuilder();

        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b));
        }

        return hexString.toString();
    }
    public static void saveLoginDetails(Integer userID, String username, String password) throws NoSuchAlgorithmException {
        String hashPassword = hashPassword(password);
        prefs.put("userID", String.valueOf(userID));
        prefs.put("username", username);
        prefs.put("password", hashPassword);
    }
    public static String getUserID() {
        return prefs.get("userID", "");
    }

    public static String getUsername() {
        return prefs.get("username", "");
    }

    public static String getPassword() {
        return prefs.get("password", "");
    }
    public static void clearLoginInfo() {
        prefs.remove("username");
        prefs.remove("password");
    }
}
