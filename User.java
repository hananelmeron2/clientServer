import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class User
{

   
  private PrintStream streamOut;
  private InputStream streamIn;
  private String nickname;
  private Socket client;

  // constructor for client
    public User(Socket client, String name) throws IOException 
      {
         this.streamOut = new PrintStream(client.getOutputStream());
         this.streamIn = client.getInputStream();
         this.client = client;
         this.nickname = name;

     }

    //getters and setters
 
  
  public PrintStream getOutStream(){
    return this.streamOut;
  }

  public InputStream getInputStream(){
    return this.streamIn;
  }

  public String getNickname(){
    return this.nickname;
  }

  public String toString(){

    return this.getNickname();

  }
}