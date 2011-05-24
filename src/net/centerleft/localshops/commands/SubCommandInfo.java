package net.centerleft.localshops.commands;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import net.centerleft.localshops.LocalShops;

import org.bukkit.command.CommandSender;

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

    public Command getCommandInstance(LocalShops plugin, String commandLabel, CommandSender sender, String command, boolean isGlobal) {
        try {
            // Get class
            Class<?> cls = Class.forName(className);
            
            // Define Constructor parameter types
            Class<?> paramTypes[] = new Class[5];
            paramTypes[0] = LocalShops.class;
            paramTypes[1] = String.class;
            paramTypes[2] = CommandSender.class;
            paramTypes[3] = String.class;
            paramTypes[4] = boolean.class;

            // Get Constructor
            Constructor<?> ct = cls.getConstructor(paramTypes);

            // Define parameters
            Object argList[] = new Object[5];
            argList[0] = plugin;
            argList[1] = commandLabel;
            argList[2] = sender;
            argList[3] = command;
            argList[4] = isGlobal;

            // Create new instance of class
            Object obj = ct.newInstance(argList);

            // Cast if possible to Command & Return
            if (obj instanceof Command) {
                return (Command) obj;
            }
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
