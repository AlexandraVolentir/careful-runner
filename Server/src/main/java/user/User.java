package user;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private List<String> friendsList;

    private boolean isLogged;

    public User(String name) {
        this.name = name;
    }

    public User() {
        isLogged = false;
        friendsList = new ArrayList<>();
    }

    public boolean isLogged() {
        return isLogged;
    }

    public void setLogged(boolean logged) {
        isLogged = logged;
    }

    public void addFriend(String name) {
        friendsList.add(name);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getFriendsList() {
        return this.friendsList;
    }

    public Integer getNumberOfFriends(){
        return friendsList.size();
    }

}
