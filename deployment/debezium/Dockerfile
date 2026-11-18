ARG DEBEZIUM_VERSION=3.7.1.Final
ARG ORACLE_JDBC_VERSION=23.26.4.0.0
ARG POSTGRES_JDBC_VERSION=42.7.14

# ---------- Stage 1: download plugins ----------
FROM alpine:3.20 AS downloader
ARG DEBEZIUM_VERSION
ARG ORACLE_JDBC_VERSION
ARG POSTGRES_JDBC_VERSION

RUN apk add --no-cache curl tar gzip

RUN mkdir -p /out/debezium-oracle /out/debezium-jdbc \
 && curl -fSL --retry 3 \
      "https://repo.maven.apache.org/maven2/io/debezium/debezium-connector-oracle/${DEBEZIUM_VERSION}/debezium-connector-oracle-${DEBEZIUM_VERSION}-plugin.tar.gz" \
    | tar -xz --strip-components=1 -C /out/debezium-oracle \
 && curl -fSL --retry 3 \
      "https://repo.maven.apache.org/maven2/io/debezium/debezium-connector-jdbc/${DEBEZIUM_VERSION}/debezium-connector-jdbc-${DEBEZIUM_VERSION}-plugin.tar.gz" \
    | tar -xz --strip-components=1 -C /out/debezium-jdbc \
 && curl -fSL --retry 3 \
      "https://repo.maven.apache.org/maven2/com/oracle/database/jdbc/ojdbc11/${ORACLE_JDBC_VERSION}/ojdbc11-${ORACLE_JDBC_VERSION}.jar" \
      -o /out/debezium-oracle/ojdbc11.jar \
 && curl -fSL --retry 3 \
      "https://repo.maven.apache.org/maven2/org/postgresql/postgresql/${POSTGRES_JDBC_VERSION}/postgresql-${POSTGRES_JDBC_VERSION}.jar" \
      -o /out/debezium-jdbc/postgresql.jar

# ---------- Stage 2: Kafka Connect ----------
FROM confluentinc/cp-kafka-connect:8.4.1

COPY --from=downloader /out/debezium-oracle /usr/share/java/debezium-oracle
COPY --from=downloader /out/debezium-jdbc   /usr/share/java/debezium-jdbc