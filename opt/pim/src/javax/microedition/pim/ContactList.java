package javax.microedition.pim;

public interface ContactList extends PIMList {

  Contact createContact();

  Contact importContact(Contact contact);

  void removeContact(Contact contact);

}
