import controller.Controller;
import view.AppWindow;
import view.Comm;

public class App {
    public static void main(String[] args){
        new AppWindow();
        Controller controller = new Controller();
        Comm.addRequestListener(controller);
    }
}
