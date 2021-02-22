package cn.ac.bmi.cloudphr.ckmirror;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Data
@RequiredArgsConstructor
public class ArchetypeInfo {
  private String concept;
  private String archetypeId;
  private String description;
  private String status;
  private String createdAt;
  private String updatedAt;
  private String assetCid;
  private String ckmPath;
  private String adlPath;
  private String xmlPath;

  public ArchetypeInfo(Element element) {
    Elements tds = element.getElementsByTag("td");
    this.concept = tds.get(0).text();
    this.archetypeId = tds.get(1).text();
    this.description = tds.get(2).text();
    this.status = tds.get(3).text();
    this.createdAt = tds.get(4).text();
    this.updatedAt = tds.get(5).text();
    this.assetCid = tds.get(6).text();
    this.ckmPath = CkmHelper.parseHrefFromElement(tds.get(7));
    this.adlPath = CkmHelper.parseHrefFromElement(tds.get(8));
    this.xmlPath = CkmHelper.parseHrefFromElement(tds.get(9));
  }

  public ArchetypeInfo(
          String concept,
          String archetypeId,
          String description,
          String status,
          String createdAt,
          String updatedAt,
          String assetCid,
          String ckmPath,
          String adlPath,
          String xmlPath) {
    this.concept = concept;
    this.archetypeId = archetypeId;
    this.description = description;
    this.status = status;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.assetCid = assetCid;
    this.ckmPath = ckmPath;
    this.adlPath = adlPath;
    this.xmlPath = xmlPath;
  }
}
