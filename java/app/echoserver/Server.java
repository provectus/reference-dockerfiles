// Source: https://github.com/ansman/servers/blob/master/java/Server.java by @ansman
// javac Server.java && java Server [port=8080] [host=127.0.0.1]
package echoserver;

import java.net.*;
import java.io.*;

public class Server {
  public static void main(String[] argv) throws IOException {
    String host = "0.0.0.0";
    short port = 8080;

    if(argv.length >= 2)
      host = argv[1];

    if(argv.length >= 1)
      port = Short.parseShort(argv[0]);

    ServerSocket server = null;

    try {
      server = new ServerSocket(port, 0, InetAddress.getByName(host));

      System.err.println("Server listening on " + host + ":" + port + "\n");
      int read;
      byte[] buffer = new byte[8192];

      while(true) {
        Socket client = server.accept();
        System.out.println("Connection accepted from " + client.getRemoteSocketAddress());
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        InputStream in = client.getInputStream();

        while((read = in.read(buffer)) > 0) {
          System.out.write(buffer, 0, read);
        }

        System.out.println("");

        out.write("HTTP/1.1 200 OK");
        out.close();
        in.close();
        client.close();
      }
    }
    finally {
      System.out.println("Closing");
      if(server != null)
        server.close();
    }
  }
}
