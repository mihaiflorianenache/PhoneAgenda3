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

    public Stack<FirstNameFromDatabase> getFirstName()throws SQLException, IOException, ClassNotFoundException{
        return agendaRepository.searchFirstName();
    }

    public Stack<LastNameFromDatabase> getLastName()throws SQLException, IOException, ClassNotFoundException{
        return agendaRepository.searchLastName();
    }

    public List<Agenda> searchContactAfterFirstNameOrLastName(String firstNameOrLastName,String searchAfterFirstNameOrLastName) throws SQLException, IOException, ClassNotFoundException{
        return agendaRepository.searchContact(firstNameOrLastName,searchAfterFirstNameOrLastName);
    }

    public void updateContact(String field)throws SQLException, IOException, ClassNotFoundException{
        agendaRepository.updateContact(field);
    }
}
