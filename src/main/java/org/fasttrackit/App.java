package org.fasttrackit;

import org.fasttrackit.Domain.Contact;

import java.io.IOException;
import java.sql.SQLException;

public class App 
{
    public static void main( String[] args ) throws SQLException, IOException, ClassNotFoundException {
        Contact contact=new Contact();
        contact.actionsAgenda();
    }
}
