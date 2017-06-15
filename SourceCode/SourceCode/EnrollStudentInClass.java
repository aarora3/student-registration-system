
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Scanner;

import oracle.jdbc.pool.OracleDataSource;

public class EnrollStudentInClass {

	public void enrollStdudent(){
		
		Scanner line = new Scanner(System.in);
		try
		{
			//Connection to Oracle server
	        OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
	        ds.setURL("jdbc:oracle:thin:@localhost:1521:XE");
	        Connection conn = ds.getConnection("Project2", "akki1991");
	        
			System.out.print("Enter the class id: ");
			String cid = line.nextLine();
			System.out.print("Enter the student id: ");
			String sid = line.nextLine();
			
			//calling the procedure to enroll student in class
			CallableStatement cs = conn.prepareCall("begin Proj2_student_reg_package.enroll_student_in_class(:1,:2, :3); end;");
			
			cs.setString(1, cid);
			cs.setString(2, sid);
			 
			cs.registerOutParameter(3, Types.VARCHAR);
	
			
			//executing Query
			cs.executeQuery();
			
			//printing out the result
			System.out.println(cs.getString(3));
			cs.close();
			//line.close();
		}
		catch (SQLException e) { System.out.println ("SQLException" + e.getMessage());}
		catch (Exception e) {System.out.println(e);}
	}
}