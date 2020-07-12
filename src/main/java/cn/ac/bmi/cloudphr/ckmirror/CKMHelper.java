package cn.ac.bmi.cloudphr.ckmirror;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class CKMHelper {

  public static final String FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzzz";

  private List<ArchetypeInfo> archetypeInfoList;
  private List<TemplateInfo> templateInfoList;


  public void parseCKMRepository() {
    archetypeInfoList = null;
    templateInfoList = null;
    try {
      String url = "https://www.openehr.org/ckm/retrieveResources?list=true";
      Document document = Jsoup.connect(url).get();
      Elements tbodies = document.getElementsByTag("tbody");
      assert tbodies.size() == 2; //一个是原型汇总信息表，一个是模板汇总信息表

      Element archetypeInfos = tbodies.get(0); //原型汇总信息表
      Elements trs = archetypeInfos.getElementsByTag("tr");
      if (trs.size() > 1) {
        archetypeInfoList = new ArrayList<>();
      }
      for (int i = 1; i < trs.size(); i++) {
        ArchetypeInfo archetype = new ArchetypeInfo(trs.get(i));
        archetypeInfoList.add(archetype);
        //System.out.println(archetype.getAdlPath());
      }

      Element templateInfos = tbodies.get(1); //模板汇总信息表
      trs = templateInfos.getElementsByTag("tr");
      if (trs.size() > 1) {
        templateInfoList = new ArrayList<>();
      }
      for (int i = 1; i < trs.size(); i++) {
        TemplateInfo template = new TemplateInfo(trs.get(i));
        templateInfoList.add(template);
        //System.out.println(template.getAdlPath());
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
