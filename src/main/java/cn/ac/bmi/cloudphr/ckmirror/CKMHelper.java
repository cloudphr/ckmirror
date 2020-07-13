package cn.ac.bmi.cloudphr.ckmirror;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CKMHelper {

  public static final String FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzzz";
  public static final String ARCHETYPES_DIRECTORY =
          System.getProperty("user.dir") + File.separator
                  + "openehr" + File.separator
                  + "ckm" + File.separator
                  + "archetypes" + File.separator;
  public static final String TEMPLATES_DIRECTORY =
          System.getProperty("user.dir") + File.separator
                  + "openehr" + File.separator
                  + "ckm" + File.separator
                  + "templates" + File.separator;

  public static List<ArchetypeInfo> archetypeInfoList;
  public static List<TemplateInfo> templateInfoList;

  public static void parseCKMRepository() {
    CKMHelper.archetypeInfoList = null;
    CKMHelper.templateInfoList = null;
    try {
      String url = "https://www.openehr.org/ckm/retrieveResources?list=true";
      Document document = Jsoup.connect(url).get();
      Elements tbodies = document.getElementsByTag("tbody");
      assert tbodies.size() == 2;

      Element archetypeInfos = tbodies.get(0);
      Elements trs = archetypeInfos.getElementsByTag("tr");
      if (trs.size() > 1) {
        archetypeInfoList = new ArrayList<>();
      }
      for (int i = 1; i < trs.size(); i++) {
        ArchetypeInfo archetype = new ArchetypeInfo(trs.get(i));
        archetypeInfoList.add(archetype);
        //System.out.println(archetype.getAdlPath());
      }

      Element templateInfos = tbodies.get(1);
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

  public static String parseHrefFromElement(final Element element) {
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

  public static void downloadByUrlToFile(final String urlPath, final String savedFilePath)
          throws Exception {
    URL url = new URL(urlPath);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    int code = conn.getResponseCode();
    if (code != HttpURLConnection.HTTP_OK) {
      throw new Exception("Can not read file from url:" + urlPath);
    }
    InputStream fis = new BufferedInputStream(conn.getInputStream());
    File file = new File(savedFilePath);
    OutputStream toClient = new BufferedOutputStream(new FileOutputStream(file));
    byte[] buffer = new byte[1024 * 8];
    int read = 0;
    while ((read = fis.read(buffer)) != -1) {
      toClient.write(buffer, 0, read);
    }
    toClient.flush();
    toClient.close();
    fis.close();
  }

  public static void downloadFromCKM(final String urlPath, final String id, final String postfix) {
    try {
      String downloadFilePath;
      if (postfix == "adl") {
        downloadFilePath =  ARCHETYPES_DIRECTORY + id + "." + postfix;
      } else if (postfix == "oet") {
        downloadFilePath =  TEMPLATES_DIRECTORY + id + "." + postfix;
      } else {
        throw new Exception("postfix: " + postfix + "is not supported...");
      }
      CKMHelper.downloadByUrlToFile(urlPath, downloadFilePath);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
