package interfaces;

/**
 * Defines sortable behavior for contacts.
 * Implemented by user roles that can sort contact lists.
 */
public interface Sortable {
    /**
     * Displays sorted contact lists based on selected criteria.
     */
    void sortContacts();
}
