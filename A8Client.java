import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        char ans='n';
        FileInputStream fis;

        try {
            DatagramSocket ds=new DatagramSocket();

            InetAddress ip = InetAddress.getByName("127.0.0.1");      //self adress
            byte[] himsg="Hi".getBytes();                   //hi msg for server
            DatagramPacket dp=new DatagramPacket(himsg,himsg.length,ip,8008);   //fill data in packet
            ds.send(dp);    //send hi message to server

            byte[] bytename=new byte[50];       //file name byte array
            dp=new DatagramPacket(bytename,bytename.length);
            ds.receive(dp);         //receive file name from client
            String filename=new String(dp.getData(),0,dp.getLength());
            System.out.println(filename);
            File file=new File(filename);       //create that file
            byte[] bytelen=new byte[4];
            dp=new DatagramPacket(bytelen,bytelen.length);          //receive file size from server
            ds.receive(dp);
            //String filelength=new String(dp.getData());
            ByteArrayInputStream bin=new ByteArrayInputStream(dp.getData());  //geted data convert to bytearraystream class
            //ByteArrayInputStream bis=new ByteArrayInputStream(bytelen);
            DataInputStream dis=new DataInputStream(bin);       //input stream from ByteArrayInputStream
            //String len=new String();
            int count=dis.readInt();

            System.out.println(count);      //print file size


            FileOutputStream fout=new FileOutputStream(file);

            byte[] buffer=new byte[65507];
            //int filesize=in.readInt();
            dp=new DatagramPacket(buffer,buffer.length);
            ds.receive(dp);
             bin=new ByteArrayInputStream(dp.getData());
             dis=new DataInputStream(bin);
            int a=dp.getLength();

            bin.read(buffer,0,buffer.length);  //read data in buffer
            int b=a;


            while(b<count&&b>0) {

                fout.write(buffer);             //write to file
                dp=new DatagramPacket(buffer,buffer.length);
                ds.receive(dp);             //receive  file data packet
                bin=new ByteArrayInputStream(dp.getData());
                a=dp.getLength();           //get packet data length
                dis=new DataInputStream(bin);
                 dis.read(buffer,0,buffer.length);

                b+=a;
            }
             a=dis.read(buffer,0,buffer.length);
            fout.write(buffer);

        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




}

