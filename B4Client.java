//client program to peer to peer chat and multi chat in udp
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner cin=new Scanner(System.in);
        DatagramSocket ds=new DatagramSocket(8009);
        System.out.print("\nEnter Your Choice:\n 1.Peer to Peer chat\n 2.Multi userchat\n--->>");
        int choice=cin.nextInt();

        switch(choice)
        {
            case 1:
                peerToPeer(ds);     //peer to peer mode
                break;

            case 2:
                String name;
                System.out.print("Enter your name:");   //get user name for msg identifiction
                name=cin.next();
                Readmsg obj=new Readmsg(ds);             //thread to read msg comes from server
                obj.start();                               //start thread
                while(true)                 //loop to send msg
                {
                    String newmsg=cin.nextLine();       //get msg from user
                    Msg msg=new Msg(name,newmsg);
                    //oos.writeObject(msg);       //send to socket
                    ByteArrayOutputStream bos=new ByteArrayOutputStream();
                    ObjectOutputStream dos=new ObjectOutputStream(bos);
                    dos.writeObject(msg);
                    DatagramPacket dp=new DatagramPacket(bos.toByteArray(),bos.size(),InetAddress.getByName("127.0.0.1"),8008);
                    ds.send(dp);
                }
                //break;

            default:
                System.out.println("Wrong choice");
        }
    }


    private static void peerToPeer(DatagramSocket ds) throws IOException {

        Scanner sc=new Scanner(System.in);
        byte[] msgbuffer=new byte[200];           //byte array for store hi msg from client
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        DataOutputStream dout=new DataOutputStream(bos);

        dout.writeUTF("Hi");
        DatagramPacket dp=new DatagramPacket(msgbuffer,msgbuffer.length);  //create new packet
       dp.setData(bos.toByteArray());
       dp.setLength(bos.size());
        dp.setAddress(InetAddress.getByName("127.0.0.1"));
        dp.setPort(8008);
       ds.send(dp);  //send hi msg

        DatagramPacket dprec=new DatagramPacket(msgbuffer,msgbuffer.length);
        ds.receive(dprec);


        DataInputStream din=new DataInputStream(new ByteArrayInputStream(dprec.getData()));

        String msg=din.readUTF();

        while(msg.compareToIgnoreCase("stop")!=0)       //chat stop conditio
        {
            System.out.print("-->>"+msg+"\nEnter msg:");
            msg=sc.nextLine();
            bos.reset();
            dout.writeUTF(msg);
            dp.setData(bos.toByteArray());
            dp.setLength(bos.size());
            ds.send(dp);  //send packet
            dprec=new DatagramPacket(msgbuffer,msgbuffer.length);  //create new packet
            ds.receive(dprec);
            din=new DataInputStream(new ByteArrayInputStream(dprec.getData()));
            msg=din.readUTF();
        }
    }
}

class Readmsg extends Thread   //read msg from server
{
    DatagramSocket ds;
    Readmsg(DatagramSocket s)
    {
        this.ds=s;
    }
    @Override
    public void run()
    {
        try {
                byte[] msgbuffer=new byte[200];
            while(true) {

                DatagramPacket dp=new DatagramPacket(msgbuffer,msgbuffer.length);
                ds.receive(dp);

                ByteArrayInputStream bis=new ByteArrayInputStream(dp.getData());  //byte array conversion
                ObjectInputStream ois=new ObjectInputStream(bis);
                Msg msg = (Msg) ois.readObject();   //get original received msg
                System.out.println("-->>"+msg.name + " : " + msg.msgbody);  //show msg
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
