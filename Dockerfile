FROM openjdk:11 as build-stage
ARG SBT_VERSION=1.3.7
WORKDIR /build

# Install sbt
RUN \
  curl -L -o sbt-$SBT_VERSION.deb https://dl.bintray.com/sbt/debian/sbt-$SBT_VERSION.deb && \
  dpkg -i sbt-$SBT_VERSION.deb && \
  rm sbt-$SBT_VERSION.deb && \
  apt-get update && \
  apt-get install -y sbt

ADD . /build
RUN sbt assembly

FROM openjdk:11
WORKDIR /app
RUN apt-get update && apt-get install -y libxrender1 libxtst6 libxi6
COPY --from=build-stage /build/resources ./resources
COPY --from=build-stage /build/target/scala-2.13/ScotlandYard.jar ./
EXPOSE 8080
CMD java -jar /app/ScotlandYard.jar