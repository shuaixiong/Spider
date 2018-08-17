package us.codecraft.webmagic.tools;

import au.com.bytecode.opencsv.CSVWriter;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class PageGetStoreNameAndPrice implements PageProcessor {
    private String urlPattern;

    private Site site;

    public PageGetStoreNameAndPrice(String urlPattern) {
        this.site = Site.me().setUserAgent("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)").setSleepTime(1000);

        //compile "*" expression to regex
        this.urlPattern = "(" + urlPattern.replace(".", "\\.").replace("*", "[^\"'#]*") + ")";

    }

    @Override
    public void process(Page page) {

        List<String> requests = page.getHtml().links().regex(urlPattern).all();

        //add urls to fetch
        page.addTargetRequests(requests);

        //获得产品名称
        System.out.println(page.getUrl());
        String productName=page.getHtml().css("head > title").toString();

        //获得 google sku #specs > div > div > div:nth-child(2) > span._KDu
        String productSku=page.getHtml().css("#specs > div > div ").toString();
        Integer productStartStr=productSku.indexOf("GTIN</td>");
        Integer productEndStr=productSku.indexOf("</table>");
        productSku=productSku.substring(productStartStr+31,productEndStr-27);


        //获得所有的 store name and price
        List<Store> listOfStore=new ArrayList<Store>();
        List<String[]> dataAll=new ArrayList<String[]>();

        //遍历每一个产品
        for (Object x:page.getHtml().css(".os-row").all().toArray()){
            String str=x.toString();
            //store name
            Integer StartStr=str.indexOf("=\">");
            Integer EndStr=str.indexOf(" </span> </td> \n" +
                    " <td class=\"os-rating-col\">");
            String strName=str.substring(StartStr+3,EndStr-4);
            page.putField("strName",strName);

            //detail (no tax, free shipping ,null)
            Integer detailS=str.indexOf("<td class=\"os-details-col\">");
            Integer detailE=str.indexOf("</td> \n" +
                    " <td class=\"os-price-col");
            String detail=str.substring(detailS+28,detailE).trim();
            page.putField("detail",detail);


            //product price
            Integer productPriceS=str.indexOf("_HDu\">");
            Integer productPriceE=str.indexOf("</span> <span class=\"os-total-description");
            String productPrice=str.substring(productPriceS+6,productPriceE);

            //description
            Integer descriptionS=str.indexOf("scription\">");
            Integer descriptionE=str.indexOf("</span> </td> \n" +
                    " <td class=\"os-total-col\">");
            String description=str.substring(descriptionS+11,descriptionE).trim();
            page.putField("description",description);

            //total price
            Integer StartPrice=str.indexOf("<td class=\"os-total-col\"> ");
            String strPrice=str.substring(StartPrice+25,str.length()-12);
            page.putField("strPrice",strPrice);

            //get tax and shipping info from detail
            /*****
            if detail=null
            tax=+$1.46 tax
            shipping=$5.99 shipping
            elseif detail free shipping==1 && no tax==1
            tax=0
            shipping=0
            elseif detail free shipping==1 && no tax==0
            tax= +$2.06
            shipping=0
            elseif detail free shipping==0 && no tax ==1
            tax=0
            shipping=+$17.74 shipping
            *******/
            String tax="";
            String shipping="";
            if(detail.length()==0){
                tax=description.substring(description.indexOf("+$")+2,description.indexOf("tax")).trim();
                shipping=description.substring(description.indexOf("and $")+5,description.indexOf("shipping")).trim();
            }else if(detail.contains("Free shipping")&&detail.contains("No tax")){
                tax="No tax";
                shipping="Free shipping";
            }else if(detail.contains("Free shipping")&&!detail.contains("No tax")){
                tax=description.substring(description.indexOf("+$")+2,description.indexOf("tax")).trim();
                shipping="Free shipping";
            }else if(!detail.contains("Free shipping")&&detail.contains("No tax")){
                tax="No tax";
                if(description!=null&&!description.trim().toString().equals("")) {
                    shipping = description.substring(description.indexOf("+$") + 2, description.indexOf("shipping")).trim();
                }
            }

            //add to object
            listOfStore.add(new Store(strName,strPrice.toString().trim(),shipping,productPrice,tax));

            //sort store
            sortByRuleBasedCollator(listOfStore);

        }
        page.putField("rows",page.getHtml().css(".os-row").all());

        //写入csv
        String csvFile = "/Users/william/spider/competitors.csv";
        FileWriter writer = null;


        List<String> storeNameList=new ArrayList<String>();
        List<String> storePriceList=new ArrayList<String>();
        List<String> storeDetails=new ArrayList<String>();

        //lowest price
        List<Double> priceInt=new ArrayList<Double>();
        List<String> tempList=new ArrayList<String>();

        storeNameList.add(productName.substring(7,productName.length()-8));
        storeNameList.add(productSku);


            //writer = new FileWriter(csvFile,true);
            for (Store x:listOfStore
                 ) {
                storeNameList.add("Total - "+x.getStoreName());
                storeNameList.add(x.getPriceTotal());
                storeNameList.add("Shipping - "+x.getStoreName());
                storeNameList.add(x.getShippingOfDetails());
                storeNameList.add("Tax - "+x.getStoreName());
                storeNameList.add(x.getTax());
                storeNameList.add("Price "+x.getStoreName());
                storeNameList.add(x.getProductPrice());
            }

            //写进csv
            //CSVUtilsWriter.writeLine(writer,storeNameList);
            String[] itemArr=new String[storeNameList.size()];
            itemArr=storeNameList.toArray(itemArr);
            //dataAll.add(itemArr);

            try {
                CSVWriter writerCSV = new CSVWriter(new FileWriter(csvFile,true));
                writerCSV.writeNext(itemArr);
                writerCSV.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    //对属性进行排序
    public static void sortByRuleBasedCollator(List list) {

        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return (java.text.Collator.getInstance(Locale.US)).compare(((Store) o2).getStoreName(),((Store) o1).getStoreName());
            }
        });
//        System.out.println("/////////////排序之后///////////////");
//        for (int i = 0; i < list.size(); i++) {
//            Student st = (Student) list.get(i);
//            System.out.println("st.age=" + st.getAge() + ",st.name=" + st.getName());
//        }
    }

    @Override
    public Site getSite() {
        //settings
        return site;
    }
}
