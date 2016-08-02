FROM maven:3.3.9-jdk-8

# g++ and boost are required by DTrack SDK
RUN apt-get update && apt-get install -y --no-install-recommends gcc g++ libboost-dev

RUN mkdir -p /usr/src/aida
WORKDIR /usr/src/aida

ADD . /usr/src/aida

ARG MAVEN_LOCAL_REPO=/usr/share/m2
ENV MAVEN_LOCAL_REPO "${MAVEN_LOCAL_REPO}"
RUN mkdir -p "$MAVEN_LOCAL_REPO"

RUN for d in deps/*; do \
      if [ -d "$d" ]; then \
        (cd "$d"; mvn -Dmaven.repo.local="$MAVEN_LOCAL_REPO" clean install) || exit 1; \
      fi; \
    done

RUN mvn -Dmaven.repo.local="$MAVEN_LOCAL_REPO" clean package install
CMD mvn -Dmaven.repo.local="$MAVEN_LOCAL_REPO" tomcat7:run
