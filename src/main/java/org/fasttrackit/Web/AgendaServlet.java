package org.fasttrackit.Web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.fasttrackit.Domain.Agenda;
import org.fasttrackit.Service.AgendaService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/agenda")
public class AgendaServlet extends HttpServlet {

    //anytime in a servlet we have a type service object
    private AgendaService agendaService=new AgendaService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //500 response from server
        try {
            List<Agenda> agenda = agendaService.getContact();

            //with HTTP protocol we can transfer only text
            //with ObjectMapper agenda object is use regard of transform this object in a JSON (string format)
            ObjectMapper objectMapper=new ObjectMapper();

            //agenda object is transforming in a string
            String responseJSON=objectMapper.writeValueAsString(agenda);

            //we set a header, contentType (or mime type) , regard of announcing client that he will receives a response like a JSON format
            resp.setContentType("application/json");

            //is displaying all content from database in a string format
            //because data structure who contains records from DB has been transformed in a string
            resp.getWriter().print(responseJSON);
            resp.getWriter().flush();

        }catch(Exception exception){
            resp.sendError(500,"There was a error processing your request "+exception.getMessage());
        }
    }
}
