package cn.ac.bmi.cloudphr.ckmirror;


import cn.hutool.core.date.DateUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import static cn.ac.bmi.cloudphr.ckmirror.CKMHelper.FORMAT;

@Data
@RequiredArgsConstructor
public class ArchetypeInfo {

  private String concept;
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

  public ArchetypeInfo(
          String concept,
          String archetype_id,
          String description,
          String status,
          String created_at,
          String updated_at,
          String asset_cid,
          String ckm_path,
          String adl_path,
          String xml_path) {
    this.concept = concept;
    this.archetypeID = archetype_id;
    this.description = description;
    this.status = status;
    this.createdAt = created_at;
    this.updatedAt = updated_at;
    this.assetCid = asset_cid;
    this.ckmPath = ckm_path;
    this.adlPath = adl_path;
    this.xmlPath = xml_path;
  }
}
