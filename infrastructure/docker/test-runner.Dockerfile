# -----------------------------
# Base Image
# -----------------------------
FROM maven:3.9.6-eclipse-temurin-11

# -----------------------------
# Metadata
# -----------------------------
LABEL maintainer="shivang"
LABEL description="Automation Test Runner Container"

# -----------------------------
# Environment
# -----------------------------
ENV MAVEN_OPTS="-Dmaven.repo.local=/root/.m2"

# -----------------------------
# Working Directory
# -----------------------------
WORKDIR /app

# -----------------------------
# Copy Parent POM
# -----------------------------
COPY pom.xml .

# -----------------------------
# Copy module POMs only
# (helps Docker cache dependencies)
# -----------------------------
COPY framework-core/pom.xml framework-core/
COPY web-ui/pom.xml web-ui/
COPY api/pom.xml api/
COPY perf-gatling/pom.xml perf-gatling/
COPY e2e/pom.xml e2e/

# -----------------------------
# Pre-download dependencies
# -----------------------------
RUN mvn -B -q \
    -Dmaven.test.skip=true \
    -Ddependency-check.skip=true \
    dependency:go-offline

# -----------------------------
# Copy full project source
# -----------------------------
COPY . .

# -----------------------------
# Default command
# -----------------------------
CMD ["mvn", "-B", "test"]