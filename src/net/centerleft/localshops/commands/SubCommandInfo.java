package net.centerleft.localshops.commands;

public class SubCommandInfo {
    public String className;
    public boolean global;
    public boolean local;
    
    public SubCommandInfo(String className) {
        this.className = className;
        global = false;
        local = false;
    }
    
    public SubCommandInfo(String className, boolean local, boolean global) {
        this.className = className;
        this.local = local;
        this.global = global;
    }
    
    public Command getCommandInstance(String[] args) {
        return null;
    }
}
