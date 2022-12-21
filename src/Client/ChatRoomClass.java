package Client;

import java.io.Serializable;
import java.util.ArrayList;

public class ChatRoomClass implements Serializable {
    String Name;

    public ChatRoomClass() {
        Name = "";
        CreatedBy = null;
        JoinedUsers = new ArrayList<IChatClient>();
    }

    IChatClient CreatedBy;
    ArrayList<IChatClient> JoinedUsers;

    public ChatRoomClass(String name, IChatClient createdBy) {
        Name = name;
        CreatedBy = createdBy;
        JoinedUsers = new ArrayList<IChatClient>();
        //JoinedUsers.add(createdBy);
    }

    public String getName() {
        return Name;
    }

    public IChatClient getCreatedBy() {
        return CreatedBy;
    }

    public ArrayList<IChatClient> getJoinedUsers() {
        return JoinedUsers;
    }
    public void AddNewUser(IChatClient UserName){
        JoinedUsers.add(UserName);
    }
    public void RemoveUser(int k){
        JoinedUsers.remove(k);
    }
}
