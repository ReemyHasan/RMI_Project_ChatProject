package Server;

//import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RegistryApp {
    public static void main(String[] args) throws RemoteException,RuntimeException {
        System.setProperty("java.security.policy","./ServerPolicy.policy");
        //System.setSecurityManager(new RMISecurityManager());
        Registry registry = LocateRegistry.createRegistry(5099);
        registry.rebind("ChatServer",new ServerApp());

    }
}
