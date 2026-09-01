FROM amazoncorretto:25-alpine AS appbuilder
WORKDIR /build
RUN apk add --no-cache maven

COPY ./pom.xml .
RUN mvn dependency:copy-dependencies -DincludeScope=runtime -DoutputDirectory=target/lib

COPY ./src ./src
RUN mvn package -DskipTests

FROM amazoncorretto:25-alpine AS jrebuilder
RUN apk add --no-cache binutils
WORKDIR /build

COPY --from=appbuilder /build/target/lib ./lib
COPY --from=appbuilder /build/target/javarunner-1.0.jar ./app.jar

RUN $JAVA_HOME/bin/jlink \
    --module-path /usr/lib/jvm/default-jvm/jmods:./lib:./app.jar \
    --add-modules com.brakid.runner \
    --strip-debug \
    --no-man-pages \
    --no-header-files \
    --compress=zip-9 \
    --output /java-jre

FROM alpine:latest
COPY --from=jrebuilder /java-jre /opt/java
ENV JAVA_HOME=/opt/java
ENV PATH="$JAVA_HOME/bin:$PATH"

RUN addgroup -S appgroup && adduser -S appuser -G appgroup -u 10001

WORKDIR /app
COPY --from=appbuilder /build/target/lib ./lib
COPY --from=appbuilder /build/target/javarunner-1.0.jar ./app.jar

USER 10001

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-XX:+ExitOnOutOfMemoryError", "-XX:+UseCompactObjectHeaders", "-cp", "app.jar:lib/*", "Main"]
