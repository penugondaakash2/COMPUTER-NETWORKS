import java.io.*;
import java.net.*;
import java.util.*;

class goback_server{
public static void main(String args[])throws IOException{

System.out.println("...Server...");
System.out.println("...Waiting...");
InetAddress address = InetAddress.getByName("Localhost");
ServerSocket ss = new ServerSocket(500);

Socket s1 = new Socket();
s1 = ss.accept();
BufferedInputStream in = new BufferedInputStream(s1.getInputStream());
DataOutputStream out = new DataOutputStream(s1.getOutputStream());

System.out.println("RECEIVED REQUEST FOR SENDING FRAMES ");
int n = in.read();

boolean[] array = new boolean[n];

int pc = in.read();
System.out.println("...Sending...");

if(pc==0){
for(int i=0;i<n;i++){
System.out.println("SENDING FRAME : "+i);
out.write(i);
out.flush();
System.out.println("..WAITING FOR ACKNOWLEDGE..");
try{
Thread.sleep(5000);
}
catch (Exception e){}
int a = in.read();
System.out.println("RECEIVED ACKNOWLEDGEMENT FOR FRAME : " +i+ " as "+a);
}
out.flush();
}
else{
for(int i=0;i<n;i++){
if(i==3) {
System.out.println("SENDING FRAME NUMBER : " +i);
}
else{
System.out.println("SENDING FRAME NUMBER : " +i);
out.write(i);
out.flush();
System.out.println("WAITING FOR ACKNOWLEDGEMENT ");


try {
Thread.sleep(7000);
}
catch(Exception e){}
int a = in.read();
if(a!=255){
System.out.println("RECEIVED ACK FOR FRAME NUM : "+i+" as "+a);
array[i]=true;
}
}
}
for(int a=0;a<n;a++){
if(array[a]==false){
System.out.println("RESENDING FRAME : " +a);
out.write(a);
out.flush();
System.out.println("WAITING FOR ACK");
try {
	Thread.sleep(5000);
	}
catch(Exception e){}

int b = in.read();
System.out.println("RECEIVING ACK FOR FRAME NUMBER : "+a+" as "+b);
array[a]=true;
}
}
out.flush();
}
in.close();
System.out.println("QUITTING");
}
}



