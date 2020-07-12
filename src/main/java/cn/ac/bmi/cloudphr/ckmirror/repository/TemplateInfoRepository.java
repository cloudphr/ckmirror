package cn.ac.bmi.cloudphr.ckmirror.repository;

import cn.ac.bmi.cloudphr.ckmirror.ArchetypeInfo;
import cn.ac.bmi.cloudphr.ckmirror.TemplateInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TemplateInfoRepository {

  private JdbcTemplate jdbcTemplate;

  @Autowired
  public TemplateInfoRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public TemplateInfo findByTemplateID(String id) {
    return jdbcTemplate.queryForObject(
            "select * from template_info where id=?",
            this::mapRowToTemplateInfo, id);
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
