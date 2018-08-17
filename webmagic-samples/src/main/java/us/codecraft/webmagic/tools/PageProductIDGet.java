package us.codecraft.webmagic.tools;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PageProductIDGet implements PageProcessor{

        private String urlPattern;

        private Site site;

        public PageProductIDGet(String urlPattern) {
            this.site = Site.me();
            //compile "*" expression to regex
            this.urlPattern = "(" + urlPattern.replace(".", "\\.").replace("*", "[^\"'#]*") + ")";

        }


        @Override
        public void process(Page page){
            List<String> requests = page.getHtml().links().regex(urlPattern).all();
            //add urls to fetch
            page.addTargetRequests(requests);
            //extract by XPath

            // get name of store
            String std=page.getHtml().toString();
            String str=page.getHtml().css(".psliimg").all().get(0).toString();
            Integer StartStr=str.indexOf("/shopping/product/");
            Integer EndStr=str.indexOf("?safe=");
            String strName=str.substring(StartStr+18,EndStr);
            page.putField("productID",strName);


            String csvFile = "/Users/william/spider/abc.csv";
            FileWriter writer = null;
            try {
                writer = new FileWriter(csvFile,true);
                CSVUtilsWriter.writeLine(writer, Arrays.asList(strName));
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


            //extract by Readability
            //page.putField("content", page.getHtml());
        }

        public void processFromGoogleResult(Page page){
            List<String> requests = page.getHtml().links().regex(urlPattern).all();
            //add urls to fetch
            page.addTargetRequests(requests);
            //extract by XPath

            // get name of store
            String std=page.getHtml().toString();
            String str=page.getHtml().css(".psliimg").all().get(0).toString();
            Integer StartStr=str.indexOf("/shopping/product/");
            Integer EndStr=str.indexOf("?safe=");
            String strName=str.substring(StartStr+18,EndStr);
            page.putField("productID",strName);


            String csvFile = "/Users/william/spider/abc.csv";
            FileWriter writer = null;
            try {
                writer = new FileWriter(csvFile,true);
                CSVUtilsWriter.writeLine(writer, Arrays.asList(strName));
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


            //extract by Readability
            //page.putField("content", page.getHtml());
        }

        @Override
        public Site getSite() {
            //settings
            return site;
        }

}
