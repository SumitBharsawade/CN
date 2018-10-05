//server pogram in tcp to peer to peer chat and multi chat
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static ArrayList<ObjectOutputStream> msgout;     //array of objectoutputstream to save clients reference use for multichat
    public static void main(String[] args) throws IOException {
        Scanner cin=new Scanner(System.in);
        ServerSocket ss=new ServerSocket(8008);
         msgout=new ArrayList();            //give memory


        System.out.print("\nEnter Your Choice:\n 1.Peer to Peer chat\n 2.Multi userchat\n--->>");
        int choice=cin.nextInt();

        switch(choice)
        {
            case 1:
                    peerToPeer(ss);   //call to peer to peer chat mode
                break;

            case 2:

                while(true) {
                    Socket s = ss.accept();
                    Main.msgout.add(new ObjectOutputStream(s.getOutputStream()));
                    TalkThread t = new TalkThread(s);
                    t.start();
                }

                
            default:
                System.out.println("Wrong choice");
        }
    }

    private static void peerToPeer(ServerSocket ss) throws IOException {

        Scanner sc=new Scanner(System.in);
        Socket s=ss.accept();
        DataOutputStream dout=new DataOutputStream(s.getOutputStream());
        DataInputStream din=new DataInputStream(s.getInputStream());
        dout.writeUTF("hi");  //first msg send
        String msg=din.readUTF();

        while(msg.compareToIgnoreCase("stop")!=0)       //chat stop conditio
        {
            System.out.print("-->>"+msg+"\nEnter msg:");
            msg=sc.nextLine();
            dout.writeUTF(msg);
             msg=din.readUTF();
        }
        System.out.println("--------------Chat Ended--------------");
    }
}

//new thread to send coming msg to all  clients
class TalkThread extends Thread
{
    Socket s;
    TalkThread(Socket s)
    {
        this.s=s;
    }
    @Override
    public void run()
    {
        try {

            ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
           while (true) {
               Msg msg = (Msg) ois.readObject();        //get msg sended by one of  client
               for (ObjectOutputStream dos : Main.msgout) {
                   dos.writeObject(msg);   //send to all client
               }
           }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

//serializable class act as a container for data shearing
class Msg implements Serializable
{
    public String name;  //name of sender
    public String msgbody;  //msg
    public Msg(String name, String newmsg) {
        this.name=name;
        this.msgbody=newmsg;
    }
}
