package com.csci.proxy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;



public class HTTPProxy extends Thread
{
	private Socket clientSocket; 	
    
	public HTTPProxy(Socket socket) 
	{
		this.clientSocket = socket;
		
	}

	@Override
	public void run()
	{
		try
		{			
	        BufferedReader clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));	      
	        String input;
	        
	        input = clientIn.readLine();
	        //we only are concerned with valid GET requests
	        if(input == null || !input.contains("GET"))
	        	clientSocket.close();
	        else
	        {	        	
	        	System.out.println(input);	       
	        	
	        	//GET url HTTPVERSION
	        	//we care about the second spot after splitting
	        	URL url = new URL(input.split(" ")[1]);
	        	String host = url.getHost();
	        	String file = url.getFile();
	        
	        	//file names can't contain certain symbols, so get rid of all symbols
	        	String textFileName = "cache\\" + (host+"_"+file).replaceAll("[^a-zA-Z0-9.-]", "") + ".txt";
	        	
	        	
	        	//first check if we have cached this request
	        	if(!cacheFileExists(textFileName))
	        	{	        		
		        	Socket server = new Socket(host,80);
		        	
		        	//sends the GET request to the server
		        	createGetRequest(host, file, server);
		        		    
		        	
		        	OutputStream clientOutput = clientSocket.getOutputStream();
		        	InputStream serverInput = server.getInputStream();
		        	
		        	//handle image files differently
		        	if(!isImageFile(file))
		        	{		        		
		        		FileWriter fstream = new FileWriter(textFileName);
			        	BufferedWriter textOut = new BufferedWriter(fstream);
		        		
		        		PrintWriter out = new PrintWriter(clientOutput, true);
			        	BufferedReader serverInputReader = new BufferedReader(new InputStreamReader(serverInput));
			        	
			        	int responseLength = 0;
			        	String line;
			        	while((line = serverInputReader.readLine()) != null) 
			        	{	
			        		//grab the length
			        		if(line.startsWith("Content-Length"))
			        			responseLength = Integer.parseInt(line.split(" ")[1]);
			        		
			        		textOut.write(line + "\n");
			        		out.println(line); 		        		
			        		System.out.println(line);
		        		}
			        	
			        	logRequest(clientSocket.getInetAddress().getHostAddress(),url.toString(),responseLength);
			        	
			        	serverInputReader.close();
			        	textOut.close();
		        	}
		        	else
		        	{				        		
		        		int count;
		        		//create some temporary storage
		        		byte[] buffer = new byte[8192];
		        		//transfer bytes from input to browser
		        		while ((count = serverInput.read(buffer)) > 0)
	    				{
		        			
	    				    clientOutput.write(buffer, 0, count);
	    				}	
		        		
		        	}
		        	server.close();
		        	clientSocket.close();
        		}
	        	else
	        	{
	        		System.out.println("Cached file");
	        		
	        		PrintWriter clientOut = new PrintWriter(clientSocket.getOutputStream(), true);
	        		
	        		BufferedReader reader = new BufferedReader(new FileReader(textFileName));
	        		String currentLine;
	        		
	        		//copy the cache to the browser line by line
	        		while((currentLine = reader.readLine()) != null)
	        		{
	        			clientOut.println(currentLine); 
	        		}
	        			        			        		
	        		reader.close();	        	
		        	clientSocket.close();	        		
	        	}
	        }
		}
		catch(IOException e)
		{
			//something went wrong
			e.printStackTrace();
		}
	}


	private void createGetRequest(String host, String file, Socket server) throws IOException
	{
		PrintWriter pw = new PrintWriter(server.getOutputStream());
    
		//following the protocol
		pw.println("GET " + file + " HTTP/1.0");
		pw.println(String.format("Host: %s", host));
		pw.println("");
		pw.flush();
	}
	
	private boolean cacheFileExists(String fileName) 
	{		
		File textFile = new File(fileName);
		return textFile.exists();
	}

	private boolean isImageFile(String fileName)
	{
		String lowered = fileName.toLowerCase();
		//check if file name contains common image file extensions
		return lowered.contains(".jpg") || lowered.contains("gif") || lowered.contains(".bmp") || lowered.contains(".png") || lowered.contains(".ico");
	}
	
	private void logRequest(String ip, String url, int responseLength) throws IOException
	{
		FileWriter fstream = new FileWriter("proxy.log",true);
    	BufferedWriter logOut = new BufferedWriter(fstream);
    	
		String dateTime = new SimpleDateFormat("MMM dd yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
		String logLine = String.format("%s\t%s\t%s\t%s",dateTime, ip, url, responseLength);
		
		logOut.append(logLine + "\n");
		System.out.println(logLine);
		logOut.close();
	}
	

}
