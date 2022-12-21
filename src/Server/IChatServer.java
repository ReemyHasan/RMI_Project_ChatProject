package Server;

import Client.ChatRoomClass;
import Client.IChatClient;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface IChatServer extends Remote {
        void RegisterChatClient(IChatClient iChatClient)throws RemoteException;
        void BroadCastMessage(String msg,ChatRoomClass chatRoomClass, List<String> Selected) throws RemoteException;
        void BroadCastChatRooms() throws RemoteException;
        void BroadCastClient( ChatRoomClass chatRoomClass)throws RemoteException;
        ArrayList<IChatClient> ShowBlockedClient( )throws RemoteException;
        void BroadCastFile( String msg, ChatRoomClass chatRoomClass,byte [] s )throws RemoteException;
        void AddClientToChatRoom(ChatRoomClass chatRoomClass,IChatClient iChatClient) throws RemoteException;
        void AddNewChatRoom(ChatRoomClass chatRoomClass)throws RemoteException;
        void deleteChatRoom(int j) throws RemoteException;
        void SignOutClient(int j) throws RemoteException;
        void SignInClient(int j) throws RemoteException;
        ArrayList<IChatClient> ShowClient()throws RemoteException;
        ArrayList<ChatRoomClass> ShowChatRoom()throws RemoteException;
        void BlockClient(List<String> l) throws RemoteException;
        void UnBlockClient(ArrayList<IChatClient> clients) throws RemoteException;

}
