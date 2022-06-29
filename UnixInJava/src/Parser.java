//Author: Mira
//Usage to seprate the commands and the args.

import java.io.IOException;
import java.util.*;
public class Parser {
     String[] argS;// Will be filled by arguments extracted by parse method
     String cmd; // Will be filled by the command extracted by parse method
    public Parser(String str){

        String[] argS=new String[100];// Will be filled by arguments extracted by parse method
        String cmd=""; // Will be filled by the command extracted by parse method
        parse(str);
    }
    public boolean parse(String str) {

        Map<String, Integer> commandPar = new HashMap<String, Integer>();

        commandPar.put("cd", new Integer(0));
        commandPar.put("ls", new Integer(1));
        commandPar.put("cp", new Integer(2));
        commandPar.put("cat", new Integer(1));
        commandPar.put("more", new Integer(0));
        commandPar.put("|", new Integer(2));
        commandPar.put("<", new Integer(2));
        commandPar.put("<<", new Integer(2));
        commandPar.put("mkdir", new Integer(100));
        commandPar.put("rmdir", new Integer(100));
        commandPar.put("mv", new Integer(2));
        commandPar.put("rm", new Integer(1));
        commandPar.put("args", new Integer(100));
        commandPar.put("date", new Integer(0));
        commandPar.put("help", new Integer(0));
        commandPar.put("pwd", new Integer(0));
        commandPar.put("clear", new Integer(0));

        argS = str.split(" ");
        int cmdIndex = 0;
        if (Arrays.asList(argS).contains("|")) {
            cmd = "|";
            cmdIndex = Arrays.asList(argS).indexOf("|");
            List<String> list = new ArrayList<String>(Arrays.asList(argS));
        } else if (Arrays.asList(argS).contains("<")) {
            cmd = "<";
            cmdIndex = Arrays.asList(argS).indexOf("<");

            List<String> list = new ArrayList<String>(Arrays.asList(argS));
            list.remove(argS[cmdIndex]);
            argS = list.toArray(new String[0]);
        } else if (Arrays.asList(argS).contains("<<")) {
            cmd = "<<";
            cmdIndex = Arrays.asList(argS).indexOf("<<");

            List<String> list = new ArrayList<String>(Arrays.asList(argS));
            list.remove(argS[cmdIndex]);
            argS = list.toArray(new String[0]);

        } else {
            cmd = argS[0];
            List<String> list = new ArrayList<String>(Arrays.asList(argS));
            list.remove(argS[cmdIndex]);
            argS = list.toArray(new String[0]);
        }


        //for(String s: argS){System.out.println(s);}
        if (commandPar.containsKey(cmd)) {
            int p = commandPar.get(cmd);
            int q = argS.length ;
            if (q >= p) {
                return true;
            } else {
                System.out.println("too few arguments");
                return false;
            }
        }
        else {
            System.out.println("command not found.");
            return false;
        }
    }










    public String getCmd() {
        return cmd;
    }

    public String[] getArguments() {
        return argS;
    }


}
