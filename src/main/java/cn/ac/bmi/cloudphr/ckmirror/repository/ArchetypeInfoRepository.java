package cn.ac.bmi.cloudphr.ckmirror.repository;

import cn.ac.bmi.cloudphr.ckmirror.ArchetypeInfo;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ArchetypeInfoRepository {
  private JdbcTemplate jdbcTemplate;

  @Autowired
  public ArchetypeInfoRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public ArchetypeInfo findByArchetypeId(String id) {
    try {
      return jdbcTemplate.queryForObject(
              "SELECT * FROM archetype_info WHERE archetype_id=?",
              this::mapRowToArchetypeInfo, id);
    } catch (EmptyResultDataAccessException e) {
      return null;
    }
  }

  public ArchetypeInfo deleteByArchetypeId(String id) {
    ArchetypeInfo archetypeInfo = findByArchetypeId(id);
    jdbcTemplate.update("DELETE FROM archetype_info WHERE archetype_id=?", id);
    return archetypeInfo;
  }

  public ArchetypeInfo insertOne(ArchetypeInfo archetypeInfo) {
    jdbcTemplate.update("INSERT INTO archetype_info "
        + "(concept, archetype_id, description, status,"
        + " created_at, updated_at, asset_cid, ckm_path, adl_path, xml_path)"
        + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
        archetypeInfo.getConcept(),
        archetypeInfo.getArchetypeId(),
        archetypeInfo.getDescription(),
        archetypeInfo.getStatus(),
        archetypeInfo.getCreatedAt(),
        archetypeInfo.getUpdatedAt(),
        archetypeInfo.getAssetCid(),
        archetypeInfo.getCkmPath(),
        archetypeInfo.getAdlPath(),
        archetypeInfo.getXmlPath()
    );
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
