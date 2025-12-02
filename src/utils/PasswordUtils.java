package utils;

/**
 * Handles password hashing and verification.
 */
public class PasswordUtils
{
    /**
     * Hashes a password using SHA-256.
     * Converts the result to a lowercase hexadecimal string.
     *
     * @param password the plaintext password
     * @return hashed password in hex format, or null if error
     */
    public static String hashPassword(String password)
    {
        try
        {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes("UTF-8"));

            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes)
            {
                sb.append(String.format("%02x", b)); // convert to hex
            }
            return sb.toString();
        }
        catch (Exception e)
        {
            System.out.println(ConsoleUtils.RED + "Hashing error: " + e.getMessage() + ConsoleUtils.RESET);
            return null;
        }
    }

    /**
     * Verifies if a plaintext password matches a hashed value.
     * (Optional, used for testing or future extensions.)
     *
     * @param plainPassword plaintext password input
     * @param hashed stored hashed value
     * @return true if passwords match, false otherwise
     */
    public static boolean verifyPassword(String plainPassword, String hashed)
    {
        String hashedInput = hashPassword(plainPassword);
        return hashedInput != null && hashedInput.equals(hashed);
    }
}
