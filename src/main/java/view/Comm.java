package view;

import controller.Request;
import controller.RequestListener;
import java.util.HashSet;
import java.util.Set;

public class Comm {

    private static final Set<RequestListener> reqListners= new HashSet<>();

    private Comm(){}

    public static void addRequestListener(RequestListener req){
        reqListners.add(req);
    }

    static void fireRequestEvent(Request req){
        for(RequestListener rl : reqListners){
            rl.requestReceived(req);
        }
    }
}
