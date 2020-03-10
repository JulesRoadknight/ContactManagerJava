import java.util.ArrayList;

public class ContactManager {

    private ConsoleIO consoleIO;
    private ArrayList<Contact> contactList;

    public ContactManager(ConsoleIO consoleIO, ArrayList<Contact> contactList) {
       this.consoleIO = consoleIO;
       this.contactList = contactList;
    }

    public void printMenuOptions() {
        consoleIO.display("Welcome to Contact Manager\nPlease select an option:\n1. New Contact\n2. Update Contact\n" +
                "3. Delete Contact\n4. View Contacts\n5. Exit");
    }

    public void showMenu() {
        boolean complete = false;
        while (!complete) {
            printMenuOptions();
            int userInput = consoleIO.getMenuInput();
            switch (userInput) {
                case 1: {
                    newContact();
                    break;
                }
                case 2: {
                    updateContact();
                    break;
                }
                case 3: {
                    deleteContact();
                    break;
                }
                case 4: {
                    displayContacts();
                    break;
                }
                default:
                    complete = true;
                    break;
            }
        }
        consoleIO.clearScreen();
    }

    public void newContact() {
        consoleIO.clearScreen();
        Contact contact = new Contact(
                consoleIO.getStringInput(ContactFields.FirstName, false),
                consoleIO.getStringInput(ContactFields.LastName,false),
                consoleIO.getStringInput(ContactFields.Address,false),
                consoleIO.getStringInput(ContactFields.PhoneNumber, false),
                consoleIO.getStringInput(ContactFields.DOB, false) ,
                consoleIO.getStringInput(ContactFields.Email,false
                ));
        contactList.add(contact);
        consoleIO.clearScreen();
    }

    public String getFieldName(int fieldNumber) {
        switch (fieldNumber) {
            case 1: return ContactFields.FirstName;
            case 2: return ContactFields.LastName;
            case 3: return ContactFields.Address;
            case 4: return ContactFields.PhoneNumber;
            case 5: return ContactFields.DOB;
            default: return ContactFields.Email;
        }
    }

    public void updateField(String value, Contact contact, int field) {
        if (isBlank(value)) {
            switch (field) {
                case 1: contact.FirstName = value; break;
                case 2: contact.LastName = value; break;
                case 3: contact.Address = value; break;
                case 4: contact.PhoneNumber = value; break;
                case 5: contact.DOB = value; break;
                default: contact.Email = value; break;
            }
        }
    }

    private boolean isBlank(String value) {
        return !value.matches(String.valueOf(ValidateInput.blankString));
    }

    public void updateContact() {
        if (contactsExist()) {
            displayContacts();
            try {
                String contactNumber = consoleIO.getStringInput("contact choice", true);
                Contact contact = contactList.get(Integer.parseInt(contactNumber) - 1);
                updateContactFields(contact);
            } catch (Exception e) {
                consoleIO.clearScreen();
                consoleIO.display("No such contact");
            }
        }
    }

    public void updateContactFields(Contact contact) {
        consoleIO.clearScreen();
        consoleIO.display("Select a field to update: 1. First name 2. Last name " +
                "3. Address 4. Phone number 5. DOB, 6. Email");
        int field = Integer.parseInt(consoleIO.getStringInput("Field", false));
        consoleIO.display(getFieldName(field) + " is currently: " + returnFieldValue(contact, field));
        updateField(consoleIO.getStringInput(getFieldName(field), true), contact, field);
        consoleIO.clearScreen();
    }

    public String returnFieldValue(Contact contact, int field) {
        switch (field) {
            case 1: return contact.FirstName;
            case 2: return contact.LastName;
            case 3: return contact.Address;
            case 4: return contact.PhoneNumber;
            case 5: return contact.DOB;
            default: return contact.Email;
        }
    }

    public boolean contactsExist() {
        consoleIO.clearScreen();
        if (contactList.size() == 0) {
            consoleIO.display("There are no contacts yet");
            return false;
        } else {
            return true;
        }
    }

    private void deleteContact() {
        if (contactsExist()) {
            displayContacts();
            consoleIO.display("Please select a contact");
            deleteSelectedContact();
        }
    }

    public void deleteSelectedContact() {
        int index = this.consoleIO.getMenuInput();
        consoleIO.clearScreen();
        try {
            this.contactList.remove(index - 1);
            consoleIO.display("Contact deleted");
        } catch (Exception e) {
            consoleIO.display("No such contact");
        }
    }

    public void printContactDetails(Contact contact) {
        consoleIO.display(ContactFields.FirstName + ": " + contact.FirstName + "\n" +
                ContactFields.LastName + ": " + contact.LastName + "\n" +
                ContactFields.Address + ": " + contact.Address + "\n" +
                ContactFields.PhoneNumber + ": " + contact.PhoneNumber + "\n" +
                ContactFields.DOB + ": " + contact.DOB + "\n" +
                ContactFields.Email + ": " + contact.Email
                );
    }

    public void displayContacts() {
        consoleIO.clearScreen();
        if (contactsExist()) {
            displayContactsLoop();
        }
    }

    public void displayContactsLoop() {
        for (int i = 0; i < contactList.size(); i++) {
            consoleIO.display(String.valueOf(i + 1));
            printContactDetails(contactList.get(i));
        }
    }
}
