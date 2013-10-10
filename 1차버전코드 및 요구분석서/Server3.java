import java.io.IOException;
import java.net.ServerSocket;    // ���� ����
import java.net.Socket;
 
public class Server3 {
    public static void main(String[] args) {
    	//db_connect db_cn = new db_connect();
    	//db_cn.connect(); 
    	Server3 s1 = new Server3();
    	s1.Server_On();
      
    }
    public void Server_On()
    {
        ServerSocket serverSocket = null;    // �������� ��ü �ʱ�ȭ

        try {
            serverSocket = new ServerSocket(1818);  // �������� ��Ʈ��ȣ ��ü ����
           // System.out.println("���� ON");
            while(true){
                Socket socket = serverSocket.accept();   // ���� ������ accept �żҵ带 ����
                                                                                   //  Ŭ���̾�Ʈ�� ���� Ȯ��(Ŭ���̾�Ʈ �����ϸ� ����)
                if(socket != null){
                    Thread thread = new Thread(new ServerThread(socket));   // ���� �Ǿ����� ���� ����  ������ ��ü ����
                    thread.start();                                                                              // ������ ����
                    System.out.println("Client Access");                                         // Ŭ���̾�Ʈ ����
                }
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}


