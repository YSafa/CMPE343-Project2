package users;

import java.sql.*;
import utils.*;
import static utils.ConsoleUtils.*;

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
     * Changes the user's password in the database.
     *
     * @param newPass the new password
     */

    // BUNU YAPARKEN ESKİ ŞİFREYİ SORUP KONTROL ETSİN, EĞER ŞİFRELER EŞLEŞİRSE YENİ ŞİFRE GİRİLEBİLSİN !!!!!!!!!!!!!!!!!!!!!!
    public void changePassword(String newPass)
    {
        try (Connection conn = DBUtils.connect();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE users SET password_hash=? WHERE user_id=?"))
        {
            stmt.setString(1, PasswordUtils.hashPassword(newPass));
            stmt.setInt(2, id);
            stmt.executeUpdate();
            System.out.println(GREEN + "Password updated successfully." + RESET);
        }
        catch (SQLException e)
        {
            System.out.println(RED + "Error updating password: " + e.getMessage() + RESET);
        }
    }
}
