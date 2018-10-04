import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main {

        //Method to do trignometric calculation
        static Double Calculation(char choice,Double value)
        {

            switch (choice)
            {
                    case 1:
                    return Math.sin(value);

                    case 2:
                        return Math.cos(value);

                    case 3:
                        return Math.tan(value);
                    case 4:
                        return 1/Math.tan(value);
                    case 5:
                        return 1/Math.cos(value);

                    case 6:
                        return 1/Math.sin(value);
            }
            return -1.0;
        }
        //
    public static void main(String[] args) throws IOException {
	// write your code here
        Scanner sc=new Scanner(System.in); //Scanner for input from terminal
        Socket s = null;                    //client socket
        ServerSocket ss = null;             //server socket object
        DataInputStream in = null;          //data input from socket
        DataOutputStream out= null;         //data output for socket
        File file= null;
        FileInputStream fin= null;          //object for read from file

        try {

             ss=new ServerSocket(8008);  //create serversocket with port number 8008
             s=ss.accept();          //accept connection from client
             in=new DataInputStream(s.getInputStream());
             out=new DataOutputStream(s.getOutputStream());


            out.writeUTF("Hi from server\n");   //send hi message to client
            out.flush();                            //flush all data to stream
            System.out.println(in.readUTF());       //read hi from client

            System.out.print("Enter file path to transfer:");
            String path=sc.next();
             file =new File(path);

             fin=new FileInputStream(file);
            byte[] buffer=new byte[(int)file.length()];  //create byte array of file size
            int count=fin.read(buffer,0,buffer.length); //read data from file
            System.out.println("count"+count);
            out.writeInt(count);
            out.write(buffer,0,count);          //write file data to socket

            out.flush();

            System.out.println(in.readUTF());

            char choice=in.readChar();          //read operation choice from client
            while(choice!='y')
            {
                out.writeDouble(Calculation(choice,in.readDouble()));
                choice=in.readChar();
            }

        } catch (IOException e) {
            System.out.println(e);

        }finally {
            //close all allocated resource
            if(s!=null) s.close();
            if(ss!=null) ss.close();
            if(in!=null)in.close();
            if(fin!=null) fin.close();
            if(out!=null)out.close();

        }

    }
}



//C:\Users\hp\Desktop\iphone.mp3
