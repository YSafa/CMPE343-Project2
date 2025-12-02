package utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Utility class for console operations and ANSI color codes.
 */
public class ConsoleUtils
{
    // ========== ANSI COLORS ==========
    /** ANSI reset color code */
    public static final String RESET = "\u001B[0m";
    /** ANSI red color code */
    public static final String RED = "\u001B[31m";
    /** ANSI green color code */
    public static final String GREEN = "\u001B[32m";
    /** ANSI yellow color code */
    public static final String YELLOW = "\u001B[33m";
    /** ANSI blue color code */
    public static final String BLUE = "\u001B[34m";
    /** ANSI purple color code */
    public static final String PURPLE = "\u001B[35m";
    /** ANSI cyan color code */
    public static final String CYAN = "\u001B[36m";
    /** ANSI gray color code */
    public static final String GRAY = "\u001B[90m";
    /** ANSI bright purple color code */
    public static final String BRIGHT_PURPLE = "\u001B[95m";

    // ========== UTILITIES ==========

    /**
     * Clears the console screen using ANSI escape codes.
     */
    public static void clearScreen()
    {
        try {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        } catch (Exception ignored) {}
    }

    /**
     * Draws a formatted header with title and optional username.
     *
     * @param title the title to display
     * @param username the username of the logged-in user (nullable)
     */
    public static void drawHeader(String title, String username, String firstName, String lastName)
    {
        clearScreen();
        String line = GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET;
        System.out.println(line);
        if (username != null && !username.isEmpty())
        {
            String displayName = firstName + " " + lastName;
            int totalWidth = 70;
            int padLength = totalWidth - ("CMPE343 CONTACT MANAGEMENT SYSTEM ".length() + displayName.length());
            String padding = " ".repeat(Math.max(1, padLength));
            System.out.println(RED + "CMPE343 CONTACT MANAGEMENT SYSTEM " + padding + "[" + displayName + "]" + RESET);
        }
        else
        {
            System.out.println(RED + "CMPE343 CONTACT MANAGEMENT SYSTEM" + RESET);
        }
        System.out.println(line);
        System.out.println(YELLOW + " " + title + RESET);
        System.out.println(line);
    }

    /**
     * Pauses the program execution until the user presses Enter.
     */
    public static void pause()
    {
        System.out.print(GRAY + "\nPress Enter to continue..." + RESET);
        new Scanner(System.in).nextLine();
    }

    /** Displays a single contact's full information in a consistent format. */
    public static void displaySingleContact(ResultSet rs)
    {
        try
        {
            System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);
            System.out.println(BLUE + "Contact Details:" + RESET);
            System.out.println(GRAY + "──────────────────────────────────────────────────────────────────────────────" + RESET);

            System.out.printf(
                    "%s %s %s (%s)\n" +
                            "📞 Primary: %s | ☎ Secondary: %s\n" +
                            "✉ Email: %s\n" +
                            "🔗 LinkedIn: %s\n" +
                            "🎂 Birth Date: %s\n" +
                            "🕒 Created: %s | Updated: %s\n",
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
                    rs.getString("updated_at")
            );
        }
        catch (SQLException e)
        {
            System.out.println(RED + "Error displaying contact: " + e.getMessage() + RESET);
        }
    }

}
