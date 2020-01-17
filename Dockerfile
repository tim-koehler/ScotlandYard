FROM hseeberger/scala-sbt:graalvm-ce-19.3.0-java11_1.3.7_2.13.1
WORKDIR /ScotlandYard
ADD . /ScotlandYard
CMD sbt run