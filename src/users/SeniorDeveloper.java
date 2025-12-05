package users;

import java.sql.*;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import utils.*;
import static utils.ConsoleUtils.*;

/**
 * Represents a Senior Developer user with extended privileges: ADD & DELETE.
 */
public class SeniorDeveloper extends JuniorDeveloper
{
    public SeniorDeveloper(ResultSet rs) throws SQLException { super(rs); }

    /**
     * Displays the Senior Developer menu.
     * Inherits Read/Update from Junior, adds Add/Delete.
     */
    @Override
    public void displayMenu()
    {
        Scanner sc = new Scanner(System.in);
        int choice = 0;

        do
        {
            try
            {
                drawHeader(role + " Menu (Senior)", username, firstName, lastName);
                System.out.println("1. List Contacts");
                System.out.println("2. Search Contacts");
                System.out.println("3. Sort Contacts");
                System.out.println("4. Update Contact");

                // --- SENIOR DEVELOPER PRIVILEGES ---
                System.out.println(GREEN + "5. Add New Contact" + RESET);
                System.out.println(RED + "6. Delete Contact" + RESET);

                System.out.println("7. Change Password");
                System.out.println("8. Logout");
                System.out.println(YELLOW + "0. Undo Last Action (Add / Update / Delete Contact)" + RESET);
                System.out.print(CYAN + "Select: " + RESET);

                String input = sc.nextLine().trim();
                if (input.isEmpty()) continue;
                choice = Integer.parseInt(input);

                switch (choice)
                {
                    case 1:
                        clearScreen();
                        listContacts();
                        pause();
                        break;

                    case 2:
                        clearScreen();
                        searchContacts();
                        break;

                    case 3:
                        clearScreen();
                        sortContacts();
                        pause();
                        break;

                    case 4:
                        clearScreen();
                        updateContact();
                        pause();
                        break;

                    case 5:
                        clearScreen();
                        addContact();
                        pause();
                        break;

                    case 6:
                        clearScreen();
                        deleteContact();
                        pause();
                        break;

                    case 7:
                        clearScreen();
                        changePasswordInteractive();
                        break;
                    case 0:
                        undoLastAction();
                        pause();
                        break;
                    case 8:
                        // logout
                        break;
                    default:
                        System.out.println(RED + "Invalid choice!" + RESET);
                        pause();
                        break;
                }

            }
            catch (Exception e)
            {
                System.out.println(RED + "Error: " + e.getMessage() + RESET);
                pause();
            }
        } while (choice != 8);
    }

    /**
     * Adds a new contact using SQL INSERT.
     */
    private void addContact()
    {
        Scanner sc = new Scanner(System.in);
        System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
        System.out.println(GREEN + "ADD NEW CONTACT" + RESET);
        System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);

        String fName;
        do
        {
            System.out.print(CYAN + "First Name (Required): " + RESET);
            fName = sc.nextLine().trim();
            if (fName.isEmpty())
                System.out.println(RED + "First Name cannot be empty!" + RESET);
        } while (fName.isEmpty());

        System.out.print(CYAN + "Middle Name (Optional): " + RESET);
        String middle = sc.nextLine().trim();
        if (middle.isEmpty()) middle = null;

        String lName;
        do
        {
            System.out.print(CYAN + "Last Name (Required): " + RESET);
            lName = sc.nextLine().trim();
            if (lName.isEmpty())
                System.out.println(RED + "Last Name cannot be empty!" + RESET);
        } while (lName.isEmpty());

        String nick;
        do
        {
            System.out.print(CYAN + "Nickname (Required): " + RESET);
            nick = sc.nextLine().trim();
            if (nick.isEmpty())
                System.out.println(RED + "Nickname cannot be empty!" + RESET);
        } while (nick.isEmpty());

        String phone;
        do
        {
            System.out.print(CYAN + "Primary Phone (10 digits, e.g., 5554443322): " + RESET);
            phone = sc.nextLine().trim();
            if (phone.isEmpty() || !phone.matches("\\d{10}"))
            {
                System.out.println(RED + "Invalid phone number! Must be 10 digits." + RESET);
                phone = "";
            }
        } while (phone.isEmpty());

        System.out.print(CYAN + "Secondary Phone (Optional, 10 digits): " + RESET);
        String phoneSecondary = sc.nextLine().trim();

        // Eğer boş girildiyse null yap
        if (phoneSecondary == null || phoneSecondary.isEmpty())
        {
            phoneSecondary = null;
        }// Boş değil ama 10 haneli değilse hata ver
        else if (!phoneSecondary.matches("\\d{10}"))
        {
            System.out.println(RED + "Invalid secondary phone number! Must be 10 digits." + RESET);
            phoneSecondary = null;
        }

        String email;
        do
        {
            System.out.print(CYAN + "Email (Required): " + RESET);
            email = sc.nextLine().trim();
            if (email.isEmpty() || !email.contains("@"))
            {
                System.out.println(RED + "Invalid email address." + RESET);
                email = "";
            }
        } while (email.isEmpty());

        System.out.print(CYAN + "LinkedIn URL (Optional, must contain 'linkedin.com' if entered): " + RESET);
        String linkedin = sc.nextLine().trim();

        // Eğer boşsa direkt null yap
        if (linkedin == null || linkedin.isEmpty())
        {
            linkedin = null;
        }// Boş değilse doğruluğunu kontrol et
        else if (!linkedin.toLowerCase().contains("linkedin.com"))
        {
            System.out.println(RED + "Invalid LinkedIn URL! Must contain 'linkedin.com'." + RESET);
            linkedin = null;
        }

        LocalDate birthDate = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        do
        {
            System.out.print(CYAN + "Birth Date (YYYY-MM-DD): " + RESET);
            String dateInput = sc.nextLine().trim();
            if (dateInput.isEmpty()) {
                System.out.println(RED + "Birth Date is required!" + RESET);
                continue;
            }
            try
            {
                birthDate = LocalDate.parse(dateInput, formatter);
                if (birthDate.isAfter(LocalDate.now()))
                {
                    System.out.println(RED + "Birth date cannot be in the future." + RESET);
                    birthDate = null;
                }
            }
            catch (DateTimeParseException e)
            {
                System.out.println(RED + "Invalid date format." + RESET);
            }
        } while (birthDate == null);

        String sql = """
                INSERT INTO contacts (
                    first_name, middle_name, last_name, nickname,
                    phone_primary, phone_secondary, email,
                    linkedin_url, birth_date, created_at, updated_at
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())
                """;

        try (Connection conn = DBUtils.connect();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, fName);
            stmt.setString(2, middle);
            stmt.setString(3, lName);
            stmt.setString(4, nick);
            stmt.setString(5, phone);
            stmt.setString(6, phoneSecondary);
            stmt.setString(7, email);
            stmt.setString(8, linkedin);
            stmt.setDate(9, java.sql.Date.valueOf(birthDate));

            int rows = stmt.executeUpdate();
            if (rows > 0)
            {
                System.out.println(GREEN + "Contact added successfully!" + RESET);

                // ===== UNDO QUERY SAVE =====
                try (Statement st = conn.createStatement();
                     ResultSet rs = st.executeQuery("SELECT LAST_INSERT_ID() AS id")) {

                    if (rs.next()) {
                        int newId = rs.getInt("id");
                        String undoDelete = "DELETE FROM contacts WHERE contact_id=" + newId;
                        undoStack.push(undoDelete);
                    }

                } catch (SQLException e) {
                    System.out.println(RED + "Failed to record undo for add contact: " + e.getMessage() + RESET);
                }

            } else {
                System.out.println(RED + "Failed to add contact." + RESET);
            }

        }
        catch (SQLException e)
        {
            System.out.println(RED + "Database Error: " + e.getMessage() + RESET);
        }
    }

    /**
     * Deletes a contact using SQL DELETE.
     */
    private void deleteContact()
    {
        Scanner sc = new Scanner(System.in);
        listContacts();
        System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
        System.out.println(RED + "DELETE CONTACT" + RESET);
        System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);

        System.out.print(CYAN + "Enter ID to delete (or 0 to cancel): " + RESET);
        try
        {
            String input = sc.nextLine().trim();
            if (input.isEmpty()) return;
            int id = Integer.parseInt(input);
            if (id == 0) return;

            System.out.print(RED + "Are you sure? (type 'YES'): " + RESET);
            if (!sc.nextLine().trim().equals("YES"))
            {
                System.out.println(YELLOW + "Cancelled." + RESET);
                return;
            }
            try (Connection connBackup = DBUtils.connect();
            PreparedStatement stmtBackup = connBackup.prepareStatement("SELECT * FROM contacts WHERE contact_id=?")) {
            stmtBackup.setInt(1, id); 
            ResultSet rsBackup = stmtBackup.executeQuery();
    
        if (rsBackup.next()) {
            String fn = rsBackup.getString("first_name");
            String mn = rsBackup.getString("middle_name");
            String ln = rsBackup.getString("last_name");
            String nk = rsBackup.getString("nickname");
            String p1 = rsBackup.getString("phone_primary");
            String p2 = rsBackup.getString("phone_secondary");
            String em = rsBackup.getString("email");
            String li = rsBackup.getString("linkedin_url");
            java.sql.Date bd = rsBackup.getDate("birth_date");

            String mnSql = (mn == null) ? "NULL" : "'" + mn + "'";
            String p2Sql = (p2 == null) ? "NULL" : "'" + p2 + "'";
            String liSql = (li == null) ? "NULL" : "'" + li + "'";
            String bdSql = (bd == null) ? "NULL" : "'" + bd.toString() + "'";

            String restoreSql = String.format(
                        "INSERT INTO contacts (contact_id, first_name, middle_name, last_name, nickname, phone_primary, phone_secondary, email, linkedin_url, birth_date, created_at, updated_at) " +
                        "VALUES (%d, '%s', %s, '%s', '%s', '%s', %s, '%s', %s, %s, NOW(), NOW())",
                        id, fn, mnSql, ln, nk, p1, p2Sql, em, liSql, bdSql
                    );
        undoStack.push(restoreSql);
    }
} catch (Exception e) {
    System.out.println("Undo backup error: " + e.getMessage());
}
            try (Connection conn = DBUtils.connect();
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM contacts WHERE contact_id=?"))
            {
                stmt.setInt(1, id);
                int rows = stmt.executeUpdate();
                if (rows > 0)
                    System.out.println(GREEN + "Contact deleted successfully." + RESET);
                else
                    System.out.println(RED + "Contact ID not found." + RESET);
            }
        }
        catch (Exception e)
        {
            System.out.println(RED + "Invalid input or error: " + e.getMessage() + RESET);
        }
    }

    /**
     * Undo the last contact action (add, update, or delete).
     * Uses the stored SQL command to restore the previous state.
     * Shows which type of action was reverted.
     */
    @Override
    protected void undoLastAction()
    {
        if (undoStack.isEmpty())
        {
            System.out.println(RED + "No contact action to undo!" + RESET);
            return;
        }

        String sql = undoStack.pop();

        String actionType;
        if (sql.trim().toUpperCase().startsWith("DELETE")) {
            actionType = "ADD";
        } else if (sql.trim().toUpperCase().startsWith("INSERT")) {
            actionType = "DELETE";
        } else if (sql.trim().toUpperCase().startsWith("UPDATE")) {
            actionType = "UPDATE";
        } else {
            actionType = "UNKNOWN";
        }

        try (Connection conn = DBUtils.connect();
             Statement stmt = conn.createStatement())
        {

            int affected = stmt.executeUpdate(sql);

            if (affected > 0)
            {
                System.out.println(GREEN + "Undo successful! (Reverted: " + actionType + ")" + RESET);

                // Gösterilecek kişi bilgisi (varsa)
                try (ResultSet rs = stmt.executeQuery("SELECT * FROM contacts ORDER BY updated_at DESC, created_at DESC LIMIT 1"))
                {
                    if (rs.next())
                    {
                        String name = rs.getString("first_name") + " " +
                                (rs.getString("middle_name") != null ? rs.getString("middle_name") + " " : "") +
                                rs.getString("last_name");
                        String nick = rs.getString("nickname");

                        switch (actionType)
                        {
                            case "ADD":
                                System.out.printf(RED + "Removed Contact" + RESET);
                                break;
                            case "DELETE":
                                System.out.printf(BLUE + "Restored Contact: %s (%s)%n" + RESET, name.trim(), nick);
                                break;
                            case "UPDATE":
                                System.out.printf(YELLOW + "Reverted Contact: %s (%s)%n" + RESET, name.trim(), nick);
                                break;
                            default:
                                System.out.printf(GRAY + "Affected Contact: %s (%s)%n" + RESET, name.trim(), nick);
                        }
                    }
                }
            } else {
                System.out.println(YELLOW + "Undo executed, but no rows were affected." + RESET);
            }

        } catch (SQLException e) {
            System.out.println(RED + "Undo failed: " + e.getMessage() + RESET);
            undoStack.push(sql); // hata olursa geri koy
        }
    }


}
