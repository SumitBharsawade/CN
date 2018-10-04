import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner sc=new Scanner(System.in);

        Socket s = null;                    //client socket
        DataInputStream in = null;          //data input from socket
        DataOutputStream out= null;         //data output for socket
        File file= null;
        FileOutputStream fout= null;          //object for write file

        try {

            Socket socket=new Socket("localhost",8008);  //connection for local host to port number 8008
             in=new DataInputStream(socket.getInputStream());
             out=new DataOutputStream(socket.getOutputStream());
             file=new File("demo.mp4");;
             fout=new FileOutputStream(file);

            System.out.println(in.readUTF());  //read hi from server
            out.writeUTF("Hlo from client"); //send hi to server
            out.flush();

            byte[] buffer=new byte[65536];
            int filesize=in.readInt();
            int a=in.read(buffer);
            int b=a;
            while(b<filesize) {
                fout.write(buffer, 0, a);
                System.out.println("File received" + "-->" + b);
                a=in.read(buffer);
                b+=a;
            }
            fout.write(buffer, 0, a);

            System.out.println("File received" + "-->" + b);

            out.writeUTF("File Transfer complete");  //write file received msg to server
            out.flush();

            while(true) {
                //Enter choice as per operation
                System.out.print("\nChoose Trigonometric operation :\n 1.sin\n 2.cos\n 3.tan\n 4.cot" +
                        "\n 5.sec\n 6.cosec\n 7.exit\n --->>>");
                char choice = sc.next().charAt(0);

                if(choice=='7')
                {
                    out.writeChar('y');  //for end connection send y to server
                    sc.close();         //close all allocated resources
                    in.close();
                    out.close();
                    fout.close();
                    System.exit(0);  //exit program
                }
                else{
                    System.out.print("\nEnter angle Degree:");
                    Double value=sc.nextDouble();

                    out.writeChar(1);
                    out.writeDouble((Double) (value*3.14/180));  //convert degree to radian
                    System.out.println("\nANS:"+in.readDouble());   //print Ans from server
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //close all allocated resources
            if(s!=null) s.close();
            if(in!=null)in.close();
            if(fout!=null) out.close();
            if(out!=null)out.close();        }
    }
}
