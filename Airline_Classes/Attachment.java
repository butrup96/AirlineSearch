package com.wrox;

public class Attachment
{
	/*
	 * Initialized variables.
	 */
    private String name;

    private byte[] contents;
    
    /*
     * Getxxxx = get variable information.
     * Setxxxx = set variable information for testing and passing information.
     */

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public byte[] getContents()
    {
        return contents;
    }

    public void setContents(byte[] contents)
    {
        this.contents = contents;
    }
}
