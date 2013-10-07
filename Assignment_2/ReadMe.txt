Instructions


1. Make sure to change the network settings for the bowser.

For example, we choose our PORT to be 9898 so we changed the servername: localhost
and Port: 9898 in our proxy settings

2. Run the java program and it should say the proxy server is running which means 
socket connection has been initialized. 

3. Open the website on browser, and you should be able to see the response in the output 
window of java and it would also be cached as a txt file. Your browser should also contain the content.

4. If the webpage has been requested before, it will be opened from the cache. 
	The cache is a text file located in the cache folder. This folder is located wherever you are running the application from.
	If you are using Eclipse, it will be inside the project folder (SocketFun/cache).

5. Our log file is called "proxy.log". It contains the date, browser's IP address, the requested URL, and the content size.





