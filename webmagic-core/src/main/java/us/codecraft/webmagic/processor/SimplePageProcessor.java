package us.codecraft.webmagic.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * A simple PageProcessor.
 *
 * @author code4crafter@gmail.com <br>
 * @since 0.1.0
 */
public class SimplePageProcessor implements PageProcessor {

    private String urlPattern;

    private Site site;

    public SimplePageProcessor(String urlPattern) {
        this.site = Site.me();
        //compile "*" expression to regex
        this.urlPattern = "(" + urlPattern.replace(".", "\\.").replace("*", "[^\"'#]*") + ")";

    }

    @Override
    public void process(Page page) {
        List<String> requests = page.getHtml().links().regex(urlPattern).all();
        //add urls to fetch
        page.addTargetRequests(requests);
        //extract by XPath
        //page.putField("title", page.getHtml().xpath("//title"));
        //page.putField("os-seller-name-primary",page.getHtml().css(".os-seller-name-primary").all());
        // get name of store
       // page.putField("os-row",page.getHtml().css(".os-row").all());
        String csvFile = "/Users/william/spider/abc.csv";
        FileWriter writer = null;


        for (Object x:page.getHtml().css(".os-row").all().toArray()
                ) {
            String str=x.toString();
            Integer StartStr=str.indexOf("=\">");
            Integer EndStr=str.indexOf(" </span> </td> \n" +
                    " <td class=\"os-rating-col\">");
            String strName=str.substring(StartStr+3,EndStr-4);
            System.out.println(strName);//name
            page.putField("strName",strName);

            Integer StartPrice=str.indexOf("<td class=\"os-total-col\"> ");
            String strPrice=str.substring(StartPrice+25,str.length()-12);
            System.out.println(strPrice);
            page.putField("strPrice",strPrice);




        }
        //page.putField("os-total-col",page.getHtml().xpath("//*[@id=\"os-sellers-table\"]/tbody/tr[3]/td[1]/span/a"));
        //page.putField("html", page.getHtml().toString());
        //extract by Readability
        //page.putField("content", page.getHtml().smartContent());
    }

    @Override
    public Site getSite() {
        //settings
        return site;
    }
}
