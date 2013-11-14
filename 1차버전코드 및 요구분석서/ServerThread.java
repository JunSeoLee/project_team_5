import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
 
public class ServerThread implements Runnable {
 
    private Socket socket;
  
    public ServerThread(Socket socket) {         // �Ķ������ ������ �ڽ��� �������� ġȯ
        this.socket = socket;
    }
 
    public void run() {   // �������� run �޼ҵ带 Overriding
        BufferedReader bufferedReader = null;        // ���� ��ü �ʱ�ȭ
        PrintWriter printWriter = null;                       // ��� ��Ʈ�� ��ü �ʱ�ȭ
        db_connect db_cn = new db_connect();        // db ����
        //db_cn.connect();
        
        try {
            while(true){     // ���� �ݺ�
                 
                bufferedReader = new BufferedReader(   // Ŭ���̾�Ʈ�� ���� ���� �� ��� ��Ʈ�� ���� ��ü
                		new InputStreamReader( socket.getInputStream()));  
                printWriter = new PrintWriter(socket.getOutputStream(), true);  // Ŭ���̾�Ʈ�� �����͸� ���� �� ��ü
                 
                String str = bufferedReader.readLine();   // Ŭ���̾�Ʈ�κ��� �޽��� ����
                if(str == null){
                    System.out.println("client disconnect");
                    break;
                }
                 
                  System.out.println("client message : " + str);
               
                  switch(Integer.parseInt(str))   // �޽��� �ǵ�
                  {
                         case 1:       // �α��� ���
                        	 printWriter.println("�α���â�Դϴ�");
                        	 String log_id = bufferedReader.readLine();         // id ����
                        	 String log_pw = bufferedReader.readLine();      // pw ����
                        	 
                        	 String[] b_info = db_cn.login_compare(log_id, log_pw);   // db�α��� �޼��� ȣ��
                        	 if(b_info == null)  // ���� �� ��ġ
                        	 {
                        		 printWriter.println("-1"); 
                        	 }
                        	 else      // ���� ��ġ
                        	 {
                        		 printWriter.println("1");
                        		 for(int i=0; i<b_info.length; i++)
                            	 {
                        			 printWriter.println(b_info[i]);    // ����� ���� ����
                            	 }
                        	 }
                        	 continue;
                         case 2:        // �α��� ��� ���
                        	 printWriter.println("�α��� ����ϱ� â�Դϴ�");  // Ŭ���̾�Ʈ�� �޽��� ����
                        	 String off_id = bufferedReader.readLine();       // ����� ���̵� ����
                        	 String off_sum = bufferedReader.readLine();   // ��� �׼� ����
                        	 String total_off_sum = db_cn.log_offering(off_sum, off_id);  // db �α��� ��� �޼ҵ� ȣ��
                        	 if(total_off_sum != null)
                        	 {
                        		 printWriter.println("1");
                        		 printWriter.println(total_off_sum);    // �� ��� �ݾ� ����
                        	 }else
                        	 {
                        		 printWriter.println("-1");           // ���� �� ��ġ
                        	 }
                        	 continue;
                         case 3:                 // ����� ��� ���
                        	 printWriter.println("����� ����ϱ� â�Դϴ�");
                        	 String n_off_branch = bufferedReader.readLine();  // ��ȸ���� ����
                        	 String n_off_sum = bufferedReader.readLine();      // ����� ��� �ݾ� ����
                        	 String assets = db_cn.n_log_offering(n_off_branch, n_off_sum);   // db ����� ��� ȣ��
                        	 if(assets != null)
                        	 {
                        		 printWriter.println("1");
                        		 printWriter.println(assets);         // �ش米ȸ������ �� �ڻ� ����
                        	 }else
                        	 {
                        		 printWriter.println("-1");         // ���� ����ġ
                        	 }
                        	 continue;
                         case 4:
                            	 
                        	 continue;
                  }           
            }          
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(bufferedReader != null){  // ���� Ŭ����
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(printWriter != null){  // ��� ��Ʈ�� Ŭ����
                printWriter.close();
            }
            if(socket != null){       // ���� Ŭ����
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
