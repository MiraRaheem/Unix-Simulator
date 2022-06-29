import javafx.scene.shape.Path;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.io.File.separator;


public class Terminal {
        public Object date;
        private String HomeDirctory = "C:\\Users";
        private String CurrentDirectory = "C:\\Users";
        private Stack<String> PreviousDirectories=new Stack<>();
        private String insteadReturn;

        //Author: Mira and Nourhan.
        //Usage: just an intaliztion of the variables to avoid exceptions
        public Terminal(){
            PreviousDirectories.push(HomeDirctory);
            PreviousDirectories.push(HomeDirctory);
        }

        public String IsPath(String path) {
            if (path.charAt(0) == '\"') {
                path = path.replace("\"", "");
                String realPath = CurrentDirectory + "\\" + path;
                return realPath;
            }
            else return path;
        }

        //Author: Mira and Nourhan.
        //Usage: To detect if the given path exist to avoid repeation
        public Boolean IsFileExist(String path){
            File tmpDire= new File(path);
            Boolean exist=tmpDire.exists();
            return exist;
        }

        public Boolean IsDire(String path){
            for(int i=0; i<path.length();i++){
                if (path.charAt(i) == '.'){
                    return false;
                }
            }
            return true;
        }

        //Author :Mira and Nourhan.
        /*Usage: 1.Change directory to a (given path)
                 2.Get back to the last path used(previous ..)
                 3.Return to home directory (cd || cd~)
                 4.Go back to parent (cd-)
         */
        public void cd(String path){
            if (path == null || path == "~"){
                CurrentDirectory = HomeDirctory;
                System.out.println(CurrentDirectory + '>');
                PreviousDirectories.push(CurrentDirectory);
            }
            //sub string bdl if
            else if(path==".."){
                String[] allPrevious=CurrentDirectory.split("/");
                int p=(allPrevious.length)-2;
                CurrentDirectory="";
                for(int i=0; i<=p; i++){
                    if (i<p){
                        System.out.println(allPrevious[i]);

                        CurrentDirectory+=allPrevious[i]+"/";   }
                    else if (i==p){
                        CurrentDirectory+=allPrevious[i];
                    }
                }
                int s=CurrentDirectory.length()-1;
                CurrentDirectory.replace(CurrentDirectory.charAt(s),'c');

                System.out.println(CurrentDirectory + '>');
                PreviousDirectories.push(CurrentDirectory);

            }
            else if (path=="-"){
                PreviousDirectories.pop();
                CurrentDirectory=PreviousDirectories.pop();
                System.out.println(CurrentDirectory+">");
            }
            else{
                path=IsPath(path);
                if(IsFileExist(path)) {
                    CurrentDirectory = path;
                    System.out.println(CurrentDirectory + '>');
                    PreviousDirectories.push(CurrentDirectory);
                    insteadReturn=path;
                }
                else {
                    System.out.println("File directory doesnot exist");
                    return ;
                }
            }
        }

        //Author :Mira and Nourhan.
        /*Usage: 1.List all contents of a directory.
                 2.List all contents inside a file.
                 3.return false if file does not exist.
         */
        public void ls(String path){
            path=IsPath(path);
            if(IsFileExist(path) && IsDire(path)){
                File f=new File(path);
                ArrayList<File> files=new ArrayList<File>(Arrays.asList(f.listFiles()));
                for(File s: files){
                    insteadReturn+=s.getName();
                    System.out.println(s.getName());}
            }
            else if(IsFileExist(path)&& !IsDire(path)){

                try (Scanner input = new Scanner(new File(path))) {
                    while ((input.hasNextLine())) {
                        System.out.println(input.nextLine());
                        insteadReturn+=input.nextLine();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else{System.out.println("File path does not exist");}

        }

        //Need Directory
        public void cp(String sourcePath, String destinationPath ){
            File source = new File (sourcePath);
            File destination = new File (destinationPath);

            FileChannel SourceChannel = null;
            FileChannel DestinationChannel = null;

            try{
                SourceChannel = new FileInputStream(source).getChannel();
                DestinationChannel = new FileOutputStream(destination).getChannel();
                DestinationChannel.transferFrom(SourceChannel,0,SourceChannel.size());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    SourceChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    DestinationChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //Need Directory
        public void mv(String sourcePath, String destinationPath){
            cp(sourcePath, destinationPath );
            File source = new File(sourcePath);
            source.delete();
        }

        //Not done
        public void rm(String sourcePath){

        }

        //Author:Mira and Nourhan
        //Usage:It creates a directory
        public void mkdir(String[]args){
            for(String s: args){
                File file = new File(s);
                s=IsPath(s);
                if (!file.exists()) {
                    new File(s).mkdir();
                }
                else{System.out.println("Directory already exits");}
            }
        }

        //Author:Mira and Nourhan
        //Usage: removes empty directory
        public void rmdir(String[]args){
            for(String s: args) {
                s=IsPath(s);
                File file = new File(s);
                if(file.exists()) {
                    File[] allFiles = file.listFiles();
                    if (allFiles.length == 0) {
                        file.delete();
                    }
                    else{
                        System.out.println("This directory contains a file or more.");
                    }

                }
                else {
                    System.out.println("It does not exist");
                }
            }

        }


        //Author :Mira and Nourhan.
        /*Usage: 1.If given one arg it creates and name the file
                 2.If given more than one it prints the concatented file out and create a new file to put the output at it
         */
        public void cat(String args[]) throws IOException {
            if(args.length==1){

                try {
                    String path=args[0];
                    path=IsPath(path);
                    File file = new File(path);
                    /*If file gets created then the createNewFile()
                     * method would return true or if the file is
                     * already present it would return false
                     */
                    boolean fvar = file.createNewFile();
                    if (fvar){
                        System.out.println("File has been created successfully");
                    }
                    else{
                        System.out.println("File already present at the specified location");
                    }
                } catch (IOException e) {
                    System.out.println("Exception Occurred:");
                    e.printStackTrace();
                }
            }

            else {
                OutputStream out=new FileOutputStream("concanted.txt");
                byte[] buf=new byte[1000];
                for(String d:args) {
                    d=IsPath(d);
                    if (IsFileExist(d)) {
                        InputStream in =new FileInputStream(d);
                        int b=0;
                        while ((b=in.read(buf))>=0){
                            out.write(buf,0,b);

                        }
                        in.close();
                    }
                }
                out.close();
                for(String s:args) {
                    s=IsPath(s);
                    if (IsFileExist(s)) {
                        Scanner input=new Scanner(new File(s));
                        while ((input.hasNextLine())){
                            System.out.println(input.nextLine());
                        }

                    }
                }
            }
        }


        //Author :Mira.
        //NOT DONE YET
        /*Usage: 1.List all contents of a directory.
                 2.List all contents inside a file.
                 3.return false if file does not exist.
         */
        public void pipe(String comd) throws IOException {
           /* String comd = "";
           for(int i=0;i<args.length;i++ ){comd+=args[i];}
           System.out.println(comd);*/
           String replaceComd=comd.replace("|","!");
           String [] Totalcom;
           Totalcom=replaceComd.split("!");
           String func1=Totalcom[0];
           Parser P1=new Parser(func1);
            String cmd=P1.getCmd();
           String[] arguments=P1.getArguments();
           if(cmd.equals("cd")){cd(arguments[0]);}
           else if(cmd.equals("cat")){cat(arguments);}
           else if(cmd.equals("ls")){ls(arguments[0]);}
           else if(cmd.equals("date")){Date();}
           else if(cmd.equals("pwd")){Pwd();}
           else if(cmd.equals("help")){help();}
           for(int i=1; i<Totalcom.length;i++){
               Clear();
               Totalcom[i]=Totalcom[i].replaceAll("\\s","");
               Parser P2=new Parser(Totalcom[i]);
               String cmd2=P2.getCmd();
               String[] arguments2=P1.getArguments();
               if(cmd2.equals("more")){more();}
               else if(cmd2.equals("date")){Date();}
               else if(cmd2.equals("pwd")){Pwd();}
               else if(cmd2.equals("help")){help();
               }

           }


            }


        //Author :Mira.
        /*Usage: 1.Seperate the args and collect the function to clasiffy the action.
                 2.Execute the first fun action then pass the returned string to the file after the > sign
                 3.If the file does not exist, it will be created.
                 4.If the file exists, it will be replaced.
         */
        public void oneArrow(String comd) throws FileNotFoundException, UnsupportedEncodingException {

            String replaceComd=comd.replace("<","!");
            String [] Totalcom;
            Totalcom=replaceComd.split("!");
            /*for(String s: Totalcom){
                System.out.println(s);
            }*/
            String func1=Totalcom[0];

            Parser P1=new Parser(func1);
            String cmd=P1.getCmd();
            String[] arguments=P1.getArguments();
            if(cmd.equals("cd")){cd(arguments[0]);}
            //else if(cmd.equals("cat")){cat(arguments[0]);}
            else if(cmd.equals("ls")){ls(arguments[0]);}
            else if(cmd.equals("date")){Date();}
            else if(cmd.equals("pwd")){Pwd();}
            else if(cmd.equals("help")){help();}


            Totalcom[1]=Totalcom[1].replaceAll("\\s","");
            String completePath=Totalcom[1];

            File file = new File(completePath);
            System.out.println(completePath);
            String absoluteFile = completePath;
            String path=file.getAbsolutePath().substring(0,file.getAbsolutePath().lastIndexOf("\\"))+"\\";
            System.out.println(path);
            System.out.println(IsFileExist(path));
            if(IsFileExist(path)){

                System.out.println("ll");
                PrintWriter writer = new PrintWriter(completePath, "UTF-8");
                writer.println(insteadReturn);
                writer.close();
            }
        }

        //Author :Mira.
        /*Usage: 1.Seperate the args and collect the function to clasiffy the action.
                 2.Execute the first fun action then pass the returned string to the file after the >> sign
                 3.If the file does not exist, it will be created.
                 4.If the file exists, it will be appended.
         */
        public void twoArrows(String comd) throws IOException {
            String replaceComd=comd.replace("<<","!");
            String [] Totalcom;
            Totalcom=replaceComd.split("!");
            /*for(String s: Totalcom){
                System.out.println(s);
            }*/
            String func1=Totalcom[0];

            Parser P1=new Parser(func1);
            String cmd=P1.getCmd();
            String[] arguments=P1.getArguments();
            if(cmd.equals("cd")){cd(arguments[0]);}
            //else if(cmd.equals("cat")){cat(arguments[0]);}
            else if(cmd.equals("ls")){ls(arguments[0]);}
            else if(cmd.equals("date")){Date();}
            else if(cmd.equals("pwd")){Pwd();}
            else if(cmd.equals("help")){help();}


            Totalcom[1]=Totalcom[1].replaceAll("\\s","");
            String completePath=Totalcom[1];

            File file = new File(completePath);
            System.out.println(completePath);
            String absoluteFile = completePath;
            String path=file.getAbsolutePath().substring(0,file.getAbsolutePath().lastIndexOf("\\"))+"\\";
            System.out.println(path);
            System.out.println(IsFileExist(path));
            if(IsFileExist(path)){
                FileOutputStream outputStream = new FileOutputStream(completePath, true);
                byte[] strToBytes = insteadReturn.getBytes();
                outputStream.write(strToBytes);

                outputStream.close();
            }


        }

        //Author :Mira.
        /*Usage: 1.Takes the output of some functions
                 2.Print only when press enter
         */
        public void  more(String[]args) throws FileNotFoundException {

                for(String s:args) {
                    s=IsPath(s);
                    if (IsFileExist(s)) {
                        Scanner input=new Scanner(new File(s));
                        while ((input.hasNextLine())){
                            System.out.println(input.nextLine());
                            Scanner scanner = new Scanner(System.in);
                            scanner.nextLine();
                        }

                    }
                }
            }


        public void more(){

                for (int i = 0; i < insteadReturn.length(); i++) {
                    if (i + 10 < insteadReturn.length()) {
                        System.out.print(insteadReturn.substring(i, i + 100));
                        i = i + 10;
                        Scanner scanner = new Scanner(System.in);
                        scanner.nextLine();
                    } else {
                        System.out.print(insteadReturn.substring(i, insteadReturn.length() - 1));
                    }
                }

        }

        //Author :Mira.
        /*Usage: Get current date */
        public void Date(){
            insteadReturn= new String(String.valueOf(java.time.LocalDateTime.now()));
            System.out.println(java.time.LocalDateTime.now());
        }

        //Author :Mira.
        /*Usage: Get currenty directory*/
        public void Pwd(){
            insteadReturn=CurrentDirectory;
            System.out.println(CurrentDirectory);
        }

        //Author :Mira.
        /*Usage: Clear the screen*/
        public void Clear(){
            for(int i = 0; i < 10; i++) // Default Height of cmd is 300 and Default width is 80
            {
                System.out.print("\n");
            }
        }

        //Author :Mira.
        /*Usage: Clear the screen*/
        public void help(){
            System.out.println("cd: 1.Change directory to a (given path)\n" +
                    "    2.Get back to the last path used(previous ..)\n" +
                    "    3.Return to home directory (cd || cd~)\n" +
                    "    4.Go back to parent (cd-)\n" +
                    "ls: 1.List all contents of a directory.\n" +
                    "    2.List all contents inside a file.\n" +
                    "    3.return false if file does not exist.\n" +
                    "cat:1.If given one arg it creates and name the file\n" +
                    "    2.If given more than one it prints the concatented file out and create a new file to put the output at it\n" +
                    "< : 1.Seperate the args and collect the function to clasiffy the action.\n" +
                    "    2.Execute the first fun action then pass the returned string to the file after the > sign\n" +
                    "    3.If the file does not exist, it will be created.\n" +
                    "    4.If the file exists, it will be replaced.\n" +
                    "<<: 1.Seperate the args and collect the function to clasiffy the action.\n" +
                    "    2.Execute the first fun action then pass the returned string to the file after the > sign\n" +
                    "    3.If the file does not exist, it will be created.\n" +
                    "    4.If the file exists, it will be appened.\n" +
                    "more:1.Takes the output of some functions\n" +
                    "     2.Print only when press enter\n" +
                    "date:Get current date\n" +
                    "pwd:Get currenty directory\n" +
                    "clear:Clear the screen");
            insteadReturn="cd: 1.Change directory to a (given path)\n" +
                    "    2.Get back to the last path used(previous ..)\n" +
                    "    3.Return to home directory (cd || cd~)\n" +
                    "    4.Go back to parent (cd-)\n" +
                    "ls: 1.List all contents of a directory.\n" +
                    "    2.List all contents inside a file.\n" +
                    "    3.return false if file does not exist.\n" +
                    "cat:1.If given one arg it creates and name the file\n" +
                    "    2.If given more than one it prints the concatented file out and create a new file to put the output at it\n" +
                    "< : 1.Seperate the args and collect the function to clasiffy the action.\n" +
                    "    2.Execute the first fun action then pass the returned string to the file after the > sign\n" +
                    "    3.If the file does not exist, it will be created.\n" +
                    "    4.If the file exists, it will be replaced.\n" +
                    "<<: 1.Seperate the args and collect the function to clasiffy the action.\n" +
                    "    2.Execute the first fun action then pass the returned string to the file after the > sign\n" +
                    "    3.If the file does not exist, it will be created.\n" +
                    "    4.If the file exists, it will be appened.\n" +
                    "more:1.Takes the output of some functions\n" +
                    "     2.Print only when press enter\n" +
                    "date:Get current date\n" +
                    "pwd:Get currenty directory\n" +
                    "clear:Clear the screen";

        }


    }

