import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ContactManagerTest {

    ContactManager contactManager;
    ArrayList<Contact> contactList;
    ConsoleIO consoleIO;
    Contact contact;

    @Before
    public void initialize() {
        contactList = new ArrayList<>();
        contactManager = new ContactManager(consoleIO, contactList);
        contact = new Contact("Namey", "Namerson", "A Palace", "130077", "01/01/1999", "email@email.com");
        this.contactList.add(contact);
    }

    @Test
    public void canReadNewContact() {
        assertEquals("email@email.com", contactManager.returnFieldValue(contact, 6));
    }

    @Test
    public void canDeleteNewContact() {
        this.contactList.remove(0);
        assertTrue(this.contactList.size() == 0);
    }


    @Test
    public void canUpdateFirstName() {
        assertEquals("Namey", contactManager.returnFieldValue(contact, 1));
        contactManager.updateField("Elnamo", contact, 1);
        assertEquals("Elnamo", contactManager.returnFieldValue(contact, 1));
    }

    @Test
    public void canUpdateLastName() {
        assertEquals("Namerson", contactManager.returnFieldValue(contact, 2));
        contactManager.updateField("Sir", contact, 2);
        assertEquals("Sir", contactManager.returnFieldValue(contact, 2));
    }

    @Test
    public void canUpdateAddress() {
        assertEquals("A Palace", contactManager.returnFieldValue(contact, 3));
        contactManager.updateField("7 Palace place", contact, 3);
        assertEquals("7 Palace place", contactManager.returnFieldValue(contact, 3));
    }

    @Test
    public void canUpdatePhoneNumber() {
        assertEquals("130077", contactManager.returnFieldValue(contact, 4));
        contactManager.updateField("01", contact, 4);
        assertEquals("01", contactManager.returnFieldValue(contact, 4));
    }

    @Test
    public void canUpdateDOB() {
        assertEquals("01/01/1999", contactManager.returnFieldValue(contact, 5));
        contactManager.updateField("30/01/13", contact, 5);
        assertEquals("30/01/13", contactManager.returnFieldValue(contact, 5));
    }

    @Test
    public void canUpdateEmail() {
        assertEquals("email@email.com", contactManager.returnFieldValue(contact, 6));
        contactManager.updateField("new@email.com", contact, 6);
        assertEquals("new@email.com", contactManager.returnFieldValue(contact, 6));
    }
}
