package com.github.ncredinburgh.tomcat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.IntrospectionUtils;


/**
 * Enables Tomcat to read properties from an external file and substitute them using <code>${property}</code> 
 * syntax in Tomcat configuration files.
 * 
 * Usage:
 * 
 * Set the following Java system properties on Tomcat startup:
 * 
 * <code>
 * -Dorg.apache.tomcat.util.digester.PROPERTY_SOURCE=com.github.ncredinburgh.tomcat.ExternalPropertySource"
 * -Dcom.github.ncredinburgh.tomcat.ExternalPropertySource.FILENAME=tomcat.properties
 * </code>
 * 
 * The value of <code>com.github.ncredinburgh.tomcat.ExternalPropertySource.FILENAME</code> should point
 * to a valid Java properties file.
 */
public class ExternalPropertySource implements IntrospectionUtils.PropertySource {
	public static final String FILENAME = ExternalPropertySource.class.getName() + ".FILENAME";
	private static final Log LOGGER = LogFactory.getLog(ExternalPropertySource.class);
	private final Properties properties = new Properties();
	
	public ExternalPropertySource() throws IOException {
		String filename = System.getProperty(FILENAME);
		if (filename != null) {
			File propertyFile = new File(filename);
			if (propertyFile.exists()) {
				LOGGER.info("Reading configuration properties from external file: " + filename);
				properties.load(new FileInputStream(propertyFile));
			} else {
				LOGGER.warn("External configuration properties file not found: " + filename);
			}
		}
	}
	
	public String getProperty(String key) {
		if (properties == null)  return null;
		
        return properties.getProperty(key);
	}

}
