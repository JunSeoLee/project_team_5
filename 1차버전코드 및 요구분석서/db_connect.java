import java.sql.Connection;      // MYSQL�� �ڹٸ� �������ֱ� ���� ����̹� JDBC(Connector/J)�� ��ġ
import java.sql.DriverManager;  // ����̹� �޴���
import java.sql.PreparedStatement; // ���� ������ �̿�
import java.sql.ResultSet;          // ������ �˻� ���
import java.sql.SQLException;  // sql�� ���ܹ� 
import java.sql.Statement;     // ���� ���� �� �̿�

public class db_connect {
	Connection con ;    // MYSQL�� �ڹٸ� �������ֱ� ���� ��ü
	PreparedStatement ps = null;     // ���������� �Է� ��ü �ʱ�ȭ
	Statement st = null;                // ���� ������ �Է� ��ü �ʱ�ȭ
	ResultSet rs = null;                 // ������ ��� ��ü �ʱ�ȭ
	//String sql = "select * from believer";
	
	public static void main(String[] args)
	{
		db_connect db_cn = new db_connect();
	}
	db_connect(){
		try
		{
			Class.forName("org.gjt.mm.mysql.Driver");      // mysql.Driver �˻�
			System.out.println("����̹� �˻� ����");
		}catch(ClassNotFoundException e)
		{
			System.out.println("����̹� �˻� ����");
		}
		con = null;
		String url = "jdbc:mysql://localhost:3307/cims_db";   // java db connecter: mysql // ����ȣ��Ʈ : ��Ʈ��ȣ // ����� DB �Է�
		String user = "root";              // ��Ʈ����
		String pass = "rp1424xx";    //  ��й�ȣ
		try
		{
			con = DriverManager.getConnection(url,user,pass);   // ���� �Է»����� �Ķ���ͷ� ���� �õ�
			System.out.println("My-sql ���� ����");
		}catch(SQLException e)
		{
			System.out.println("My-sql ���� ����");
		}
	}
	public String log_offering(String off_sum, String off_id)  // �α��� ���
	{
		
		String off_sum_total = indiv_off_inquiry(off_id);       // ������ݳ�����ȸ �Լ�ȣ��
		
		String sql1= "UPDATE believer SET offering_sum = '";      // ���� �� ����� ������Ʈ�ϴ� ������ �۾�
		String sql2 = "' WHERE believer_id = '";
		String sql3 = "';";
		
		String total_sql = sql1.concat(Integer.toString(Integer.parseInt(off_sum)+Integer.parseInt(off_sum_total)))
				.concat(sql2).concat(off_id).concat(sql3);           //  ��� ���� �� ��ݰ� ��������� �� ��ݾ��� ���ϴ� ������
		System.out.println(total_sql);
		try{                // ��������
			st = con.createStatement();     //���� �� db�� �������� �Է��غ�
			int n = st.executeUpdate(total_sql);   // �������� ������Ʈ
			if(n>0)    // ��������� ���� ��
			{
				return indiv_off_inquiry(off_id);     // ������ �ݾ��� �ٽ� ��ȸ �� ����
			}else      // ������� �ʾ��� ��
			{
				return null;
			}
		}catch(SQLException e){
				e.printStackTrace();
		}
		return null;
		/*
		try{       // ��������
			
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
	public String n_log_offering(String n_off_branch, String off_sum)  // ����� ���
	{
		String assets = total_off_inquiry(n_off_branch);     // �Ҽ� �� ��ȸ�� ��ݳ��� ��ȸ ������
		System.out.println(assets);
		String sql1= "UPDATE branch SET branch_assets= '";  // ����� ��� �� �ݾװ� �ش米ȸ�� �ڻ��� ���ϴ� ���� �� 
		String sql2 = "' WHERE branch_name = '";
		String sql3 = "';";
		
		String total_sql = sql1.concat(Integer.toString(Integer.parseInt(off_sum)+Integer.parseInt(assets)))
				.concat(sql2).concat(n_off_branch).concat(sql3);;
				
		System.out.println(total_sql);
		try{                // ��������
			st = con.createStatement();     // ������ ������Ʈ
			int n = st.executeUpdate(total_sql);
			if(n>0)
			{
				return total_off_inquiry( n_off_branch );  // ������Ʈ �� �ݾ��� ����
			}else{
				return null;
			}
			}catch(SQLException e){
			e.printStackTrace();
			}
			return null;
	}
	
	public String indiv_off_inquiry(String iq_id)  // ���� ��� ���� ��ȸ
	{
		String sql1 = " SELECT offering_sum FROM believer WHERE  believer_id = '";
		String sql2 = "';";
		
		String total_sql = sql1.concat(iq_id).concat(sql2);
		
		try{
			ps = con.prepareStatement(total_sql);    // select���� ����ϱ� ���� �������� �Է»���
			rs = ps.executeQuery();                             // �������� ����� resultset�� ���ͷ����ͷ� ����
			String off_sum_total;
			while(rs.next())
			{
				off_sum_total = rs.getString("offering_sum");    // offering_sum Į���� ���� �������� ����
				return off_sum_total;
			}
		}catch(SQLException e){
				e.printStackTrace();
		}
		return null;
	}
	public String total_off_inquiry(String n_off_branch)  // �ش米ȸ�� ��ü��ݳ��� ��ȸ
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
				total = rs.getString("branch_assets");   // ��ȸ�����ڻ� ��ȯ
				return total;
			}
		}catch(SQLException e){
				e.printStackTrace();
		}
		return null;
	}
	public String[] login_compare(String id, String pw)  // �α��� ��
	{
		String sql1 = " SELECT * FROM believer WHERE ( believer_id = '";  // ���̵�� ��й�ȣ�� �� ���� ��
		String sql2 = "' ) AND ( believer_pw = '";
		String sql3 = "')";

		String total_sql = sql1.concat(id).concat(sql2).concat(pw).concat(sql3);

		try{
			ps = con.prepareStatement(total_sql);
			rs = ps.executeQuery();
			String[] b_info = new String[8];
			while(rs.next())
			{
				b_info[0] = rs.getString("believer_id");                    // ��Ʈ�� �迭�� �������� ��ȯ
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
