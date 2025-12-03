package users;

import java.sql.*;
import utils.*;
import static utils.ConsoleUtils.*;
import java.util.Scanner;

/**
 * Represents a generic system user.
 * Extended by specific user role classes (Tester, Developer, Manager).
 */
public abstract class User
{
    // ========== FIELDS ==========
    protected int id;
    protected String username, firstName, lastName, role;

    // ========== CONSTRUCTOR ==========
    public User(ResultSet rs) throws SQLException
    {
        this.id = rs.getInt("user_id");
        this.username = rs.getString("username");
        this.firstName = rs.getString("first_name");
        this.lastName = rs.getString("last_name");
        this.role = rs.getString("role");
    }

    // ========== ABSTRACT METHODS ==========
    /**
     * Displays the role-specific menu options.
     */
    public abstract void displayMenu();

    // ========== PASSWORD UPDATE ==========
    /**
     * Lets the user change their password.
     * <p>
     * First, it asks for the current password and checks if it is correct.
     * Then it asks for a new password and a confirmation.
     * The password will change only if:
     * <ul>
     *   <li>The old password is correct.</li>
     *   <li>The new password and the confirmation are the same.</li>
     * </ul>
     * <p>
     */
    public void changePasswordInteractive()
    {
        Scanner sc = new Scanner(System.in);

        try (Connection conn = DBUtils.connect();
             PreparedStatement checkStmt = conn.prepareStatement("SELECT password_hash FROM users WHERE user_id=?"))
        {
            // Kullanıcıdan şimdiki şifresini iste
            System.out.print(CYAN + "Enter your current password: " + RESET);
            String currentPass = sc.nextLine().trim();

            checkStmt.setInt(1, id);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next())
            {
                String storedHash = rs.getString("password_hash");

                // Hash doğrulaması
                if (!PasswordUtils.verifyPassword(currentPass, storedHash))
                {
                    System.out.println(RED + "Incorrect current password. Password not changed." + RESET);
                    pause();
                    return;
                }

                // Yeni şifreyi iste
                System.out.print(CYAN + "Enter new password: " + RESET);
                String newPass = sc.nextLine().trim();

                System.out.print(CYAN + "Confirm new password: " + RESET);
                String confirmPass = sc.nextLine().trim();

                if (!newPass.equals(confirmPass))
                {
                    System.out.println(RED + "Passwords do not match. Password not changed." + RESET);
                    pause();
                    return;
                }

                //Şifreyi güncelle
                try (PreparedStatement updateStmt = conn.prepareStatement(
                        "UPDATE users SET password_hash=?, updated_at=NOW() WHERE user_id=?"))
                {
                    updateStmt.setString(1, PasswordUtils.hashPassword(newPass)); // hash ile
                    //updateStmt.setString(1, newPass); // düz metin
                    updateStmt.setInt(2, id);
                    updateStmt.executeUpdate();

                    System.out.println(GREEN + "Password updated successfully." + RESET);
                    pause();
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println(RED + "Error changing password: " + e.getMessage() + RESET);
            pause();
        }
    }

}
