package bean;

/**
 * Created by joehu on 2016/3/29.
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import UTIL.DataUtil;
import UTIL.URLUtil;

/**
 * 处理NewItem的业务类
 * @author zhy
 *
 */
public class NewsItemBiz {
    /**
     * 业界、移动、云计算
     *
     * @return
     * @throws CommonException
     */
    public List<NewsItem> getNewsItem(int newsType, int currentPage) throws CommonException {
        String urlStr = URLUtil.generateURL(newsType, currentPage);

        String htmlStr = DataUtil.doGet(urlStr);

        List<NewsItem> mItems = new ArrayList<NewsItem>();
        NewsItem newsItem;

        Document doc = Jsoup.parse(htmlStr);
        Elements units = doc.getElementsByClass("unit");
        for (int i = 0; i < units.size(); i++)
        {
            newsItem = new NewsItem();
            newsItem.setNewsType(newsType);

            Element unit_ele = units.get(i);

            Element h1_ele = unit_ele.getElementsByTag("h1").get(0);
            Element h1_a_ele = h1_ele.child(0);
            String title = h1_a_ele.text();
            String href = h1_a_ele.attr("href");

            newsItem.setLink(href);
            newsItem.setTitle(title);

            Element h4_ele = unit_ele.getElementsByTag("h4").get(0);
            Element ago_ele = h4_ele.getElementsByClass("ago").get(0);
            String date = ago_ele.text();

            newsItem.setDate(date);

            Element dl_ele = unit_ele.getElementsByTag("dl").get(0);// dl
            Element dt_ele = dl_ele.child(0);// dt
            try
            {// 可能没有图片
                Element img_ele = dt_ele.child(0);
                String imgLink = img_ele.child(0).attr("src");
                newsItem.setImgLink(imgLink);
            } catch (IndexOutOfBoundsException e)
            {

            }
            Element content_ele = dl_ele.child(1);// dd
            String content = content_ele.text();
            newsItem.setContent(content);
            mItems.add(newsItem);
        }

        return mItems;
    }
}
