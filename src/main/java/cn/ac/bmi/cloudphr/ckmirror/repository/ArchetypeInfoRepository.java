package cn.ac.bmi.cloudphr.ckmirror.repository;

import cn.ac.bmi.cloudphr.ckmirror.ArchetypeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class ArchetypeInfoRepository {
  private JdbcTemplate jdbcTemplate;

  @Autowired
  public ArchetypeInfoRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public ArchetypeInfo findByArchetypeID(String id) {
    return jdbcTemplate.queryForObject(
            "select * from archetype_info where id=?",
            this::mapRowToArchetypeInfo, id);
  }

  private ArchetypeInfo mapRowToArchetypeInfo(ResultSet rs, int rowNum)
          throws SQLException {
    return new ArchetypeInfo(
            rs.getString("concept"),
            rs.getString("archetype_id"),
            rs.getString("description"),
            rs.getString("status"),
            rs.getString("created_at"),
            rs.getString("updated_at"),
            rs.getString("asset_cid"),
            rs.getString("ckm_path"),
            rs.getString("adl_path"),
            rs.getString("xml_path"));
  }
}
