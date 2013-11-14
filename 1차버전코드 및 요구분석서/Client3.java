import java.io.BufferedReader;  // 일정량 character단위 단위로 일정량 데이터를 교환하기 위해
import java.io.IOException;
import java.io.InputStreamReader; // InputStream과는 달리 입력스트림에서 데이터를 character단위로 처리하기 위해
import java.io.PrintWriter;   // 주어진 데이터를 문자출력 스트림으로 출력하기 위해
import java.net.Socket;       // 소켓 통신 이용
import java.net.UnknownHostException;  // 호스트 알지못해 예외
import java.util.*;
 
public class Client3 {
	
	Socket socket = null;            // 소켓 객체 초기화
    BufferedReader bufferedReader = null;    //  리더 객체 초기화
    BufferedReader bufferedReaderFromServer = null;  // 서버 리더 객체 초기화
    PrintWriter printWriter = null;   // 문자출력 스트림 객체 초기화
    
    //String b_id, b_pw, b_name, b_add, b_pos ,b_tel, b_branch, off_sum; 
    String base_branch="목포성결교회";
    static String[] b_info = new String[8];   // 사용자 정보를 담아 둘 스트링 배열
    
    static Scanner in = new Scanner(System.in);
	boolean login_status = false;                // 로그인 상태 변수
	
    public static void main(String[] args) {
    	
        Client3 c1 = new Client3();
        
        while(true)
        {
        	c1.total_display();
            int choice =  in.nextInt();       // 메뉴 입력
            switch(choice)
            {
               case 1:
            	   c1.offering();                    // 헌금하기 및 내정보 조회 및 개인헌금내역조회 및 전체 헌금내역 조회
            	   continue;
               case 3:
            	   c1.login();                        // 로그인 하기 
            	   continue;
               default:
            	   continue;
            }
        }
    }

    Client3()
    {
    	 try {
    		    socket = new Socket("127.0.0.1", 1818);           // 호스트와 포트번호입력 후 객체 생성
    	        bufferedReader = new BufferedReader(new InputStreamReader(System.in));   // 서버로 보낼 매시지 리더 객체 생성 
    	        printWriter = new PrintWriter(socket.getOutputStream());     // 소켓의 출력스트림 객체 생성
    	        bufferedReaderFromServer = new BufferedReader(
    	        		new InputStreamReader(socket.getInputStream()));   // 서버의 매시지를 받을 리더 객체 생성
    	 } catch (UnknownHostException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         } 
    }
    public void login()         // 로그인
    {
    	try {
    		printWriter.println("1");       // 1번 기능을 하겟다고 서버로 매시지 전송
    		printWriter.flush();               // 버퍼 초기화
    		
    		String strFromServer = bufferedReaderFromServer.readLine();       // 서버의 응답을 받아옴
            System.out.println("Server message : " + strFromServer);
    		
            String id,pw;
        	System.out.print("id 입력: ");              // id 전송
        	id = bufferedReader.readLine();
        	printWriter.println(id);
        	printWriter.flush();
  	
        	System.out.print("pw 입력: ");           // pw 전송
        	pw = bufferedReader.readLine();
        	printWriter.println(pw);
        	printWriter.flush();
        	
        	strFromServer = bufferedReaderFromServer.readLine();            // 응답 받음
            System.out.println("Server message : " + strFromServer);
            
            if(Integer.parseInt(strFromServer) == -1)         // 전송한 id와 pw의 정보가 일치하지 않을 경우
            {
            	System.out.println("사용자의 정보가 일치하지 않습니다.");
            	return;
            }
            else if(Integer.parseInt(strFromServer) == 1)      // 정보가 일치 할 경우
            {
            	for(int i=0; i<b_info.length; i++)
           	    {
            		/*
            		if(b_info[i] == null){
                        System.out.println("서버와 연결이 끊어졌습니다.");
                        return;
                    }
                    */
            		b_info[i] = bufferedReaderFromServer.readLine();   // 내 정보 스트링 배열에 서버로 부터 정보 수신
             	}
            	System.out.println(b_info[2]+"님 환영합니다! 로그인 되셨습니다!!"); 
            	login_status = true;
            	//System.out.println("\t아이디       \t이  름\t주  소\t연락처 \t가입교회명\t직  책 \t헌금총액");
            	String[] col = new String[8];
            	col[0] = "아이디: "; col[2] = "이름: "; col[3] = "주소: "; col[4]= "연락처: ";col[5]= "가입교회명: ";
                col[6] = "직책: "; col[7] = "헌금총액: ";
            	for(int i=0; i<b_info.length; i++)     // 내정보 출력
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
    public void offering()     // 헌금하기
    {
    	try {
            display1();    
            int choice =in.nextInt();
            if(choice == 1)    // 로그인 헌금
            {
            	if(login_status == false)  // 비 로그인일 경우
            	{
            		login();   // 로그인
            	}
            	else  // 로그인 상태 일 경우
            	{
            		printWriter.println("2");    // 2번 기능을 하겠다고 송신
            		printWriter.flush();
            		String strFromServer = bufferedReaderFromServer.readLine();
                    System.out.println("Server message : " + strFromServer);
            		
                    printWriter.println(b_info[0]);  // 로그인 상태 일 경우 사용자의 아이디를 송신
                    System.out.print("금액 입력: ");
                    printWriter.flush();
                    String sum = bufferedReader.readLine();    // 헌금을 금액 입력 및 전송
                    printWriter.println(sum);
            		printWriter.flush();
            		
            		strFromServer = bufferedReaderFromServer.readLine();      // 서버의 응답을 받음
                    if(Integer.parseInt(strFromServer) == -1)
                    {
                    	System.out.println("헌금 실패");
                    	return;
                    }
                    else if(Integer.parseInt(strFromServer) == 1)  // 개인 헌금 내역 출력
                    {
                    	String off_sum_total =  bufferedReaderFromServer.readLine();
                    	System.out.println("헌금 성공");
                    	System.out.println(b_info[0]+ "님 \n헌금 금액: "+sum+"\n총 헌금금액: "+ off_sum_total);
                    }
            	}
            }
            else if(choice ==2)  // 무기명 헌금
            {
            	printWriter.println("3");   // 3번 기능
        		printWriter.flush();
        		String strFromServer = bufferedReaderFromServer.readLine();
                System.out.println("Server message : " + strFromServer);
                
                printWriter.println(base_branch);  // 교회지점 이름을 전송
        		printWriter.flush();
                System.out.print("금액 입력: ");     // 금액 입력
                String sum = bufferedReader.readLine();
                printWriter.println(sum);
                printWriter.flush();
                
        		
        		strFromServer = bufferedReaderFromServer.readLine();   // 서버응답
                if(Integer.parseInt(strFromServer) == -1)
                {
                	System.out.println("헌금 실패");
                	return;
                }
                else if(Integer.parseInt(strFromServer) == 1)        // 교회지점헌금내역 조회
                {
                	strFromServer =  bufferedReaderFromServer.readLine();
                	System.out.println("헌금 성공");
                	System.out.println("무기명님"+ " \n헌금 금액: "+sum+"\n본 교회 총 헌금금액: "+ strFromServer );
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
    	System.out.println("**자동 헌금 시스템**");
    	System.out.println("* 1. 헌금                 *");
    	System.out.println("* 2. 조회                 *");
    	System.out.println("* 3. 신도가입             *");
    	System.out.println("* 4. 내 정보              *");
    	System.out.println("**********************");
    }
    public void display1()
    {
    	System.out.println("**자동 헌금 시스템**");
    	System.out.println("* 1. 개인 헌금          *");
    	System.out.println("* 2. 익명 헌금          *");
    	System.out.println("* 3. 뒤로 가기          *");
    	System.out.println("**********************");
    }
    public void display2()
    {
    	System.out.println("**  자동 헌금 시스템  **");
    	System.out.println("* 1. 개인헌금내역조회 *");
    	System.out.println("* 2. 전체헌금수납현황 *");
    	System.out.println("* 3. 뒤로 가기             *");
    	System.out.println("************************");
    }
}
