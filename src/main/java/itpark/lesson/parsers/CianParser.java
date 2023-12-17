package itpark.lesson.parsers;

import itpark.lesson.model.entity.Advertisement;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 1ommy
 * @version 17.12.2023
 */
public class CianParser {
    public List<Advertisement> parse(Integer page, Integer countAdvertisements) {
        List<Advertisement> advertisementList = new ArrayList<>();

        for (int i = 1; i <= page; i++) {
            extractInfoFromCian(i, advertisementList);
        }

        return advertisementList.subList(0, countAdvertisements);
    }

    private void extractInfoFromCian(Integer page, List<Advertisement> advertisementList) {
        var doc = getDocument(page);


        var ads = doc.select("div#frontend-serp > div:nth-child(1) > div:nth-child(4)").get(0).children();


        mapRawDataToList(advertisementList, ads);
    }

    private void mapRawDataToList(List<Advertisement> advertisementList, Elements ads) {
        for (int i = 0; i < ads.size(); i++) {
            var ad = ads.get(i);
            if (ad != null) {
                if (ad.child(0).tagName().equalsIgnoreCase("article")) {
                    var item = ads.get(i).child(0).child(0).child(1).child(0).child(0);

                    var adLink = getHref(item);
                    var advertiserName = getAdvertiserName(item);
                    var phoneNumber = "N/A";
                    var subway = getSubway(item);
                    var address = getAdAddress(item);
                    var apartmentArea = getApartmentArea(item);
                    var price = getPrice(item);
                    var description = getDescription(item);

                    Advertisement advertisement = Advertisement.builder().metroStation(subway).price(price).uri(adLink).description(description).title(advertiserName).region(address).build();

                    advertisementList.add(advertisement);
                }
            }
        }
    }

    private String getDescription(Element item) {
        var element = item.select("> div[data-name=GeneralInfoSectionRowComponent] > div[data-name=Description] > p");
        return element.text();
    }

    private String getPrice(Element item) {
        var priceElement = item.select("> div[data-name=GeneralInfoSectionRowComponent] > div[data-name=ContentRow] > span[data-mark=MainPrice] > span");
        if (priceElement.size() > 0) {
            return priceElement.text();
        }
        return "N/A";
    }

    private String getApartmentArea(Element item) {
        // var res = "";
        var first = item.select("> div[data-name=GeneralInfoSectionRowComponent] > a > span[data-mark=OfferTitle] > span").text();
        var second = item.select("> div[data-name=GeneralInfoSectionRowComponent] a > div > span[data-mark=OfferSubtitle] > span").text();
        var third = item.select("> div[data-name=GeneralInfoSectionRowComponent] a > div > span[data-mark=OfferSubtitle]").text();
        return first + " " + second;
    }


    private String getAdAddress(Element item) {
        var address = new StringBuffer();
        var addressNode = item.select("> div[data-name=GeneralInfoSectionRowComponent] > div > a[data-name=GeoLabel]");
        if (addressNode.size() == 0) {
            address.append("N/A");
        } else {
            for (var j : addressNode) {
                address.append(j.text() + " ");
            }
        }
        return address.toString();
    }

    private String getSubway(Element item) {
        String subway;
        var one = item.select("> div[data-name=GeneralInfoSectionRowComponent] > div[data-name=SpecialGeo] > a > div[data-name=GeoLabel]");
        var two = item.select("> div[data-name=GeneralInfoSectionRowComponent] > div[data-name=SpecialGeo] > a > div");
        subway = String.join(" ", one.text(), two.text());
        return subway;
    }

    private String getAdvertiserName(Element item) {
        return item.select("> div[data-name=GeneralInfoSectionRowComponent] > div[data-name=ContentRow] > a").text();
    }

    private String getHref(Element item) {
        return item.child(0).attributes().get("href");
    }

    private Document getDocument(int pageNumber) {
        try {

            /* https://www.cian.ru/cat.php?deal_type=sale&engine_version=2&offer_type=flat&p=1&region=1&room1=1&room2=1&room3=1 */

            var doc = Jsoup.connect("https://www.cian.ru/cat.php?" + "deal_type=sale" + "&engine_version=2" + "&offer_type=flat" + "&p=" + pageNumber + "&region=1" + "&room1=1" + "&room2=1" + "&room3=1")
                    .cookie("_CIAN_GK", "7a4e2c2b-b252-4d8a-8f07-389a5f9dbd4d").cookie("_gcl_au", "1.1.1478589100.1702208517").cookie("login_mro_popup", "1").cookie("tmr_lvid", "6ce0dcd836034d8f6a3dc7b75021d4ab").cookie("tmr_lvidTS", "1702208516928").cookie("sopr_utm", "%7B%22utm_source%22%3A+%22google%22%2C+%22utm_medium%22%3A+%22organic%22%7D").cookie("sopr_session", "3ffa5421524c48c5").cookie("uxfb_usertype", "searcher").cookie("_gid", "GA1.2.548151434.1702208518").cookie("uxs_uid", "20184270-9751-11ee-96a2-dbad52df3a27").cookie("_ym_uid", "1702208518380227365").cookie("_ym_d", "1702208518").cookie("_ym_isad", "1").cookie("_ym_visorc", "b").cookie("afUserId", "effec3dc-b98d-485f-8c76-390a6012bd97-p").cookie("AF_SYNC", "1702208520576").cookie("cookie_agreement_accepted", "1").cookie("session_region_id", "1").cookie("session_region_name", "%D0%9C%D0%BE%D1%81%D0%BA%D0%B2%D0%B0").cookie("forever_region_id", "1").cookie("forever_region_name", "%D0%9C%D0%BE%D1%81%D0%BA%D0%B2%D0%B0").cookie("session_main_town_region_id", "1").cookie("__cf_bm=_LxkeBsfFz6QSWmpUARHcC4gLnbo.4wCk0lgVeibVjo-1702209661-0-ATWINUxtd7sCVflZeeE/bSsQq2jd+2ep5mSfh3yDFkxLcVYs9n7L7iKnd2+cFyQwobwYMhBhINuF+/Pvam12upk", "").cookie("tmr_detect", "1%7C1702209923552").cookie("_ga", "GA1.2.1712194648.1702208518").cookie("_ga_3369S417EL", "GS1.1.1702208518.1.1.1702209989.60.0.0").cookie("viewpageTimer", "2502.902").userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36").referrer("https://www.google.com/").get();

            // File in = new File("C:/users/1/Desktop/index.html");
            // var doc = Jsoup.parse(in, null);

            return doc;


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
