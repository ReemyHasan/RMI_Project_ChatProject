package Client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IChatClient extends Remote {
    void GUI(ClientGUI clientGUI)throws RemoteException;
    void GUI(ChatRoom chatRoom)throws RemoteException;
    void RetrieveMessage(String Message)throws RemoteException;
    void RetrieveClients(String Message)throws RemoteException;
    void closeChat()throws RemoteException;
    void RetrieveChatRooms()throws RemoteException;
    void RetrieveFile(String Message,byte [] inc)throws RemoteException;
    ArrayList<String> ShowUserDetails()throws RemoteException;
    void ChangeClientStatue(boolean s)throws RemoteException;
}
