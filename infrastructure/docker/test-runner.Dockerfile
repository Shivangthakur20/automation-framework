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
# Set working directory
# -----------------------------
WORKDIR /app

# -----------------------------
# Copy parent POM
# -----------------------------
COPY pom.xml .

# -----------------------------
# Copy module POMs (for caching)
# -----------------------------
COPY framework-core/pom.xml framework-core/
COPY web-ui/pom.xml web-ui/
COPY api/pom.xml api/
COPY perf-gatling/pom.xml perf-gatling/
COPY e2e/pom.xml e2e/

# -----------------------------
# Download dependencies
# -----------------------------
RUN mvn -B -q \
    -Dmaven.test.skip=true \
    dependency:go-offline

# -----------------------------
# Copy full source code
# -----------------------------
COPY . .

# -----------------------------
# Default command
# -----------------------------
CMD ["mvn","-B","test"]