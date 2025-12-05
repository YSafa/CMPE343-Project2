package users;

import java.sql.*;
import java.util.Scanner;
import java.util.Stack;
import utils.*;
import static utils.ConsoleUtils.*;
import static utils.PasswordUtils.*;

/**
 * Manager user role can manage system users and view contact statistics.
 */
public class Manager extends SeniorDeveloper
{
    /**
     * Keeps SQL undo commands for restoring previous user data.
     */
    protected static Stack<String> undoStack = new Stack<>();

    /**
     * Creates a Manager user using database data.
     *
     * @param rs ResultSet containing the user's row information
     * @throws SQLException if reading column values fails
     */
    public Manager(ResultSet rs) throws SQLException { super(rs); }

    /**
     * Shows the Manager menu and performs actions based on user selection.
     * Loops until the user selects Logout.
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
                System.out.println(YELLOW + "0. Undo Last Action (Add / Update / Delete User)" + RESET);
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
                        changePasswordInteractive();
                        break;

                    case 7:
                        // logout
                        break;

                    case 0:
                        clearScreen();
                        undoLastUserAction();
                        pause();
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
     * Lists all users stored in the database and prints them on screen.
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
     * Adds a new user to the system.
     * Reads user information from console and inserts it into the database.
     *
     * @param sc Scanner used for reading input
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

            String r = "";
            while (true)
            {
                System.out.println(GRAY + "Select Role:" + RESET);
                System.out.println("1) TESTER");
                System.out.println("2) JUNIOR_DEV");
                System.out.println("3) SENIOR_DEV");
                System.out.println("4) MANAGER");
                System.out.print(CYAN + "Select (1-4): " + RESET);

                String roleChoice = sc.nextLine().trim();

                switch (roleChoice)
                {
                    case "1": r = "TESTER"; break;
                    case "2": r = "JUNIOR_DEV"; break;
                    case "3": r = "SENIOR_DEV"; break;
                    case "4": r = "MANAGER"; break;
                    default:
                        System.out.println(RED + "Invalid choice! Please select 1–4." + RESET);
                        continue;
                }
                break;
            }


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
                {
                    System.out.println(GREEN + "User added successfully." + RESET);

                    // ===== UNDO QUERY SAVE =====
                    // Get the ID of the newly added user
                    try (Statement st = conn.createStatement();
                         ResultSet rs = st.executeQuery("SELECT LAST_INSERT_ID() AS id")) {
                        if (rs.next())
                        {
                            int newId = rs.getInt("id");
                            String undoDelete = "DELETE FROM users WHERE user_id=" + newId;
                            undoStack.push(undoDelete);
                        }
                    } catch (SQLException e) {
                        System.out.println(RED + "Failed to record undo for add operation: " + e.getMessage() + RESET);
                    }

                } else {
                    System.out.println(RED + "User could not be added." + RESET);
                }


            }

        } catch (SQLException e) {
            System.out.println(RED + "Database error while adding user: " + e.getMessage() + RESET);
        }
    }

    /**
     * Updates an existing user's information.
     * Empty fields keep their old values.
     * Role selection is validated using a numeric menu.
     *
     * @param sc Scanner used for reading input
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

                String currentHash = rs.getString("password_hash");


                System.out.println(YELLOW + "Leave blank to keep current value." + RESET);
                System.out.print(CYAN + "New Username (" + currentUsername + "): " + RESET);
                String newUsername = sc.nextLine().trim();
                System.out.print(CYAN + "New Password (blank to skip): " + RESET);
                String newPassword = sc.nextLine().trim();
                System.out.print(CYAN + "New First Name (" + currentFirst + "): " + RESET);
                String newFirst = sc.nextLine().trim();
                System.out.print(CYAN + "New Last Name (" + currentLast + "): " + RESET);
                String newLast = sc.nextLine().trim();
                String newRole = null;

                while (true) {
                    System.out.println(GRAY + "Select New Role (leave blank to keep current)" + RESET);
                    System.out.println("1) TESTER");
                    System.out.println("2) JUNIOR_DEV");
                    System.out.println("3) SENIOR_DEV");
                    System.out.println("4) MANAGER");
                    System.out.print(CYAN + "Select (1-4) or press Enter to keep (" + currentRole + "): " + RESET);

                    String roleInput = sc.nextLine().trim();

                    if (roleInput.isEmpty()) {
                        newRole = currentRole;
                        break;
                    }

                    switch (roleInput) {
                        case "1": newRole = "TESTER"; break;
                        case "2": newRole = "JUNIOR_DEV"; break;
                        case "3": newRole = "SENIOR_DEV"; break;
                        case "4": newRole = "MANAGER"; break;

                        default:
                            System.out.println(RED + "Invalid role! Please select 1–4 or press Enter." + RESET);
                            continue;
                    }
                    break;
                }



                if (newUsername.isEmpty()) newUsername = currentUsername;
                if (newFirst.isEmpty()) newFirst = currentFirst;
                if (newLast.isEmpty()) newLast = currentLast;

                // ===== UNDO QUERY SAVE =====
                String undoQuery = String.format(
                        "UPDATE users SET username='%s', first_name='%s', last_name='%s', role='%s', password_hash='%s', updated_at=NOW() WHERE user_id=%d",
                        currentUsername, currentFirst, currentLast, currentRole, currentHash, uid
                );
                undoStack.push(undoQuery);


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
     * Deletes a user by user_id.
     * A Manager cannot delete their own account.
     *
     * @param sc Scanner used for reading input
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

            // manager can't delete itself
            if (id == this.id)
            {
                System.out.println(RED + "You cannot delete your own account!" + RESET);
                return;
            }

            System.out.print(RED + "Are you sure? (y/n): " + RESET);
            if (!sc.nextLine().trim().equalsIgnoreCase("y")) {
                System.out.println(YELLOW + "Cancelled." + RESET);
                return;
            }

            // ===== UNDO QUERY SAVE (DELETE REVERSE) =====
            String backupSql = String.format(
                    "SELECT * FROM users WHERE user_id=%d", id
            );
            try (Statement backupStmt = conn.createStatement();
                 ResultSet rs = backupStmt.executeQuery(backupSql)) {
                if (rs.next()) {
                    String undoInsert = String.format(
                            "INSERT INTO users (user_id, username, password_hash, first_name, last_name, role, created_at, updated_at) " +
                                    "VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                            rs.getInt("user_id"),
                            rs.getString("username").replace("'", "''"),
                            rs.getString("password_hash").replace("'", "''"),
                            rs.getString("first_name").replace("'", "''"),
                            rs.getString("last_name").replace("'", "''"),
                            rs.getString("role"),
                            rs.getString("created_at"),
                            rs.getString("updated_at")
                    );
                    undoStack.push(undoInsert);
                }
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
     * Reverts the last user modification (Add, Update, or Delete) using the undo stack.
     */
    protected void undoLastUserAction() {
        if (undoStack.isEmpty()) {
            System.out.println(RED + "No user action to undo!" + RESET);
            return;
        }

        // Pop last SQL undo command
        String sql = undoStack.pop();

        try (Connection conn = DBUtils.connect();
             Statement stmt = conn.createStatement()) {

            int affected = stmt.executeUpdate(sql);

            if (affected > 0) {
                System.out.println(GREEN + "Undo successful! Last user change reverted." + RESET);
                System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
                System.out.println(YELLOW + "Last change has been undone from the system." + RESET);
                System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);

                // ===== Extract user_id from the SQL =====
                int uid = -1;
                String lower = sql.toLowerCase();
                int idx = lower.indexOf("where user_id=");
                if (idx != -1) {
                    try {
                        uid = Integer.parseInt(sql.substring(idx + 13).replaceAll("[^0-9]", ""));
                    } catch (NumberFormatException ignored) {}
                } else if (lower.contains("values (")) {
                    // If it was an INSERT undo (from delete), try to get the inserted id
                    String[] parts = sql.split("[(),]");
                    for (String p : parts) {
                        p = p.trim();
                        if (p.matches("\\d+")) {
                            uid = Integer.parseInt(p);
                            break;
                        }
                    }
                }

                // ===== Show restored user info if available =====
                if (uid != -1) {
                    try (ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE user_id=" + uid)) {
                        if (rs.next()) {
                            System.out.printf(BLUE + "Restored User: %s %s (%s) [Role: %s]%n" + RESET,
                                    rs.getString("first_name"),
                                    rs.getString("last_name"),
                                    rs.getString("username"),
                                    rs.getString("role"));
                        } else {
                            System.out.println(GRAY + "(Restored user record not found in database.)" + RESET);
                        }
                    }
                } else {
                    System.out.println(GRAY + "(User ID could not be determined from undo query.)" + RESET);
                }

            } else {
                System.out.println(YELLOW + "Undo executed, but no rows were affected." + RESET);
            }

        } catch (SQLException e) {
            System.out.println(RED + "Undo failed: " + e.getMessage() + RESET);
            // If undo failed, push back to stack for retry safety
            undoStack.push(sql);
        }
    }



    /**
     * Displays contact-related statistics such as total count, LinkedIn usage,
     * oldest and youngest birth dates, and average age.
     */
    private void showContactStats()
    {
        try (Connection conn = DBUtils.connect()) {
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
                 ResultSet rs = stmt.executeQuery(statsQuery)) {
                if (rs.next()) {
                    System.out.println("Total contacts       : " + rs.getInt("total"));
                    System.out.println("With LinkedIn URL    : " + rs.getInt("with_linkedin"));
                    System.out.println("Without LinkedIn URL : " + rs.getInt("without_linkedin"));
                }
            }

            String ageQuery = "SELECT MIN(birth_date) AS oldest, MAX(birth_date) AS youngest FROM contacts WHERE birth_date IS NOT NULL";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(ageQuery)) {
                if (rs.next()) {
                    System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
                    System.out.println("Oldest birth_date   : " + rs.getString("oldest"));
                    System.out.println("Youngest birth_date : " + rs.getString("youngest"));
                }
            }
            String avgAgeQuery = """
                    SELECT AVG(TIMESTAMPDIFF(YEAR, birth_date, CURDATE())) AS avg_age
                    FROM contacts
                    WHERE birth_date IS NOT NULL
                    """;


            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(avgAgeQuery)) {
                if (rs.next()) {
                    System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
                    double avgAge = rs.getDouble("avg_age");

                    if (rs.wasNull()) {
                        System.out.println("Average age         : N/A (no birth dates available)");
                    } else {
                        System.out.println("Average age         : " + String.format("%.2f", avgAge));
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println(RED + "Database error: " + e.getMessage() + RESET);
            }
        }
    }
