package Client;

import Server.IChatServer;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class LogInPage extends JFrame implements Serializable {
    private JTextField UserName;
    private JPasswordField PasswordField;
    private JLabel Username;
    private JLabel Password;
    private JPanel panel;
    private JButton logInButton;
    private JButton signUPButton;

    public LogInPage(IChatServer iChat,IChatClient iChatClient,ChatRoomClass chatRoomClass) {
        this.setContentPane(panel);
        logInButton.addMouseListener(new MouseAdapter() {
            @Override
            public synchronized void mouseClicked(MouseEvent e) {

                if (Username.getText()!=null && PasswordField.getText()!=null ) {
                    int g=0;
                    try {

                        for(IChatClient c:iChat.ShowBlockedClient()){
                        if (c.ShowUserDetails().get(0).equals(Username.getText().toString())) {
                            g=1;
                        }
                        }
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                    if(g==1){
                        JOptionPane.showMessageDialog( null,"you are blocked by Admin");

                    }
                    else {
                        IChatClient c = null;
                        int r = 0;
                        try {

                            String UserName1 = UserName.getText().toString();
                            String psw = PasswordField.getText().toString();
                            ArrayList<IChatClient> cc = iChat.ShowClient();
                            //System.out.println(UserName1+", "+psw);
                            if (UserName1.equals(iChatClient.ShowUserDetails().get(0))) {
                                for (int j = 0; j < cc.size(); j++) {
                                    if (cc.get(j).ShowUserDetails().get(0).equals(UserName1)
                                            && cc.get(j).ShowUserDetails().get(1).equals(psw)) {
                                        // System.out.println("Hellllllllllooooooooooo");
                                        //if(cc.get(j).ShowUserDetails().get(2).equals("false")) {
                                        iChat.SignInClient(j);
                                        c = iChat.ShowClient().get(j);
                                        //}
                                      /*else{
                                         // r=1;
                                          JOptionPane.showMessageDialog( null,"this user is already online \n ");
                                      }*/
                                        break;
                                    }

                                }
                            } else {
                                r = 1;
                            }
                        } catch (RemoteException ex) {
                            throw new RuntimeException(ex);
                        }
                        if (c != null) {
                            dispose();
                        /*ChatRoom chatRoom = null;
                        try {
                            chatRoom = new ChatRoom(c, iChat);
                        } catch (RemoteException ex) {
                            throw new RuntimeException(ex);
                        }
                        chatRoom.setVisible(true);
                        chatRoom.setSize(500, 400);*/
                            try {
                                chatRoomClass.AddNewUser(iChatClient);
                                iChat.AddClientToChatRoom(chatRoomClass, iChatClient);
                                dispose();
                                ClientGUI clientGUI = new ClientGUI(iChat, iChatClient, chatRoomClass);
                                clientGUI.setVisible(true);
                                clientGUI.setSize(600, 600);
                            } catch (RemoteException ex) {
                                throw new RuntimeException(ex);
                            }

                        } else if (r == 1) {
                            JOptionPane.showMessageDialog(null, "please use your own user name \n ");

                        } else {
                            JOptionPane.showMessageDialog(null, "UserName or Password is not correct \n try again or sign up");
                        }
                    }
                }
                else {
                    JOptionPane.showMessageDialog( null,"there is an empty field \n please fill it and try again");
                }
            }
        });
        signUPButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                SignUpForm signUpForm = new SignUpForm( iChat);
                signUpForm.setVisible(true);
                signUpForm.setSize(500,500);
            }
        });
    }


}
