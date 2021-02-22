package cn.ac.bmi.cloudphr.ckmirror;

import cn.ac.bmi.cloudphr.ckmirror.repository.ArchetypeInfoRepository;
import cn.ac.bmi.cloudphr.ckmirror.repository.TemplateInfoRepository;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import me.tongfei.progressbar.ProgressBar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class App implements CommandLineRunner {
  @Autowired
  private DataSource dataSource;

  @Autowired
  private ArchetypeInfoRepository archetypeInfoRepository;

  @Autowired
  private TemplateInfoRepository templateInfoRepository;

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    showConnection();
    log.info(System.getProperty("user.dir"));
    updateDatabase();
  }

  private void showConnection() throws SQLException {
    log.info(dataSource.toString());
    Connection conn = dataSource.getConnection();
    log.info(conn.toString());
    conn.close();
  }

  public void updateDatabase() {
    CkmHelper.parseCkmRepository();
    updateArchetypes();
    updateTemplates();
  }

  public void updateArchetypes() {
    log.info("Archetypes update process started...");
    for (ArchetypeInfo info : ProgressBar.wrap(CkmHelper.archetypeInfoList, "Traversing")) {
      ArchetypeInfo archetypeInfo = archetypeInfoRepository.findByArchetypeId(info.getArchetypeId());
      if (archetypeInfo == null || !info.getUpdatedAt().equals(archetypeInfo.getUpdatedAt())) {
        CkmHelper.downloadFromCkm(info.getAdlPath(), info.getArchetypeId(), "adl");
        if (archetypeInfo != null) {
          archetypeInfoRepository.deleteByArchetypeId(info.getArchetypeId());
        }
        archetypeInfoRepository.insertOne(info);
      }
    }
    log.info("Archetypes update process finished...");
  }

  public void updateTemplates() {
    log.info("Templates update process started...");
    for (TemplateInfo info : ProgressBar.wrap(CkmHelper.templateInfoList, "Traversing")) {
      TemplateInfo templateInfo = templateInfoRepository.findByTemplateId(info.getTemplateId());
      if (templateInfo == null || !info.getUpdatedAt().equals(templateInfo.getUpdatedAt())) {
        CkmHelper.downloadFromCkm(info.getAdlPath(), info.getTemplateId(), "oet");
        if (templateInfo != null) {
          templateInfoRepository.deleteByTemplateId(info.getTemplateId());
        }
        templateInfoRepository.insertOne(info);
      }
    }
    log.info("Templates update process finished...");
  }
}
