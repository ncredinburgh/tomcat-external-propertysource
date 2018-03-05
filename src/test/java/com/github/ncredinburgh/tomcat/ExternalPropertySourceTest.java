package com.github.ncredinburgh.tomcat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ExternalPropertySourceTest {

	private ExternalPropertySource testee;
	
	@Before
	public void resetProperties() {
		System.clearProperty(ExternalPropertySource.FILENAME);
	}
	
	@Test
	public void shouldDoNothingWhenFilenameNotSet() throws Exception {
		testee = new ExternalPropertySource();
	}
	
	@Test
	public void shouldReturnPropertyValueIfInFile() throws Exception {
		String filename = this.getClass().getResource("/testfile.properties").getFile(); 
		System.setProperty(ExternalPropertySource.FILENAME, filename);
		testee = new ExternalPropertySource();
		String actualValue = testee.getProperty("aProperty");
		Assert.assertEquals("expectedValue", actualValue);
	}
	
	@Test
	public void shouldReturnNullIfPropertyValueNotInFile() throws Exception {
		String filename = this.getClass().getResource("/testfile.properties").getFile(); 
		System.setProperty(ExternalPropertySource.FILENAME, filename);
		testee = new ExternalPropertySource();
		String actualValue = testee.getProperty("anotherProperty");
		Assert.assertNull(actualValue);
	}
}
