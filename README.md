# Java Refresher

Familiarizing myself (again) with Java modules and the exact mechanics of Java dependency management (classpath, slim JARs)

## Modules
* which classes can my code access from libraries I use - explicit declaration (e.g Guava classes)
* which classes can be accessed from my code - e.g from an unnamed module (stripped main-method) in the Docker JRE

## Commands
* Load dependencies: ```mvn dependency:copy-dependencies -DincludeScope=runtime```
* Compile: ```mvn compile```
* Run Classes: ```java -cp "target/classes/:target/dependency/*" Main```
* Package: ```mvn package```
* Run Jar: ```java -cp "target/javarunner-1.0.jar:target/dependency/*" Main```
* Run Class via Maven: ```mvn exec:exec```

### Docker/Finch:

```
docker build -t javarunner .
docker run --rm javarunner
```
