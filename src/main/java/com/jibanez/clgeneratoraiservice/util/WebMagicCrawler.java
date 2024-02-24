package com.jibanez.clgeneratoraiservice.util;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.ExtractByUrl;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

//implement PageProcessor
public class WebMagicCrawler implements PageProcessor {

    private final Site SITE = Site.me();

    public static void main(String[] args) {
        //define the url to scrape
        //will run in a separate thread
    }

    @Override
    public void process(Page page) {
        //fetch all blog headings storing them in a list
        List<String> rs = page.getHtml().css("div.blog-header a h2").all();

        //loop through the list printing out its contents
        for (String s:rs ){
            System.out.println("Heading "+ s);
        }
    }

    @Override
    public Site getSite() {
        return SITE;
    }

    public static class GithubRepoPageProcessor implements PageProcessor {

        private final Site SITE = Site.me().setRetryTimes(3).setSleepTime(1000);

        @Override
        public void process(Page page) {
            page.addTargetRequests(page.getHtml().links().regex("(https://github\\.com/\\w+/\\w+)").all());
            page.putField("author", page.getUrl().regex("https://github\\.com/(\\w+)/.*").toString());
            page.putField("name", page.getHtml().xpath("//h1[@class='public']/strong/a/text()").toString());
            if (page.getResultItems().get("name")==null){
                //skip this page
                page.setSkip(true);
            }
            page.putField("readme", page.getHtml().xpath("//div[@id='readme']/tidyText()"));
        }

        @Override
        public Site getSite() {
            return SITE;
        }

        public static void main(String[] args) {
            Spider.create(new GithubRepoPageProcessor()).addUrl("https://github.com/code4craft").thread(5).run();
        }
    }

    @TargetUrl("https://github.com/\\w+/\\w+")
    @HelpUrl("https://github.com/\\w+")
    public static class GithubRepo {

        @ExtractBy(value = "//h1[@class='public']/strong/a/text()", notNull = true)
        private String name;

        @ExtractByUrl("https://github\\.com/(\\w+)/.*")
        private String author;

        @ExtractBy("//div[@id='readme']/tidyText()")
        private String readme;

        public static void main(String[] args) {
            OOSpider.create(Site.me().setSleepTime(1000)
                            , new ConsolePageModelPipeline(), GithubRepo.class)
                    .addUrl("https://github.com/code4craft").thread(5).run();
        }
    }
}