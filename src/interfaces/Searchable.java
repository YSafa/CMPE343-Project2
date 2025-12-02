package interfaces;

/**
 * Defines searchable behavior for contacts.
 * Implemented by user roles that can perform contact search operations.
 */
public interface Searchable {
    /**
     * Executes search logic for contacts in the database.
     */
    void searchContacts();
}
