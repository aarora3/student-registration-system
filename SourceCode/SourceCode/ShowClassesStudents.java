
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import oracle.jdbc.internal.OracleTypes;
import oracle.jdbc.pool.OracleDataSource;

public class ShowClassesStudents {

	public void showClassesStudents(){
		Scanner line = new Scanner(System.in);
		try
		{
			
			//Connection to Oracle server
	        OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
	        ds.setURL("jdbc:oracle:thin:@localhost:1521:xe");
	        Connection conn = ds.getConnection("Project2", "akki1991");
			
			System.out.print("Enter the class id: ");
			String cid = line.nextLine();

			//calling stored procedure to show class details and students enrolled in it
			CallableStatement cs = conn.prepareCall("{call Proj2_student_reg_package.show_classes_student_details(?,?,?,?)}");

			cs.setString(1, cid);
			cs.registerOutParameter(2, OracleTypes.VARCHAR);
			cs.registerOutParameter(3, OracleTypes.CURSOR);
			cs.registerOutParameter(4, OracleTypes.CURSOR);
			
			cs.execute();
			
			
			String res = cs.getString(2);
			
			if(res.equals("success")){
				
				ResultSet rs = (ResultSet)cs.getObject(3);
				
				System.out.println("The class details are:\n");
				System.out.println("CLASSID\tCOURSE TITLE\tYEAR\tSEMESTER");
				
				//printing the result obtained
				while(rs.next()){
		            System.out.println(rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t" 
		            					+ rs.getString(4));
				}
				
				rs = (ResultSet)cs.getObject(4);
				
				System.out.println("\nAnd the students enrolled in it are:\n");
				System.out.println("SID\tLASTNAME");
				
				while(rs.next()){
		            System.out.println(rs.getString(1) + "\t" + rs.getString(2));
				}
				
			}else{
				System.out.println(res);
			}
			
			
			
			cs.close();
			//line.close();
		}
		catch (SQLException e) { System.out.println ("SQLException caught" + e.getMessage());}
		catch (Exception e) {System.out.println (e);}
	}
}
