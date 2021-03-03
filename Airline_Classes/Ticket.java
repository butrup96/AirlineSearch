package com.wrox;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Ticket
{
	//Instanced Variables.
    private String customerName;

    private String start;

    private String end;
    
    private String date;

    private String number;
    
    private Map<String, Attachment> attachments = new LinkedHashMap<>();
    
 
    /*
     * Retrieves customerName
     */
    public String getCustomerName()
    {
        return customerName;
    }
    /*
     * Sets customerName
     */

    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }
    
    /*
     * retrieves start time
     */
    public String getStart()
    {
        return start;
    }
    
    /*
     * sets start time
     */
    public void setStart(String start)
    {
        this.start = start;
    }
    
    /*
     * gets end time
     */
    public String getEnd()
    {
        return end;
    }
    
    /*
     * sets end time
     */
    public void setEnd(String end)
    {
        this.end = end;
    }
    
    /*
     * gets the flight date
     */
    public String getDate()
    {
        return date;
    }
    /*
     * sets flight date
     */
    public void setDate(String date)
    {
        this.date  = date;
    }
    /*
     * gets flight number
     */
    public void getNumber()
    {
        return number;
    }
    
    /*
     * sets flight number
     */
    public int setNumber(int number)
    {
        this.number = number;
        
    }
    
    public Attachment getAttachment(String name)
    {
        return this.attachments.get(name);
    }

    public Collection<Attachment> getAttachments()
    {
        return this.attachments.values();
    }

    public void addAttachment(Attachment attachment)
    {
        this.attachments.put(attachment.getName(), attachment);
    }

    public int getNumberOfAttachments()
    {
        return this.attachments.size();
    }
}
