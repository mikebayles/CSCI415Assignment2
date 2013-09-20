package com.csci.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;

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
	        	System.out.println(input);
	        	if(input.toLowerCase().startsWith("get"))
	        		out.println(get(input.toLowerCase().split("get")[1]));
	        }
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
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
