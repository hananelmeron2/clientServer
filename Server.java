import java.io.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.awt.Color;

public class Server extends Thread
{
  //list of clients 
  public List<User> clients;
  //the socket of the server listing
  private ServerSocket server;
  BufferedReader in;
  PrintWriter out;
  //to stop the server 
  boolean flag=true;
  //open the socket between client and server
  Socket client;
  
  public static void main(String[] args) throws IOException 
  {
    new Server(5000).start();
  }
  //constructor
  public Server(int port) 
  {
	  try 
	  {
		  
    this.clients = new ArrayList<User>();
    server = new ServerSocket(port) ;
	client=server.accept();
	User srv = new User(client, "server");
	this.clients.add(srv);
	} 
	  catch (IOException e) 
	  {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   
  }
  
  public void stop2() throws IOException
  {
	  flag=false;
	  server.close();
  }
  
  @Override
  public void run()
  {
	  try 
	  {

    System.out.println("Port 5000 is now open.");
    
    while (flag) 
    {
    	if(!flag)
    		return;
      // accepts a new client
       client = server.accept();

      // get nickname of newUser
      String nickname = (new Scanner ( client.getInputStream() )).nextLine();
      nickname = nickname.replace(",", ""); 
      nickname = nickname.replace(" ", "_");
      
      if(!this.clients.contains(nickname))
      { 
       System.out.println("New Client: \"" + nickname + "\"\n\t     Host:" + client.getInetAddress().getHostAddress()); 
    // create new User
       User newUser = new User(client, nickname);
          
      // add newUser message to list
       for(User client1:this.clients)
       {
     	  {
     		  client1.getOutStream().println(
     				 newUser.getNickname()+ "<span>:Connected " +"</span>");   
     	 }
       }
      this.clients.add(newUser);
      
      
      // create a new thread for newUser incoming messages handling
      new Thread(new UserHandler(this, newUser)).start();
      }
    }
    }
      catch(Exception e)
      {
    	  e.printStackTrace();
      }
    	 
    }


  // delete a user from the list
  public void removeUser(User user){
	  
    this.clients.remove(user);
    for(User user1:this.clients)
    {
  	  //if(!clients.contains(client1))
  	  {
  		  user1.getOutStream().println(
  				  user.getNickname()+ "<span>:Disconnected " +"</span>");   
  	 }
    }
  }

  // send incoming msg to all Users
  public void broadcastMessages(String msg, User userSender) {
    for (User client : this.clients) {
      client.getOutStream().println(
          userSender.toString() + "<span>: " + msg+"</span>");
    }
  }

  // send list of clients to all Users
  public void broadcastAllUsers(){
    for (User client : this.clients) {
      client.getOutStream().println(this.clients);
    }
  }

  // send message to a User (String)
  public void sendMessageToUser(String msg, User userSender, String user){
    boolean find = false;
    for (User client : this.clients) {
      if (client.getNickname().equals(user) && client != userSender) {
        find = true;
        userSender.getOutStream().println(userSender.toString() + " -> " + client.toString() +": " + msg);
        client.getOutStream().println(
            "(<b>Private</b>)" + userSender.toString() + "<span>: " + msg+"</span>");
      }
    }
    if (!find) {
      userSender.getOutStream().println(userSender.toString() + " -> (<b>no one!</b>): " + msg);
    }
  }
}
