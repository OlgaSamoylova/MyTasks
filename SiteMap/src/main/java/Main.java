import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ForkJoinPool;

public class Main {
    final static String URL_PATH = "https://skillbox.ru/";
    public static void main(String[] args) throws IOException {

        File file = new File("C:\\Users\\olga-\\Desktop\\reposit\\15_Multithreading\\SiteMap\\src\\resultFolder\\map.txt");
        if (!file.exists()){
            file.createNewFile();
        }
        try {
            StringBuilder map = new ForkJoinPool().invoke(new SiteMapWriter(URL_PATH));
            PrintWriter writer = new PrintWriter(file);
            writer.write(map.toString());
            writer.flush();
            writer.close();
            System.out.println("Карта сайта " + URL_PATH + " записана в файл " + file.getPath());

        }
        catch (Exception ex){
            ex.printStackTrace();
        }


    }
}
