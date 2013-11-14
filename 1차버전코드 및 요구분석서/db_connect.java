import java.sql.Connection;      // MYSQL과 자바를 연결해주기 위한 드라이버 JDBC(Connector/J)를 설치
import java.sql.DriverManager;  // 드라이버 메니저
import java.sql.PreparedStatement; // 동적 쿼리문 이용
import java.sql.ResultSet;          // 쿼리문 검색 결과
import java.sql.SQLException;  // sql문 예외문 
import java.sql.Statement;     // 정적 쿼리 문 이용

public class db_connect {
	Connection con ;    // MYSQL과 자바를 연결해주기 위한 객체
	PreparedStatement ps = null;     // 동적쿼리문 입력 객체 초기화
	Statement st = null;                // 정적 쿼리문 입력 객체 초기화
	ResultSet rs = null;                 // 쿼리문 결과 객체 초기화
	//String sql = "select * from believer";
	
	public static void main(String[] args)
	{
		db_connect db_cn = new db_connect();
	}
	db_connect(){
		try
		{
			Class.forName("org.gjt.mm.mysql.Driver");      // mysql.Driver 검색
			System.out.println("드라이버 검색 성공");
		}catch(ClassNotFoundException e)
		{
			System.out.println("드라이버 검색 실패");
		}
		con = null;
		String url = "jdbc:mysql://localhost:3307/cims_db";   // java db connecter: mysql // 로컬호스트 : 포트번호 // 사용할 DB 입력
		String user = "root";              // 루트권한
		String pass = "rp1424xx";    //  비밀번호
		try
		{
			con = DriverManager.getConnection(url,user,pass);   // 위의 입력사항을 파라미터로 연결 시도
			System.out.println("My-sql 접속 성공");
		}catch(SQLException e)
		{
			System.out.println("My-sql 접속 실패");
		}
	}
	public String log_offering(String off_sum, String off_id)  // 로그인 헌금
	{
		
		String off_sum_total = indiv_off_inquiry(off_id);       // 개인헌금내역조회 함수호출
		
		String sql1= "UPDATE believer SET offering_sum = '";      // 지불 한 헌금을 업데이트하는 쿼리문 작업
		String sql2 = "' WHERE believer_id = '";
		String sql3 = "';";
		
		String total_sql = sql1.concat(Integer.toString(Integer.parseInt(off_sum)+Integer.parseInt(off_sum_total)))
				.concat(sql2).concat(off_id).concat(sql3);           //  방금 지불 한 헌금과 현재까지의 총 헌금액을 합하는 쿼리문
		System.out.println(total_sql);
		try{                // 정적쿼리
			st = con.createStatement();     //연결 된 db의 정적쿼리 입력준비
			int n = st.executeUpdate(total_sql);   // 쿼리문을 업데이트
			if(n>0)    // 변경사항이 있을 시
			{
				return indiv_off_inquiry(off_id);     // 합해진 금액을 다시 조회 후 리턴
			}else      // 변경되지 않았을 시
			{
				return null;
			}
		}catch(SQLException e){
				e.printStackTrace();
		}
		return null;
		/*
		try{       // 동적쿼리
			
			ps = con.prepareStatement(total_sql);
			int n = ps.executeUpdate();
			if(n>0)
			{
				return true;
			}else
			{
				return false;
			}
		}catch(SQLException e){
				e.printStackTrace();
		}
		return false;
		*/
		
	}
	public String n_log_offering(String n_off_branch, String off_sum)  // 무기명 헌금
	{
		String assets = total_off_inquiry(n_off_branch);     // 소속 된 교회의 헌금내역 조회 쿼리문
		System.out.println(assets);
		String sql1= "UPDATE branch SET branch_assets= '";  // 무기명 헌금 된 금액과 해당교회의 자산을 합하는 쿼리 문 
		String sql2 = "' WHERE branch_name = '";
		String sql3 = "';";
		
		String total_sql = sql1.concat(Integer.toString(Integer.parseInt(off_sum)+Integer.parseInt(assets)))
				.concat(sql2).concat(n_off_branch).concat(sql3);;
				
		System.out.println(total_sql);
		try{                // 정적쿼리
			st = con.createStatement();     // 쿼리문 업데이트
			int n = st.executeUpdate(total_sql);
			if(n>0)
			{
				return total_off_inquiry( n_off_branch );  // 업데이트 된 금액을 리턴
			}else{
				return null;
			}
			}catch(SQLException e){
			e.printStackTrace();
			}
			return null;
	}
	
	public String indiv_off_inquiry(String iq_id)  // 개인 헌금 내역 조회
	{
		String sql1 = " SELECT offering_sum FROM believer WHERE  believer_id = '";
		String sql2 = "';";
		
		String total_sql = sql1.concat(iq_id).concat(sql2);
		
		try{
			ps = con.prepareStatement(total_sql);    // select문을 사용하기 위해 동적쿼리 입력생성
			rs = ps.executeQuery();                             // 쿼리문의 결과를 resultset에 이터레이터로 대입
			String off_sum_total;
			while(rs.next())
			{
				off_sum_total = rs.getString("offering_sum");    // offering_sum 칼럼의 값을 가져오고 리턴
				return off_sum_total;
			}
		}catch(SQLException e){
				e.printStackTrace();
		}
		return null;
	}
	public String total_off_inquiry(String n_off_branch)  // 해당교회의 전체헌금내역 조회
	{
		String sql1 = " SELECT branch_assets FROM branch WHERE  branch_name = '";
		String sql2 = "';";
		
		String total_sql = sql1.concat(n_off_branch).concat(sql2);
		
		try{
			ps = con.prepareStatement(total_sql);
			rs = ps.executeQuery();
			String total;
			while(rs.next())
			{
				total = rs.getString("branch_assets");   // 교회지점자산 반환
				return total;
			}
		}catch(SQLException e){
				e.printStackTrace();
		}
		return null;
	}
	public String[] login_compare(String id, String pw)  // 로그인 비교
	{
		String sql1 = " SELECT * FROM believer WHERE ( believer_id = '";  // 아이디와 비밀번호의 비교 쿼리 문
		String sql2 = "' ) AND ( believer_pw = '";
		String sql3 = "')";

		String total_sql = sql1.concat(id).concat(sql2).concat(pw).concat(sql3);

		try{
			ps = con.prepareStatement(total_sql);
			rs = ps.executeQuery();
			String[] b_info = new String[8];
			while(rs.next())
			{
				b_info[0] = rs.getString("believer_id");                    // 스트링 배열로 가져오고 반환
				b_info[1] = rs.getString("believer_pw");
				b_info[2] = rs.getString("believer_name");
				b_info[3] = rs.getString("believer_address");
				b_info[4] = rs.getString("believer_telnum");
				b_info[5] = rs.getString("believer_branch_name");
				b_info[6] = rs.getString("believer_position"); 
				b_info[7] = rs.getString("offering_sum");
				//System.out.println(c_id);

				return b_info;
			}
			
		}catch(SQLException e){
				e.printStackTrace();
		}
		return null;
	}
}
