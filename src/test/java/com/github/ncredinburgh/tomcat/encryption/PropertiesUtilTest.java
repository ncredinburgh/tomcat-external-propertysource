package com.github.ncredinburgh.tomcat.encryption;

import java.io.StringReader;

import org.junit.Assert;
import org.junit.Test;


public class PropertiesUtilTest {
	
	@Test
	public void shoudReturnTrueWhenEmpty() throws Exception {
		String content = new String();
		
		boolean result = PropertiesUtil.isPropertiesFile(new StringReader(content));
		
		Assert.assertEquals(true, result);
	}
	
	@Test
	public void shoudReturnTrueWhenCommentHash() throws Exception {
		String content = new String("# This is a comment");
		
		boolean result = PropertiesUtil.isPropertiesFile(new StringReader(content));
		
		Assert.assertEquals(true, result);	
	}
	
	@Test
	public void shoudReturnTrueWhenCommentBang() throws Exception {
		String content = new String("! This is a comment");
		
		boolean result = PropertiesUtil.isPropertiesFile(new StringReader(content));
		
		Assert.assertEquals(true, result);	
	}
	
	@Test
	public void shoudReturnTrueWhenEqualsFormProperty() throws Exception {
		String content = new String("name=value");
		
		boolean result = PropertiesUtil.isPropertiesFile(new StringReader(content));
		
		Assert.assertEquals(true, result);	
	}
	
	@Test
	public void shoudReturnTrueWhenSpacedEqualsFormProperty() throws Exception {
		String content = new String("name = value");
		
		boolean result = PropertiesUtil.isPropertiesFile(new StringReader(content));
		
		Assert.assertEquals(true, result);	
	}
	
	@Test
	public void shoudReturnTrueWhenColonFormProperty() throws Exception {
		String content = new String("name:value");
		
		boolean result = PropertiesUtil.isPropertiesFile(new StringReader(content));
		
		Assert.assertEquals(true, result);	
	}
	
	@Test
	public void shoudReturnTrueWhenSpacedColonFormProperty() throws Exception {
		String content = new String("name : value");
		
		boolean result = PropertiesUtil.isPropertiesFile(new StringReader(content));
		
		Assert.assertEquals(true, result);	
	}
	
	@Test
	public void shoudReturnTrueWhenSpaceFormProperty() throws Exception {
		String content = new String("name value");
		
		boolean result = PropertiesUtil.isPropertiesFile(new StringReader(content));
		
		Assert.assertEquals(true, result);	
	}
	
	@Test
	public void shoudReturnFalseWhenMalformedProperty() throws Exception {
		String content = new String("name");
		
		boolean result = PropertiesUtil.isPropertiesFile(new StringReader(content));
		
		Assert.assertEquals(false, result);	
	}
}
