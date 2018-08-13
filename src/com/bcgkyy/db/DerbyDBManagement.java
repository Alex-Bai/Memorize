package com.bcgkyy.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class DerbyDBManagement{

	private String protocol = "jdbc:derby:";
	private String dbName = "kyyDB";
	private String userName = "user1";
	private String password = "user1";
    
	private Connection conn = null;
    private ArrayList<Statement> statements = new ArrayList<Statement>(); // list of Statements, PreparedStatements
    private PreparedStatement psInsert;
    private PreparedStatement psUpdate;
    private PreparedStatement psDelete;
    private PreparedStatement psQuery;
    private Statement statement;
    private ResultSet resultSet;
	
    public DerbyDBManagement() {    	    	
    	/*Properties props = new Properties();
    	props.put("user", userName);
    	props.put("password", password);  */  
    	Properties props = new Properties(); // connection properties
        // providing a user name and password is optional in the embedded
        // and derbyclient frameworks
        props.put("user", "user1");
        props.put("password", "user1");
    	try {
			conn = DriverManager.getConnection(protocol + dbName + ";create=true");
			conn.setAutoCommit(false);			
			
			statement = conn.createStatement();
			statements.add(statement);
						
		} catch (SQLException e) {			
			e.printStackTrace();
		}
    }
   
    public boolean createTable(String sqlStr, String tableName) {
    	try {
    		resultSet = conn.getMetaData().getTables(null, conn.getSchema(), tableName.toUpperCase(), null);
    		boolean res = true;
    		if(!resultSet.next()) {
    			res = statement.execute(sqlStr);
    			conn.commit();
    		}						
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
		}    	
    	return false;
    }
    
    public boolean insertRecords(String sqlStr, List<String[]> records) {
    	try {
    		psInsert = conn.prepareStatement(sqlStr);
			statements.add(psInsert);
			if(records != null && !records.isEmpty()) {
				for(String[] record : records) {
	    			for(int i=0; i<record.length; i++) {
	    				psInsert.setString(i+1, record[i]);
	    			}
	    			psInsert.executeUpdate();
	    		}
			}    		
    		conn.commit();
    		return true;
		} catch (SQLException e) {			
			e.printStackTrace();
		}
    	return false;
    }
    
    public boolean updateRecords(String sqlStr, List<String> conditions) {
    	try {
			psUpdate = conn.prepareStatement(sqlStr);
			statements.add(psUpdate);
			if(conditions != null && !conditions.isEmpty()) {
				for(int i=0; i<conditions.size(); i++) {
					psUpdate.setString(i+1, conditions.get(i));
				}
			}			
			psUpdate.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return true;
    }
    
    public boolean deleteRecords(String sqlStr, List<String> conditions) {
    	try {
    		psDelete = conn.prepareStatement(sqlStr);
			statements.add(psDelete);
			if(conditions != null && !conditions.isEmpty()) {
				for(int i=0; i<conditions.size(); i++) {
					psDelete.setString(i+1, conditions.get(i));
				}
			}			
			psDelete.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return true;
    }
    
    public void addColumn(String sqlStr) {
    	try {
			psUpdate = conn.prepareStatement(sqlStr);
			psUpdate.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public List<List<String>> searchRecord(String sqlStr, List<String> conditions) {
    	List<List<String>> records = new ArrayList<List<String>>();    	
    	try {        
    		psQuery = conn.prepareStatement(sqlStr);
    		if(conditions != null && !conditions.isEmpty()) {
    			for(int i=0; i<conditions.size(); i++) {    			
        			psQuery.setString(i+1, conditions.get(i));
        		}
    		}    		
    		ResultSet rs = psQuery.executeQuery();
			statements.add(psQuery);
			
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
	    	while(rs.next()) {
	    		List<String> record = new ArrayList<String>();
	    		for(int i=0; i< columnCount; i++) {
	    			record.add(rs.getString(i+1));
				}
	    		records.add(record);
	    	}
		} catch (SQLException e) {			
			e.printStackTrace();
		}
    	    	 
		
    	
    	return records;
    }
    
    public boolean close() {
    	try {
			DriverManager.getConnection("jdbc:derby:;shutdown=true");
		} catch (SQLException se)
        {
            if (( (se.getErrorCode() == 50000)
                    && ("XJ015".equals(se.getSQLState()) ))) {
                // we got the expected exception
                System.out.println("Derby shut down normally");
                // Note that for single database shutdown, the expected
                // SQL state is "08006", and the error code is 45000.
            } else {
                // if the error code or SQLState is different, we have
                // an unexpected exception (shutdown failed)
                System.err.println("Derby did not shut down normally");
                printSQLException(se);
            }
        } finally {
			if (resultSet != null) {
            	try {
					resultSet.close();
					resultSet = null;
				} catch (SQLException e) {			
					e.printStackTrace();
				}
            	
            }
            int i = 0;
            while (!statements.isEmpty()) {
                Statement st = (Statement)statements.remove(i);
                if (st != null) {
                    try {
						st.close();
						st = null;
					} catch (SQLException e) {						
						e.printStackTrace();
					}
                    
                }
            }
            if (conn != null) {
                try {
					conn.close();
					conn = null;
				} catch (SQLException e) {
					e.printStackTrace();
				}                
            }
        }
    	return true;
    }    
    
    public void printSQLException(SQLException e)
    {
        // Unwraps the entire exception chain to unveil the real cause of the
        // Exception.
        while (e != null)
        {
            System.err.println("\n----- SQLException -----");
            System.err.println("  SQL State:  " + e.getSQLState());
            System.err.println("  Error Code: " + e.getErrorCode());
            System.err.println("  Message:    " + e.getMessage());
            // for stack traces, refer to derby.log or uncomment this:
            //e.printStackTrace(System.err);
            e = e.getNextException();
        }
    }
    
	public static void main(String[] args) {
		DerbyDBManagement ddbm = new DerbyDBManagement();

		/*String createTableSql = "create table english_images (word varchar(40), image_url varchar(100), explanation varchar(200))";
		boolean tableCreated = ddbm.createTable(createTableSql);
		System.out.println("output the create table status: "+tableCreated);*/
		
		
		/*String insertSqlStr = "insert into english_images values (?,?,?)";				
		ddbm.insertRecords(insertSqlStr, Arrays.asList(new String[] {"image4", "EnglishImages/image4.png", "this is the explanation for image4"}, 
				new String[] {"image5", "EnglishImages/image4.png", "this is the explanation for image5"}));*/
		
		
		//String updateSqlStr = "update english_images set date_year=?, date_month=?, date_day=? where word=?";
/*		String updateSqlStr = "update english_images set image_url=? where word=?";
		ddbm.updateRecords(updateSqlStr, Arrays.asList("EnglishImages/2018-08-12/04.png", "image5"));*/
		
		/*String deleteSqlStr = "delete from english_images where word=?";
		ddbm.deleteRecords(deleteSqlStr, Arrays.asList("image4"));*/
		
		/*String addColumnSqlStr = "ALTER TABLE english_images ADD date_day varchar(20)";
		ddbm.addColumn(addColumnSqlStr);*/
		
		//String condition = "image4";
		//String searchSqlStr = "select * from english_images where word=?";
		//ResultSet rs = ddbm.searchRecord(searchSqlStr, Arrays.asList(condition));
		String searchSqlStr = "select * from english_images";
		List<List<String>> records = ddbm.searchRecord(searchSqlStr, null);
		for(List<String> record : records) {
			for(String str : record) {
				System.out.println("output the value: "+str);
			}
			System.out.println();
		}						
		
		ddbm.close();
	}

}
