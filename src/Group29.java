/**
 * CMPE343 Project #2 - Contact Management System
 * Group 29
 */

import java.sql.*;
import java.util.*;

public class Group29 {

    // ========== ANSI COLORS ==========
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";
    public static final String GRAY = "\u001B[90m";

    // ========== JDBC CONFIG ==========
    private static final String DB_URL = "jdbc:mysql://localhost:3306/cmpe343_proj2";
    private static final String USER = "myuser";
    private static final String PASS = "1234";

    // ========== MAIN ==========
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            clearScreen();
            drawHeader("Login", null);

            System.out.print(CYAN + "Username: " + RESET);
            String username = sc.nextLine();
            System.out.print(CYAN + "Password: " + RESET);
            String password = sc.nextLine();

            User loggedUser = authenticate(username, password);
            if (loggedUser != null) {
                System.out.println(GREEN + "\nLogin successful!" + RESET);
                pause();
                loggedUser.displayMenu();
                // logout sonrası onay
                System.out.print(YELLOW + "\nAre you sure you want to exit? (y/n): " + RESET);
                if (sc.nextLine().equalsIgnoreCase("y")) break;
            } else {
                System.out.println(RED + "\nInvalid credentials." + RESET);
                pause();
                System.out.print(YELLOW + "\nTry again? (y/n): " + RESET);
                if (!sc.nextLine().equalsIgnoreCase("y")) break;
            }
        }
        System.out.println(GREEN + "\nGoodbye 👋" + RESET);
    }

    // ========== UTILITIES ==========
    public static void clearScreen() {
        try {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        } catch (Exception ignored) {}
    }

    public static void drawHeader(String title, String username) {
        clearScreen();
        String line = GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET;
        System.out.println(line);
        if (username != null && !username.isEmpty()) {
            int totalWidth = 70;
            int padLength = totalWidth - (" CMPE343 CONTACT MANAGEMENT SYSTEM ".length() + username.length());
            String padding = " ".repeat(Math.max(1, padLength));
            System.out.println(BLUE + " CMPE343 CONTACT MANAGEMENT SYSTEM " + padding + "[" + username + "]" + RESET);
        } else {
            System.out.println(BLUE + " CMPE343 CONTACT MANAGEMENT SYSTEM" + RESET);
        }
        System.out.println(line);
        System.out.println(YELLOW + " " + title + RESET);
        System.out.println(line);
    }

    public static void pause() {
        System.out.print(GRAY + "\nPress Enter to continue..." + RESET);
        new Scanner(System.in).nextLine();
    }

    // ========== DATABASE AUTH ==========
    public static User authenticate(String username, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM users WHERE username=? AND password_hash=?")) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String role = rs.getString("role").trim().toUpperCase();
                return switch (role) {
                    case "TESTER" -> new Tester(rs);
                    case "JUNIOR_DEV" -> new JuniorDeveloper(rs);
                    case "SENIOR_DEV" -> new SeniorDeveloper(rs);
                    case "MANAGER" -> new Manager(rs);
                    default -> null;
                };
            }
        } catch (SQLException e) {
            System.out.println(RED + "Database error: " + e.getMessage() + RESET);
        }
        return null;
    }

    // ========== ABSTRACT USER ==========
    static abstract class User {
        protected int id;
        protected String username, firstName, lastName, role;

        public User(ResultSet rs) throws SQLException {
            this.id = rs.getInt("user_id");
            this.username = rs.getString("username");
            this.firstName = rs.getString("first_name");
            this.lastName = rs.getString("last_name");
            this.role = rs.getString("role");
        }

        abstract void displayMenu();

        void changePassword(String newPass) {
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                 PreparedStatement stmt = conn.prepareStatement(
                         "UPDATE users SET password_hash=? WHERE user_id=?")) {
                stmt.setString(1, newPass);
                stmt.setInt(2, id);
                stmt.executeUpdate();
                System.out.println(GREEN + "Password updated successfully." + RESET);
            } catch (SQLException e) {
                System.out.println(RED + "Error updating password: " + e.getMessage() + RESET);
            }
        }
    }

    // ========== INTERFACES ==========
    interface Searchable { void searchContacts(); }
    interface Sortable { void sortContacts(); }

    // ========== TESTER CLASS ==========
    static class Tester extends User implements Searchable, Sortable {
        public Tester(ResultSet rs) throws SQLException { super(rs); }

        @Override
        void displayMenu() {
            Scanner sc = new Scanner(System.in);
            int choice = 0;
            do {
                try {
                    drawHeader(role + " Menu", username);
                    System.out.println("1. List Contacts");
                    System.out.println("2. Search Contacts");
                    System.out.println("3. Sort Contacts");
                    System.out.println("4. Change Password");
                    System.out.println("5. Logout");
                    System.out.print(CYAN + "Select: " + RESET);
                    String input = sc.nextLine().trim();
                    choice = Integer.parseInt(input);
                    switch (choice) {
                        case 1 -> { clearScreen(); listContacts(); }
                        case 2 -> { clearScreen(); searchContacts(); }
                        case 3 -> { clearScreen(); sortContacts(); }
                        case 4 -> { clearScreen(); System.out.print("New Password: "); changePassword(sc.nextLine()); }
                        case 5 -> {}
                        default -> {
                            System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
                            System.out.println(RED + "Invalid choice! Please select between 1–5." + RESET);
                            System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
                            pause();
                        }
                    }
                    if (choice != 5) pause();
                } catch (NumberFormatException e) {
                    System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
                    System.out.println(RED + "Invalid input. Please enter a number between 1–5." + RESET);
                    System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
                    pause();
                } catch (Exception e) {
                    System.out.println(RED + "Unexpected error: " + e.getMessage() + RESET);
                    pause();
                }
            } while (choice != 5);
        }

        void listContacts() {
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM contacts")) {
                System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
                System.out.println(BLUE + "Contacts:" + RESET);
                System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
                while (rs.next()) {
                    System.out.printf("%d - %s %s | %s\n",
                            rs.getInt("contact_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"));
                }
            } catch (SQLException e) {
                System.out.println(RED + "Error: " + e.getMessage() + RESET);
            }
        }

        public void searchContacts() {
            Scanner sc = new Scanner(System.in);
            System.out.print("Search term: ");
            String name = sc.nextLine();
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                 PreparedStatement stmt = conn.prepareStatement(
                         "SELECT * FROM contacts WHERE first_name LIKE ? OR last_name LIKE ?")) {
                stmt.setString(1, "%" + name + "%");
                stmt.setString(2, "%" + name + "%");
                ResultSet rs = stmt.executeQuery();
                System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
                System.out.println(BLUE + "Search Results:" + RESET);
                System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
                while (rs.next()) {
                    System.out.printf("%s %s - %s\n",
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"));
                }
            } catch (SQLException e) {
                System.out.println(RED + "Search failed." + RESET);
            }
        }

        public void sortContacts() {
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM contacts ORDER BY last_name ASC")) {
                System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
                System.out.println(GREEN + "Contacts Sorted by Last Name:" + RESET);
                System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
                while (rs.next()) {
                    System.out.printf("%s, %s\n", rs.getString("last_name"), rs.getString("first_name"));
                }
            } catch (SQLException e) {
                System.out.println(RED + "Sort error." + RESET);
            }
        }
    }

    // ========== ROLE CLASSES ==========
    static class JuniorDeveloper extends Tester { public JuniorDeveloper(ResultSet rs) throws SQLException { super(rs); } }
    static class SeniorDeveloper extends JuniorDeveloper { public SeniorDeveloper(ResultSet rs) throws SQLException { super(rs); } }
    static class Manager extends SeniorDeveloper {
        public Manager(ResultSet rs) throws SQLException { super(rs); }

        @Override
        void displayMenu() {
            drawHeader(role + " Menu", username);
            System.out.println(GREEN + "Welcome, Manager " + firstName + "!" + RESET);
            System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
            System.out.println("Future functionality: Add user, remove user, stats, etc.");
            pause();
        }
    }
}
