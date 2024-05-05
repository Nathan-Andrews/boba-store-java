

import java.sql.*;

import javax.naming.spi.DirStateFactory.Result;

/**
     * The db (database) class which contains a Connection object and an instance of the database
     * Has the capability to create database instances and execute commands and queries
     *
     * @author tom surovik
*/
public class db {
  Connection conn;
  private static db singleton;

  // construct class; maybe should be singleton?
  private db(){
    String teamName = "02";
    String dbName = "csce331_550_"+teamName+"db";
    String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;

    // set up credentials and stuff
    new dbSetup(); 

    try {
        conn = DriverManager.getConnection(dbConnectionString, dbSetup.user, dbSetup.pswd);
     } catch (Exception e) {
        e.printStackTrace();
        System.err.println(e.getClass().getName()+": "+e.getMessage());
        System.exit(0);
     }

     System.out.println("Opened database successfully");
  }

  // get singleton object, or instantiate it
  // get singleton object, or instantiate it
    /**
     * Returns the database object if it exists, otherwise, create a new instance of it
     * 
     * @return singleton the db or a new instance of the db
    */
  public static db getInstance(){
    if (singleton == null){
      // construct the singleton
      singleton = new db();
    }

    // singleton has already been created or just created
    return singleton;
  }

  // methods for the class
  /**
     * Executes a command given to the database
     * 
     * @param cmd the String command
     * @return boolean representing whether the command was executed
     */
  public boolean execute(String cmd){
    try{
        Statement createStmt = conn.createStatement();
        
        return createStmt.execute(cmd);
    }catch (Exception e){
        e.printStackTrace();
        System.err.println(e.getClass().getName()+": "+e.getMessage());
        System.exit(0);

        return false;
    }
  }

  /**
     * Gives the database a query and executes it
     * 
     * @param cmd the String query
     */
  public ResultSet executeQuery(String cmd){
    try{
        Statement createStmt = conn.createStatement();
        
        return createStmt.executeQuery(cmd);
    }catch (Exception e){
        e.printStackTrace();
        System.err.println(e.getClass().getName()+": "+e.getMessage());
        System.exit(0);

        return null;
    }
  }

  /**
     * Returns the Connection object of db
     *
     * @return Connection object
     */
  public Connection getConnection(){
    return conn;
  }
}//end Class
