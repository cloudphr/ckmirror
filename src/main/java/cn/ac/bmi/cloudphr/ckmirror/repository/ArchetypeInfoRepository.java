package cn.ac.bmi.cloudphr.ckmirror.repository;

import cn.ac.bmi.cloudphr.ckmirror.ArchetypeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
    try {
      return jdbcTemplate.queryForObject(
              "select * from archetype_info where archetype_id=?",
              this::mapRowToArchetypeInfo, id);
    } catch (EmptyResultDataAccessException e) {
      return null;
    }
  }

  public ArchetypeInfo deleteByArchetypeID(String id) {
    ArchetypeInfo archetypeInfo = findByArchetypeID(id);
    jdbcTemplate.update("delete from archetype_info where archetype_id=?", id);
    return archetypeInfo;
  }

  public ArchetypeInfo insertOne(ArchetypeInfo archetypeInfo) {
    jdbcTemplate.update("insert into archetype_info " +
            "(concept, archetype_id, description, status, created_at, updated_at, asset_cid, ckm_path, adl_path, xml_path)" +
            "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
            archetypeInfo.getConcept(),
            archetypeInfo.getArchetypeID(),
            archetypeInfo.getDescription(),
            archetypeInfo.getStatus(),
            archetypeInfo.getCreatedAt(),
            archetypeInfo.getUpdatedAt(),
            archetypeInfo.getAssetCid(),
            archetypeInfo.getCkmPath(),
            archetypeInfo.getAdlPath(),
            archetypeInfo.getXmlPath());
    return archetypeInfo;
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
