package org.fasttrackit.Persistence;

import org.fasttrackit.Domain.Agenda;
import org.fasttrackit.Domain.FirstNameFromDatabase;
import org.fasttrackit.Domain.LastNameFromDatabase;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class AgendaRepository {

    public void createContact(Agenda agenda)throws SQLException, IOException, ClassNotFoundException {
        try (Connection connection = DatabaseConfiguration.getConnection()) {
            String insertContact="INSERT INTO agenda (`firstName`,`lastName`,`phoneNumber`) VALUES (?,?,?)"+"ON DUPLICATE KEY UPDATE firstName=firstName+1;";

            PreparedStatement preparedStatement =connection.prepareStatement(insertContact);
            preparedStatement.setString(1,agenda.getFirstName());
            preparedStatement.setString(2,agenda.getLastName());
            preparedStatement.setString(3,agenda.getPhoneNumber());
            preparedStatement.executeUpdate();
        }
    }

    public List<Agenda> getContact()throws SQLException, IOException, ClassNotFoundException {
        try(Connection connection=DatabaseConfiguration.getConnection()) {
            String getContact = "SELECT id,`firstName`,`lastName`,`phoneNumber` FROM agenda ORDER BY firstName desc";
            Statement statement = connection.createStatement();
            statement.execute(getContact);

            ResultSet resultSet = statement.executeQuery(getContact);
            List<Agenda> response = new ArrayList<>();

            while (resultSet.next()) {
                Agenda agenda = new Agenda();
                agenda.setId(resultSet.getInt("id"));
                agenda.setFirstName(resultSet.getString("firstName"));
                agenda.setLastName(resultSet.getString("lastName"));
                agenda.setPhoneNumber(resultSet.getString("phoneNumber"));
                response.add(agenda);
            }
            return response;
        }
    }

    public int getNumberContacts() throws SQLException, IOException, ClassNotFoundException{
        int i=0;
        try(Connection connection=DatabaseConfiguration.getConnection()){
            String numberContact="SELECT id FROM agenda";
            Statement statement=connection.createStatement();
            statement.execute(numberContact);

            ResultSet resultSet=statement.executeQuery(numberContact);
            while(resultSet.next()){
                i++;
            }
        }
        return i;
    }

    public String getContact(String personForUpdateContact)throws SQLException, IOException, ClassNotFoundException {
        try(Connection connection=DatabaseConfiguration.getConnection()) {
            String getContact = "SELECT `phoneNumber` FROM agenda WHERE firstName="+"'"+personForUpdateContact.split(" ")[0]+"'"+"AND lastName="+"'"+personForUpdateContact.split(" ")[1].split("-")[0]+"'"+"AND phoneNumber="+"'"+personForUpdateContact.split(" ")[1].split("-")[1]+"'";
            Statement statement = connection.createStatement();
            statement.execute(getContact);

            ResultSet resultSet = statement.executeQuery(getContact);
            while(resultSet.next()) {
                return resultSet.getString("phoneNumber");
            }
        }
        return null;
    }

    public Stack<FirstNameFromDatabase> searchFirstName()throws SQLException, IOException, ClassNotFoundException{
        try (Connection connection = DatabaseConfiguration.getConnection()) {

            String getFirstName = "SELECT DISTINCT firstName FROM agenda;";

            Statement statement = connection.createStatement();
            statement.execute(getFirstName);

            ResultSet resultSet = statement.executeQuery(getFirstName);
            Stack<FirstNameFromDatabase> allFirstNames = new Stack<>();
            while (resultSet.next()) {
                FirstNameFromDatabase firstNameFromDatabase = new FirstNameFromDatabase();
                firstNameFromDatabase.setFirstName(resultSet.getString("firstName"));
                allFirstNames.push(firstNameFromDatabase);
            }
            return allFirstNames;
        }
    }

    public Stack<LastNameFromDatabase> searchLastName()throws SQLException, IOException, ClassNotFoundException{
        try (Connection connection = DatabaseConfiguration.getConnection()) {

            String getLastName = "SELECT DISTINCT lastName FROM agenda;";

            Statement statement = connection.createStatement();
            statement.execute(getLastName);

            ResultSet resultSet = statement.executeQuery(getLastName);
            Stack<LastNameFromDatabase> allLastNames = new Stack<>();
            while (resultSet.next()) {
                LastNameFromDatabase lastNameFromDatabase = new LastNameFromDatabase();
                lastNameFromDatabase.setLastName(resultSet.getString("lastName"));
                allLastNames.push(lastNameFromDatabase);
            }
            return allLastNames;
        }
    }

    public List<Agenda> searchContact(String optionSearch, String firstNameOrLastName) throws SQLException, IOException, ClassNotFoundException {
        try (Connection connection = DatabaseConfiguration.getConnection()) {
            String query = "SELECT id,`firstName`,`lastName`,`phoneNumber` FROM agenda WHERE " + optionSearch + "=" + "'" + firstNameOrLastName + "'";
            Statement statement = connection.createStatement();
            statement.execute(query);

            ResultSet resultSet = statement.executeQuery(query);
            List<Agenda> searchedContact = new ArrayList<>();
            while (resultSet.next()) {
                Agenda agenda = new Agenda();
                agenda.setId(resultSet.getInt("id"));
                agenda.setFirstName(resultSet.getString("firstName"));
                agenda.setLastName(resultSet.getString("lastName"));
                agenda.setPhoneNumber(resultSet.getString("phoneNumber"));
                searchedContact.add(agenda);
            }
            return searchedContact;
        }
    }

    public void updatePhoneNumber(String field,String person) throws SQLException, IOException, ClassNotFoundException{
        try(Connection connection=DatabaseConfiguration.getConnection()){
            String updatePhoneNumber="UPDATE agenda SET phoneNumber="+"'"+field+"'"+"WHERE firstName="+"'"+person.split(" ")[0]+"'"+"AND lastName="+"'"+person.split(" ")[1].split("-")[0]+"'";
            Statement statement=connection.createStatement();
            statement.execute(updatePhoneNumber);
        }
    }

    public void deleteContact(String contactDelete)throws SQLException, IOException, ClassNotFoundException{
        try(Connection connection=DatabaseConfiguration.getConnection()){
            String deleteContact="DELETE FROM agenda WHERE firstName="+"'"+contactDelete.split("-")[0].split(" ")[0]+"'AND lastName="+"'"+contactDelete.split("-")[0].split(" ")[1]+"'AND phoneNumber="+"'"+contactDelete.split("-")[1]+"'";
            Statement statement=connection.createStatement();
            statement.execute(deleteContact);
        }
    }
}
