FROM gradle:8.12.0-jdk21 AS BUILDER
ENV GRADLE_USER_HOME=/cache
ENV WORKDIR=/usr/src/app
WORKDIR $WORKDIR
RUN --mount=type=bind,target=.,rw \
    --mount=type=cache,target=$GRADLE_USER_HOME \
    ./gradlew -i bootJar --stacktrace && \
    mv $WORKDIR/build/libs/modelfirst.jar /modelfirst.jar
RUN echo "Cache stage completed" && ls -la /modelfirst.jar

FROM bellsoft/liberica-openjdk-alpine:21 AS RUNNER
ENV WORKDIR=/usr/src/app
WORKDIR $WORKDIR
EXPOSE 8080
RUN mkdir -p /var/log
RUN apk add -q tzdata && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone
COPY --from=BUILDER /modelfirst.jar ./modelfirst.jar
CMD java --add-opens java.base/java.lang=ALL-UNNAMED -jar ./modelfirst.jar
