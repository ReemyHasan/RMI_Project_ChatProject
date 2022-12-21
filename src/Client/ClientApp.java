package Client;

import Server.IChatServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ClientApp extends UnicastRemoteObject implements IChatClient, Serializable {
    private IChatServer iChatServer;
    public ClientGUI gui = null;
    public ChatRoom guiCR = null;
    private String UserName;
    private String FirstName;
    private String LastName;
    private String PSW;
    private String authorization;
    private boolean status;

    protected ClientApp(String firstName, String lastName, String pSW, IChatServer iChatServer, String auth) throws RemoteException {
        this.FirstName = firstName;
        this.LastName = lastName;
        this.PSW = pSW;
        this.UserName = this.FirstName + "." + this.LastName;
        this.iChatServer = iChatServer;
        authorization = auth;
        //this.gui = Gui;
        iChatServer.RegisterChatClient(this);
        status = true;
    }

    @Override
    public void GUI(ClientGUI clientGUI)throws RemoteException {
        this.gui = clientGUI;
    }
    public void GUI(ChatRoom guis)throws RemoteException {
        this.guiCR = guis;
    }
    @Override
    public void RetrieveMessage(String Message) throws RemoteException {
        gui.getChat().setText(gui.getChat().getText()+"\n"+Message);
    }

    @Override
    public void RetrieveClients(String Message) throws RemoteException {
        //System.out.println(Message);
        String[] mm = Message.split("\n");
        //for(String s : mm)
        //    System.out.println(s);
        //gui.getClients().setText(Message);
        gui.getClientsList().setModel(new DefaultComboBoxModel(mm));

    }

    @Override
    public void closeChat() throws RemoteException {
        gui.getDefaultCloseOperation();
        gui.dispose();
        guiCR.setVisible(true);
        /*HomePage homePage = new HomePage(iChatServer);
        homePage.setVisible(true);
        homePage.setSize(500,400);*/
    }

    @Override
    public void RetrieveChatRooms() throws RemoteException {
        ArrayList<ChatRoomClass> rooms = iChatServer.ShowChatRoom();
        String[] mm = {};
        guiCR.getComboBox1().setModel(new DefaultComboBoxModel(mm));
        for (ChatRoomClass room : rooms){
            guiCR.getComboBox1().addItem(room.getName()+"     created_by                                                                  "+room.getCreatedBy().ShowUserDetails().get(0));
        }
    }

    @Override
    public void RetrieveFile(String filename, byte [] inc) throws RemoteException {
        try {
            File serverpathfile = new File(filename);
            FileOutputStream out=new FileOutputStream(serverpathfile);
            byte [] data=inc;

            out.write(data);
            out.flush();
            out.close();

        } catch (IOException e) {

            e.printStackTrace();
        }

        gui.getFiles().setText(filename);
    }

    @Override
    public ArrayList<String> ShowUserDetails()throws RemoteException {
        ArrayList<String> details = new ArrayList<>();
        details.add(UserName);
        details.add(PSW);
        details.add(status+"");
        details.add(authorization);
        return details;
    }

    @Override
    public void ChangeClientStatue(boolean s)throws RemoteException {
            status = s;

    }


    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException {
        IChatServer iChat = (IChatServer) Naming.lookup("rmi://localhost:5099/ChatServer");
        HomePage homePage = new HomePage(iChat);
        homePage.setVisible(true);
        homePage.setSize(500,400);

    }
}
