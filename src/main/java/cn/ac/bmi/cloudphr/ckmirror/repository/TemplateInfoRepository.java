package cn.ac.bmi.cloudphr.ckmirror.repository;

import cn.ac.bmi.cloudphr.ckmirror.TemplateInfo;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TemplateInfoRepository {

  private JdbcTemplate jdbcTemplate;

  @Autowired
  public TemplateInfoRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public TemplateInfo findByTemplateId(String id) {
    try {
      return jdbcTemplate.queryForObject(
              "SELECT * FROM template_info WHERE template_id=?",
              this::mapRowToTemplateInfo, id);
    } catch (EmptyResultDataAccessException e) {
      return null;
    }
  }

  public TemplateInfo deleteByTemplateId(String id) {
    TemplateInfo templateInfo = findByTemplateId(id);
    jdbcTemplate.update("DELETE FROM template_info WHERE template_id=?", id);
    return templateInfo;
  }

  public TemplateInfo insertOne(TemplateInfo templateInfo) {
    jdbcTemplate.update("INSERT INTO template_info "
        + "(template, template_id, status, created_at, updated_at, asset_cid, ckm_path, adl_path)"
                     + "VALUES(?, ?, ?, ?, ?, ?, ?, ?)",
            templateInfo.getTemplate(),
            templateInfo.getTemplateId(),
            templateInfo.getStatus(),
            templateInfo.getCreatedAt(),
            templateInfo.getUpdatedAt(),
            templateInfo.getAssetCid(),
            templateInfo.getCkmPath(),
            templateInfo.getAdlPath());
    return templateInfo;
  }

  private TemplateInfo mapRowToTemplateInfo(ResultSet rs, int rowNum)
          throws SQLException {
    return new TemplateInfo(
            rs.getString("template"),
            rs.getString("template_id"),
            rs.getString("status"),
            rs.getString("created_at"),
            rs.getString("updated_at"),
            rs.getString("asset_cid"),
            rs.getString("ckm_path"),
            rs.getString("adl_path"));
  }
}
