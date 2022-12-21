package Server;

import Client.ChatRoomClass;
import Client.IChatClient;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerApp extends  UnicastRemoteObject implements IChatServer, Serializable {
    private ArrayList<IChatClient> iChatClients = new ArrayList<IChatClient>();
    private ArrayList<ChatRoomClass> ChatRooms = new ArrayList<ChatRoomClass>();
    private ArrayList<IChatClient> blockedClients = new ArrayList<IChatClient>();

    protected ServerApp() throws RemoteException {

    }

    @Override
    public void RegisterChatClient(IChatClient iChatClient) throws RemoteException {
        this.iChatClients.add(iChatClient);
    }

    @Override
    public void BroadCastMessage(String msg,ChatRoomClass chatRoomClass, List<String> Selected) throws RemoteException {
        ChatRoomClass s = new ChatRoomClass();
        for (ChatRoomClass c:ChatRooms){
            if(c.getName().equals(chatRoomClass.getName())) {
                s = c;
                break;
            }
        }
        if(Selected.isEmpty()) {
            for (IChatClient icc : s.getJoinedUsers()) {
                icc.RetrieveMessage(msg);
            }
        }
        else{
            for (String v: Selected)
            {
                for(IChatClient icc: s.getJoinedUsers())
                {
                    if(icc.ShowUserDetails().get(0).equals(v))
                    {
                        icc.RetrieveMessage(msg);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void BroadCastChatRooms() throws RemoteException {
        for(IChatClient iChatClient: iChatClients){
            iChatClient.RetrieveChatRooms();
        }
    }

    @Override
    public void BroadCastClient( ChatRoomClass chatRoomClass) throws RemoteException {
        String Users = "";

        ChatRoomClass s = new ChatRoomClass();
        for (ChatRoomClass c:ChatRooms){
            if(c.getName().equals(chatRoomClass.getName())) {
                s = c;
                break;
            }
        }

        for(IChatClient iChatClient1: s.getJoinedUsers())
        {

                Users+=iChatClient1.ShowUserDetails().get(0)+"\n";


        }
        ArrayList<IChatClient> RoomClients = s.getJoinedUsers();
        for(IChatClient RoomClient: RoomClients){
            RoomClient.RetrieveClients(Users);
        }
    }

    @Override
    public ArrayList<IChatClient> ShowBlockedClient() throws RemoteException {
        return blockedClients;
    }

    @Override
    public void BroadCastFile(String msg,ChatRoomClass chatRoomClass, byte [] s) throws RemoteException {
        for(IChatClient icc: chatRoomClass.getJoinedUsers()){
            icc.RetrieveFile(msg,s);
        }
    }

    @Override
    public void AddClientToChatRoom(ChatRoomClass chatRoomClass,IChatClient iChatClient) throws RemoteException {
        for (ChatRoomClass c:ChatRooms){
            if(c.getName().equals(chatRoomClass.getName())) {
                c.AddNewUser(iChatClient);
                break;
            }
        }
    }

    @Override
    public void AddNewChatRoom(ChatRoomClass chatRoomClass) throws RemoteException {
        this.ChatRooms.add(chatRoomClass);

    }

    @Override
    public void deleteChatRoom(int j) throws RemoteException {
        /*for(ChatRoomClass d : ChatRooms)
            System.out.print(d.getName()+",");
        System.out.println("");*/
        for(IChatClient client:ChatRooms.get(j).getJoinedUsers()){
            client.closeChat();
        }
        ChatRooms.remove(j);
        /*for(ChatRoomClass d : ChatRooms)
            System.out.print(d.getName()+",");
        System.out.println("");*/
    }

    @Override
    public void SignOutClient(int j) throws RemoteException {
        //iChatClients.get(j).ChangeClientStatue(false);
        if (ChatRooms.size()>0) {
            for (int i=0; i< ChatRooms.size();i++) {
                for(int k=0;k<ChatRooms.get(i).getJoinedUsers().size();k++)
                {
                    if(ChatRooms.get(i).getJoinedUsers().get(k).ShowUserDetails().get(0)
                            .equals(iChatClients.get(j).ShowUserDetails().get(0))){
                        ChatRooms.get(i).RemoveUser(k);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void SignInClient(int j) throws RemoteException {
        iChatClients.get(j).ChangeClientStatue(true);
    }

    @Override
    public ArrayList<IChatClient> ShowClient()throws RemoteException {
        /*for(IChatClient d : iChatClients) {
            //System.out.print(d.ShowUserDetails().get(0) + "," + d.ShowUserDetails().get(1));
           // System.out.println("");
        }
        System.out.println("");*/
        return iChatClients;
    }

    @Override
    public ArrayList<ChatRoomClass> ShowChatRoom() throws RemoteException{

        return ChatRooms;
    }

    @Override
    public void BlockClient(List<String> Selected) throws RemoteException {
        if(!Selected.isEmpty()) {
            for (String v: Selected)
            {

                for(int j=0;j<iChatClients.size();j++)
                {
                    if(iChatClients.get(j).ShowUserDetails().get(0).equals(v))
                    {
                        blockedClients.add(iChatClients.get(j));
                        SignOutClient(j);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void UnBlockClient(ArrayList<IChatClient> clients) throws RemoteException {

    }

}
