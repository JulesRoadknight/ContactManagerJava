import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.function.Executable;
import org.postgresql.util.PSQLException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DatabaseTest {

    Database database;
    Contact contact;
    Contact updateContact;
    Connection connection;
    Statement statement;
    ArrayList<Contact> contactArray;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @Before
    public void setUp() throws SQLException {
        String testString = "Testing";
        InputStream fixedInput = new ByteArrayInputStream(testString.getBytes());
        ConsoleIOSpy consoleIO = new ConsoleIOSpy(fixedInput, outputStream);

        contactArray = new ArrayList<Contact>();
        contact = new Contact("Jamey", "Namerson", "A Palace", "130077", "01/01/1999", "email@email.com", consoleIO);
        updateContact = new Contact("Update First", "Change Last", "Change of Address", "5550123", "30/12/2015", "much@improved.email", consoleIO);
        database = new Database(contactArray, consoleIO, Constants.testContactManagerDB, Constants.DBName);
        connection = DriverManager.getConnection(Constants.testContactManagerDB, "postgres", "contactManager1");
        statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
    }

    @After
    public void tearDown() throws SQLException {
        String sql = "DELETE FROM contactmanagerdb";
        statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.execute(sql);
    }

    @Test
    public void createsNewContact() throws SQLException {
        database.createContact(contact);
        database.createContact(updateContact);
        String getCount = "SELECT * FROM contactmanagerdb";
        ResultSet countAll = statement.executeQuery(getCount);
        countAll.next();
        assertEquals(1, countAll.getRow());
    }

    @Test
    public void deletesContact() throws SQLException {
        database.createContact(contact);

        String getCreatedContactID = "SELECT id FROM contactmanagerdb WHERE first_name = 'Jamey'";
        ResultSet set = statement.executeQuery(getCreatedContactID);
        set.next();
        int contactID = Integer.parseInt(set.getString(1));

        database.deleteContact(contactID);

        String getCount = "SELECT * FROM contactmanagerdb";
        ResultSet countAll = statement.executeQuery(getCount);
        countAll.next();
        assertEquals(0, countAll.getRow());
    }

    @Test
    public void getContact() throws Exception {
        database.createContact(contact);

        String getCreatedContactID = "SELECT id FROM contactmanagerdb WHERE first_name = 'Jamey'";
        ResultSet getID = statement.executeQuery(getCreatedContactID);
        getID.next();
        int contactID = Integer.parseInt(getID.getString(1));

        String getContactByID = "SELECT * FROM contactmanagerdb WHERE id = " + contactID + ";";
        ResultSet setContact = statement.executeQuery(getContactByID);
        setContact.next();

        assertEquals(database.getContact(contactID).getFieldValue(1), setContact.getString("first_name"));
        assertEquals(database.getContact(contactID).getFieldValue(2), setContact.getString("last_name"));
        assertEquals(database.getContact(contactID).getFieldValue(3), setContact.getString("address"));
        assertEquals(database.getContact(contactID).getFieldValue(4), setContact.getString("phone_number"));
        assertEquals(database.getContact(contactID).getFieldValue(5), setContact.getString("dob"));
        assertEquals(database.getContact(contactID).getFieldValue(6), setContact.getString("email"));
    }

    @Test
    public void showContacts() {
        database.createContact(contact);
        database.createContact(contact);
        database.createContact(contact);
        database.createContact(contact);
        database.showContacts();
        System.out.println(database.contactArray.size());
        assertEquals(4, database.contactArray.size());
    }

    @Test(expected = PSQLException.class)
    public void showContactFails() throws SQLException {
        database.createContact(contact);

        String getHighestID = "SELECT id FROM contactmanagerdb ORDER BY id DESC LIMIT 1";
        ResultSet getID = statement.executeQuery(getHighestID);
        int maxID = Integer.parseInt(getID.getString(1));
        int noSuchID = maxID + 1000;

        assertThrows(Exception.class, (Executable) database.getContact(noSuchID));
    }

    @Test
    public void updateContact() {
        database.createContact(contact);
    }
}
