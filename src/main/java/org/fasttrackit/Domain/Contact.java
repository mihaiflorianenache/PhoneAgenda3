package org.fasttrackit.Domain;

import org.fasttrackit.Service.AgendaService;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Stack;

public class Contact {

    private Agenda agenda = new Agenda();
    private AgendaService agendaService = new AgendaService();
    private Stack<String> searchAfterName = new Stack<>();
    private String firstNameAfterYouChoose;
    private String lastNameAfterYouChoose;
    private String firstName="firstName";
    private String lastName="lastName";
    private String phoneNumber="phoneNumber";
    private HashMap<String,String> pairFirstNameLastName=new HashMap<>();

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
                throw new MyException("The phone number must contains only digits, + and/or - sign");
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

    private String settingPhoneNumber() {
        System.out.println("Write your phone number");
        Scanner scanner = new Scanner(System.in);
        String phoneNumber = scanner.nextLine();
        try {
            checkPhoneNumber(phoneNumber);
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

    private void writePhoneNumber() throws SQLException, IOException, ClassNotFoundException{
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
        System.out.println("The agend contains follow contacts:");
        for (Agenda phoneList : agendaService.getContact()) {
            System.out.println("Id: " + phoneList.getId() + ",First name: " + phoneList.getFirstName() + ",Last name: " + phoneList.getLastName() + ",Phone number: " + phoneList.getPhoneNumber());
        }
    }

    private String searchContact() throws SQLException, IOException, ClassNotFoundException {
        System.out.println("Select the choice after you want to search a contact: 1-first name, 2-last name");
        Scanner scanner = new Scanner(System.in);
        try {
            int wayChoiceContact = scanner.nextInt();
            if (wayChoiceContact < 1 || wayChoiceContact > 2) return searchContact();
            else if (wayChoiceContact == 1) {
                //search after first name
                System.out.println("Choose a contact after one of the follow first names");
                for (FirstNameFromDatabase firstNameFromDatabase : agendaService.getFirstName()) {
                    System.out.println(firstNameFromDatabase.getFirstName());
                    searchAfterName.add(firstNameFromDatabase.getFirstName());
                }
                firstNameAfterYouChoose = chooseContactAfterFirstName();
                System.out.println("The contacts are");
                searchPhisicallyContact(firstNameAfterYouChoose,agendaService,firstName);

            } else if (wayChoiceContact == 2) {
                //search after last name
                System.out.println("Choose a contact after one of the follow last names");
                for (LastNameFromDatabase lastNameFromDatabase : agendaService.getLastName()) {
                    System.out.println(lastNameFromDatabase.getLastName());
                    searchAfterName.add(lastNameFromDatabase.getLastName());
                }
                lastNameAfterYouChoose=chooseContactAfterLastName();
                System.out.println("The contacts are");
                searchPhisicallyContact(lastNameAfterYouChoose,agendaService,lastName);
            }
        } catch (InputMismatchException exception) {
            System.out.println("You didn't select a available choice. Try again.");
            return searchContact();
        }
        return null;
    }

    private String chooseContactAfterFirstName() {
        System.out.println("Choose a contact after one of the follow first names");
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
        }catch(InputMismatchException exception){
            System.out.println("You didn't select a valid option. Try again.");
            return chooseContactAfterFirstName();
        }
    }

    private String chooseContactAfterLastName(){
        int i;
        try {
            System.out.println("Choose a contact after one of the follow last names");
            for (i = 0; i < searchAfterName.size(); i++) {
                if (i != searchAfterName.size() - 1) System.out.print((i + 1) + "-" + searchAfterName.get(i) + ", ");
                else System.out.print((i + 1) + "-" + searchAfterName.get(i) + "\n");
            }

            Scanner scanner=new Scanner(System.in);
            int optionLastName=scanner.nextInt();
            if (optionLastName < 1 || optionLastName > searchAfterName.size()) return chooseContactAfterLastName();
            else return searchAfterName.get(optionLastName - 1);
        }catch(InputMismatchException exception){
            System.out.println("You didn't select a valid option");
            return chooseContactAfterLastName();
        }
    }

    private void searchPhisicallyContact(String searchAfterFirstNameOrLastName,AgendaService agendaService,String firstNameOrLastName) throws SQLException, IOException, ClassNotFoundException{
        for(Agenda agenda:agendaService.searchContactAfterFirstNameOrLastName(firstNameOrLastName,searchAfterFirstNameOrLastName)){
            System.out.println("Id: " + agenda.getId() + ",First name: " + agenda.getFirstName() + ",Last name: " + agenda.getLastName() + ",Phone number: " + agenda.getPhoneNumber());
        }
    }

    private String updateContact()throws SQLException, IOException, ClassNotFoundException{
        try {
            String option=optionUpdateContact();
            if(option.equals("y"))
                chooseFieldForUpdate();
        }catch(MyException exception){
            System.out.println(exception);
            return updateContact();
        }
        return null;
    }

    private String optionUpdateContact()throws MyException{
        System.out.println("Do you want to update a contact ? Y/N");
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.nextLine();
        if(!answer.equals("y") && !answer.equals("Y") && !answer.equals("n") && !answer.equals("N"))
            throw new MyException("You must to choose between yes and or regard of updateing a contact");

        else
            if(answer.equals("y") || answer.equals("Y"))
                return "y";
            else
                if(answer.equals("n") || answer.equals("N"))
                    return "n";
                else
                    return null;
    }

    private String chooseFieldForUpdate()throws SQLException, IOException, ClassNotFoundException{
        try {
            System.out.println("Choice what you want to change between 1-first name, 2-last name and 3-phone number");
            Scanner scanner = new Scanner(System.in);
            int fieldUpdate = scanner.nextInt();
            if (fieldUpdate < 1 || fieldUpdate > 3) return chooseFieldForUpdate();
            else

                if (fieldUpdate==1){
                //change the first name

                }
                else

                    if(fieldUpdate==2)
                    {
                        //change the last name

                    }
                    else
                        if(fieldUpdate==3){
                            //change the phone number
                            selectFirstNameLastNameForUpdateingPhoneNumber();
                            agendaService.updateContact(phoneNumber);
                        }

        }catch(InputMismatchException exception){
            System.out.println("You didn't choice a valid option. Try again.");
            return chooseFieldForUpdate();
        }
        return null;
    }

    private void selectFirstNameLastNameForUpdateingPhoneNumber()throws SQLException, IOException, ClassNotFoundException{
        for (Agenda phoneList : agendaService.getContact()) {
            pairFirstNameLastName.put(phoneList.getFirstName(),phoneList.getLastName());
        }
    }

    public void actionsAgenda() throws SQLException, IOException, ClassNotFoundException {
        createContact();
        getContacts();
        searchContact();
        updateContact();
    }
}
