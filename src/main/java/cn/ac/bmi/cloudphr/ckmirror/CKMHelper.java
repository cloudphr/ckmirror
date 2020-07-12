package cn.ac.bmi.cloudphr.ckmirror;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class CKMHelper {

  public final static String FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzzz";


  public static void parseCKMRepository() {
    try {
      String url = "https://www.openehr.org/ckm/retrieveResources?list=true";
      Document document = Jsoup.connect(url).get();
      Elements tbodies = document.getElementsByTag("tbody");
      assert tbodies.size() == 2;

      Element archetypeInfos = tbodies.get(0);
      Elements trs = archetypeInfos.getElementsByTag("tr");
      for (int i = 1; i < trs.size(); i++) {
        ArchetypeInfo archetype = new ArchetypeInfo(trs.get(i));
        System.out.println(archetype.getAdlPath());
      }

      Element templateInfos = tbodies.get(1);
      trs = templateInfos.getElementsByTag("tr");
      for (int i = 1; i < trs.size(); i++) {
        TemplateInfo template = new TemplateInfo(trs.get(i));
        System.out.println(template.getAdlPath());
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  public static String parseHrefFromElement(Element element) {
    String url = null;

    if (element != null) {
      if ("a".equalsIgnoreCase(element.tagName())) {
        url = element.attr("href");
      } else {
        Elements elements = element.getElementsByTag("a");
        if (elements != null && elements.size() == 1) {
          url = elements.get(0).attr("href");
        }
      }
    }

    return url;
  }
}
