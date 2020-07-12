package cn.ac.bmi.cloudphr.ckmirror;


import cn.hutool.core.date.DateUtil;
import lombok.Data;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.persistence.Entity;
import javax.persistence.Id;

import static cn.ac.bmi.cloudphr.ckmirror.CKMHelper.FORMAT;

@Data
@Entity
public class ArchetypeInfo {

  private String concept;
  @Id
  private String archetypeID;
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
    this.archetypeID = tds.get(1).text();
    this.description = tds.get(2).text();
    this.status = tds.get(3).text();
    this.createdAt = DateUtil.parse(tds.get(4).text(), FORMAT).toString();
    this.updatedAt = DateUtil.parse(tds.get(5).text(), FORMAT).toString();
    this.assetCid = tds.get(6).text();
    this.ckmPath = CKMHelper.parseHrefFromElement(tds.get(7));
    this.adlPath = CKMHelper.parseHrefFromElement(tds.get(8));
    this.xmlPath = CKMHelper.parseHrefFromElement(tds.get(9));
  }
}
