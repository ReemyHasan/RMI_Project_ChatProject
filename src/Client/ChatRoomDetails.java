package Client;

import Server.IChatServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ChatRoomDetails extends JFrame implements Serializable {
    private JPanel panel;
    private JTextArea Details;
    private JButton JOINButton;
    private JButton backButton;
    private JLabel RoomName;
    private JButton signOutButton;
    private JButton adminPageButton;

    public ChatRoomDetails(IChatClient iChatClient, IChatServer iChatServer,ChatRoomClass chatRoomClass) throws HeadlessException, RemoteException {
        this.setContentPane(panel);
        if(iChatClient.ShowUserDetails().get(3).equals("Admin")){
            adminPageButton.setVisible(true);
        }
        String Users = "";
        for(IChatClient iChatClient1: chatRoomClass.getJoinedUsers())
        {
            Users+=iChatClient1.ShowUserDetails().get(0)+"\n";
        }
        this.RoomName.setText( chatRoomClass.getName()+" Details");
        Details.setText("Created by "+chatRoomClass.getCreatedBy().ShowUserDetails().get(0)+"\nJoined Users:\n"+ Users);
        JOINButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                /*try {
                    chatRoomClass.AddNewUser(iChatClient);
                    iChatServer.AddClientToChatRoom(chatRoomClass,iChatClient);
                    dispose();
                    ClientGUI clientGUI = new ClientGUI(iChatServer,iChatClient, chatRoomClass);
                    clientGUI.setVisible(true);
                    clientGUI.setSize(600, 600);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }*/
                dispose();
                LogInPage LogIn = new LogInPage(iChatServer,iChatClient,chatRoomClass);
                LogIn.setVisible(true);
                LogIn.setSize(500, 400);
            }
        });
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                ChatRoom chatRoom = null;
                try {
                    chatRoom = new ChatRoom(iChatClient, iChatServer);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
                chatRoom.setVisible(true);
                chatRoom.setSize(500, 400);
            }
        });
        signOutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    ArrayList<IChatClient> cc = iChatServer.ShowClient();
                    for(int j = 0; j<cc.size();j++) {
                        if (cc.get(j).ShowUserDetails().get(0).equals(iChatClient.ShowUserDetails().get(0))) {
                            iChatServer.SignOutClient(j);
                            System.exit(0);
                            break;
                        }
                    }

                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        adminPageButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AdminPage adminPage = new AdminPage(iChatClient,iChatServer);
                adminPage.setVisible(true);
                adminPage.setSize(500,500);
                adminPage.setLocation(500,500);
            }
        });
    }
}
