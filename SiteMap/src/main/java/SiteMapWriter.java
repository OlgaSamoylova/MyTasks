import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class SiteMapWriter extends RecursiveTask<StringBuilder> {

    String urlPath;
    StringBuilder siteMap = new StringBuilder();
    ArrayList<String> siteLinks;
    String tabCount;


    public SiteMapWriter(String urlPath) {
        this.urlPath = urlPath;
        siteLinks = new ArrayList<>();
        this.tabCount = "";
    }
    public SiteMapWriter(String urlPath, ArrayList<String> siteLinks, String tabCount) {
        this.urlPath = urlPath;
        this.siteLinks = siteLinks;
        this.tabCount = tabCount;
    }

    @Override
    protected StringBuilder compute() {

        siteMap.append(tabCount + urlPath + "\n");
        siteLinks.add(urlPath);
        try {
            Document document = Jsoup.connect(urlPath).get();
            Elements elements = document.select("a");
            
            List<SiteMapWriter> taskList = new ArrayList<>();
            String prevChildPath = "";

            for (Element element : elements){
                String urlPathChild = element.absUrl("href");
                if(urlPath.substring(8,10).equals("www")){
                    urlPath = urlPath.substring(0, 8) + urlPath.substring(11);
                }

                if (urlPathChild.length() >= urlPath.length() && !urlPathChild.equals(prevChildPath)) {
                    if ((urlPath.equals(urlPathChild.substring(0, urlPath.length())))
                        && !siteLinks.contains(urlPathChild)
                        && !(urlPath.equals(urlPathChild.substring(0, urlPathChild.length() - 1)))){
                        SiteMapWriter task = new SiteMapWriter(urlPathChild, siteLinks, tabCount + "\t");
                        Thread.sleep(110);
                        task.fork();
                        taskList.add(task);
                    }
                }
                prevChildPath = urlPathChild;

            }
            for (SiteMapWriter task : taskList) {
                siteMap.append(task.join());
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return siteMap;
    }
}
