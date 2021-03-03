package com.wrox;

public class Search {

	private String country;

	private String currency;

	private String locale;

	private String origin;

	private String destination;

	private String outboundpartialdate;
	
	/*
	 * get and set methods for SearchServlet
	 */

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getOutboundpartialdate() {
		return outboundpartialdate;
	}

	public void setOutboundpartialdate(String outboundpartialdate) {
		this.outboundpartialdate = outboundpartialdate;
	}

	/*
	 * methods to print GET as a string
	 */
	@Override
	public String toString() {
		String uri = "/" + this.getCountry() + "/" + this.getCurrency() + "/" + this.getLocale() + "/"
				+ this.getOrigin() + "/" + this.getDestination() + "/" + this.getOutboundpartialdate();
		return uri;

	}

}