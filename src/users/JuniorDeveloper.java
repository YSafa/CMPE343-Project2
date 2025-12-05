package users;

import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;
import utils.*;
import static utils.ConsoleUtils.*;
import java.util.Stack;

/**
 * Represents a Junior Developer user with Tester permissions.
 * Can update existing contacts in the database.
 */
public class JuniorDeveloper extends Tester
{

    protected static java.util.Stack<String> undoStack = new java.util.Stack<>();

    protected void undoLastAction() {
        if (undoStack.isEmpty()) {
            System.out.println(ConsoleUtils.RED + "Nothing to undo!" + ConsoleUtils.RESET);
            return;
        }
        String sql = undoStack.pop();
        try (Connection conn = DBUtils.connect();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println(ConsoleUtils.GREEN + "Undo successful! Last action reverted." + ConsoleUtils.RESET);
        } catch (SQLException e) {
            System.out.println(ConsoleUtils.RED + "Undo failed: " + e.getMessage() + ConsoleUtils.RESET);
            undoStack.push(sql); 
        }
    }
    // --------------------------------------------------

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
                System.out.println(YELLOW + "0. Undo Last Action (Update Contact)" + RESET);
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
                        changePasswordInteractive();
                        break;
                    case 6:
                        break;
                    case 0:
                        undoLastAction();
                        pause();
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
                 ResultSet rs = stmt.executeQuery("SELECT contact_id, first_name, middle_name, last_name, phone_primary FROM contacts ORDER BY contact_id"))
            {
                while (rs.next())
                {
                    int id = rs.getInt("contact_id");
                    String first = rs.getString("first_name");
                    String middle = rs.getString("middle_name");
                    String last = rs.getString("last_name");
                    String phone = rs.getString("phone_primary");

                    System.out.printf(
                            "%s[%03d]%s  %s%-12s%s %s%-12s%s %s%-12s%s  %s📞%s %s\n",
                            YELLOW, id, RESET,
                            BRIGHT_PURPLE, first, RESET,
                            BRIGHT_PURPLE, (middle != null ? middle : ""), RESET,
                            BRIGHT_PURPLE, (last != null ? last : ""), RESET,
                            BLUE, RESET, (phone != null ? phone : "N/A")
                    );
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
            clearScreen();
            System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
            System.out.println(YELLOW + "Select the field to update:" + RESET);
            System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
            System.out.println("1. First Name");
            System.out.println("2. Middle Name");
            System.out.println("3. Last Name");
            System.out.println("4. Nickname");
            System.out.println("5. Primary Phone (max 10 digit)");
            System.out.println("6. Secondary Phone  (max 10 digit)");
            System.out.println("7. Email  (ssss@example.com)");
            System.out.println("8. LinkedIn URL  (https://linkedin.com/in/example)");
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
            // ===================================================
            //        LOAD OLD VALUE AND SHOW IT TO USER
            // ===================================================

            String oldValue = "";
            try (Connection connOld = DBUtils.connect();
                 PreparedStatement stmtOld = connOld.prepareStatement(
                         "SELECT " + fieldName + " FROM contacts WHERE contact_id=?"))
            {
                stmtOld.setInt(1, contactId);
                ResultSet rsOld = stmtOld.executeQuery();
                if (rsOld.next())
                {
                    oldValue = rsOld.getString(1);
                    if (oldValue == null) oldValue = "";
                }
            }
            // Eski değeri ekranda göster
            System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
            System.out.println(YELLOW + "Current " + fieldLabel + ": " + RESET + BRIGHT_PURPLE + oldValue + RESET);
            System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);



            System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
            System.out.print(CYAN + "Enter new " + fieldLabel + ": " + RESET);
            String newValue = sc.nextLine().trim();

            if (newValue.isEmpty())
            {
                System.out.println(RED + "Value cannot be empty. Update cancelled." + RESET);
                return;
            }



            if (fieldName.equals("first_name") || fieldName.equals("middle_name") ||
                    fieldName.equals("last_name") || fieldName.equals("nickname"))
            {
                if (!newValue.matches("[a-zA-ZçğıöşüÇĞİÖŞÜ]+"))
                {
                    System.out.println(RED + "Invalid name. Only letters are allowed." + RESET);
                    return;
                }
            }

            if (fieldName.equals("phone_primary") || fieldName.equals("phone_secondary"))
            {
                if (!newValue.matches("\\d{10}"))
                {
                    System.out.println(RED + "Invalid phone number. Must be exactly 10 digits." + RESET);
                    return;
                }
            }

            if (fieldName.equals("email"))
            {
                if (!newValue.contains("@") || !newValue.contains("."))
                {
                    System.out.println(RED + "Invalid email format." + RESET);
                    return;
                }
            }

            if (fieldName.equals("linkedin_url"))
            {
                if (!newValue.toLowerCase().contains("linkedin.com"))
                {
                    System.out.println(RED + "Invalid LinkedIn URL. Must contain 'linkedin.com'." + RESET);
                    return;
                }
            }

            if (fieldName.equals("birth_date"))
            {

                if (!newValue.matches("\\d{4}-\\d{2}-\\d{2}"))
                {
                    System.out.println(RED + "Invalid date format. Use YYYY-MM-DD." + RESET);
                    return;
                }

                try {
                    LocalDate date = LocalDate.parse(newValue);


                    if (date.isAfter(LocalDate.now())) {
                        System.out.println(RED + "Birth date cannot be in the future." + RESET);
                        return;
                    }

                } catch (Exception e) {
                    System.out.println(RED + "Invalid date. Cannot parse date." + RESET);
                    return;
                }
            }
            // ===================================================
            //               UNDO QUERY SAVE
            // ===================================================

            String undoQuery = String.format(
                    "UPDATE contacts SET %s='%s', updated_at=NOW() WHERE contact_id=%d",
                    fieldName, oldValue, contactId
            );
            undoStack.push(undoQuery);
            // ===================================================
            //               UPDATE QUERY EXECUTION
            // ===================================================

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

                        if (rs2.next())
                            displaySingleContact(rs2);
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
