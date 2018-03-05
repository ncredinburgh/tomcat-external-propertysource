package com.github.ncredinburgh.tomcat;

import static com.github.ncredinburgh.tomcat.Defaults.DEFAULT_CIPHER_SPEC;
import static com.github.ncredinburgh.tomcat.encryption.PropertiesUtil.isPropertiesFile;
import static java.nio.file.Files.readAllBytes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.xml.bind.DatatypeConverter;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.IntrospectionUtils;

import com.github.ncredinburgh.tomcat.encryption.Cipher;
import com.github.ncredinburgh.tomcat.encryption.FileEncryptor;
import com.github.ncredinburgh.tomcat.encryption.JCACipher;


/**
 * Enables Tomcat to read properties from an external Java properties file and substitute them using 
 * <code>${property}</code> syntax in Tomcat configuration files.
 * 
 * Usage:
 * 
 * Set the following Java system properties on Tomcat startup:
 * <dl>
 * 	<dt>org.apache.tomcat.util.digester.PROPERTY_SOURCE</dt>
 * 	<dd>Set to to the value <code>com.github.ncredinburgh.tomcat.ExternalPropertySource</code>
 * 	See <a href="http://tomcat.apache.org/tomcat-7.0-doc/config/systemprops.html#Property_replacements">Tomcat documentation</a>.</dd>
 *
 * 	<dt>com.github.ncredinburgh.tomcat.ExternalPropertySource.FILENAME</dt>
 * 	<dd>The path of the external Java properties file. See Relative Paths.</dd>
 * </dl>
 * 
 * <h3>Encryption Support</h3>
 * 
 * The property source can read files from an encrypted Java properties file. To enable encryption support
 * set the following optional Java system properties on Tomcat startup:
 *
 * <dl>
 * 	<dt>com.github.ncredinburgh.tomcat.ExternalPropertySource.KEYFILE</dt>
 * 	<dd>The path to a valid decryption key file. See Relative Paths.</dd>
 * 
 * 	<dt>com.github.ncredinburgh.tomcat.ExternalPropertySource.CIPHER</dt>
 * 	<dd>A cipher spec and should be of the form <code>algorithm/mode/padding</code>. See {@link javax.crypto.Cipher}. 
 * 	If no <code>CIPHER</code> property is specified, the default cipher is used: {@link Defaults#DEFAULT_CIPHER_SPEC}.</dd>
 * 
 *  <dt>com.github.ncredinburgh.tomcat.ExternalPropertySource.CIPHERIV</dt>
 *  <dd>The initialization vector required when using some block cipher modes in base64 encoding.</dd>
 * </dl>
 * 
 * <h3>Relative Paths</h3>
 * 
 * If a relative path is specified in any system property value it will be resolved 
 * against the current working directory (system property <code>user.dir</code>).
 */
public class ExternalPropertySource implements IntrospectionUtils.PropertySource {
	public static final String FILENAME = ExternalPropertySource.class.getName() + ".FILENAME";
	public static final String KEYFILE = ExternalPropertySource.class.getName() + ".KEYFILE";
	public static final String CIPHER = ExternalPropertySource.class.getName() + ".CIPHER";
	public static final String CIPHERIV = ExternalPropertySource.class.getName() + ".CIPHERIV";
	private static final Log LOGGER = LogFactory.getLog(ExternalPropertySource.class);
	private final Properties properties = new Properties();
	
	public ExternalPropertySource() throws IOException {
		LOGGER.debug("Creating ExternalPropertySource");
		LOGGER.debug("Relative filenames are resolved against USER.DIR which is: " + System.getProperty("user.dir"));
		
		String propertyFilename = System.getProperty(FILENAME);
		if (propertyFilename != null) {
			File propertyFile = new File(propertyFilename);
			loadProperties(propertyFile);				
		} else {
			LOGGER.debug("No properties file specified");
		}
	}

	public String getProperty(String key) {
		if (properties == null)  return null;
		
		return properties.getProperty(key);
	}

	private void loadProperties(File propertyFile) throws IOException {
		LOGGER.info("Reading properties from external file: " + propertyFile.getAbsolutePath());
		
		String keyFilename = System.getProperty(KEYFILE);
		if (keyFilename == null) {
			loadClearProperties(propertyFile);
		} else {
			loadEncryptedProperties(propertyFile, new File(keyFilename));
		}
		if (properties.size() == 0) {
			LOGGER.warn("No properties were found in external file");
		}
	}

	private void loadClearProperties(File propertyFile) throws IOException {
		if (!isPropertiesFile(new FileReader(propertyFile))) {
			LOGGER.warn("The external file does not look like a Java properties file");
		}
		properties.load(new FileInputStream(propertyFile));
	}

	private void loadEncryptedProperties(File propertyFile, File keyFile) throws IOException, FileNotFoundException {
		if (isPropertiesFile(new FileReader(propertyFile))) {
			LOGGER.warn("The external file does not look like an encrypted file");
		}

		LOGGER.debug("Using decryption key file: " + keyFile.getAbsolutePath());
		String cipherSpec = System.getProperty(CIPHER, DEFAULT_CIPHER_SPEC);
		LOGGER.debug("Using cipher: " + cipherSpec);		
		String cipherIV = System.getProperty(CIPHERIV);
		LOGGER.debug("Using cipher iv: " + cipherIV);
	
		Cipher cipher = new JCACipher(cipherSpec, readAllBytes(keyFile.toPath()), decodeIV(cipherIV));
		FileEncryptor decryptor = new FileEncryptor(cipher);

		properties.load(decryptor.decryptFile(propertyFile));
	}

	private byte[] decodeIV(String iv) {
		if (iv == null) return null;
		return DatatypeConverter.parseBase64Binary(iv);
	}
}
