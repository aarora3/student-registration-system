
import java.sql.*;

import oracle.jdbc.OracleTypes;
import oracle.jdbc.pool.OracleDataSource;

public class ShowClasses {

	/**
	 * function to show all classes
	 */
   public void showClasses(){

	   try{
		   
        //Connection to Oracle server
        OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
        ds.setURL("jdbc:oracle:thin:@localhost:1521:xe");
        Connection conn = ds.getConnection("Project2", "akki1991");
        
        //Validating Connection
	      if(conn.isValid(10)){
	    	  System.out.println("Connection successfull");
	      }
	      else{
	    	  System.out.println("Connection Unsuccessfull --- Try Again\nExiting");
	    	  System.exit(1);
	      }
        
        CallableStatement cs = conn.prepareCall("{call Proj2_student_reg_package.show_classes(?)}");
		cs.registerOutParameter(1, OracleTypes.CURSOR);

		//executing calling of stored procedure
		cs.execute();
		
		ResultSet rs = (ResultSet)cs.getObject(1);
		
		System.out.println("CLASSID\tDEPT_CODE\tCOURSE_NO\tSECT_NO\tYEAR\tSEMESTER\tLIMIT\tCLASS_SIZE");
		
		//printing the results
		while(rs.next()){
            System.out.println(rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t" 
            					+ rs.getString(4) + "\t" + rs.getString(5) + "\t" + rs.getString(6)
            					+"\t" + rs.getString(7) + "\t" + rs.getString(8));
		}
		cs.close();
   
    }
   catch (SQLException e) { System.out.println ("SQLException" + e.getMessage());}
   catch (Exception e) {System.out.println (e);}
  }
} 


