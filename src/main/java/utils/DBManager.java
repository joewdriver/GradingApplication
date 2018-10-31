package utils;

import java.awt.peer.SystemTrayPeer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import com.mysql.jdbc.Driver;

/**
 * Created by jdriver on 10/29/18.
 */
public class DBManager {
    public DBManager() {
        try{
            System.out.println("Checkpoint 0");
//            Course.forName("com.mysql.cj.jdbc.Driver");
//            System.out.println("Checkpoint 1");
//            Connection con= DriverManager.getConnection(
//                    "jdbc:mysql://sql9.freemysqlhosting.net/sql9261878","sql9261878","N5thwVUwNj");
//            System.out.println("Checkpoint 2");
//            //TODO: remove these lines, they are meant to test the connection during development
//            System.out.println(con.getMetaData().toString());
//            con.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
