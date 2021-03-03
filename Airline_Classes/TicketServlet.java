package com.wrox;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet(
        name = "ticketServlet",
        urlPatterns = {"/tickets"},
        loadOnStartup = 1
)
@MultipartConfig(
        fileSizeThreshold = 5_242_880, //5MB
        maxFileSize = 20_971_520L, //20MB
        maxRequestSize = 41_943_040L //40MB
)

/*
 * Tickets Main Page.
 */
public class TicketServlet extends HttpServlet
{
    private volatile int TICKET_ID_SEQUENCE = 1;

    private Map<Integer, Ticket> ticketDatabase = new LinkedHashMap<>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
    	/*
    	 * Displays the List.
    	 */
        String action = request.getParameter("action");
        if(action == null)
            action = "list";
        
        /*
         * User input determines action.
         */
        switch(action)
        {
            case "create":
                this.showTicketForm(response);
                break;
            case "view":
                this.viewTicket(request, response);
                break;
            case "download":
                this.downloadAttachment(request, response);
                break;
            case "list":
            default:
                this.listTickets(response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
    	/*
    	 * Displays the list.
    	 */
        String action = request.getParameter("action");
        if(action == null)
            action = "list";
        
        /*
         * User input with determine the action of the list.
         */
        switch(action)
        {
            case "create":
                this.createTicket(request, response);
                break;
            case "list":
            default:
                response.sendRedirect("tickets");
                break;
        }
    }

    /*
     * Displays the Ticket Form.
     */
    private void showTicketForm(HttpServletResponse response)
            throws ServletException, IOException
    {
        PrintWriter writer = this.writeHeader(response);

        writer.append("<h2>Create a Ticket</h2>\r\n");
        writer.append("<form method=\"POST\" action=\"tickets\" ").append("enctype=\"multipart/form-data\">\r\n");
        writer.append("<input type=\"hidden\" name=\"action\" ").append("value=\"create\"/>\r\n");
        writer.append("Passenger Name<br/>\r\n");
        writer.append("<input type=\"text\" name=\"customerName\"/><br/><br/>\r\n");
        writer.append("Starting Location<br/>\r\n");
        writer.append("<input type=\"text\" name=\"start\"/><br/><br/>\r\n");
        writer.append("Destination<br/>\r\n");
        writer.append("<input type=\"text\" name=\"end\"/><br/><br/>\r\n");
        writer.append("Date<br/>\r\n");
        writer.append("<input type=\"text\" name=\"date\"/><br/><br/>\r\n");
        writer.append("Number of Passengers<br/>\r\n");
        writer.append("<input type=\"text\" name=\"num\"/><br/><br/>\r\n");
        writer.append("<input type=\"submit\" name=\"submit\"/><br/><br/>\r\n");

        this.writeFooter(writer);
    }

    /*
     * Displays the specific Ticket.
     */
    private void viewTicket(HttpServletRequest request,HttpServletResponse response)
            throws ServletException, IOException
    {
        String idString = request.getParameter("ticketId");
        Ticket ticket = this.getTicket(idString, response);
        if(ticket == null)
            return;
        /*
         * Prints writer header.
         */
        PrintWriter writer = this.writeHeader(response);
        /*
         * Attaches more information to the ticket.
         */
        writer.append("<h2>Ticket #").append(idString).append(": ").append(ticket.getSubject()).append("</h2>\r\n");
        writer.append("<i>Customer Name - ").append(ticket.getCustomerName()).append("</i><br/><br/>\r\n");
        writer.append(ticket.getBody()).append("<br/><br/>\r\n");
		writer.append("<i>Start - ").append(ticket.getStart()).append("</i><br/><br/>\r\n");
		writer.append("<i>Destination - ").append(ticket.getEnd()).append("<br/><br/>\r\n");
		writer.append("<i>Travel Date - ").append(ticket.getDate()).append("<br/><br/>\r\n");
		writer.append("<i>Number of Passengers - ").append(ticket.getNumber()).append("<br/><br/>\r\n");
		writer.append("<a href=\"tickets\">Return to list tickets</a>\r\n");
		writer.append("</div>");
		this.writeFooter(writer;)
    
    /*
     * Displays the attachment for download.
     */

		private void listOfTickets(HttpServletResponse response) throws ServletException, IOException {
			PrintWriter writer = this.writeHeader(response);

			writer.append("<h2>Tickets</h2>\r\n");
			writer.append("<div class=\"container\">");
			writer.append("<a href=\"tickets?action=create\">Create Ticket").append("</a><br/><br/>\r\n");

			if (this.ticketDatabase.size() == 0)
				writer.append("<i>There are no tickets in the system.</i>\r\n");
			else {
				for (int id : this.ticketDatabase.keySet()) {
					String idString = Integer.toString(id);
					Ticket ticket = this.ticketDatabase.get(id);
					writer.append("Ticket #").append(idString).append(": <a href=\"tickets?action=view&ticketId=")
							.append(idString).append("\">").append(ticket.getDate()).append("</a> (customer: ")
							.append(ticket.getCustomerName()).append(")<br/>\r\n");
				}
			}
			writer.append("</div>");
			this.writeFooter(writer);
    }


		/*
		 * This method creates the ticket for the customer.
		 * 
		 * 
		 */
    private void createTicket(HttpServletResponse response, HttpServletRequest request)
    		throws ServletException, IOException
    {
    		Ticket ticket = new Ticket();
    		ticket.setCustomerName(request.getParameter("customerName"));
    		ticket.setStart(request.getParameter("start"));
    		ticket.setEnd(request.getParameter("end"));
    		ticket.setDate(request.getParameter("date"));
    		ticket.setNum(request.getParameter("number"));
    		
    		int ID;
    		synchronized (this) {
    			ID = this.TICKET_ID_SEQUENCE++;
    			this.ticketDatabase.put(ID, ticket);
    		}

    		response.sendRedirect("tickets?action=view&ticketId=" + id);

    }

    
    /*
     * Adds the ticket to the database.
     */
    private Ticket getTicket(String idString, HttpServletResponse response)
            throws ServletException, IOException
    {
        if(idString == null || idString.length() == 0)
        {
            response.sendRedirect("tickets");
            return null;
        }

        try
        {
            Ticket ticket = this.ticketDatabase.get(Integer.parseInt(idString));
            if(ticket == null)
            {
                response.sendRedirect("tickets");
                return null;
            }
            return ticket;
        }
        catch(Exception e)
        {
            response.sendRedirect("tickets");
            return null;
        }
    }
    
    /*
     * Displays the writer header and footer in the server page.
     */

    private PrintWriter writeHeader(HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        PrintWriter writer = response.getWriter();
        writer.append("<!DOCTYPE html>\r\n")
              .append("<html>\r\n")
              .append("    <head>\r\n")
              .append("        <title>Flight Ticket Booking</title>\r\n")
              .append("    </head>\r\n")
              .append("    <body>\r\n");

        return writer;
    }

    
    /*
     * Completes HTML page with writer's name.
     */
	private void writeFooter(PrintWriter writer) {
		writer.append("    <footer>Brandon Utrup</footer></body>\r\n").append("</html>\r\n");
	}
}
