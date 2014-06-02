package com.example.myguard1;

import java.util.HashMap;
import java.util.Map;

/**
 * POJO class to hold query attributes
 *
 */
public class Query {
    String command;
    String user;
    HashMap<String, String> params;

    public Query(String command, String user, HashMap<String, String> params) {
        this.command = command;
        this.user = user;
        this.params = params;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return command + " " + user + " " + params.size();
    }
}
