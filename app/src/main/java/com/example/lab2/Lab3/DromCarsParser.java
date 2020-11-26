package com.example.lab2.Lab3;

import com.example.lab2.Lab3.Car;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DromCarsParser {
    public List<Car> getCars(String html) throws IOException {

        List<Car> carsResult = null;
        Document document = Jsoup.parse(html);

        /// html/body/div[2]/div[4]/div[1]/div[1]/div[4]/div/div[2]/a[1]
        Element element = document.select("html > body > div").get(1)
                .select(">div").get(3)
                .select(">div").get(0)
                .select(">div").get(0)
                .select(">div").get(3);
        if (element.text().contains("Сортировка") && element.text().contains("отличная цена")) {
            System.out.println();
            Elements cars = element.select("a[data-ftid=bulls-list_bull]");
            carsResult = new ArrayList<>();
            for (Element car_html : cars) {
                Car car = new Car();
                car.setHref(car_html.attr("href"));
                ;
                ///////////////////////////////////////////////////////////////
                Elements images_elements = car_html
                        .select("> div[data-ftid=bull_image]")
                        .select(">div").get(0)
                        .select("img");
                if (images_elements.size() > 0) {
                    Element img = images_elements.get(0);
                    String image = (String) img.attr("data-srcset");
                    String[] s = image.split(" ");
                    List<String> images = new ArrayList<>();
                    for (String s1 : s) {
                        if (s1.length() > 10)
                            images.add(s1);
                    }
                    car.setImage_x1(images.get(0));
                    car.setImage_x2(images.get(1));
                }

                ///////////////////////////////////////////////////////////////
                String[] id_split = car.getHref().split("/");
                car.setId(id_split[id_split.length - 1].split("\\.")[0]);
                ///////////////////////////////////////////////////////////////
                Element name = car_html
                        .select(">div").get(1)
                        .select(">div").get(0)
                        .select(">div").get(0)
                        .select("span").get(0);
                car.setName(name.text());

                ///////////////////////////////////////////////////////////////
                Elements info = car_html
                        .select(">div").get(1)
                        .select(">div").get(1)
                        .select("span");

                StringBuilder info_stringBuilder = new StringBuilder();
                for (int i = 0; i < info.size(); i++) {
                    info_stringBuilder.append(info.get(i).text());
                }
                car.setInfo(info_stringBuilder.toString());
                ///////////////////////////////////////////////////////////////
                Element price_elements = car_html
                        .select(">div").get(2)
                        .select(">div").get(0)
                        .select(">div").get(0)
                        .select("span").get(0)
                        .select("span").get(0);
                String price  = price_elements.text();
                car.setPrice(price.substring(0, price.length() - 2));
                carsResult.add(car);
            }
        }
        return carsResult;
    }


//    private String getPage(String url) throws IOException {
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        String result = null;
//
//        try {
//
//            HttpGet request = new HttpGet(url);
//
//            request.addHeader(HttpHeaders.USER_AGENT, "Googlebot");
//
//            CloseableHttpResponse response = httpClient.execute(request);
//
//            try {
//
//                System.out.println(response.getStatusLine().getStatusCode());   // 200
//                HttpEntity entity = response.getEntity();
//                if (entity != null) {
//                    result = EntityUtils.toString(entity);
//                }
//
//            } finally {
//                response.close();
//            }
//        } finally {
//            httpClient.close();
//        }
//        return result;
//    }

}
