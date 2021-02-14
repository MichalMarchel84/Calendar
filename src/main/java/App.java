import controller.Controller;
import model.Model;
import view.AppWindow;
import view.Comm;

public class App {
    private static final Thread onExit = new Thread(){
        @Override
        public void run(){
            Model.disconnectFromDB();
        }
    };

    public static void main(String[] args){
        new AppWindow();
        Controller controller = new Controller();
        Comm.addRequestListener(controller);
        Model.connectToDB();
        Runtime.getRuntime().addShutdownHook(onExit);
    }
}
