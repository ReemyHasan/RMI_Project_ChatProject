package Client;

import Server.IChatServer;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class AdminPage extends JFrame{
    private JPanel panel;
    private JList clients;
    private JList chatrooms;
    private JButton blockButton;
    private JList blockedClients;
    private JButton unblockButton;

    public AdminPage(IChatClient iChatClient, IChatServer iChatServer) {

        this.setContentPane(panel);
        try {
            ArrayList<ChatRoomClass> rooms = iChatServer.ShowChatRoom();
            ArrayList<IChatClient> clients = iChatServer.ShowClient();
            ArrayList<IChatClient> blockedClients = iChatServer.ShowBlockedClient();

            String c ="",d="",b="";
            for(ChatRoomClass chatRoomClass:rooms)
            {
                c+=chatRoomClass.getName()+"\n";
            }
            for(IChatClient f:clients){
                d+=f.ShowUserDetails().get(0)+"\n";
            }
            for (IChatClient f:blockedClients){
                b+=f.ShowUserDetails().get(0)+"\n";
            }
            String[] Chatrooms = c.split("\n");
            String[] Clients = d.split("\n");
            String[] Blocked = b.split("\n");
            this.clients.setModel(new DefaultComboBoxModel(Clients));
            this.blockedClients.setModel(new DefaultComboBoxModel(Blocked));
            this.chatrooms.setModel(new DefaultComboBoxModel(Chatrooms));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        blockButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {

                    List<String> Selected = clients.getSelectedValuesList();

                    if(Selected.contains(iChatClient.ShowUserDetails().get(0)))
                    {
                        Selected.remove(iChatClient.ShowUserDetails().get(0));
                    }
                    String b= "";
                    for(String v: Selected){
                        b+=v+"\n";
                    }
                    String[] Blocked = b.split("\n");
                    iChatServer.BlockClient(Selected);
                    DefaultListModel listModel = (DefaultListModel) blockedClients.getModel();
                    listModel.addElement(Blocked);
                    blockedClients.setModel(listModel);

                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        unblockButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }
        });
    }


}
