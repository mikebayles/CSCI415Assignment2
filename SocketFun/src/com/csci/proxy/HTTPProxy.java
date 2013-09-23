package com.csci.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.naming.ldap.Rdn;

public class HTTPProxy extends Thread
{
	private Socket socket; 
    
	public HTTPProxy(Socket socket) 
	{
		this.socket = socket;
		
	}
	
	@Override
	public void run()
	{
		try
		{			
	        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	        
	        String input;
	        
	        input = in.readLine();
	        
	        if(input == null || !input.contains("GET"))
	        	socket.close();
	        else
	        {
	        	String host = getHost(input);
	        	Socket server = new Socket(InetAddress.getByName(host),80);
	        	PrintWriter pw = new PrintWriter(server.getOutputStream());
	        	
	        	pw.println("GET " + getFile(input) + " HTTP/1.0");
	        	pw.println(String.format("Host: %s", host));
	        	pw.println("");
	        	pw.flush();
	        		        
	        	
	        	BufferedReader br = new BufferedReader(new InputStreamReader(server.getInputStream()));	        	
	        	String t;
	        	while((t = br.readLine()) != null) out.println(t);
	        	
	        	br.close();
	        	server.close();
	        	socket.close();
	        }
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	public void rune() 
	{
		try
		{
	        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	        String input;
	        do
	        {	        	
	        	input = in.readLine();
	        	
	        	if(input != null && input.toLowerCase().contains("get"))
	        	{

		        	String url = getHost(input);
		        	logRequest(input, url);
		        	out.print(get(url));
		        	
	        	}	        	
	        }
	        while(input != null);
	        
	        socket.close();
	        
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public String getHost(String input)
	{			
		//System.out.println(input);
		
		//System.out.println(input.split(" ")[1]);
		//System.out.println(input.split(" ")[1].split("http://")[1]);
		String ret = "";
		
		try
		{
			ret = input.split(" ")[1].split("http://")[1].split("/")[0];
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			System.out.println("Could not get host of " + input);
		}
		return ret;
				
	}
	
	public String getFile(String input)
	{
		URL url = null;
		try 
		{		
			url = new URL(input.split(" ")[1]);
			System.out.println(url.getFile());
		} 
		catch (MalformedURLException e) 
		{			
			e.printStackTrace();
		}
		
		
		return url.getFile();
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
			e.printStackTrace();
		}
		
		return result;
	}
}
