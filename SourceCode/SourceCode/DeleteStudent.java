
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Scanner;

import oracle.jdbc.pool.OracleDataSource;

public class DeleteStudent {

	/**
	 * function to delete student entry
	 */
	public void deleteStudent(){
		Scanner line = new Scanner(System.in);
		try
		{
			//Connection to Oracle server
	        OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
	        ds.setURL("jdbc:oracle:thin:@localhost:1521:XE");
	        Connection conn = ds.getConnection("Project2", "akki1991");
			
	        //taking student id as user input
			System.out.print("Enter student id: ");
			String sid = line.nextLine();

			//Calling stored procedure to delete the student in package Proj2_student_reg_package
			CallableStatement cs = conn.prepareCall("begin Proj2_student_reg_package.delete_student(:1,:2); end;");

			cs.setString(1, sid);
 
			cs.registerOutParameter(2, Types.VARCHAR);

			
			cs.executeQuery();	//executing Query
			System.out.println(cs.getString(2));
			cs.close();	
			//line.close();
		}
		catch (SQLException e) { System.out.println ("SQLException" + e.getMessage());}
		catch (Exception e) {System.out.println (e);}
	}
}
