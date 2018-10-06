//server pogram in Udp to peer to peer chat and multi chat
import sun.net.PortConfig;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static ArrayList<UserDetail> user;     //array of user to save clients reference use for multichat
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Scanner cin=new Scanner(System.in);
        DatagramSocket ds=new DatagramSocket(8008);     //datagram server of port=8008
        user=new ArrayList();            //give memory


        System.out.print("\nEnter Your Choice:\n 1.Peer to Peer chat\n 2.Multi userchat\n--->>");
        int choice=cin.nextInt();

        switch(choice)
        {
            case 1:
                peerToPeer(ds);   //call to peer to peer chat mode
                break;

            case 2:
                System.out.print("Enter how many users:");
                int a=cin.nextInt();
                for(int i=1;i<a+1;i++)//get chat users information
                {
                        UserDetail obj=new UserDetail();
                    System.out.print("Enter "+i+ ". user ip adress:");
                    obj.setIa(InetAddress.getByName(cin.next()));
                    System.out.print("Enter "+i+ ". user port number:");
                    obj.setPort(cin.nextInt());
                    user.add(obj);//add to arraylist
                }
                byte[] himsg=new byte[200];           //byte array for store hi msg from client

                ByteArrayOutputStream bos =new ByteArrayOutputStream();
                DataOutputStream dout=new DataOutputStream(bos);

                while(true) {

                    DatagramPacket dp=new DatagramPacket(himsg,himsg.length);  //create new packet
                    ds.receive(dp);     //get first hi msaage from client

                    //code to send received msg to all other users
                    for(UserDetail i:user)
                    {
                        dp.setAddress(i.getIa());
                        dp.setPort(i.getPort());
                        ByteArrayInputStream bo=new ByteArrayInputStream(dp.getData());
                        ObjectInputStream oos=new ObjectInputStream(bo);
                        Msg msg=(Msg) oos.readObject();
                        System.out.println("-->>"+msg.name+":"+msg.msgbody);
                        ds.send(dp);
                    }
                }


            default:
                System.out.println("Wrong choice");
        }
    }

    private static void peerToPeer(DatagramSocket ds) throws IOException {

        Scanner sc=new Scanner(System.in);
        byte[] himsg=new byte[200];           //byte array for store hi msg from client
        ByteArrayOutputStream bos =new ByteArrayOutputStream();
        DataOutputStream dout=new DataOutputStream(bos);


        DatagramPacket dp=new DatagramPacket(himsg,himsg.length);  //create new packet
        ds.receive(dp);     //get first hi msaage from client

        InetAddress clientads=dp.getAddress();          //store client ip adress
        int clientport=dp.getPort();

        ByteArrayInputStream bis=new ByteArrayInputStream(dp.getData());
        DataInputStream din=new DataInputStream(bis);
        System.out.println(din.readUTF());

        dout.writeUTF("Hi");
        dp.setAddress(clientads);
        dp.setPort(clientport);
        dp.setData(bos.toByteArray());
        dp.setLength(bos.size());
        ds.send(dp);

        DatagramPacket dprec=new DatagramPacket(himsg,himsg.length);  //create new packet
        ds.receive(dprec);

        din=new DataInputStream(new ByteArrayInputStream(dprec.getData()));

         String msg=din.readUTF();

        while(msg.compareToIgnoreCase("stop")!=0)       //chat stop conditio
        {
            System.out.print("-->>"+msg+"\nEnter msg:");
            msg=sc.nextLine();
            bos.reset();    //reet the bytearraystream
            dout.writeUTF(msg);
            dp.setData(bos.toByteArray());
            dp.setLength(bos.size());
            ds.send(dp);
            dprec=new DatagramPacket(himsg,himsg.length);  //create new packet
            ds.receive(dprec);
            din=new DataInputStream(new ByteArrayInputStream(dprec.getData()));
            msg=din.readUTF();
        }
        bos.reset();  //reet the bytearraystream
        dout.writeUTF("stop");
        dp.setData(bos.toByteArray());
        dp.setLength(bos.size());
        ds.send(dp);  //send last stop msg
        System.out.println("--------------Chat Ended--------------");
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
class UserDetail
{
     InetAddress ia;
    int port;

    public InetAddress getIa() {
        return ia;
    }

    public void setIa(InetAddress ia) {
        this.ia = ia;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
