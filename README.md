# Get Started

[Square SDK Guide](https://developer.squareup.com/docs/sdks/java/using-java-sdk)
# Working in this repo

Copy the example config file, and place your Square Access Token inside of the new file. Set your Access Token here

```
$ cp src/main/resources/config.properties.example src/main/resources/config.properties
```

Compile the program
```
$ mvn package -DskipTests
```

Execute the code
```
$ mvn exec:java -Dexec.mainClass="com.square.examples.Quickstart"
```



