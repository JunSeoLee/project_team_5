import java.io.IOException;
import java.net.ServerSocket;    // 서버 소켓
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
        ServerSocket serverSocket = null;    // 서버소켓 객체 초기화

        try {
            serverSocket = new ServerSocket(1818);  // 서버소켓 포트번호 객체 생성
           // System.out.println("서버 ON");
            while(true){
                Socket socket = serverSocket.accept();   // 서버 소켓의 accept 매소드를 통한
                                                                                   //  클라이언트의 접근 확인(클라이언트 접속하면 리턴)
                if(socket != null){
                    Thread thread = new Thread(new ServerThread(socket));   // 접속 되었으면 서버 다중  스레드 객체 생성
                    thread.start();                                                                              // 스레드 시작
                    System.out.println("Client Access");                                         // 클라이언트 접속
                }
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}


