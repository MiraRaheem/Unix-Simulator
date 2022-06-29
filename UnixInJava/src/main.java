import java.io.IOException;

public class main {
    public static void main(String args[]) throws IOException {

        Terminal item = new Terminal();
        String str="cd c:// | help";
        Parser p1=new Parser(str);
        String cmd=p1.getCmd();
        String[] commands=p1.getArguments();
        if (p1.parse(str)) {
            if (cmd.equals("cd")) {
                item.cd(commands[0]);
            }

            else if(cmd.equals("|")){
                item.pipe(str);
            }
            else if(cmd.equals("<")){
                item.oneArrow(str);
            }
            else if(cmd.equals("<<")){
                item.twoArrows(str);
            }
        }
    }
}
