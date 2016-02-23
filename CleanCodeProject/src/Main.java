
import java.io.IOException;

public class Main {
    public static void main(String[] args)  {
        Chat c = new Chat();
       try {
           c.menu();
       }
       catch(IOException e){
           System.exit(0);
       }

    }
}