package com.mkb.socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class SocketHelloWorld 
{
	public static void main(String[] args) throws IOException 
	{
        ServerSocket listener = new ServerSocket(9090);
        
        System.out.println(listener.getInetAddress().getHostName());
        try 
        {
            while (true) 
            {
                Socket socket = listener.accept();
                try 
                {
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    
                    out.println(new Date().toString());
                    out.println("Hi mike");
                } 
                finally 
                {
                    socket.close();
                }
            }
        }
        finally 
        {        	
            listener.close();
        }
    }
}
