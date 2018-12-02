import templates.LoginView;
import utils.DBManager;

import java.io.File;

/**
 * Created by jdriver on 10/26/18.
 */
public class Application {
    public static void main(String[] args) {

        File dbFile = new File("gradium.db");
        boolean firstTime = true;
        if(dbFile.exists()) {
            firstTime = false;
        }

        DBManager db = new DBManager();
        if(firstTime) {
            db.buildDB();
        }
        db.closeDB();
        LoginView login = new LoginView();
        login.setVisible(true);
    }
}
