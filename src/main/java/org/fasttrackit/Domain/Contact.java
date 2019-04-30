package org.fasttrackit.Domain;

import org.fasttrackit.Service.AgendaService;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.*;

public class Contact {

    private Agenda agenda = new Agenda();
    private AgendaService agendaService = new AgendaService();
    private Stack<String> searchAfterName = new Stack<>();
    private String firstNameAfterYouChoose;
    private String lastNameAfterYouChoose;
    private String firstName = "firstName";
    private String lastName = "lastName";
    private String phoneNumber;
    private String newLastName;
    private Vector<String> pairFirstName = new Vector<>();
    private Vector<String> pairLastName = new Vector<>();
    private Vector<String> numberPhone = new Vector<>();
    private Stack<String> personsFromAgend = new Stack<>();
    private Vector<String> numberPhoneForUpdateingFirstNameLastName = new Vector<>();
    private List<String> contactDeleting = new ArrayList<>();
    private List<Integer> deleteManyContacts = new ArrayList<>();
    private Stack<String> contactForDelete = new Stack<>();

    private void checkFirstNameOrLastName(String firstNameOrLastName, String fieldName) throws MyException {

        int i;
        for (i = 0; i < fieldName.length(); i++) {
            if ((fieldName.charAt(i) < (int) 'a' || fieldName.charAt(i) > 'z') && (fieldName.charAt(i) < (int) 'A' || fieldName.charAt(i) > 'Z') && fieldName.charAt(i) != ' ' && fieldName.charAt(i) != '\t')
                throw new MyException(firstNameOrLastName + " can contains only letters");
        }

        int numberSpace = 0;
        for (i = 0; i < fieldName.length(); i++) {
            if (fieldName.charAt(i) == ' ' || fieldName.charAt(i) == '\t')
                numberSpace++;
        }

        if (numberSpace == fieldName.length())
            throw new MyException("This is not a " + firstNameOrLastName);

        if (fieldName.trim().charAt(0) < (int) 'A' || fieldName.trim().charAt(0) > (int) 'Z') {
            throw new MyException(firstNameOrLastName + " must begin with uppercase character");
        }
    }

    private void checkPhoneNumber(String phoneNumber) throws MyException {
        int i;
        for (i = 0; i < phoneNumber.trim().length(); i++) {
            if ((phoneNumber.trim().charAt(i) < (int) '0' || phoneNumber.trim().charAt(i) > (int) '9') && phoneNumber.trim().charAt(i) != '+' && phoneNumber.trim().charAt(i) != '-')
                throw new MyException("The phone number must contains only digits, + and/or - sign. Also it can not contains spaces.");
        }
    }

    private String settingFirstName() {
        System.out.println("Write your first name");
        Scanner scanner = new Scanner(System.in);
        String firstName = scanner.nextLine();
        try {
            checkFirstNameOrLastName("first name", firstName);
            return firstName;
        } catch (MyException exception) {
            System.out.println(exception);
            return settingFirstName();
        }
    }

    private String settingLastName() {
        System.out.println("Write your last name");
        Scanner scanner = new Scanner(System.in);
        String lastName = scanner.nextLine();
        try {
            checkFirstNameOrLastName("last name", lastName);
            return lastName;
        } catch (MyException exception) {
            System.out.println(exception);
            return settingLastName();
        }
    }

    private String settingPhoneNumber() throws SQLException, IOException, ClassNotFoundException {
        System.out.println("Write your phone number");
        Scanner scanner = new Scanner(System.in);
        String phoneNumber = scanner.nextLine();
        try {
            checkPhoneNumber(phoneNumber);
            for (Agenda agenda : agendaService.listPhoneNumber()) {
                if (phoneNumber.trim().equals(agenda.getPhoneNumber().trim())) {
                    System.out.println("A contact with this number is existing now. Put other number.");
                    return settingPhoneNumber();
                }
            }
            return phoneNumber;
        } catch (MyException exception) {
            System.out.println(exception);
            return settingPhoneNumber();
        }
    }

    private void writeFirstName() {
        String firstName = settingFirstName();
        agenda.setFirstName(firstName.trim());
    }

    private void writeLastName() {
        String lastName = settingLastName();
        agenda.setLastName(lastName.trim());
    }

    private void writePhoneNumber() throws SQLException, IOException, ClassNotFoundException {
        String phoneNumber = settingPhoneNumber();
        agenda.setPhoneNumber(phoneNumber.trim());
    }

    private void createContact() throws SQLException, IOException, ClassNotFoundException {
        /*create contact*/
        writeFirstName();
        writeLastName();
        writePhoneNumber();

        /*insert contact in database*/
        agendaService.createContact(agenda);
    }

    private void getContacts() throws SQLException, IOException, ClassNotFoundException {
        if (agendaService.getNumberContacts() != 0) {
            System.out.println("The agend is containing follow contacts:");
            for (Agenda phoneList : agendaService.getContact()) {
                System.out.println("Id: " + phoneList.getId() + ",First name: " + phoneList.getFirstName() + ",Last name: " + phoneList.getLastName() + ",Phone number: " + phoneList.getPhoneNumber());
            }
        } else
            System.out.println("You are not having any contact in agend");
    }

    private String searchContact() throws SQLException, IOException, ClassNotFoundException {
        if (agendaService.getNumberContacts() != 0) {
            System.out.println("Select the choice after you are wanting to search a contact: 1-first name, 2-last name");
            Scanner scanner = new Scanner(System.in);
            try {
                int wayChoiceContact = scanner.nextInt();
                if (wayChoiceContact < 1 || wayChoiceContact > 2) return searchContact();
                else if (wayChoiceContact == 1) {
                    //search after first name
                    if (agendaService.getNumberContacts() > 1) {
                        for (FirstNameFromDatabase firstNameFromDatabase : agendaService.getFirstName()) {
                            System.out.println(firstNameFromDatabase.getFirstName());
                            searchAfterName.add(firstNameFromDatabase.getFirstName());
                        }

                        //if we have more than one first name for all contacts
                        if (searchAfterName.size() > 1) {
                            firstNameAfterYouChoose = chooseContactAfterFirstName();
                            System.out.println("The contacts are");
                            searchPhisicallyContact(firstNameAfterYouChoose, agendaService, firstName);
                        }

                        //if we have only a first name for all contacts
                        else {
                            System.out.println("The contacts are");
                            int i = 0;
                            List<Agenda> sameValueFirstName = new ArrayList<>();

                            //put all contacts in list
                            for (Agenda agenda : agendaService.getContact()) {
                                sameValueFirstName.add(agenda);
                            }

                            //display all contacts from list
                            for (i = 0; i < sameValueFirstName.size(); i++) {
                                System.out.println("Id: " + sameValueFirstName.get(i).getId() + " ,First Name: " + sameValueFirstName.get(i).getFirstName() + " ,Last Name: " + sameValueFirstName.get(i).getLastName() + " ,Phone Number: " + sameValueFirstName.get(i).getPhoneNumber());
                            }
                        }
                    }
                    //if agend contains only a contact
                    else {
                        System.out.println("The contacts are");
                        System.out.println("Id: " + agendaService.getContact().get(0).getId() + " ,First name: " + agendaService.getContact().get(0).getFirstName() + " ,Last name: " + agendaService.getContact().get(0).getLastName() + ",Phone number: " + agendaService.getContact().get(0).getPhoneNumber());
                    }

                } else if (wayChoiceContact == 2) {
                    //search after last name
                    if (agendaService.getNumberContacts() > 1) {
                        for (LastNameFromDatabase lastNameFromDatabase : agendaService.getLastName()) {
                            System.out.println(lastNameFromDatabase.getLastName());
                            searchAfterName.add(lastNameFromDatabase.getLastName());
                        }

                        //if we have more than one first name for all contacts
                        if (searchAfterName.size() > 1) {
                            lastNameAfterYouChoose = chooseContactAfterLastName();
                            System.out.println("The contacts are");
                            searchPhisicallyContact(lastNameAfterYouChoose, agendaService, lastName);
                        }

                        //if we have only a first name for all contacts
                        else {
                            System.out.println("The contacts are");
                            int i = 0;
                            List<Agenda> sameValueLastName = new ArrayList<>();

                            //put all contacts in list
                            for (Agenda agenda : agendaService.getContact()) {
                                sameValueLastName.add(agenda);
                            }

                            //display all contacts from list
                            for (i = 0; i < sameValueLastName.size(); i++) {
                                System.out.println("Id: " + sameValueLastName.get(i).getId() + " ,First Name: " + sameValueLastName.get(i).getFirstName() + " ,Last Name: " + sameValueLastName.get(i).getLastName() + " ,Phone Number: " + sameValueLastName.get(i).getPhoneNumber());
                            }
                        }
                    } else {
                        System.out.println("The contacts are");
                        System.out.println("Id: " + agendaService.getContact().get(0).getId() + " ,First name: " + agendaService.getContact().get(0).getFirstName() + " ,Last name: " + agendaService.getContact().get(0).getLastName() + ",Phone number: " + agendaService.getContact().get(0).getPhoneNumber());
                    }
                }
            } catch (InputMismatchException exception) {
                System.out.println("You didn't select a available choice. Try again.");
                return searchContact();
            }
        } else {
            System.out.println("You are not having a contact in your agend.");
        }
        return null;
    }

    private String chooseContactAfterFirstName() {
        System.out.println("Choose contacts after one of the follow first names");
        try {
            int i;
            for (i = 0; i < searchAfterName.size(); i++) {
                if (i != searchAfterName.size() - 1) System.out.print((i + 1) + "-" + searchAfterName.get(i) + ", ");
                else System.out.print((i + 1) + "-" + searchAfterName.get(i) + "\n");
            }

            Scanner scanner = new Scanner(System.in);
            int optionFirstName = scanner.nextInt();
            if (optionFirstName < 1 || optionFirstName > searchAfterName.size()) return chooseContactAfterFirstName();
            else return searchAfterName.get(optionFirstName - 1);
        } catch (InputMismatchException exception) {
            System.out.println("You didn't select a valid option. Try again.");
            return chooseContactAfterFirstName();
        }
    }

    private String chooseContactAfterLastName() {
        int i;
        try {
            System.out.println("Choose contacts after one of the follow last names");
            for (i = 0; i < searchAfterName.size(); i++) {
                if (i != searchAfterName.size() - 1) System.out.print((i + 1) + "-" + searchAfterName.get(i) + ", ");
                else System.out.print((i + 1) + "-" + searchAfterName.get(i) + "\n");
            }

            Scanner scanner = new Scanner(System.in);
            int optionLastName = scanner.nextInt();
            if (optionLastName < 1 || optionLastName > searchAfterName.size()) return chooseContactAfterLastName();
            else return searchAfterName.get(optionLastName - 1);
        } catch (InputMismatchException exception) {
            System.out.println("You didn't select a valid option");
            return chooseContactAfterLastName();
        }
    }

    private void searchPhisicallyContact(String searchAfterFirstNameOrLastName, AgendaService agendaService, String firstNameOrLastName) throws SQLException, IOException, ClassNotFoundException {
        for (Agenda agenda : agendaService.searchContactAfterFirstNameOrLastName(firstNameOrLastName, searchAfterFirstNameOrLastName)) {
            System.out.println("Id: " + agenda.getId() + ",First name: " + agenda.getFirstName() + ",Last name: " + agenda.getLastName() + ",Phone number: " + agenda.getPhoneNumber());
        }
    }

    private String updateContact() throws SQLException, IOException, ClassNotFoundException {
        int numberContacts = agendaService.getNumberContacts();
        try {
            if (numberContacts != 0) {
                String option = optionUpdateContact();
                if (option.trim().equals("y"))
                    chooseFieldForUpdate();
            } else
                System.out.println("You don't have any contact in your agend");
        } catch (MyException exception) {
            System.out.println(exception);
            return updateContact();
        }
        return null;
    }

    private String optionUpdateContact() throws MyException {
        System.out.println("Do you want to update a contact ? Y/N");
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.nextLine();
        if (!answer.trim().equals("y") && !answer.trim().equals("Y") && !answer.trim().equals("n") && !answer.trim().equals("N"))
            throw new MyException("You must to choose between 'yes' and 'no' regard of updateing a contact");

        else if (answer.trim().equals("y") || answer.trim().equals("Y"))
            return "y";
        else if (answer.trim().equals("n") || answer.trim().equals("N"))
            return "n";
        else
            return null;
    }

    private String chooseFieldForUpdate() throws SQLException, IOException, ClassNotFoundException {
        //change the phone number
        selectFirstNameLastNameForUpdate();
        for (int i = 0; i < pairFirstName.size(); i++) {
            personsFromAgend.push(pairFirstName.get(i).concat(" ").concat(pairLastName.get(i)).concat("-").concat(numberPhone.get(i)));
        }

        if (personsFromAgend.size() == 1) {
            System.out.println("Change number at " + personsFromAgend.get(0));
            phoneNumber = writeNewNumberPhone(personsFromAgend.get(0));
            agendaService.updatePhoneNumber(phoneNumber, personsFromAgend.get(0));
        } else {
            String personForUpdateNumber = choicePersonForUpdateNumber();
            phoneNumber = writeNewNumberPhone(personForUpdateNumber);
            //phoneNumber must to be different from others numbers from list
            checkIfNumberExistInAgend(phoneNumber);
            agendaService.updatePhoneNumber(phoneNumber, personForUpdateNumber);
        }
        return null;
    }

    private String checkIfNumberExistInAgend(String phoneNumber) throws SQLException, IOException, ClassNotFoundException {
        for (Agenda agenda : agendaService.getContact()) {
            if (phoneNumber.trim().equals(agenda.getPhoneNumber())) {
                System.out.println("This number belongs at other contact. Choose other number.");
                personsFromAgend.clear();
                return chooseFieldForUpdate();
            }
        }
        return null;
    }

    private String choicePersonForUpdateNumber() {
        try {
            System.out.println("Choice one from follow persons for updateing phone number");
            int i = 1;
            for (String updateNumber : personsFromAgend) {
                if (i != personsFromAgend.size()) System.out.print(i + "-" + updateNumber + ", ");
                else System.out.print(i + "-" + updateNumber + "\n");
                i++;
            }

            Scanner scanner = new Scanner(System.in);
            int choicePerson = scanner.nextInt();
            if (choicePerson < 1 || choicePerson > personsFromAgend.size()) return choicePersonForUpdateNumber();
            else
                return personsFromAgend.get(choicePerson - 1);
        } catch (InputMismatchException exception) {
            System.out.println("You didn't choice a valid option. Try again.");
            return choicePersonForUpdateNumber();
        }
    }

    private String choicePersonForUpdateLastName() {
        try {
            System.out.println("Choice one from follow persons for updateing last name");
            int i = 1;
            for (String updateLastName : personsFromAgend) {
                if (i != personsFromAgend.size()) System.out.print(i + "-" + updateLastName + ", ");
                else System.out.print(i + "-" + updateLastName + "\n");
                i++;
            }

            Scanner scanner = new Scanner(System.in);
            int choicePerson = scanner.nextInt();
            if (choicePerson < 1 || choicePerson > personsFromAgend.size()) return choicePersonForUpdateLastName();
            else
                return personsFromAgend.get(choicePerson - 1);
        } catch (InputMismatchException exception) {
            System.out.println("You didn't choice a valid option. Try again.");
            return choicePersonForUpdateLastName();
        }
    }

    private String writeNewNumberPhone(String personForUpdateNumber) throws SQLException, IOException, ClassNotFoundException {
        System.out.println("Write new phone number");
        String oldNumber = agendaService.getContact(personForUpdateNumber);
        Scanner scanner = new Scanner(System.in);
        String newNumber = scanner.nextLine();
        try {
            checkPhoneNumber(newNumber);
        } catch (MyException exception) {
            System.out.println(exception);
            return writeNewNumberPhone(personForUpdateNumber);
        }
        if (oldNumber.trim().equals(newNumber.trim())) {
            System.out.println("You must put other number.");
            return writeNewNumberPhone(personForUpdateNumber);
        }
        return newNumber;
    }

    private String writeNewLastName(String personForUpdateLastName) {
        System.out.println("Who is new last name for " + personForUpdateLastName + " ?");
        Scanner scanner = new Scanner(System.in);
        String newLastName = scanner.nextLine();
        try {
            checkFirstNameOrLastName("last name", newLastName);
        } catch (MyException exception) {
            System.out.println(exception);
            return writeNewLastName(personForUpdateLastName);
        }
        return newLastName;
    }

    private void selectFirstNameLastNameForUpdate() throws SQLException, IOException, ClassNotFoundException {
        for (Agenda agenda : agendaService.getContact()) {
            numberPhone.addElement(agenda.getPhoneNumber());
        }

        for (Agenda phoneListForFirstName : agendaService.getContact()) {
            pairFirstName.addElement(phoneListForFirstName.getFirstName());
        }

        for (Agenda phoneListForLastName : agendaService.getContact()) {
            pairLastName.addElement(phoneListForLastName.getLastName());
        }
    }

    private void getNumberPhone() throws SQLException, IOException, ClassNotFoundException {
        for (Agenda listPhoneNumber : agendaService.getContact()) {
            numberPhoneForUpdateingFirstNameLastName.addElement(listPhoneNumber.getPhoneNumber());
        }
    }

    private String deleteContact() throws SQLException, IOException, ClassNotFoundException {
        if (agendaService.getNumberContacts() != 0) {
            System.out.println("Do you want to delete a contact ? Y/N");
            BufferedReader deleteContact = new BufferedReader(new InputStreamReader(System.in));
            String choiceDeleteContact = deleteContact.readLine();
            if (!choiceDeleteContact.trim().equals("Y") && !choiceDeleteContact.trim().equals("y") && !choiceDeleteContact.trim().equals("N") && !choiceDeleteContact.trim().equals("n")) {
                System.out.println("You must to choose between Y and N");
                return deleteContact();
            } else if (choiceDeleteContact.equals("Y") || choiceDeleteContact.equals("y")) {
                eraseContact();
            }
        } else
            System.out.println("You are not having a contact in agend.");
        return null;
    }


    private String eraseContact() throws SQLException, IOException, ClassNotFoundException {

        int numberContactInAgend = 0;
        for (Agenda agenda : agendaService.getContact()) {
            numberContactInAgend++;
        }

        //if table contains only a contact
        if (numberContactInAgend == 1) {
            deleteOnePerson();
        }

        //if table contains many contacts
        else if (numberContactInAgend > 1) {
            System.out.println("Do you want to delete 1-one contact or 2-many contacts ?");
            try {
                BufferedReader numberContact = new BufferedReader(new InputStreamReader(System.in));
                int choiceNumberContacts = Integer.parseInt(numberContact.readLine());
                if (choiceNumberContacts < 1 || choiceNumberContacts > 2) return eraseContact();
                else if (choiceNumberContacts == 1)
                    deleteManyPersons(choiceNumberContacts);
                else if (choiceNumberContacts == 2)
                    deleteManyPersons(choiceNumberContacts);
            } catch (NumberFormatException exception) {
                return eraseContact();
            }
        }
        return null;
    }

    private String deleteManyPersons(int choiceNumberContacts) throws SQLException, IOException, ClassNotFoundException {
        int i = 1;
        System.out.println("Select contacts who you want to delete");
        for (Agenda agenda : agendaService.getContact()) {
            contactDeleting.add(agenda.getFirstName() + " " + agenda.getLastName() + "-" + agenda.getPhoneNumber());
        }

        for (String browseContactDeleting : contactDeleting) {
            if (i != contactDeleting.size())
                System.out.print(i + "-" + contactDeleting.get(i - 1) + ", ");
            else System.out.print(i + "-" + contactDeleting.get(i - 1) + "\n");
            i++;
        }

        try {
            BufferedReader deletePerson = new BufferedReader(new InputStreamReader(System.in));
            int choiceDeleteContact = Integer.parseInt(deletePerson.readLine());
            if (choiceDeleteContact < 1 || choiceDeleteContact > contactDeleting.size()) {
                contactDeleting.clear();
                return deleteManyPersons(choiceNumberContacts);
            } else
                //if we have many contacts and we want to delete only a contact
                if (choiceNumberContacts == 1) {
                    agendaService.deleteContact(contactDeleting.get(choiceDeleteContact - 1));
                }

            //if we have many contacts and we want to delete many contacts
            if (choiceNumberContacts == 2) {
                if (!deleteManyContacts.contains(choiceDeleteContact))
                    deleteManyContacts.add(choiceDeleteContact);


                if (deleteManyContacts.size() == 1) {
                    System.out.println("You must to select a new contact for delete");
                    contactDeleting.clear();
                    return deleteManyPersons(choiceNumberContacts);
                }

                if (deleteManyContacts.size() >= 2 && deleteManyContacts.size() < contactDeleting.size()) {
                    String deleteOtherContact = doYouWantToDeleteOtherContact();
                    if (deleteOtherContact.trim().equals("y")) {
                        contactDeleting.clear();
                        return deleteManyPersons(choiceNumberContacts);
                    }
                }
            }

            for (int contact : deleteManyContacts) {
                agendaService.deleteContact(contactDeleting.get(contact - 1));
            }

        } catch (NumberFormatException exception) {
            System.out.println("You didn't choice a valid option. Try again.");
            contactDeleting.clear();
            return deleteManyPersons(choiceNumberContacts);
        }
        return null;
    }

    private String doYouWantToDeleteOtherContact() throws IOException {
        System.out.println("Do you want to delete other contact beside contacts which you choose for delete so far? Y/N");
        BufferedReader answerDeleteOtherContact = new BufferedReader(new InputStreamReader(System.in));
        String answer = answerDeleteOtherContact.readLine();
        if (!answer.equals("Y") && !answer.equals("y") && !answer.equals("N") && !answer.equals("n")) {
            System.out.println("You didn't write a valid answer. Try again.");
            return doYouWantToDeleteOtherContact();
        } else if (answer.equals("Y") || answer.equals("y"))
            return "y";
        else return "n";
    }

    private String deletePerson() throws SQLException, IOException, ClassNotFoundException {
        int i = 1;
        System.out.println("Select a contact who you want to delete");
        for (Agenda agenda : agendaService.getContact()) {
            contactDeleting.add(agenda.getFirstName() + " " + agenda.getLastName() + "-" + agenda.getPhoneNumber());
        }

        for (String browseContactDeleting : contactDeleting) {
            if (i != contactDeleting.size())
                System.out.print(i + "-" + contactDeleting.get(i - 1) + ", ");
            else System.out.print(i + "-" + contactDeleting.get(i - 1) + "\n");
            i++;
        }

        try {
            BufferedReader deletePerson = new BufferedReader(new InputStreamReader(System.in));
            int choiceDeleteContact = Integer.parseInt(deletePerson.readLine());
            if (choiceDeleteContact < 1 || choiceDeleteContact > contactDeleting.size()) {
                contactDeleting.clear();
                return deletePerson();
            } else
                deletePhisicallyContact(contactDeleting.get(choiceDeleteContact - 1));
        } catch (NumberFormatException exception) {
            System.out.println("You didn't choice a valid option. Try again.");
            contactDeleting.clear();
            return deletePerson();
        }
        return null;
    }

    private void deleteOnePerson() throws SQLException, IOException, ClassNotFoundException {
        System.out.println("The following contact will be deleted");
        System.out.println("Id: " + agendaService.getContact().get(0).getId() + " ,First Name: " + agendaService.getContact().get(0).getFirstName() + " ,Last Name: " + agendaService.getContact().get(0).getLastName() + " ,Phone Number: " + agendaService.getContact().get(0).getPhoneNumber());
        agendaService.deleteContact(agendaService.getContact().get(0).getFirstName() + " " + agendaService.getContact().get(0).getLastName() + "-" + agendaService.getContact().get(0).getPhoneNumber());
    }

    private void deletePhisicallyContact(String contactDelete) throws SQLException, IOException, ClassNotFoundException {
        agendaService.deleteContact(contactDelete);
    }

    public void actionsAgenda() throws SQLException, IOException, ClassNotFoundException {
        createContact();
        getContacts();
        searchContact();
        updateContact();
        deleteContact();
    }
}
