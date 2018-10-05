//client program to peer to peer chat and multi chat
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

    public class Main {

        public static void main(String[] args) throws IOException {
            Scanner cin=new Scanner(System.in);
            Socket s=new Socket("localhost",8008);
            ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
            System.out.print("\nEnter Your Choice:\n 1.Peer to Peer chat\n 2.Multi userchat\n--->>");
            int choice=cin.nextInt();

            switch(choice)
            {
                case 1:
                    peerToPeer(s);     //peer to peer mode
                    break;

                case 2:
                        String name;
                        System.out.print("Enter your name:");   //get user name for msg identifiction
                        name=cin.next();
                        Readmsg obj=new Readmsg(s);             //thread to read msg comes from server
                        obj.start();                               //start thread
                        while(true)                 //loop to send msg
                        {
                            String newmsg=cin.nextLine();       //get msg from user
                            Msg msg=new Msg(name,newmsg);
                            oos.writeObject(msg);       //send to socket
                        }
                    //break;

                default:
                    System.out.println("Wrong choice");
            }
        }


        private static void peerToPeer(Socket s) throws IOException {

            Scanner sc=new Scanner(System.in);
            DataOutputStream dout=new DataOutputStream(s.getOutputStream());
            DataInputStream din=new DataInputStream(s.getInputStream());
            String msg=din.readUTF();

            while(true)
            {

                System.out.print("-->>"+msg+"\nEnter msg:");
                msg=sc.nextLine();
                dout.writeUTF(msg);
                if(msg.compareToIgnoreCase("stop")==0)
                    break;
                msg=din.readUTF();
            }
                s.close();
            System.out.println("--------------Chat Ended--------------");
        }
    }

class Readmsg extends Thread   //read msg from server
{
    Socket s;
    Readmsg(Socket s)
    {
        this.s=s;
    }
    @Override
    public void run()
    {
        try {
            ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
            while(true) {
                Msg msg = (Msg) ois.readObject();
                System.out.println(msg.name + " : " + msg.msgbody);  //show msg
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

class Msg implements Serializable       //msg container class
{
    public String name;
    public String msgbody;

    public Msg(String name, String newmsg) {
        this.name=name;
        this.msgbody=newmsg;
    }
}
