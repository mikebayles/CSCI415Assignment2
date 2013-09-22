package com.csci.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.naming.ldap.Rdn;

public class HTTPProxy extends Thread
{
	private Socket socket;
    private int clientNumber;
    
	public HTTPProxy(Socket socket, int clientNumber) 
	{
		this.socket = socket;
		this.clientNumber = clientNumber;
	}
	
	@Override
	public void run() 
	{
		try
		{
	        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	        
	        while(true)
	        {	        	
	        	String input = in.readLine();
	        	if(input != null && input.toLowerCase().contains("get"))
	        	{

		        	String url = parseRequest(input);
		        	logRequest(input, url);
		        	out.print(get(url));
		        	socket.close();
	        	}	        	
	        }
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public String parseRequest(String input)
	{
		String firstLine = input.split("\\r?\\n")[0];
		if(firstLine.toLowerCase().contains("get"))
			return firstLine.toLowerCase().split(" ")[1];
		
		return "";
	}
	
	public void logRequest(String input, String url)
	{
		String dateTime = new SimpleDateFormat("MM dd yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
		System.out.println(String.format("%s\t%s\t%s\t%s",dateTime, socket.getInetAddress(), url, input.length()));
	}
	
	public String get(String urlIn)
	{
		URL url;
		HttpURLConnection conn;
		BufferedReader reader;
		String line;
		String result = "";
		
		try
		{
			url = new URL(urlIn);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while((line = reader.readLine()) != null)
			{
				result += line;
			}
			
			reader.close();		
		}
		catch(IOException e)
		{
			System.out.println(urlIn + " is bad");
			e.printStackTrace();
		}
		
		return result;
	}
}
