package com.csci.proxy;

import java.net.ServerSocket;


public class ProxyServer 
{
	public static void main(String[] args) throws Exception 
	{
        System.out.println("The proxy server is running.");
        int clientNumber = 0;
        ServerSocket listener = new ServerSocket(9898);
        try 
        {
            while (true) 
            {
                new HTTPProxy(listener.accept(), clientNumber++).start();
            }
        } 
        finally 
        {
            listener.close();
        }
	}
}
