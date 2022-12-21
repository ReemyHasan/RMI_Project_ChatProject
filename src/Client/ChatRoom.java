package Client;

import Server.IChatServer;

import javax.swing.*;
import java.awt.desktop.UserSessionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ChatRoom extends JFrame implements Serializable{
    private JComboBox comboBox1;
    private JTextField roomName;

    public JComboBox getComboBox1() {
        return comboBox1;
    }

    private JButton deleteChatRoomButton;
    private JButton createNewChatRoomButton;
    private JPanel panel;
    private JButton goToRoomDetailsButton;
    private JButton signOutButton;
    private JButton adminPageButton;

    public ChatRoom(IChatClient iChatClient, IChatServer iChatServer) throws RemoteException {
        this.setContentPane(panel);

        if(iChatClient.ShowUserDetails().get(3).equals("Admin")){
            adminPageButton.setVisible(true);
        }
        iChatClient.GUI(this);
        iChatServer.BroadCastChatRooms();
        createNewChatRoomButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ChatRoomClass CRC = null;
                try {
                    // if chat room name is separated by space
                    if(!roomName.getText().equals("")) {
                        String[] s = roomName.getText().split(" ");
                        String y = String.join("_", s);
                        CRC = new ChatRoomClass(y, iChatClient);
                        //CRC.AddNewUser(iChatClient);
                        //System.out.println("Chat room is "+CRC.getCreatedBy() +"        "+ CRC.getName() + "\n");

                        iChatServer.AddNewChatRoom(CRC);
                        iChatServer.BroadCastChatRooms();
                    }
                    else{
                        JOptionPane.showMessageDialog( null,"fill the corresponding text field");
                    }
                    //dispose();
                    //ChatRoom chatRoom1 = new ChatRoom(iChatClient, iChatServer);
                    //chatRoom1.setVisible(true);
                    //chatRoom1.setSize(500, 400);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        deleteChatRoomButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (comboBox1.getItemCount() > 0) {
                    String c = comboBox1.getSelectedItem().toString();
                    //System.out.println("combo box is    "+ c);
                    String[] cc = c.split(" ", 2);
                    //System.out.println(cc[0]+"mmmm");
                    int i = 0;
                    try {

                        for (int j = 0; j < iChatServer.ShowChatRoom().size(); j++) {
                            //System.out.println(iChatClient.ShowUserDetails().get(0));
                            if (iChatServer.ShowChatRoom().get(j).getName().equals(cc[0]) &&
                                    (iChatServer.ShowChatRoom().get(j).getCreatedBy().ShowUserDetails().get(0).equals(iChatClient.ShowUserDetails().get(0))
                                            || iChatClient.ShowUserDetails().get(3).equals("Admin"))) {
                                i = 1;
                                iChatServer.deleteChatRoom(j);
                                JOptionPane.showMessageDialog(null, "Room deleted Successfully");
                                iChatServer.BroadCastChatRooms();
                                //dispose();
                                //ChatRoom chatRoom = new ChatRoom(iChatClient, iChatServer);
                                //chatRoom.setVisible(true);
                                //chatRoom.setSize(500, 400);
                                break;
                            }

                        }
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                    if (i == 0) {
                        JOptionPane.showMessageDialog(null, "Sorry, You don't have permission to delete this room\n");
                    }
                }
            }
        });
        goToRoomDetailsButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (comboBox1.getItemCount()>0) {
                    String c = comboBox1.getSelectedItem().toString();
                    String[] cc = c.split(" ", 2);
                    try {
                        for (int j = 0; j < iChatServer.ShowChatRoom().size(); j++) {

                            if (iChatServer.ShowChatRoom().get(j).getName().equals(cc[0])) {
                                dispose();
                                ChatRoomDetails chatRoomDetails = new ChatRoomDetails(iChatClient, iChatServer, iChatServer.ShowChatRoom().get(j));
                                chatRoomDetails.setVisible(true);
                                chatRoomDetails.setSize(500, 400);
                                break;
                            }

                        }
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                }
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

                            //System.exit(0);

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
                adminPage.setLocation(100,100);
            }
        });
    }


}
