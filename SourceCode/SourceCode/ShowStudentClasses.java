
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import oracle.jdbc.internal.OracleTypes;
import oracle.jdbc.pool.OracleDataSource;

public class ShowStudentClasses {

	public void showStudentClasses(){
		Scanner line = new Scanner(System.in);
		try
		{

			//Connection to Oracle server
	        OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
	        ds.setURL("jdbc:oracle:thin:@localhost:1521:xe");
	        Connection conn = ds.getConnection("Project2", "akki1991");
			
			System.out.print("Enter student id: ");
			String sid = line.nextLine();


			CallableStatement cs = conn.prepareCall("{call Proj2_student_reg_package.show_students_classes(?,?,?)}");



			cs.setString(1, sid);

			cs.registerOutParameter(2, OracleTypes.VARCHAR);
			cs.registerOutParameter(3, OracleTypes.CURSOR);

			cs.execute();
			
			String res = cs.getString(2);
			
			
			if(res.equals("success")){
			
				System.out.println("SID\tLASTNAME\tSTATUS\tCLASSID\tCOURSE\tTITLE\tYEAR\tSEMESTER");
				
				ResultSet rs = (ResultSet)cs.getObject(3);

				while(rs.next()){
		            System.out.println(rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t" 
		            					+ rs.getString(4) + "\t" + rs.getString(5) + "\t" + rs.getString(6)
		            					+ rs.getString(7) + "\t" + rs.getString(8));
				}
			}
			else{
				System.out.println(res);
			}
			cs.close();
			//line.close();
		}
		catch (SQLException e) { System.out.println ("SQLException" + e.getMessage());}
		catch (Exception e) {System.out.println (e);}
	}
}
