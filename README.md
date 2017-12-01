[![Build Status](https://travis-ci.org/ncredinburgh/tomcat-external-propertysource.svg?branch=master)](https://travis-ci.org/ncredinburgh/tomcat-external-propertysource)

Tomcat External PropertySource
==============================

This library provides a [Tomcat Property Source](https://tomcat.apache.org/tomcat-7.0-doc/api/org/apache/tomcat/util/IntrospectionUtils.PropertySource.html) that reads property values from an external file. 


Getting Started
---------------
### Download the Library
* Download the latest version of the library using the link in the Maven Central badge at the top of this page.

### Install the library
* Copy the JAR file to the folder `${TOMCAT_HOME}/lib`

### Configure Tomcat
* Configure Tomcat to use the property source provided by the library and specify the filename of the property file using the following Java system properties.

```
-Dorg.apache.tomcat.util.digester.PROPERTY_SOURCE=com.github.ncredinburgh.tomcat.ExternalPropertySource"
-Dcom.github.ncredinburgh.tomcat.ExternalPropertySource.FILENAME=tomcat.properties
```

*Replace the value `tomcat.properties` with the path of your file.* 

If a relative path is specified it will be resolved against the current working directory (system property `user.dir`).

For more information on Tomcat property replacement and other system properties see the [Tomcat Configuration Reference](http://tomcat.apache.org/tomcat-7.0-doc/config/systemprops.html).

Contributing
------------

All contributions are welcome. Just fork this repository and send us a merge request.  Just make sure your code meets the following requirements:

* You follow the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
* All the unit tests pass when running `mvn test`

Releases
--------

-
