import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream dout=new DataOutputStream(bout);
        char ans='n';
        try {

            DatagramSocket ds=new DatagramSocket(8008);     //datgram server of port=8008
            InetAddress ip = InetAddress.getByName("127.0.0.1"); //convert ip address to internate format
            byte[] himsg=new byte[5];           //byte array for store hi msg from client

            DatagramPacket dp=new DatagramPacket(himsg,himsg.length);  //create new packet
            ds.receive(dp);

            InetAddress clientip=dp.getAddress();     //gives client's is adress
            int clientport=dp.getPort();               //gives client port number

            System.out.println(new String(himsg));        //print hi msg comes from client
            do{
                System.out.print("\nEnter file path to send:");
                String path=sc.next();                              //get file name with path whic we want to send
                int c=path.length();                                 //gives file length
                c--;

                while(path.charAt(c)!='\\')                   //parse only file name from path
                    c--;


                    c++;

            String filename=path.substring(c);
            System.out.println("name:"+filename);
            byte[] bytename=filename.getBytes();        //get bytes of file name to send to client

            dp=new DatagramPacket(bytename,bytename.length,clientip,clientport);
            ds.send(dp);        //file name sended to client


                 File   file =new File(path);       //open file to read data
                FileInputStream  fin=new FileInputStream(file);
                byte[] buffer=new byte[65507];  //create byte array of file size
                int count= (int) (file.length());



                dout.writeInt(count);
                dp.setData(bout.toByteArray());
                dp.setLength(bout.toByteArray().length);
                ds.send(dp);

                    bout=new ByteArrayOutputStream();
                    dout=new DataOutputStream(bout);

                 count=fin.read(buffer,0,buffer.length); //read data from file
                dout.write(buffer,0,count);     //write to output stream
                dout.flush();


                while(count>0) {

                    dp.setData(bout.toByteArray());         //add data to packet
                    dp.setLength(bout.toByteArray().length);  //specify packet data size
                    ds.send(dp);    //send packet
                    count=fin.read(buffer,0,buffer.length); //read data from file


                    bout=new ByteArrayOutputStream();       //byte array output stream
                    dout=new DataOutputStream(bout);           //data output stream for byte-array-output-stream
                    try {
                        dout.write(buffer, 0, count);
                    }catch(IndexOutOfBoundsException e)     //exception if data not found
                    {
                        break;              //break the while loop if data not found
                    }
                    dout.flush();           //flush the data to stream
                }

                System.out.println("File sended Sucessfully");


            }while(ans=='y'||ans=='Y');



            //DatagramPacket dp=new DatagramPacket()
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (IndexOutOfBoundsException e)
        {
            System.out.println(e);
        }

    }
}
//C:\Users\hp\Desktop\iphone.mp3