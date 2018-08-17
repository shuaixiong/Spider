package us.codecraft.webmagic;

import org.junit.Ignore;
import org.junit.Test;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.SimplePageProcessor;
import us.codecraft.webmagic.samples.HuxiuProcessor;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;
import us.codecraft.webmagic.tools.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author code4crafter@gmail.com <br>
 * Date: 13-4-20
 * Time: 下午7:46
 */
public class SpiderTest {


    @Ignore
    @Test
    public void testSpider() throws InterruptedException {
        Spider me = Spider.create(new HuxiuProcessor()).addPipeline(new FilePipeline());
        me.run();
    }

    @Ignore
    @Test
    public void testGlobalSpider(){
//        PageProcessor pageProcessor = new MeicanProcessor();
//        Spider.me().pipeline(new FilePipeline()).scheduler(new FileCacheQueueScheduler(pageProcessor.getSite(),"/data/temp/webmagic/cache/")).
//                processor(pageProcessor).run();
        //SimplePageProcessor pageProcessor2 = new SimplePageProcessor( "http://www.diaoyuweng.com/thread-*-1-1.html");
//        System.out.println(pageProcessor2.getSite().getCharset());
//        pageProcessor2.getSite().setSleepTime(50000);
       // Spider.create(pageProcessor2).addUrl("http://www.diaoyuweng.com/home.php?mod=space&uid=88304&do=thread&view=me&type=thread&from=space").addPipeline(new FilePipeline("/Users/william/spider/")).scheduler(new FileCacheQueueScheduler("/Users/william/spider/cache/")).
       // Spider.create(pageProcessor2).addUrl("https://www.google.com/shopping/product/14821698975592601437?safe=strict&rlz=1C5CHFA_enUS755US755&biw=1021&bih=904&q=EcoPlus+Eco+Air+2+Two+Outlet+-+3+Watt+126+GPH&oq=EcoPlus+Eco+Air+2+Two+Outlet+-+3+Watt+126+GPH&prds=hsec:online&sa=X&ved=0ahUKEwi5l_XjpKPXAhVX8mMKHaJrBA0Q2SsIDg").addPipeline(new FilePipeline("/Users/william/spider/")).
       // run();



        //get name and price from static page in one page
      //  Spider.create(pageProcessor2).addUrl("https://www.google.com/shopping/product/14821698975592601437/online?safe=strict&rlz=1C5CHFA_enUS755US755&biw=1021&bih=904&q=EcoPlus+Eco+Air+2+Two+Outlet+-+3+Watt+126+GPH&oq=EcoPlus+Eco+Air+2+Two+Outlet+-+3+Watt+126+GPH&sa=X&ved=0ahUKEwio89Op0qrXAhUn2IMKHdhXBC8Q6SQIKA").addPipeline(new FilePipeline("/Users/william/spider/")).
      //          run();
        String path="/Users/william/spider/";
        String filepath=path+"competitorsOfIDS.csv";
//
        //获取google product id
        PageProductIDGet pageProductIDGet=new PageProductIDGet("");
        List<ProductInfo> productName=new ArrayList<ProductInfo>();

        String FILE_NAME="/Users/william/spider/products.xlsx";
        productName.addAll(excelRead.readFile(FILE_NAME));

//        try {
//            HttpSamp.acquireIDs();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        Spider.create(pageProductIDGet).addUrl(
//                "https://www.google.com/search?safe=strict&biw=1021&bih=897&tbm=shop&ei=7vwAWtmvOsjqjwP_uIeIBg&q=FoxFarm Ocean Forest Organic Potting Soil 1.5 cu ft&oq=Hurricane+Supreme+Wall+Mount+Fan+16+in&gs_l=psy-ab.3...126674.126674.0.127607.1.1.0.0.0.0.71.71.1.1.0....0...1.2.64.psy-ab..0.0.0....0.snbECVXfzhQ"
//        ).addPipeline(new FilePipeline("/Users/william/spider/")).run();

        //获取id
        for (int i=0;i<productName.size();i++){
            Spider.create(pageProductIDGet).addUrl(
                    "https://www.google.com/search?safe=strict&biw=1021&bih=897&tbm=shop&ei=7vwAWtmvOsjqjwP_uIeIBg&q="+productName.get(i).getName()+" GTIN "+productName.get(i).getGTIN()+" zenhydro&oq="+productName.get(i).getName()+" GTIN "+productName.get(i).getGTIN()+"  zenhydro&gs_l=psy-ab.3...126674.126674.0.127607.1.1.0.0.0.0.71.71.1.1.0....0...1.2.64.psy-ab..0.0.0....0.snbECVXfzhQ"
            ).addPipeline(new FilePipeline("/Users/william/spider/")).run();
//            //Spider.create(pageProductIDGet).addUrl("https://www.google.com/search?safe=strict&biw=1021&bih=897&tbm=shop&ei=IhEWWp_7LOHf0gKI2bXIDw&btnG=Search&q=AN+Jungle+Juice+Micro+23L+GTIN+845268005660+zenhydro"
//            Spider.create(pageProductIDGet).addUrl("https://www.google.com/search?safe=strict&tbm=shop&q=AN+Jungle+Juice+Micro+23L++zenhydro+gtin"
//            ).addPipeline(new FilePipeline("/Users/william/spider/")).run();
        }

        //读取productID, 生成竞争者信息
//        List<String> ids=CSVReader.ReadProductId(filepath);
//        PageGetStoreNameAndPrice pageGetStoreNameAndPrice = new PageGetStoreNameAndPrice("");
//        for(int j=0;j<ids.size();j++){
//            Spider.create(pageGetStoreNameAndPrice).addUrl("https://www.google.com/shopping/product/"+ids.get(j).trim()+"/").
//                    thread(5).run();
//        }
    }

    @Ignore
    @Test
    public void test(){
        System.out.println(System.getProperty("java.io.tmpdir"));
    }


    @Ignore
    @Test
    public void languageSchema() {


        /**
         *
         * _hrefs = regex("<a[^<>]*href=[\"']{1}(/yewu/.*?)[\"']{1}")
         * title = r(""<title>(.*)</title>"")
         * body = x("//dd[@class='w133']")
         *
         * site.domain = "sh.58.com"
         * site.ua=""
         * site.cookie="aa:bb"
         *
         */

        /**
         *
         *
         * if (page == r('') && refer(1) == 1) {
         *
         *      type = _refer(1)
         *      content = _text.t().c()
         *      title = x("asd@asd").r("",1)
         *      body[r(_currentUrl).g(1)] = body[r(_currentUrl).g(1)] + (x("").r("",1,2).c())
         *
         *      body=body[r(_currentUrl).g(1)]
         *      tags[%] = (tags[%] + xpath('')) . r('')
         *
         *      _targetUrls.add('' + x('').r(''))
         *      _sourceUrls.add()
         *      _header.put("","");
         *      _cookie.add("asdsadasdsa");
         *
         *
         * }
         *
         * _cookie.add(_cookie[''])
         *
         * if (page == r('') && refer(1) == 1)
         *  (
         *      _targetUrl = '' + x('') & r('')
         *      _sourceUrl = ''
         *  )
         *
         */

        /**
         * <condition></>
         * <selector>
         *     <fields>
         *
         *     <type>
         *         <selector></selector>
         *         <selector></selector>
         *     </type>
         *         </>
         *     </>
         */

        /**
         *
         * if (model.url('') && model.refer(1) == 1)
         *  (
         *
         *      model.set(type, model.refer(1))
         *      content = t(_html) > c()
         *      title = x(_html, 'asd@asd') > r('',1)
         *      body[r(_currentUrl).g(1)] = body[r(_currentUrl).g(1)] + (x('') > r('',1,2) > c()) | x('')
         *      tags[%] = tags + xpath('') > r('')
         *      model.setTargetUrl();
         *
         *      _targetUrl = '' + x('') & r('')
         *      _sourceUrl = ''
         * )
         *
         * _cookie.add(_cookie[''])
         *
         * if (page == r('') && refer(1) == 1)
         *  (
         *      _targetUrl = '' + x('') & r('')
         *      _sourceUrl = ''
         *  )
         *
         */
    }
}
