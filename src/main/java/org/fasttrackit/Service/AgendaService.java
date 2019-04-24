package org.fasttrackit.Service;

import org.fasttrackit.Domain.Agenda;
import org.fasttrackit.Domain.FirstNameFromDatabase;
import org.fasttrackit.Domain.LastNameFromDatabase;
import org.fasttrackit.Persistence.AgendaRepository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Stack;

public class AgendaService {

    private AgendaRepository agendaRepository=new AgendaRepository();

    public void createContact(Agenda agenda) throws SQLException, IOException, ClassNotFoundException{
        System.out.println("Creating item: "+agenda);
        agendaRepository.createContact(agenda);
    }

    public List<Agenda> getContact()throws SQLException, IOException, ClassNotFoundException {
       return agendaRepository.getContact();
    }

    public int getNumberContacts() throws SQLException, IOException, ClassNotFoundException{
        return agendaRepository.getNumberContacts();
    }

    public String getContact(String personForUpdateNumber)throws SQLException, IOException, ClassNotFoundException {
        return agendaRepository.getContact(personForUpdateNumber);
    }

    public Stack<FirstNameFromDatabase> getFirstName()throws SQLException, IOException, ClassNotFoundException{
        return agendaRepository.searchFirstName();
    }

    public Stack<LastNameFromDatabase> getLastName()throws SQLException, IOException, ClassNotFoundException{
        return agendaRepository.searchLastName();
    }

    public List<Agenda> searchContactAfterFirstNameOrLastName(String firstNameOrLastName,String searchAfterFirstNameOrLastName) throws SQLException, IOException, ClassNotFoundException{
        return agendaRepository.searchContact(firstNameOrLastName,searchAfterFirstNameOrLastName);
    }

    public List<Agenda> listPhoneNumber()throws SQLException, IOException, ClassNotFoundException{
        return agendaRepository.getContact();
    }

    public void updatePhoneNumber(String field,String person)throws SQLException, IOException, ClassNotFoundException{
        agendaRepository.updatePhoneNumber(field,person);
    }

    public void deleteContact(String contactDelete)throws SQLException, IOException, ClassNotFoundException{
        agendaRepository.deleteContact(contactDelete);
    }
}
