import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
 
public class ServerThread implements Runnable {
 
    private Socket socket;
  
    public ServerThread(Socket socket) {         // 파라미터의 소켓을 자신의 소켓으로 치환
        this.socket = socket;
    }
 
    public void run() {   // 스레드의 run 메소드를 Overriding
        BufferedReader bufferedReader = null;        // 리더 객체 초기화
        PrintWriter printWriter = null;                       // 출력 스트림 객체 초기화
        db_connect db_cn = new db_connect();        // db 연결
        //db_cn.connect();
        
        try {
            while(true){     // 무한 반복
                 
                bufferedReader = new BufferedReader(   // 클라이언트로 부터 수신 할 출력 스트림 리더 객체
                		new InputStreamReader( socket.getInputStream()));  
                printWriter = new PrintWriter(socket.getOutputStream(), true);  // 클라이언트로 데이터를 전송 할 객체
                 
                String str = bufferedReader.readLine();   // 클라이언트로부터 메시지 수신
                if(str == null){
                    System.out.println("client disconnect");
                    break;
                }
                 
                  System.out.println("client message : " + str);
               
                  switch(Integer.parseInt(str))   // 메시지 판독
                  {
                         case 1:       // 로그인 기능
                        	 printWriter.println("로그인창입니다");
                        	 String log_id = bufferedReader.readLine();         // id 수신
                        	 String log_pw = bufferedReader.readLine();      // pw 수신
                        	 
                        	 String[] b_info = db_cn.login_compare(log_id, log_pw);   // db로그인 메서드 호출
                        	 if(b_info == null)  // 정보 불 일치
                        	 {
                        		 printWriter.println("-1"); 
                        	 }
                        	 else      // 정보 일치
                        	 {
                        		 printWriter.println("1");
                        		 for(int i=0; i<b_info.length; i++)
                            	 {
                        			 printWriter.println(b_info[i]);    // 사용자 정보 전송
                            	 }
                        	 }
                        	 continue;
                         case 2:        // 로그인 헌금 기능
                        	 printWriter.println("로그인 헌금하기 창입니다");  // 클라이언트로 메시지 전송
                        	 String off_id = bufferedReader.readLine();       // 사용자 아이디 수신
                        	 String off_sum = bufferedReader.readLine();   // 헌금 액수 수신
                        	 String total_off_sum = db_cn.log_offering(off_sum, off_id);  // db 로그인 헌금 메소드 호출
                        	 if(total_off_sum != null)
                        	 {
                        		 printWriter.println("1");
                        		 printWriter.println(total_off_sum);    // 총 헌금 금액 전송
                        	 }else
                        	 {
                        		 printWriter.println("-1");           // 정보 불 일치
                        	 }
                        	 continue;
                         case 3:                 // 무기명 헌금 기능
                        	 printWriter.println("무기명 헌금하기 창입니다");
                        	 String n_off_branch = bufferedReader.readLine();  // 교회지점 수신
                        	 String n_off_sum = bufferedReader.readLine();      // 무기명 헌금 금액 수신
                        	 String assets = db_cn.n_log_offering(n_off_branch, n_off_sum);   // db 무기명 헌금 호출
                        	 if(assets != null)
                        	 {
                        		 printWriter.println("1");
                        		 printWriter.println(assets);         // 해당교회지점의 총 자산 전송
                        	 }else
                        	 {
                        		 printWriter.println("-1");         // 정보 불일치
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
            if(bufferedReader != null){  // 버퍼 클로즈
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(printWriter != null){  // 출력 스트림 클로즈
                printWriter.close();
            }
            if(socket != null){       // 소켓 클로즈
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
