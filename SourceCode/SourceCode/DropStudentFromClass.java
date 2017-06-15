
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Scanner;

import oracle.jdbc.pool.OracleDataSource;

public class DropStudentFromClass {

	/**
	 * function to drop a student from the class
	 */
	public void dropStudent(){
		Scanner line = new Scanner(System.in);
		try
		{

			//Connection to Oracle server
	        OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
	        ds.setURL("jdbc:oracle:thin:@localhost:1521:xe");
	        Connection conn = ds.getConnection("Project2", "akki1991");
			
			System.out.print("Enter the class id: ");
			String cid = line.nextLine();
			System.out.print("Enter the student id: ");
			String sid = line.nextLine();
			
			//calling stored procedure to drop a class in package Proj2_student_reg_package
			CallableStatement cs = conn.prepareCall("begin Proj2_student_reg_package.drop_student_from_class(:1,:2, :3); end;");
			
			cs.setString(1, cid);
			cs.setString(2, sid);
			 
			cs.registerOutParameter(3, Types.VARCHAR);

			//Executing Query
			cs.executeQuery();
			System.out.println(cs.getString(3));
			cs.close();
			///line.close();
		}
		catch (SQLException e) { System.out.println ("SQLException" + e.getMessage());}
		catch (Exception e) {System.out.println (e);}
	}
}
