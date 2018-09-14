import java.util.Scanner;

//java program for subnet calculation(ipv4)
class Subnet
{
	int ip[];  //arry to store ip adress
	Scanner sc;
	
	Subnet()
	{
		sc=new Scanner(System.in);
		ip=new int[5];    //array for storing ip adress
	}
	
	//Method for user input
	void getdata()
	{


		System.out.print("Enter ip adress:");
		String strip=sc.next();   //get input from user as String
		strip+=".";
		
		int j=0,k=0;  //j for start of subatring ,k for position i ip array
		
		//convert String to int array
		for(int i=0;i<strip.length();i++)
		{
		
			//System.out.println(".".equals(strip.charAt(i)));
			if(strip.charAt(i)=='.')
			{

				ip[k]=Integer.parseInt(strip.substring(j,i));
				j=i+1;
				k++;	
			}
			
		}
		//
		
	}
	
	void calculate()
	{
		int bit=0;		//no. of bit for subnet
	
		//check if array is out of bound	
		if(ip[0]<0||ip[0]>240)
		{	
			System.out.println("Invalid Ip address");
			return;
		}
		//
		
		//to calculate subnet get input
		System.out.print("\nEnter No of subnet you want :");
		int no_sub=sc.nextInt();
		
		if(ip[0]<128)   //for class A
		{
			System.out.println("Class :A");
			System.out.println("Default Subnet Mask :255.0.0.0");
			bit=bitcalculate(no_sub);//call to calculate no of bit requird
			if(bit>23)
			{
				System.out.println("Subnetting Not Possible");
			}
			else
			{
				
				System.out.print("Subnet Mask :255.");
				
				for(int i=1;i<=bit;i++)
				{
					System.out.print("1");
					if(i%9==0)
						System.out.print(".");
				}
				
				for(int i=bit+1;i<25;i++)
				{
					System.out.print("0");
					if(i%8==0)
						System.out.print(".");
				}
			}
		}
		else if(ip[0]<192)   //for class B
		{
			System.out.println("Class :B");
			System.out.println("Default Subnet Mask :255.255.0.0");
			bit=bitcalculate(no_sub);  //call to calculate no of bit requird
			if(bit>15)	
			{
				System.out.println("Subnetting Not Possible");
			}
			else
			{
				
				System.out.print("Subnet Mask :255.255.");
				
				for(int i=1;i<=bit;i++) //print  one equal to bit
				{
					System.out.print("1");
					if(i%9==0)
						System.out.print(".");
				}
				
				if(bit%8==0)
					System.out.print(".");
				
				for(int i=bit+1;i<17;i++) //print remaining as zeros
				{
					System.out.print("0");
					if(i%8==0)
						System.out.print(".");
				}
			}
		}
		else if(ip[0]<224)	//for class C
		{
			System.out.println("Class :C");
			System.out.println("Default Subnet Mask :255.255.255.0");
			bit=bitcalculate(no_sub);
			if(bit>7)  //filter for class c
			{
				System.out.println("Subnetting Not Possible");
			}
			else
			{
				
				System.out.print("Subnet Mask :255.255.225.");
				
				for(int i=1;i<=bit;i++)
				{
					System.out.print("1");
					if(i%9==0)
						System.out.print(".");
				}
				
				for(int i=bit+1;i<9;i++)
				{
					System.out.print("0");
					if(i%8==0)
						System.out.print(".");
				}
			}
		}
		else	//for class D
		{
			System.out.println("Class :D");
			System.out.println("Reserved for multicasting");
			return;
		}
		
		
	}

	//calculate no of bit required for given subnet
	private int bitcalculate(int no)
	{
		int i=1;
		
		while(Math.pow(2,i)<no)
		{
			i++;
		}
		return i;
	}

	public static void main(String[] args)
	{
		Subnet obj=new Subnet();
		
		obj.getdata();
		obj.calculate();
		System.out.println("\n");
	}
} 

/*	Output

sumit@sumit-HP-Pavilion-Notebook:~/Desktop/cn$ java Subnet
Enter ip adress:191.168.11.66

Enter No of subnet you want :240
Class :B
Default Subnet Mask :255.255.0.0
Subnet Mask :255.255.11111111.00000000.


sumit@sumit-HP-Pavilion-Notebook:~/Desktop/cn$ java Subnet
Enter ip adress:230.168.11.25

Enter No of subnet you want :5
Class :D
Reserved for multicasting

*/
