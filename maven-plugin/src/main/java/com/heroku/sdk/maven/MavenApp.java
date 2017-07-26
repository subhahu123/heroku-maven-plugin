package com.heroku.sdk.maven;

import com.heroku.sdk.deploy.App;
import com.heroku.sdk.maven.executor.ListDependencies;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MavenApp extends App {

  private Log log;

  private boolean isUploadProgressEnabled;

  public MavenApp(String name, File rootDir, File targetDir, Log log, boolean logProgress) {
    this(name, rootDir, targetDir, new ArrayList<String>(), log, logProgress);
  }

  public MavenApp(String name, File rootDir, File targetDir, List<String> buildpacks, Log log, boolean logProgress) {
    super("heroku-maven-plugin", name, rootDir, targetDir, buildpacks);
    this.log = log;
    this.isUploadProgressEnabled = logProgress;
  }

  @Override
  public void logUploadProgress(Long uploaded, Long contentLength) {
    logInfo("[" + uploaded + "/" + contentLength + "]");
  }

  @Override
  public Boolean isUploadProgressEnabled() {
    return isUploadProgressEnabled;
  }

  @Override
  protected void prepare(List<File> includedFiles, Map<String, String> processTypes) throws IOException {
    super.prepare(includedFiles, processTypes);

    File appTargetDir = new File (getAppDir(), "target");
    FileUtils.forceMkdir(appTargetDir);
    FileUtils.copyFile(
            new File(getTargetDir(), ListDependencies.FILENAME),
            new File(appTargetDir, ListDependencies.FILENAME));
    FileUtils.copyFile(new File(getRootDir(), "pom.xml"), new File(getAppDir(), "pom.xml"));
  }

  public void logInfo(String message) {
    log.info(message);
  }

  public void logDebug(String message) {
    log.debug(message);
  }

  public void logWarn(String message) {
    log.warn(message);
  }

}
