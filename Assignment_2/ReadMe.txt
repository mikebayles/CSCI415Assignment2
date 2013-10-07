Instructions

1. Import project into Eclipse by File->Import->General->Existing Project into Workspace and browse to our folder.

2. Run the java program (ProxyServer) and it should say the proxy server is running which means 
socket connection has been initialized. If you weren't able to run in Eclipse, 
use the jar file and the jar command in the command window (java -jar Assignment2.jar)

3. Configure network settings for the bowser.

For example, we choose our PORT to be 9898 so we changed the servername: localhost
and Port: 9898 in our proxy settings

4. Open the website on browser, and you should be able to see the response in the output 
window of java and it would also be cached as a txt file. Your browser should also contain the content.

5. If the webpage has been requested before, it will be opened from the cache. 
	The cache is a text file located in the cache folder. This folder is located wherever you are running the application from.
	If you are using Eclipse, it will be inside the project folder (SocketFun/cache).

6. Our log file is called "proxy.log". It contains the date, browser's IP address, the requested URL, and the content size.





