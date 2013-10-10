import java.io.BufferedReader;  // ������ character���� ������ ������ �����͸� ��ȯ�ϱ� ����
import java.io.IOException;
import java.io.InputStreamReader; // InputStream���� �޸� �Է½�Ʈ������ �����͸� character������ ó���ϱ� ����
import java.io.PrintWriter;   // �־��� �����͸� ������� ��Ʈ������ ����ϱ� ����
import java.net.Socket;       // ���� ��� �̿�
import java.net.UnknownHostException;  // ȣ��Ʈ �������� ����
import java.util.*;
 
public class Client3 {
	
	Socket socket = null;            // ���� ��ü �ʱ�ȭ
    BufferedReader bufferedReader = null;    //  ���� ��ü �ʱ�ȭ
    BufferedReader bufferedReaderFromServer = null;  // ���� ���� ��ü �ʱ�ȭ
    PrintWriter printWriter = null;   // ������� ��Ʈ�� ��ü �ʱ�ȭ
    
    //String b_id, b_pw, b_name, b_add, b_pos ,b_tel, b_branch, off_sum; 
    String base_branch="�������ᱳȸ";
    static String[] b_info = new String[8];   // ����� ������ ��� �� ��Ʈ�� �迭
    
    static Scanner in = new Scanner(System.in);
	boolean login_status = false;                // �α��� ���� ����
	
    public static void main(String[] args) {
    	
        Client3 c1 = new Client3();
        
        while(true)
        {
        	c1.total_display();
            int choice =  in.nextInt();       // �޴� �Է�
            switch(choice)
            {
               case 1:
            	   c1.offering();                    // ����ϱ� �� ������ ��ȸ �� ������ݳ�����ȸ �� ��ü ��ݳ��� ��ȸ
            	   continue;
               case 3:
            	   c1.login();                        // �α��� �ϱ� 
            	   continue;
               default:
            	   continue;
            }
        }
    }

    Client3()
    {
    	 try {
    		    socket = new Socket("127.0.0.1", 1818);           // ȣ��Ʈ�� ��Ʈ��ȣ�Է� �� ��ü ����
    	        bufferedReader = new BufferedReader(new InputStreamReader(System.in));   // ������ ���� �Ž��� ���� ��ü ���� 
    	        printWriter = new PrintWriter(socket.getOutputStream());     // ������ ��½�Ʈ�� ��ü ����
    	        bufferedReaderFromServer = new BufferedReader(
    	        		new InputStreamReader(socket.getInputStream()));   // ������ �Ž����� ���� ���� ��ü ����
    	 } catch (UnknownHostException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         } 
    }
    public void login()         // �α���
    {
    	try {
    		printWriter.println("1");       // 1�� ����� �ϰٴٰ� ������ �Ž��� ����
    		printWriter.flush();               // ���� �ʱ�ȭ
    		
    		String strFromServer = bufferedReaderFromServer.readLine();       // ������ ������ �޾ƿ�
            System.out.println("Server message : " + strFromServer);
    		
            String id,pw;
        	System.out.print("id �Է�: ");              // id ����
        	id = bufferedReader.readLine();
        	printWriter.println(id);
        	printWriter.flush();
  	
        	System.out.print("pw �Է�: ");           // pw ����
        	pw = bufferedReader.readLine();
        	printWriter.println(pw);
        	printWriter.flush();
        	
        	strFromServer = bufferedReaderFromServer.readLine();            // ���� ����
            System.out.println("Server message : " + strFromServer);
            
            if(Integer.parseInt(strFromServer) == -1)         // ������ id�� pw�� ������ ��ġ���� ���� ���
            {
            	System.out.println("������� ������ ��ġ���� �ʽ��ϴ�.");
            	return;
            }
            else if(Integer.parseInt(strFromServer) == 1)      // ������ ��ġ �� ���
            {
            	for(int i=0; i<b_info.length; i++)
           	    {
            		/*
            		if(b_info[i] == null){
                        System.out.println("������ ������ ���������ϴ�.");
                        return;
                    }
                    */
            		b_info[i] = bufferedReaderFromServer.readLine();   // �� ���� ��Ʈ�� �迭�� ������ ���� ���� ����
             	}
            	System.out.println(b_info[2]+"�� ȯ���մϴ�! �α��� �Ǽ̽��ϴ�!!"); 
            	login_status = true;
            	//System.out.println("\t���̵�       \t��  ��\t��  ��\t����ó \t���Ա�ȸ��\t��  å \t����Ѿ�");
            	String[] col = new String[8];
            	col[0] = "���̵�: "; col[2] = "�̸�: "; col[3] = "�ּ�: "; col[4]= "����ó: ";col[5]= "���Ա�ȸ��: ";
                col[6] = "��å: "; col[7] = "����Ѿ�: ";
            	for(int i=0; i<b_info.length; i++)     // ������ ���
           	    {
            		if(i != 1)
            		{
            			System.out.println(col[i]+b_info[i]);
            		}
             	}
            }
    	 } catch (UnknownHostException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         } 
     }
    public void offering()     // ����ϱ�
    {
    	try {
            display1();    
            int choice =in.nextInt();
            if(choice == 1)    // �α��� ���
            {
            	if(login_status == false)  // �� �α����� ���
            	{
            		login();   // �α���
            	}
            	else  // �α��� ���� �� ���
            	{
            		printWriter.println("2");    // 2�� ����� �ϰڴٰ� �۽�
            		printWriter.flush();
            		String strFromServer = bufferedReaderFromServer.readLine();
                    System.out.println("Server message : " + strFromServer);
            		
                    printWriter.println(b_info[0]);  // �α��� ���� �� ��� ������� ���̵� �۽�
                    System.out.print("�ݾ� �Է�: ");
                    printWriter.flush();
                    String sum = bufferedReader.readLine();    // ����� �ݾ� �Է� �� ����
                    printWriter.println(sum);
            		printWriter.flush();
            		
            		strFromServer = bufferedReaderFromServer.readLine();      // ������ ������ ����
                    if(Integer.parseInt(strFromServer) == -1)
                    {
                    	System.out.println("��� ����");
                    	return;
                    }
                    else if(Integer.parseInt(strFromServer) == 1)  // ���� ��� ���� ���
                    {
                    	String off_sum_total =  bufferedReaderFromServer.readLine();
                    	System.out.println("��� ����");
                    	System.out.println(b_info[0]+ "�� \n��� �ݾ�: "+sum+"\n�� ��ݱݾ�: "+ off_sum_total);
                    }
            	}
            }
            else if(choice ==2)  // ����� ���
            {
            	printWriter.println("3");   // 3�� ���
        		printWriter.flush();
        		String strFromServer = bufferedReaderFromServer.readLine();
                System.out.println("Server message : " + strFromServer);
                
                printWriter.println(base_branch);  // ��ȸ���� �̸��� ����
        		printWriter.flush();
                System.out.print("�ݾ� �Է�: ");     // �ݾ� �Է�
                String sum = bufferedReader.readLine();
                printWriter.println(sum);
                printWriter.flush();
                
        		
        		strFromServer = bufferedReaderFromServer.readLine();   // ��������
                if(Integer.parseInt(strFromServer) == -1)
                {
                	System.out.println("��� ����");
                	return;
                }
                else if(Integer.parseInt(strFromServer) == 1)        // ��ȸ������ݳ��� ��ȸ
                {
                	strFromServer =  bufferedReaderFromServer.readLine();
                	System.out.println("��� ����");
                	System.out.println("������"+ " \n��� �ݾ�: "+sum+"\n�� ��ȸ �� ��ݱݾ�: "+ strFromServer );
                }
            }	
    	} catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
    public void total_display()
    {
    	System.out.println("**�ڵ� ��� �ý���**");
    	System.out.println("* 1. ���                 *");
    	System.out.println("* 2. ��ȸ                 *");
    	System.out.println("* 3. �ŵ�����             *");
    	System.out.println("* 4. �� ����              *");
    	System.out.println("**********************");
    }
    public void display1()
    {
    	System.out.println("**�ڵ� ��� �ý���**");
    	System.out.println("* 1. ���� ���          *");
    	System.out.println("* 2. �͸� ���          *");
    	System.out.println("* 3. �ڷ� ����          *");
    	System.out.println("**********************");
    }
    public void display2()
    {
    	System.out.println("**  �ڵ� ��� �ý���  **");
    	System.out.println("* 1. ������ݳ�����ȸ *");
    	System.out.println("* 2. ��ü��ݼ�����Ȳ *");
    	System.out.println("* 3. �ڷ� ����             *");
    	System.out.println("************************");
    }
}
