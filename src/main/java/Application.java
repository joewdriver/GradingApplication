import templates.LoginView;
import utils.DBManager;

/**
 * Created by jdriver on 10/26/18.
 */
public class Application {
    public static void main(String[] args) {

        DBManager db = new DBManager();
        //db.dropAllTables();
        db.buildDB();
        db.getCourses();

        LoginView login = new LoginView();
        login.setVisible(true);
    }
}
