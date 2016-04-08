package ee.degeetia.dumonitor.common.config.properties;

import ee.degeetia.dumonitor.common.util.IOUtil;
import ee.degeetia.dumonitor.common.util.ResourceUtil;
import ee.degeetia.dumonitor.common.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class loads properties from .properties files into {@link Property} values.
 */
public class PropertyLoader {

  private static final Logger LOG = LogManager.getLogger(PropertyLoader.class);

  /**
   * Loads properties into {@link Property} values.
   *
   * @param filenames Filenames (relative to classpath) that specify where to load properties from. Filenames are
   *                  ordered by priority (descending). Properties from higher priority files overwrite those from lower
   *                  priority files.
   * @throws IOException if an I/O error occurs when loading the properties
   */
  public void loadProperties(String... filenames) throws IOException {
    Properties properties = new Properties();

    for (int i = filenames.length - 1; i >= 0; i--) {
      InputStream propertiesFile = ResourceUtil.getClasspathResourceAsStream(filenames[i]);
      if (propertiesFile == null) {
        continue;
      }
      LOG.info("Loading properties from classpath resource {}", filenames[i]);
      try {
        properties.load(propertiesFile);
      } finally {
        IOUtil.close(propertiesFile);
      }
    }

    for (Property property : Property.values()) {
      String value = properties.getProperty(property.getKey());
      if (!StringUtil.isEmpty(value)) {
        LOG.info("Loaded property {} = {}", property.getKey(), value);
        property.setValue(value);
      }
    }
  }

}
