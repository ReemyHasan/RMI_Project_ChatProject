package Client;

import Server.IChatServer;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ClientGUI extends JFrame implements Serializable {

    private JPanel panel;
    private JTextArea chat;

    public JTextArea getChat() {
        return chat;
    }
    private JTextField text;
    private JButton sendButton;
    private JButton uploadFileButton;
    private JTextPane filesTextPane;
    private JList ClientsList;
    private JButton adminPageButton;
    private JButton signOutButton;

    public JTextPane getFiles() {
        return filesTextPane;
    }

    public JList getClientsList() {
        return ClientsList;
    }
    public ClientGUI(IChatServer iChatServer, IChatClient iChatClient, ChatRoomClass chatRoomClass) throws RemoteException {
        this.setContentPane(panel);
        if(iChatClient.ShowUserDetails().get(3).equals("Admin")){
            adminPageButton.setVisible(true);
        }
        iChatClient.GUI(this);
        /* String Users="";
        for(IChatClient iChatClient1: chatRoomClass.getJoinedUsers())
        {
            Users+=iChatClient1.ShowUserDetails().get(0)+"\n";
        }
        clients.setText(Users);*/
        iChatServer.BroadCastClient(chatRoomClass);
        sendButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //String resMsg;
                try {

                    List<String> Selected = ClientsList.getSelectedValuesList();

                    if(!Selected.contains(iChatClient.ShowUserDetails().get(0)) && !Selected.isEmpty())
                    {
                        Selected.add(iChatClient.ShowUserDetails().get(0));
                    }
                    iChatServer.BroadCastMessage(iChatClient.ShowUserDetails().get(0)+": "+text.getText(),chatRoomClass,Selected);
                    text.setText("");
                    String[] s= {};
                    ClientsList.setModel(new DefaultComboBoxModel(s));
                    iChatServer.BroadCastClient(chatRoomClass);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        uploadFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                int File_Upload = jFileChooser.showOpenDialog(null);
                if (File_Upload == JFileChooser.APPROVE_OPTION) {
                    File file = jFileChooser.getSelectedFile();
                    String[] extension = file.getName().split("\\.");
                    //System.out.println(extension.length);
                    if (extension[extension.length - 1].equals("txt") ||
                            extension[extension.length - 1].equals("java") ||
                            extension[extension.length - 1].equals("php") ||
                            extension[extension.length - 1].equals("c") ||
                            extension[extension.length - 1].equals("cpp") ||
                            extension[extension.length - 1].equals("xml") ||
                            extension[extension.length - 1].equals("exe") ||
                            extension[extension.length - 1].equals("png") ||
                            extension[extension.length - 1].equals("jpg") ||
                            extension[extension.length - 1].equals("jpeg") ||
                            extension[extension.length - 1].equals("pdf") ||
                            extension[extension.length - 1].equals("jar") ||
                            extension[extension.length - 1].equals("rar") ||
                            extension[extension.length - 1].equals("zip")
                    ) {
                        try {
                            byte [] inc=new byte[(int) file.length()];
                            FileInputStream in=new FileInputStream(file);
                            System.out.println("uploading to server...");
                            in.read(inc, 0, inc.length);
                            in.close();

                            iChatServer.BroadCastFile(file.getName()+"\n",chatRoomClass,inc);
                        } catch (FileNotFoundException ex) {
                            System.out.println("Error: " + ex.getMessage());
                        } catch (RemoteException ex) {
                            System.out.println("Error: " + ex.getMessage());
                        } catch (IOException ex) {
                            System.out.println("Error: " + ex.getMessage());
                        }
                    }
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
        signOutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    ArrayList<IChatClient> cc = iChatServer.ShowClient();
                    for(int j = 0; j<cc.size();j++) {
                        if (cc.get(j).ShowUserDetails().get(0).equals(iChatClient.ShowUserDetails().get(0))) {
                            //chatRoomClass.RemoveUser();
                            iChatServer.SignOutClient(j);
                            iChatServer.BroadCastClient(chatRoomClass);
                            iChatClient.closeChat();
                            //System.exit(0);
                            break;
                        }
                    }

                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

}
