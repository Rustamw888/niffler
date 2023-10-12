package guru.qa.niffler.config;

public class DockerConfig implements Config {

  static final DockerConfig config = new DockerConfig();

  private DockerConfig() {
  }

  @Override
  public String databaseHost() {
    return "niffler-all-db";
  }

  @Override
  public String getSpendUrl() {
    return "niffler-spend";
  }

  @Override
  public String getCategoryUrl() {
    return "niffler-spend";
  }

  @Override
  public String getFrontUrl() {
    return "http://niffler-frontend:3000/";
  }

  @Override
  public String getAuthUrl() {
    return "http://niffler-auth:9000/";
  }

}
