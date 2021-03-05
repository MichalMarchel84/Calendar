import controller.Controller;
import model.Model;
import view.AppWindow;
import view.Comm;

public class App {
    private static final Thread onExit = new Thread(Model::disconnectFromDB);

    public static void main(String[] args){
        Controller controller = new Controller(new AppWindow());
        Comm.addRequestListener(controller);
        Model.connectToDB();
        Runtime.getRuntime().addShutdownHook(onExit);
    }
}
