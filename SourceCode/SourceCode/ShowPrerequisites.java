
import java.sql.*;

import oracle.jdbc.OracleTypes;
import oracle.jdbc.pool.OracleDataSource;

public class ShowPrerequisites {

   public void showPrereq(){

	   try{
		   
        //Connection to Oracle server
        OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
        ds.setURL("jdbc:oracle:thin:@localhost:1521:xe");
        Connection conn = ds.getConnection("Project2", "akki1991");
        
	      if(conn.isValid(10)){
	    	  System.out.println("Connection successfull");
	      }
	      else{
	    	  System.out.println("Connection Unsuccessfull --- Try Again\nExiting");
	    	  System.exit(1);
	      }
        
        CallableStatement cs = conn.prepareCall("{call Proj2_student_reg_package.show_prerequisites(?)}");
		cs.registerOutParameter(1, OracleTypes.CURSOR);

		cs.execute();
		
		ResultSet rs = (ResultSet)cs.getObject(1);
		
		System.out.println("DEPT_CODE\tCOURSE_NO\tPRE_DEPT_CODE\tPRE_COURSE_NO");
		
		while(rs.next()){
            System.out.println(rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t" 
            					+ rs.getString(4));
		}
		cs.close();
   
    }
   catch (SQLException e) { System.out.println ("SQLException" + e.getMessage());}
   catch (Exception e) {System.out.println (e);}
  }
} 


