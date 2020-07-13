package cn.ac.bmi.cloudphr.ckmirror.repository;

import cn.ac.bmi.cloudphr.ckmirror.TemplateInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class TemplateInfoRepository {

  private JdbcTemplate jdbcTemplate;

  @Autowired
  public TemplateInfoRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public TemplateInfo findByTemplateID(String id) {
    try {
      return jdbcTemplate.queryForObject(
              "select * from template_info where template_id=?",
              this::mapRowToTemplateInfo, id);
    } catch (EmptyResultDataAccessException e) {
      return null;
    }
  }

  public TemplateInfo deleteByTemplateID(String id) {
    TemplateInfo templateInfo = findByTemplateID(id);
    jdbcTemplate.update("delete from template_info where template_id=?", id);
    return templateInfo;
  }

  public TemplateInfo insertOne(TemplateInfo templateInfo) {
    jdbcTemplate.update("insert into template_info " +
                    "(template, template_id, status, created_at, updated_at, asset_cid, ckm_path, adl_path)" +
                    "values(?, ?, ?, ?, ?, ?, ?, ?)",
            templateInfo.getTemplate(),
            templateInfo.getTemplateID(),
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
