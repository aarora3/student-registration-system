
import java.sql.*;

import oracle.jdbc.OracleTypes;
import oracle.jdbc.pool.OracleDataSource;

public class ShowLogs {

	/**
	 * function to call stored procedure to show all logs
	 */
   public void showLogs(){

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
        
        CallableStatement cs = conn.prepareCall("{call Proj2_student_reg_package.show_logs(?)}");
		cs.registerOutParameter(1, OracleTypes.CURSOR);

		cs.execute();
		
		ResultSet rs = (ResultSet)cs.getObject(1);
		
		System.out.println("LOGID\tWHO\tTIME\tTABLE_NAME\tOPERATION\tKEY_VALUE");
		
		while(rs.next()){
            System.out.println(rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) 
            					+ rs.getString(4) + "\t" + rs.getString(5) + "\t" + rs.getString(6));
		}
		
		cs.close();
   
    }
   catch (SQLException e) { System.out.println ("SQLException" + e.getMessage());}
   catch (Exception e) {System.out.println (e);}
  }
} 


