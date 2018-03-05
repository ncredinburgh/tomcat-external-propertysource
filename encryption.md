Tomcat External PropertySource
==============================

Encryption Support
------------------

This tomcat property source allows property values to be stored in a property file that is encrypted.  By default the cipher used is AES in ECB mode with PKCS#5 padding. The property source uses the Java Cryptography Architecture (JCA) for its cipher implementations. The cipher can be overridden with a system property. 

### Specifying the decryption key

In order for the property source to decrypt the property file the decryption key is required.  The property source expects a key compatible with the cipher in a file and the location of that file is specified using the system property `com.github.ncredinburgh.tomcat.ExternalPropertySource.KEYFILE`.

For example, in the Tomcat file `setenv.sh` the key file `conf/secret.key` can be configured as follows.

```
CATALINA_OPTS="$CATALINA_OPTS -Dcom.github.ncredinburgh.tomcat.ExternalPropertySource.KEYFILE=conf/test.key"
```

> If a decryption key file is not specified then the property source treats property file as a regular property file. If the property file is encrypted then it will not be in the format of a valid Java properties file and no property values will resolved.

### Overridding the default cipher

If the property file was encypted using a cipher other than the default then the correct cipher details are required.  The default cipher can be overridden using the system property `com.github.ncredinburgh.tomcat.ExternalPropertySource.CIPHER`.

The cipher details (algorithm, mode & padding) are specified using the format defined in [`javax.crypto.Cipher`](https://docs.oracle.com/javase/7/docs/api/javax/crypto/Cipher.html).

For example, in the Tomcat file `setenv.sh` the cipher DES in CBC mode with PKCS#5 padding can be specified as follows:

```
CATALINA_OPTS="$CATALINA_OPTS -Dcom.github.ncredinburgh.tomcat.ExternalPropertySource.CIPHER=DES/CBC/PKCS5Padding"
```

> Consult your JVM vendor documentation for details on which ciphers are supported by your JVM.

### Speifying an initialization vector

Some cipher modes require an *initialization vector* or IV.  If a cipher mode is specified that requires an IV (e.g. CBC) then an IV must be provided using the system property `com.github.ncredinburgh.tomcat.ExternalPropertySource.CIPHERIV`. Otherwise an error will result.

A cipher IV is binary data and when provided as a system property must be encoded in [Base64](https://tools.ietf.org/html/rfc4648) encoding.

For example, in the Tomcat file `setenv.sh` cipher IV can be specified as follows:

```
CATALINA_OPTS="$CATALINA_OPTS -Dcom.github.ncredinburgh.tomcat.ExternalPropertySource.CIPHERIV=PQ+xjDtWW5SVSGvlw4XtUA=="
```

> Cipher IVs do not need to be kept secret (See [Wikipedia](https://en.wikipedia.org/wiki/Block_cipher_mode_of_operation#Initialization_vector_(IV))).  However, it is recommended to change the IV every time the same key is used to encrypt a property file - even when re-encrypting the same file after making a change.

Command Line Utility
--------------------

For convenience the property source provides a command line utility for performing common required encryption operations. This utility can create an encryption key file, encrypt a property file and decrypt a property file.

You can execute the utility using the command line:

```
$ java -jar tomcat-external-propertysource-1.1.jar <command> <options>
``` 

The following commands are supported:

* `listKeyGenerators`

 Displays the names of the algorithms installed on this JVM that may be used to 
 generate keys.

* `listCiphers`

 Displays the names of the algorithms installed on this JVM that may be used to encrypt passwords.

* `generateKey <keyFilename> [<algorithm> <keySize>]`

 Generates a random encryption key into the named key file. By default the algorithm is `AES` and the key size is `128` bits. 
 
> Use `listKeyGenerators` to see which key generators are supported by your JVM.

* `encryptFile <inputFilename> <keyFilename> <outputFilename> [<algorithm/mode/padding>] [iv]`

 Encrypts the given input file using the given key file and writes to the given output file.  By default the algorithm `AES` is used in `ECB` mode with `PKCS5PADDING`. The defaults can be overridden by providing the algorithm, mode and padding through the additional argument in the format `algorithm/mode/padding`.
 
 If a cipher mode is used that requires an IV then an IV can be specified as the final argument in base64 encoding. If an IV is required but not specified then an appropriate new random IV will be used and output to the console.

> Use `listCiphers` to see which ciphers are supported by your JVM.

* `decryptFile <inputFilename> <keyFilename> <outputFilename> [<algorithm/mode/padding>] [iv]`

 Decrypts the given input file using the given key file and writes to the given output file.  By default the algorithm `AES` is used in `ECB` mode with `PKCS5PADDING`. The defaults can be overridden by providing the algorithm, mode and padding through the additional argument in the format `algorithm/mode/padding`.

 If a cipher mode is used that requires an IV then the final argument must specify the IV using base64 encoding.

> Use `listCiphers` to see which ciphers are supported by your JVM.

