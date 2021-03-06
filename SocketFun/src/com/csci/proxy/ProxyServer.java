package com.csci.proxy;

import java.io.File;
import java.net.ServerSocket;


public class ProxyServer 
{
	public static void main(String[] args) throws Exception 
	{
        System.out.println("The proxy server is running.");        
        ServerSocket listener = new ServerSocket(9898);        
        try 
        {
        	makeCacheFolder();
            while (true) 
            {
                new HTTPProxy(listener.accept()).start();
                
            }
        } 
        finally 
        {
            listener.close();
        }
	}
	
	public static void makeCacheFolder()
	{
		File cacheFolder = new File("cache");
		if(cacheFolder.exists())
			return;
		
		cacheFolder.mkdir();
	}
}
