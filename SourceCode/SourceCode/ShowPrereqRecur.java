
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Scanner;

import oracle.jdbc.pool.OracleDataSource;

public class ShowPrereqRecur {

	public void showPrereqRecur(){
		Scanner line = new Scanner(System.in);
		try
		{
			//Connection to Oracle server
	        OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
	        ds.setURL("jdbc:oracle:thin:@localhost:1521:XE");
	        Connection conn = ds.getConnection("Project2", "akki1991");
			
			System.out.print("Enter the dept code: ");
			String dcode = line.nextLine();
			System.out.print("Enter the course no: ");
			String crn = line.nextLine();

			CallableStatement cs = conn.prepareCall("begin Proj2_student_reg_package.show_prereq(:1,:2, :3); end;");

			cs.setString(1, dcode);
			cs.setString(2, crn);
			cs.registerOutParameter(3, Types.VARCHAR);

			cs.executeQuery();
			
			if(cs.getString(3) == null){
				System.out.println("No prerequisites found");
			}
			else{
				System.out.println(cs.getString(3));
			}
			cs.close();
			//line.close();
		}
		catch (SQLException e) { System.out.println ("SQLException caught" + e.getMessage());}
		catch (Exception e) {System.out.println (e);}
	}
}
