package users;

import java.sql.*;
import java.util.Scanner;
import utils.*;
import static utils.ConsoleUtils.*;

/**
 * Represents a Junior Developer user with Tester permissions.
 * Can update existing contacts in the database.
 */
public class JuniorDeveloper extends Tester
{
    public JuniorDeveloper(ResultSet rs) throws SQLException { super(rs); }

    /**
     * Displays the Junior Developer menu and handles user selections.
     * Extends Tester menu with Update Contact permission.
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
                drawHeader(role + " Menu", username, firstName, lastName);
                System.out.println("1. List Contacts");
                System.out.println("2. Search Contacts");
                System.out.println("3. Sort Contacts");
                System.out.println("4. Update Contact");
                System.out.println("5. Change Password");
                System.out.println("6. Logout");
                System.out.print(CYAN + "Select: " + RESET);

                String input = sc.nextLine().trim();
                choice = Integer.parseInt(input);

                switch (choice)
                {
                    case 1:
                        clearScreen();
                        listContacts();
                        break;
                    case 2:
                        clearScreen();
                        searchContacts();
                        break;
                    case 3:
                        clearScreen();
                        sortContacts();
                        break;
                    case 4:
                        clearScreen();
                        updateContact();
                        break;
                    case 5:
                        clearScreen();
                        System.out.print(CYAN + "New Password: " + RESET);
                        String newPass = sc.nextLine();
                        changePassword(newPass);
                        break;
                    case 6:
                        break;
                    default:
                        System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
                        System.out.println(RED + "Invalid choice! Please select between 1–6." + RESET);
                        System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
                        break;
                }

                if (choice != 6)
                    pause();

            }
            catch (NumberFormatException e)
            {
                System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
                System.out.println(RED + "Invalid input. Please enter a number between 1–6." + RESET);
                System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
                pause();
            }
            catch (Exception e)
            {
                System.out.println(RED + "Unexpected error: " + e.getMessage() + RESET);
                pause();
            }

        } while (choice != 6);
    }

    /**
     * Updates an existing contact in the database.
     */
    public void updateContact()
    {
        try
        {
            Scanner sc = new Scanner(System.in);
            clearScreen();

            System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
            System.out.println(YELLOW + "UPDATE CONTACT - Select Contact ID" + RESET);
            System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);

            try (Connection conn = DBUtils.connect();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT contact_id, first_name, last_name FROM contacts"))
            {
                while (rs.next())
                {
                    System.out.printf("[%d] %s %s\n",
                            rs.getInt("contact_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"));
                }
            }

            System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
            System.out.print(CYAN + "Enter contact_id to update: " + RESET);
            String idInput = sc.nextLine().trim();

            int contactId;
            try
            {
                contactId = Integer.parseInt(idInput);
            }
            catch (NumberFormatException e)
            {
                System.out.println(RED + "Invalid number. Returning to menu." + RESET);
                return;
            }

            boolean exists = false;
            try (Connection conn = DBUtils.connect();
                 PreparedStatement stmt = conn.prepareStatement("SELECT * FROM contacts WHERE contact_id=?"))
            {
                stmt.setInt(1, contactId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next())
                {
                    exists = true;
                }
            }

            if (!exists)
            {
                System.out.println(RED + "No contact found with that ID." + RESET);
                return;
            }

            System.out.println(GREEN + "Contact ID confirmed." + RESET);

            // ========== FIELD SELECTION ==========
            System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
            System.out.println(YELLOW + "Select the field to update:" + RESET);
            System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
            System.out.println("1. First Name");
            System.out.println("2. Middle Name");
            System.out.println("3. Last Name");
            System.out.println("4. Nickname");
            System.out.println("5. Primary Phone");
            System.out.println("6. Secondary Phone");
            System.out.println("7. Email");
            System.out.println("8. LinkedIn URL");
            System.out.println("9. Birth Date (YYYY-MM-DD)");
            System.out.println("10. Cancel");
            System.out.print(CYAN + "Select: " + RESET);

            String choice = sc.nextLine().trim();

            String fieldName = null;
            String fieldLabel = null;

            switch (choice)
            {
                case "1": fieldName = "first_name"; fieldLabel = "First Name"; break;
                case "2": fieldName = "middle_name"; fieldLabel = "Middle Name"; break;
                case "3": fieldName = "last_name"; fieldLabel = "Last Name"; break;
                case "4": fieldName = "nickname"; fieldLabel = "Nickname"; break;
                case "5": fieldName = "phone_primary"; fieldLabel = "Primary Phone"; break;
                case "6": fieldName = "phone_secondary"; fieldLabel = "Secondary Phone"; break;
                case "7": fieldName = "email"; fieldLabel = "Email"; break;
                case "8": fieldName = "linkedin_url"; fieldLabel = "LinkedIn URL"; break;
                case "9": fieldName = "birth_date"; fieldLabel = "Birth Date"; break;
                case "10":
                    System.out.println(YELLOW + "Update operation cancelled." + RESET);
                    return;
                default:
                    System.out.println(RED + "Invalid selection." + RESET);
                    return;
            }

            System.out.println(GREEN + "Field selected: " + fieldLabel + RESET);

            System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
            System.out.print(CYAN + "Enter new " + fieldLabel + ": " + RESET);
            String newValue = sc.nextLine().trim();

            if (newValue.isEmpty())
            {
                System.out.println(RED + "Value cannot be empty. Update cancelled." + RESET);
                return;
            }

            String query = "UPDATE contacts SET " + fieldName + "=?, updated_at=NOW() WHERE contact_id=?";

            try (Connection conn = DBUtils.connect();
                 PreparedStatement stmt = conn.prepareStatement(query))
            {
                stmt.setString(1, newValue);
                stmt.setInt(2, contactId);

                int rows = stmt.executeUpdate();

                if (rows > 0)
                {
                    System.out.println(GREEN + "Contact updated successfully." + RESET);
                    try (Connection conn2 = DBUtils.connect();
                         PreparedStatement stmt2 = conn2.prepareStatement("SELECT * FROM contacts WHERE contact_id=?"))
                    {
                        stmt2.setInt(1, contactId);
                        ResultSet rs2 = stmt2.executeQuery();
                        if (rs2.next()) {
                            displaySingleContact(rs2);
                        }
                    }
                    catch (Exception e)
                    {
                        System.out.println(RED + "Error loading updated contact info: " + e.getMessage() + RESET);
                    }
                }
                else
                {
                    System.out.println(RED + "Update failed. No rows modified." + RESET);
                }
            }
            catch (Exception e)
            {
                System.out.println(RED + "Database error: " + e.getMessage() + RESET);
            }

        }
        catch (Exception e)
        {
            System.out.println(RED + "Error in updateContact: " + e.getMessage() + RESET);
        }
    }
}
