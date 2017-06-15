
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import oracle.jdbc.OracleTypes;
import oracle.jdbc.pool.OracleDataSource;

public class AddStudent {
	
	/**
	 * function to add the student
	 */
	public void addStudent(){
		Scanner line = new Scanner(System.in);
		try
		{		
			
			//Connection to Oracle server
	        OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
	        ds.setURL("jdbc:oracle:thin:@localhost:1521:xe");
	        Connection conn = ds.getConnection("Project2", "akki1991");
	        
	        //Taking parameters as user input
			System.out.print("Enter the sid: ");
			String sid = line.next();
			System.out.print("Enter first name: ");
			String fname = line.next();
			System.out.print("Enter last name: ");
			String lname = line.next();
			System.out.print("Enter status: ");
			String status = line.next();
			System.out.print("Enter gpa: ");
			String gpa = line.next();
			System.out.print("Enter email: ");
			String email = line.next();
			
			//calling the stored procedure to add a student in package Proj2_student_reg_package
			CallableStatement cs = conn.prepareCall("{call Proj2_student_reg_package.add_students(?,?,?,?,?,?,?)}");
			
			//setting up the parameters
			cs.setString(1, sid);
			cs.setString(2, fname);
			cs.setString(3, lname);
			cs.setString(4, status);
			cs.setString(5, gpa);
			cs.setString(6, email);
			cs.registerOutParameter(7, OracleTypes.VARCHAR);

			cs.execute();
			String res = cs.getString(7);
			
			if(res.equals("success")){
				System.out.println("Student records added successfully");
			}
			else{
				System.out.println("Error in inserting the student - Please check the values");
			}
			cs.close();
			//line.close();
		}
		catch (SQLException e) { System.out.println("SQLException " + e.getMessage());}
		catch (Exception e) {System.out.println(e);}
	}

}

