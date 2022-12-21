package Client;

import Server.IChatServer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.rmi.RemoteException;

public class SignUpForm extends JFrame implements Serializable {
    private JTextField First;
    private JTextField Last;
    private JButton Register;
    private JLabel FirstName;
    private JLabel LastName;
    private JLabel passwordlabel;
    private JPanel panel;
    private JTextField passwordfield;
    private JComboBox authen;
    private JPasswordField AdminCode;
    private JLabel Admin_Code;

    public SignUpForm(IChatServer iChat) {
        this.setContentPane(panel);

        Register.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!First.getText().toString().equals("") && !Last.getText().toString().equals("") && !passwordfield.getText().toString().equals("") ) {
                    if ((authen.getSelectedItem().toString().equals("Admin") && AdminCode.getText().toString().equals("Admin"))
                            || !authen.getSelectedItem().toString().equals("Admin")) {
                        String Name = First.getText() + "." + Last.getText();

                        IChatClient c = null;
                        try {
                            c = iChat.ShowClient().stream().filter(
                                    iChatClient -> {
                                        try {
                                            return Name.equals(iChatClient.ShowUserDetails().get(0));
                                        } catch (RemoteException ex) {
                                            throw new RuntimeException(ex);
                                        }
                                    }
                            ).findAny().orElse(null);
                        } catch (RemoteException ex) {
                            throw new RuntimeException(ex);
                        }
                        if (c == null) {
                            try {
                                ClientApp client = new ClientApp(First.getText().toString(), Last.getText().toString(), passwordfield.getText().toString(), iChat, authen.getSelectedItem() + "");
                                dispose();
                                ChatRoom chatRoom = new ChatRoom(client, iChat);
                                chatRoom.setVisible(true);
                                chatRoom.setSize(500, 400);
                            } catch (RemoteException ex) {
                                throw new RuntimeException(ex);
                            }

                        } else {
                            JOptionPane.showMessageDialog(null, "UserName was already Used \n please change it and try again");
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Sorry, You cannot be An Admin");
                    }
                }
                else {
                    JOptionPane.showMessageDialog( null,"there is an empty field \n please fill it and try again");
                }
            }
        });
        authen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!authen.getSelectedItem().toString().equals("Admin")) {
                    Admin_Code.setVisible(false);
                    AdminCode.setVisible(false);
                }
                else{
                    Admin_Code.setVisible(true);
                    AdminCode.setVisible(true);
                }
            }
        });
    }


}
