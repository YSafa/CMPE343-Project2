package users;

import interfaces.*;
import java.sql.*;
import java.util.*;
import utils.*;
import static utils.ConsoleUtils.*;

/**
 * Represents a Tester user with permissions to view, search, and sort contacts.
 */
public class Tester extends User implements Searchable, Sortable
{
    /**
     * Constructs a Tester from a ResultSet.
     * @param rs user record from the database
     * @throws SQLException if a database error occurs
     */
    public Tester(ResultSet rs) throws SQLException { super(rs); }

    /**
     * Displays the Tester menu and handles user selections.
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
                System.out.println("4. Change Password");
                System.out.println("5. Logout");
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
                        System.out.print("New Password: ");
                        changePassword(sc.nextLine());
                        break;
                    case 5:
                        break;
                    default:
                        System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
                        System.out.println(RED + "Invalid choice! Please select between 1–5." + RESET);
                        System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
                        pause();
                        break;
                }

                if (choice != 5) pause();
            }
            catch (NumberFormatException e)
            {
                System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
                System.out.println(RED + "Invalid input. Please enter a number between 1–5." + RESET);
                System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
                pause();
            }
            catch (Exception e)
            {
                System.out.println(RED + "Unexpected error: " + e.getMessage() + RESET);
                pause();
            }
        } while (choice != 5);
    }

    /** Lists all contacts from the database. */
    void listContacts()
    {
        try (Connection conn = DBUtils.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM contacts"))
        {
            System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
            System.out.println(BLUE + "Contacts:" + RESET);
            System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);

            while (rs.next())
            {

                System.out.printf("[%d] %s %s %s (%s)\n" +
                                "   📞 Primary: %s | ☎ Secondary: %s\n" +
                                "   ✉ Email: %s\n" +
                                "   🔗 LinkedIn: %s\n" +
                                "   🎂 Birth Date: %s\n" +
                                "   🕒 Created: %s | Updated: %s\n\n",
                        rs.getInt("contact_id"),
                        rs.getString("first_name"),
                        rs.getString("middle_name") != null ? rs.getString("middle_name") : "",
                        rs.getString("last_name"),
                        rs.getString("nickname") != null ? rs.getString("nickname") : "",
                        rs.getString("phone_primary") != null ? rs.getString("phone_primary") : "N/A",
                        rs.getString("phone_secondary") != null ? rs.getString("phone_secondary") : "N/A",
                        rs.getString("email") != null ? rs.getString("email") : "N/A",
                        rs.getString("linkedin_url") != null ? rs.getString("linkedin_url") : "N/A",
                        rs.getString("birth_date") != null ? rs.getString("birth_date") : "N/A",
                        rs.getString("created_at"),
                        rs.getString("updated_at"));
            }
        }
        catch (SQLException e)
        {
            System.out.println(RED + "Error: " + e.getMessage() + RESET);
        }
    }

    /**
     * Searches contacts from database.
     * Users can search by first/middle/last name, phone number, or multiple fields.
     */
    @Override
    public void searchContacts()
    {
        Scanner sc = new Scanner(System.in);
        while (true)
        {
            clearScreen();
            System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
            System.out.println(YELLOW + "SEARCH CONTACTS" + RESET);
            System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
            System.out.println("1. Search by First/Middle Name");
            System.out.println("2. Search by Last Name");
            System.out.println("3. Search by Phone Number");
            System.out.println("4. Multi-field Search (custom)");
            System.out.println("5. Return to Menu");
            System.out.print(CYAN + "Select an option (1–5): " + RESET);

            String choice = sc.nextLine().trim();
            if (choice.equals("5")) return;

            String query = "SELECT * FROM contacts WHERE ";
            List<String> params = new ArrayList<>();

            switch (choice)
            {
                case "1":
                    System.out.print(CYAN + "Enter first or middle name: " + RESET);
                    String name = sc.nextLine();
                    query += "(LOWER(first_name) LIKE LOWER(?) OR LOWER(middle_name) LIKE LOWER(?))";
                    params.add("%" + name + "%");
                    params.add("%" + name + "%");
                    break;
                case "2":
                    System.out.print(CYAN + "Enter last name: " + RESET);
                    String lName = sc.nextLine();
                    query += "LOWER(last_name) LIKE LOWER(?)";
                    params.add("%" + lName + "%");
                    break;
                case "3":
                    System.out.print(CYAN + "Enter part of phone number: " + RESET);
                    String phone = sc.nextLine();
                    query += "phone_primary LIKE ? OR phone_secondary LIKE ?";
                    params.add("%" + phone + "%");
                    params.add("%" + phone + "%");
                    break;
                case "4":
                    System.out.println(YELLOW + "Multi-field search:" + RESET);
                    System.out.print(CYAN + "First or Middle Name: " + RESET);
                    String fn = sc.nextLine().trim();
                    System.out.print(CYAN + "Last Name: " + RESET);
                    String ln = sc.nextLine().trim();
                    System.out.print(CYAN + "Nickname: " + RESET);
                    String nn = sc.nextLine().trim();
                    System.out.print(CYAN + "Phone: " + RESET);
                    String ph = sc.nextLine().trim();
                    System.out.print(CYAN + "Email: " + RESET);
                    String em = sc.nextLine().trim();

                    List<String> filters = new ArrayList<>();
                    if (!fn.isEmpty()) {
                        filters.add("(LOWER(first_name) LIKE LOWER(?) OR LOWER(middle_name) LIKE LOWER(?))");
                        params.add("%" + fn + "%");
                        params.add("%" + fn + "%");
                    }
                    if (!ln.isEmpty()) {
                        filters.add("LOWER(last_name) LIKE LOWER(?)");
                        params.add("%" + ln + "%");
                    }
                    if (!nn.isEmpty()) {
                        filters.add("LOWER(nickname) LIKE LOWER(?)");
                        params.add("%" + nn + "%");
                    }
                    if (!ph.isEmpty()) {
                        filters.add("(phone_primary LIKE ? OR phone_secondary LIKE ?)");
                        params.add("%" + ph + "%");
                        params.add("%" + ph + "%");
                    }
                    if (!em.isEmpty()) {
                        filters.add("LOWER(email) LIKE LOWER(?)");
                        params.add("%" + em + "%");
                    }

                    if (filters.isEmpty()) {
                        System.out.println(YELLOW + "No filters entered." + RESET);
                        pause();
                        return;
                    }

                    query += String.join(" AND ", filters);
                    break;
                default:
                    System.out.println(RED + "Invalid choice." + RESET);
                    pause();
                    continue;
            }

            try (Connection conn = DBUtils.connect();
                 PreparedStatement stmt = conn.prepareStatement(query))
            {
                for (int i = 0; i < params.size(); i++)
                    stmt.setString(i + 1, params.get(i));

                ResultSet rs = stmt.executeQuery();
                clearScreen();
                System.out.println(GREEN + "Search Results:" + RESET);

                int count = 0;
                while (rs.next())
                {
                    count++;
                    System.out.printf(
                            "%s[%03d]%s  %s%-12s%s %s%-12s%s %s%-12s%s\n" +
                                    "      📞 %sPrimary:%s %-12s %s| Secondary:%s %-12s\n" +
                                    "%s──────────────────────────────────────────────────────────────────────────────%s\n",
                            YELLOW, rs.getInt("contact_id"), RESET,
                            BRIGHT_PURPLE, rs.getString("first_name"), RESET,
                            BRIGHT_PURPLE, rs.getString("middle_name") != null ? rs.getString("middle_name") : "", RESET,
                            BRIGHT_PURPLE, rs.getString("last_name"), RESET,
                            BLUE, RESET, rs.getString("phone_primary") != null ? rs.getString("phone_primary") : "N/A",
                            BLUE, RESET, rs.getString("phone_secondary") != null ? rs.getString("phone_secondary") : "N/A",
                            GRAY, RESET
                    );
                }

                if (count == 0)
                    System.out.println(YELLOW + "No matching contacts found." + RESET);

            } catch (SQLException e) {
                System.out.println(RED + "Database error: " + e.getMessage() + RESET);
            }

            pause();
        }
    }

    /** Sorts contacts by a chosen field. */
    @Override
    public void sortContacts()
    {
        Scanner sc = new Scanner(System.in);
        System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
        System.out.println(CYAN + "Select the field to sort by:" + RESET);
        System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
        System.out.println("1. First Name");
        System.out.println("2. Last Name");
        System.out.println("3. Birth Date");
        System.out.print(YELLOW + "Enter your choice (1–3): " + RESET);

        String choice = sc.nextLine().trim();
        String field = null;

        switch (choice)
        {
            case "1": field = "first_name"; break;
            case "2": field = "last_name"; break;
            case "3": field = "birth_date"; break;
            default: field = null; break;
        }

        if (field == null)
        {
            System.out.println(RED + "Invalid choice!" + RESET);
            pause();
            return;
        }

        System.out.print(CYAN + "Sort order (A = Ascending, D = Descending): " + RESET);
        String order = sc.nextLine().trim().toUpperCase();
        String sortOrder = order.equals("D") ? "DESC" : "ASC";

        String query = "SELECT * FROM contacts ORDER BY " + field + " " + sortOrder;

        try (Connection conn = DBUtils.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query))
        {
            clearScreen();
            System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
            System.out.println(GREEN + "Contacts sorted by " + field.replace("_", " ") + " (" + sortOrder + "):" + RESET);
            System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);

            int count = 0;
            while (rs.next())
            {
                count++;
                System.out.printf(
                        "%s[%03d]%s  %s%-12s%s %s%-12s%s %s%-12s%s\n" +
                                "      📞 %sPrimary:%s %-11s %s| Secondary:%s %-11s\n" +
                                "      🎂 %sBirth:%s %-10s\n" +
                                "      ✉ %sEmail:%s %s\n" +
                                "      🔗 %sLinkedIn:%s %s\n" +
                                "%s──────────────────────────────────────────────────────────────────────────────%s\n",
                        YELLOW, rs.getInt("contact_id"), RESET,
                        BRIGHT_PURPLE, rs.getString("first_name"), RESET,
                        BRIGHT_PURPLE, rs.getString("middle_name") != null ? rs.getString("middle_name") : "", RESET,
                        BRIGHT_PURPLE, rs.getString("last_name"), RESET,
                        BLUE, RESET, rs.getString("phone_primary") != null ? rs.getString("phone_primary") : "N/A",
                        BLUE, RESET, rs.getString("phone_secondary") != null ? rs.getString("phone_secondary") : "N/A",
                        PURPLE, RESET, rs.getString("birth_date") != null ? rs.getString("birth_date") : "N/A",
                        GREEN, RESET, rs.getString("email") != null ? rs.getString("email") : "N/A",
                        BRIGHT_PURPLE, RESET, rs.getString("linkedin_url") != null ? rs.getString("linkedin_url") : "N/A",
                        GRAY, RESET
                );
            }

            if (count == 0)
            {
                System.out.println(YELLOW + "No contacts found." + RESET);
            }
        }
        catch (SQLException e)
        {
            System.out.println(RED + "Error sorting contacts: " + e.getMessage() + RESET);
        }
    }


}
