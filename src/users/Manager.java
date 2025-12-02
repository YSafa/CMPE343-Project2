package users;

import java.sql.*;
import java.util.Scanner;
import utils.*;
import static utils.ConsoleUtils.*;
import static utils.PasswordUtils.*;

/**
 * Represents a Manager user with administrative privileges.
 */
public class Manager extends SeniorDeveloper
{
    public Manager(ResultSet rs) throws SQLException { super(rs); }

    /**
     * Displays the Manager-specific menu.
     */
    @Override
    public void displayMenu()
    {
        Scanner sc = new Scanner(System.in);
        int choice = 0;

        do {
            try {
                drawHeader(role + " Menu", username, firstName, lastName);
                System.out.println(GREEN + "Welcome, Manager " + firstName + " " + lastName + "!" + RESET);
                System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
                System.out.println("1. Show Contacts Statistics");
                System.out.println("2. List Users");
                System.out.println("3. Add New User");
                System.out.println("4. Update Existing User");
                System.out.println("5. Delete User");
                System.out.println("6. Change My Password");
                System.out.println("7. Logout");
                System.out.print(CYAN + "Select: " + RESET);

                String input = sc.nextLine().trim();
                choice = Integer.parseInt(input);

                switch (choice)
                {
                    case 1:
                        clearScreen();
                        showContactStats();
                        pause();
                        break;

                    case 2:
                        clearScreen();
                        listUsers();
                        pause();
                        break;

                    case 3:
                        clearScreen();
                        addUser(sc);
                        pause();
                        break;

                    case 4:
                        clearScreen();
                        updateUser(sc);
                        pause();
                        break;

                    case 5:
                        clearScreen();
                        deleteUser(sc);
                        pause();
                        break;

                    case 6:
                        clearScreen();
                        System.out.print(CYAN + "New Password: " + RESET);
                        changePassword(sc.nextLine());
                        break;

                    case 7:
                        // logout
                        break;

                    default:
                        System.out.println(RED + "Invalid choice! Please select between 1–7." + RESET);
                        pause();
                        break;
                }


            } catch (Exception e) {
                System.out.println(RED + "Error: " + e.getMessage() + RESET);
                pause();
            }

        } while (choice != 7);
    }

    /**
     * Lists all users from users table.
     */
    private void listUsers()
    {
        try (Connection conn = DBUtils.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM users ORDER BY user_id"))
        {
            System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
            System.out.println(BLUE + "Users:" + RESET);
            System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);

            int count = 0;
            while (rs.next()) {
                count++;
                System.out.printf(
                        "%d) ID: %d  |  Username: %s  |  Role: %s%n" +
                                "   Name: %s %s%n" +
                                "   Created: %s | Updated: %s%n%n",
                        count,
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("role"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("created_at"),
                        rs.getString("updated_at")
                );
            }

            if (count == 0) {
                System.out.println(YELLOW + "No users found." + RESET);
            }

        } catch (SQLException e) {
            System.out.println(RED + "Error while listing users: " + e.getMessage() + RESET);
        }
    }

    /**
     * Adds a new user.
     */
    private void addUser(Scanner sc)
    {
        try {
            System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
            System.out.println(YELLOW + "ADD NEW USER" + RESET);
            System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);

            System.out.print(CYAN + "Username: " + RESET);
            String newUsername = sc.nextLine().trim();

            System.out.print(CYAN + "Password: " + RESET);
            String newPassword = sc.nextLine().trim();

            System.out.print(CYAN + "First Name: " + RESET);
            String fn = sc.nextLine().trim();

            System.out.print(CYAN + "Last Name: " + RESET);
            String ln = sc.nextLine().trim();

            System.out.println(GRAY + "Valid roles: TESTER, JUNIOR_DEV, SENIOR_DEV, MANAGER" + RESET);
            System.out.print(CYAN + "Role: " + RESET);
            String r = sc.nextLine().trim();

            if (newUsername.isEmpty() || newPassword.isEmpty() || fn.isEmpty() || ln.isEmpty() || r.isEmpty()) {
                System.out.println(RED + "All fields are required. User not added." + RESET);
                return;
            }

            String sql = """
                INSERT INTO users (username, password_hash, first_name, last_name, role, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, NOW(), NOW())
                """;

            try (Connection conn = DBUtils.connect();
                 PreparedStatement stmt = conn.prepareStatement(sql))
            {
                stmt.setString(1, newUsername);
                stmt.setString(2, hashPassword(newPassword));
                stmt.setString(3, fn);
                stmt.setString(4, ln);
                stmt.setString(5, r);

                int affected = stmt.executeUpdate();
                if (affected > 0)
                    System.out.println(GREEN + "User added successfully." + RESET);
                else
                    System.out.println(RED + "User could not be added." + RESET);

            }

        } catch (SQLException e) {
            System.out.println(RED + "Database error while adding user: " + e.getMessage() + RESET);
        }
    }

    /**
     * Updates an existing user.
     */
    private void updateUser(Scanner sc)
    {
        try (Connection conn = DBUtils.connect())
        {
            listUsers();
            System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
            System.out.print(CYAN + "Enter user_id to update: " + RESET);
            int uid = Integer.parseInt(sc.nextLine().trim());

            String checkSql = "SELECT * FROM users WHERE user_id = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql))
            {
                checkStmt.setInt(1, uid);
                ResultSet rs = checkStmt.executeQuery();
                if (!rs.next()) {
                    System.out.println(RED + "User not found." + RESET);
                    return;
                }

                String currentUsername = rs.getString("username");
                String currentFirst = rs.getString("first_name");
                String currentLast = rs.getString("last_name");
                String currentRole = rs.getString("role");

                System.out.println(YELLOW + "Leave blank to keep current value." + RESET);
                System.out.print(CYAN + "New Username (" + currentUsername + "): " + RESET);
                String newUsername = sc.nextLine().trim();
                System.out.print(CYAN + "New Password (blank to skip): " + RESET);
                String newPassword = sc.nextLine().trim();
                System.out.print(CYAN + "New First Name (" + currentFirst + "): " + RESET);
                String newFirst = sc.nextLine().trim();
                System.out.print(CYAN + "New Last Name (" + currentLast + "): " + RESET);
                String newLast = sc.nextLine().trim();
                System.out.print(CYAN + "New Role (" + currentRole + "): " + RESET);
                String newRole = sc.nextLine().trim();

                if (newUsername.isEmpty()) newUsername = currentUsername;
                if (newFirst.isEmpty()) newFirst = currentFirst;
                if (newLast.isEmpty()) newLast = currentLast;
                if (newRole.isEmpty()) newRole = currentRole;

                StringBuilder sql = new StringBuilder(
                        "UPDATE users SET username=?, first_name=?, last_name=?, role=?, updated_at=NOW()");
                boolean changePass = !newPassword.isEmpty();
                if (changePass) sql.append(", password_hash=?");
                sql.append(" WHERE user_id=?");

                try (PreparedStatement upd = conn.prepareStatement(sql.toString()))
                {
                    int i = 1;
                    upd.setString(i++, newUsername);
                    upd.setString(i++, newFirst);
                    upd.setString(i++, newLast);
                    upd.setString(i++, newRole);
                    if (changePass) upd.setString(i++, hashPassword(newPassword));
                    upd.setInt(i, uid);

                    int rows = upd.executeUpdate();
                    System.out.println(rows > 0 ? GREEN + "User updated." + RESET : RED + "No changes made." + RESET);
                }
            }
        }
        catch (Exception e) {
            System.out.println(RED + "Error updating user: " + e.getMessage() + RESET);
        }
    }

    /**
     * Deletes a user.
     */
    private void deleteUser(Scanner sc)
    {
        listUsers();
        System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
        System.out.print(CYAN + "Enter user_id to DELETE: " + RESET);

        try (Connection conn = DBUtils.connect();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE user_id=?"))
        {
            int id = Integer.parseInt(sc.nextLine().trim());
            System.out.print(RED + "Are you sure? (y/n): " + RESET);
            if (!sc.nextLine().trim().equalsIgnoreCase("y")) {
                System.out.println(YELLOW + "Cancelled." + RESET);
                return;
            }

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            System.out.println(rows > 0 ? GREEN + "User deleted successfully." + RESET
                    : RED + "User not found." + RESET);

        } catch (Exception e) {
            System.out.println(RED + "Error deleting user: " + e.getMessage() + RESET);
        }
    }

    /**
     * Shows basic statistics about contacts table.
     */
    private void showContactStats()
    {
        try (Connection conn = DBUtils.connect())
        {
            System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
            System.out.println(BLUE + "Contacts Statistics" + RESET);
            System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);

            String statsQuery = """
                SELECT COUNT(*) AS total,
                       SUM(CASE WHEN linkedin_url IS NOT NULL AND linkedin_url <> '' THEN 1 ELSE 0 END) AS with_linkedin,
                       SUM(CASE WHEN linkedin_url IS NULL OR linkedin_url = '' THEN 1 ELSE 0 END) AS without_linkedin
                FROM contacts
                """;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(statsQuery))
            {
                if (rs.next()) {
                    System.out.println("Total contacts       : " + rs.getInt("total"));
                    System.out.println("With LinkedIn URL    : " + rs.getInt("with_linkedin"));
                    System.out.println("Without LinkedIn URL : " + rs.getInt("without_linkedin"));
                }
            }

            String ageQuery = "SELECT MIN(birth_date) AS oldest, MAX(birth_date) AS youngest FROM contacts WHERE birth_date IS NOT NULL";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(ageQuery))
            {
                if (rs.next()) {
                    System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
                    System.out.println("Oldest birth_date   : " + rs.getString("oldest"));
                    System.out.println("Youngest birth_date : " + rs.getString("youngest"));
                }
            }

        } catch (SQLException e) {
            System.out.println(RED + "Database error: " + e.getMessage() + RESET);
        }
    }
}
