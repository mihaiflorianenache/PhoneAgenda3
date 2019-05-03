package org.fasttrackit.Web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.fasttrackit.Domain.Agenda;
import org.fasttrackit.Service.AgendaService;
import org.fasttrackit.Transfer.MarkAgendaRequest;
import org.fasttrackit.Transfer.SaveAgendaRequest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
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
            resp.getWriter().close();

        }catch(SQLException exception){
            resp.sendError(500,"There was a error processing your request "+exception.getMessage());
        }
    }

    protected void doDelete(HttpServletRequest req,HttpServletResponse resp)throws ServletException, IOException{
        String id=req.getParameter("id");
        try{
            agendaService.deleteContact(agendaService.getContact().get(0).getFirstName()+" "+agendaService.getContact().get(0).getLastName()+"-"+agendaService.getContact().get(0).getPhoneNumber());
        }catch(SQLException exception){
            exception.printStackTrace();
            resp.sendError(500, "There was an error: " + exception.getMessage());
        }
    }

    protected void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException{
        setAccessControlHeaders(resp);
        ObjectMapper objectMapper=new ObjectMapper();
        SaveAgendaRequest request=objectMapper.readValue(req.getReader(),SaveAgendaRequest.class);
        Agenda agenda=new Agenda();
        try{
            agendaService.createContact(agenda);
        }catch(SQLException exception){
            exception.printStackTrace();
            resp.sendError(500, "There was an error: " + exception.getMessage());
        }
    }

    protected void doPut(HttpServletRequest req,HttpServletResponse resp)throws ServletException, IOException{
        setAccessControlHeaders(resp);
        String id=req.getParameter("id");
        ObjectMapper objectMapper=new ObjectMapper();
        MarkAgendaRequest request=objectMapper.readValue(req.getReader(),MarkAgendaRequest.class);
        try{
            agendaService.updatePhoneNumber("456",agendaService.getContact().get(0).getFirstName()+" "+agendaService.getContact().get(0).getLastName()+"-"+agendaService.getContact().get(0).getPhoneNumber());
        }catch(SQLException exception){
            exception.printStackTrace();
            resp.sendError(500, "There was an error: " + exception.getMessage());
        }
    }

    private void setAccessControlHeaders(HttpServletResponse resp){
        resp.setHeader("Access-Control-Allow-Origin","*");
        resp.setHeader("Access-Control-Allow-Methods","GET,POST,PUT,DELETE");
        resp.setHeader("Access-Control-Allow-Headers","content-type");
    }
}
