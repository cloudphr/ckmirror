package cn.ac.bmi.cloudphr.ckmirror;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Data
@RequiredArgsConstructor
public class TemplateInfo {
  private String template;
  private String templateId;
  private String status;
  private String createdAt;
  private String updatedAt;
  private String assetCid;
  private String ckmPath;
  private String adlPath;

  public TemplateInfo(Element element) {
    Elements tds = element.getElementsByTag("td");
    this.template = tds.get(0).text();
    this.templateId = tds.get(1).text();
    this.status = tds.get(2).text();
    this.createdAt = tds.get(3).text();
    this.updatedAt = tds.get(4).text();
    this.assetCid = tds.get(5).text();
    this.ckmPath = CkmHelper.parseHrefFromElement(tds.get(6));
    this.adlPath = CkmHelper.parseHrefFromElement(tds.get(7));
  }

  public TemplateInfo(
          String template,
          String templateId,
          String status,
          String createdAt,
          String updatedAt,
          String assetCid,
          String ckmPath,
          String adlPath) {
    this.template = template;
    this.templateId = templateId;
    this.status = status;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.assetCid = assetCid;
    this.ckmPath = ckmPath;
    this.adlPath = adlPath;
  }
}
