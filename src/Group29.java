/**
 * CMPE343 Project #2 - Contact Management System
 * Group 29
 */

import java.sql.*;
import java.util.*;
import utils.*;
import users.*;

/**
 * Entry point for the Contact Management System.
 * Handles authentication, login flow, and role-based menu delegation.
 */
public class Group29
{
    public static void main(String[] args)
    {
        // Opening Animation
        AnimUtils.openingAnim();

        Scanner sc = new Scanner(System.in);
        while (true)
        {
            ConsoleUtils.clearScreen();
            ConsoleUtils.drawHeader("Login", null, null, null);

            System.out.print(ConsoleUtils.CYAN + "Username: " + ConsoleUtils.RESET);
            String username = sc.nextLine();
            System.out.print(ConsoleUtils.CYAN + "Password: " + ConsoleUtils.RESET);
            String password = sc.nextLine();

            User loggedUser = authenticate(username, password);
            if (loggedUser != null)
            {
                System.out.println(ConsoleUtils.GREEN + "\nLogin successful!" + ConsoleUtils.RESET);
                ConsoleUtils.pause();

                // Role-specific menu
                loggedUser.displayMenu();

                // Option to re-login as another user
                String again;
                do {
                    System.out.print(ConsoleUtils.YELLOW + "\nWould you like to login as another user? (y/n): " + ConsoleUtils.RESET);
                    again = sc.nextLine().trim().toLowerCase();

                    if (!again.equals("y") && !again.equals("n")) {
                        System.out.println(ConsoleUtils.RED + "Please enter 'y' or 'n' only." + ConsoleUtils.RESET);
                    }
                } while (!again.equals("y") && !again.equals("n"));

                if (again.equals("n")) break;
            }
            else
            {
                System.out.println(ConsoleUtils.RED + "\nInvalid credentials." + ConsoleUtils.RESET);
                ConsoleUtils.pause();

                String retry;
                do {
                    System.out.print(ConsoleUtils.YELLOW + "\nTry again? (y/n): " + ConsoleUtils.RESET);
                    retry = sc.nextLine().trim().toLowerCase();

                    if (!retry.equals("y") && !retry.equals("n")) {
                        System.out.println(ConsoleUtils.RED + "Please enter 'y' or 'n' only." + ConsoleUtils.RESET);
                    }
                } while (!retry.equals("y") && !retry.equals("n"));

                if (retry.equals("n")) break;
            }
        }

        AnimUtils.closingAnim();
    }

    /**
     * Authenticates the user against the database using hashed password.
     *
     * @param username the entered username
     * @param password the entered password
     * @return a User subclass (Tester, JuniorDeveloper, etc.) if credentials are valid, otherwise null
     */
    public static User authenticate(String username, String password)
    {
        String query = "SELECT * FROM users WHERE BINARY username=? AND BINARY password_hash=?";
        try (Connection conn = DBUtils.connect();
             PreparedStatement stmt = conn.prepareStatement(query))
        {
            stmt.setString(1, username);
            //stmt.setString(2, PasswordUtils.hashPassword(password));
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                String role = rs.getString("role").trim().toUpperCase();
                return switch (role)
                {
                    case "TESTER" -> new Tester(rs);
                    case "JUNIOR_DEV" -> new JuniorDeveloper(rs);
                    case "SENIOR_DEV" -> new SeniorDeveloper(rs);
                    case "MANAGER" -> new Manager(rs);
                    default -> null;
                };
            }
        }
        catch (SQLException e)
        {
            System.out.println(ConsoleUtils.RED + "Database error: " + e.getMessage() + ConsoleUtils.RESET);
        }
        return null;
    }
}
