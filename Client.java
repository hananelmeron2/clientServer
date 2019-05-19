import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.io.StringReader;

public class Client
{
  private String host;
  private int port;

  public static void main(String[] args) throws UnknownHostException, IOException 
  {
    new Client("127.0.0.1", 5000).run();
  }

  public Client(String host, int port) 
  {
    this.host = host;
    this.port = port;
  }

  public void run() throws UnknownHostException, IOException
  {
    // connect client to server
    Socket client = new Socket(host, port);
    System.out.println("Client successfully connected to server!");

    // Get Socket output stream (where the client send her mesg)
    PrintStream output = new PrintStream(client.getOutputStream());

    // ask for a nickname
    Scanner sc = new Scanner(System.in);
    String nickname = sc.nextLine();
    
    // send nickname to server
    output.println(nickname);

    // while new messages
    while(sc.hasNextLine())
    {
      output.println(sc.nextLine());
    }

    // end ctrl D
    output.close();
    sc.close();
    client.close();
  }
}