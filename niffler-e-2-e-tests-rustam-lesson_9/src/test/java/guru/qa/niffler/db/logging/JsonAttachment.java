package guru.qa.niffler.db.logging;

import guru.qa.niffler.model.Spend9Json;
import io.qameta.allure.attachment.AttachmentData;

public class JsonAttachment implements AttachmentData {

  private final String name;
  private final Spend9Json text;

  public JsonAttachment(String name, Spend9Json text) {
    this.name = name;
    this.text = text;
  }

  @Override
  public String getName() {
    return name;
  }

  public Spend9Json getText() {
    return text;
  }

}
