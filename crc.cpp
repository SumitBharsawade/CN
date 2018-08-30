
#include<iostream>
using namespace std;

class CRC
{
	bool code[20],gen[20],rem[30]; //code array generator array reminder array
	int clen,glen;		      //code length  generator length			
	
	public:
	void getdata();
	void compute();
	
};

void CRC::getdata()
{
	string costr,genstr;
	//first get array of code in the form of string
	cout<<"\nEnter code:";
	cin>>costr;
	clen=costr.length();  //calculate length of code
	//convert string element to boolean array
	for(int i=0;i<clen;i++)
	{
		
		if(costr[i]=='0')
			code[i]=0;
		else
			code[i]=1;	
	}
	cout<<"code length="<<clen<<endl;

		
	// get array of generator in the form of string
	cout<<"\nEnter Generater:";
	cin>>genstr;
	glen=genstr.length();//calculate length of generator
	//convert string element to boolean array
	
	for(int i=0;i<glen;i++)
	{
	
		if(genstr[i]=='0')
			gen[i]=0;
		else
			gen[i]=1;		
	}
	cout<<"gen len="<<glen<<endl;
	
}

void CRC::compute()
{

	int k=0,l=0,m=0,t=0;      //k start of rem index l is end index of rem  
				  //m is start of code index t for counting loop
	 
	//adding glen-1 zeros in code
	for(int i=clen;i<clen+glen-1;i++)
	{
			
		code[i]=0;
				
	}
	//
	
	clen=clen+glen-1; //recalculate length of code beacuse of adding zeros
	
	//coping first glen element to rem array
	for(int i=0;i<glen;i++)
	{
		rem[i]=code[i];
	}
	//
	
	l=glen-1;
	m=glen-1;
	
	//doing xor
	for(int i=0;i<glen;i++)
	{
		if(rem[i]==gen[i])
		{
			rem[i]=0;
		}
		else
			rem[i]=1;
		
	
	}
	//
	//skipping 0 
	t=0;
	while(rem[k]==0 &&t<glen &&m<clen-1)
	{
		
		rem[++l]=code[++m];
		t++;k++;
	}
	//
	
	if(l-m>clen-1)
	{
		return;
	}
	
	//
	while(m<clen)
	{
		for(int i=k,j=0;j<glen;i++,j++)
		{
			if(rem[i]==gen[j])
			{
				rem[i]=0;
			}
			else
				rem[i]=1;		
			
		}
	for(int i=k;i<=l;i++)
	{
		cout<<rem[i]<<" ";
	}
	cout<<endl;
	cout<<"\nk l m"<<k<<l<<m;
	t=0;
	while(rem[k]==0 &&t<glen &&m<clen-1)
	{
		
		rem[++l]=code[++m];
		t++;k++;
	}
		
	
	if(m>=clen-1)
	{
		break;
	}
	
	}
	cout<<"\nk l m"<<k<<l<<m;
	
	while(rem[k]==0)
	{
		
		k++;
	}
	
	if(l-k>=glen-1)
	{
	for(int i=k,j=0;j<glen;i++,j++)
		{
			if(rem[i]==gen[j])
			{
				rem[i]=0;
			}
			else
				rem[i]=1;
		
			
		}
		
	}
	while(rem[k]==0)
	{
		
		k++;
	}	
	
	
	cout<<"\nReminder=";
	for(int i=k;i<=l;i++)
	{
		cout<<rem[i]<<" ";
	}

	//final result dividend+reminder
	for(int i=clen-(clen-k),j=k;i<clen;i++,j++)
	{
		code[i]=rem[j];
	}
	
	
	cout<<"\nFinal code=";
	for(int i=0;i<clen;i++)
	{
		cout<<code[i]<<" ";
	}
	cout<<endl;

}


int main()
{

	CRC obj;
	obj.getdata();
	obj.compute();
}

