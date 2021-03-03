package com.wrox;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;


@WebServlet(name = "searchServlet", urlPatterns = { "/search" }, loadOnStartup = 1)
@MultipartConfig(fileSizeThreshold = 5_242_880, // 5MB
		maxFileSize = 20_971_520L, // 20MB
		maxRequestSize = 41_943_040L // 40MB
)

public class SearchServlet extends HttpServlet {
	private JSONObject searchResults;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		if (action == null)
			action = "create";
		switch (action) {
		case "create":
			this.showSearchForm(response);
			break;
		case "results":
			this.showResults(request, response);
		}
	}


	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		if (action == null)
			action = "list";
		switch (action) {
		case "create":
			try {
				this.createSearch(request, response);
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			break;
		}
	}


	private void showSearchForm(HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer = this.writeHeader(response);

		writer.append("<h2>Search for flights</h2>\r\n");
		writer.append("<form method=\"POST\" action=\"search\" ").append("enctype=\"multipart/form-data\">\r\n");
		writer.append("<input type=\"hidden\" name=\"action\" ").append("value=\"create\"/>\r\n");
		writer.append("Country:<br/>\r\n");
		writer.append("<input type=\"text\" value=\"US\" name=\"country\"/><br/><br/>\r\n");
		writer.append("Currency (in USD):<br/>\r\n");
		writer.append("<input type=\"text\" value=\"USD\"name=\"currency\"/><br/><br/>\r\n");
		writer.append("Locale:<br/>\r\n");
		writer.append("<input type=\"text\" value=\"en-US\"name=\"locale\"/><br/><br/>\r\n");
		writer.append("Origin:<br/>\r\n");
		writer.append("<input type=\"text\" value=\"SFO-sky\"name=\"origin\"/><br/><br/>\r\n");
		writer.append("Destination:<br/>\r\n");
		writer.append("<input type=\"text\" value=\"LAX-sky\" name=\"destination\"/><br/><br/>\r\n");
		writer.append("Date:<br/>\r\n");
		writer.append("<input type=\"text\" value=\"anytime\" name=\"outboundpartialdate\"/><br/><br/>\r\n");
		writer.append("<input type=\"submit\" value=\"Submit\"/>\r\n");
		writer.append("</form>\r\n");

		this.writeFooter(writer);
	}


	private void showResults(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter writer = this.writeHeader(response);
		writer.append("<div class=\"container\">");
		writer.append("<h2>Results</h2>");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(searchResults.toJSONString());
		String prettyJsonString = gson.toJson(je);
		writer.append("<div>" + prettyJsonString + "</div>");

		writer.append("</div>");
		this.writeFooter(writer);
	}

	private void createSearch(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		Search search = new Search();

		search.setCountry(request.getParameter("country"));
		search.setCurrency(request.getParameter("currency"));
		search.setDestination(request.getParameter("destination"));
		search.setLocale(request.getParameter("locale"));
		search.setOrigin(request.getParameter("origin"));
		search.setOutboundpartialdate(request.getParameter("outboundpartialdate"));

		String uri = search.toString();

		OkHttpClient client = new OkHttpClient();
		Request req = new Request.Builder().url("https://rapidapi.p.rapidapi.com/apiservices/browsedates/v1.0" + uri)
				.get().addHeader("x-rapidapi-host", "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com")
				.addHeader("x-rapidapi-key", "3809e09f1dmsh9176bde2a3d6f99p1bf0c9jsnbcb081672d91").build();

		Response res = client.newCall(req).execute();
		JSONParser parser = new JSONParser();
		JSONObject json = ((JSONObject) parser.parse(res.body().string()));
		this.searchResults = json;
		this.showResults(request, response);
	}


	private PrintWriter writeHeader(HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");

		PrintWriter writer = response.getWriter();
		writer.append("<!DOCTYPE html>\r\n").append("<html>\r\n")
				.append("    <head><link rel=\"stylesheet\" href=\"style.css\">\r\n")
				.append("        <title>Airline Ticketing</title>\r\n").append("    </head>\r\n")
				.append("    <body>\r\n");

		return writer;
	}


}
