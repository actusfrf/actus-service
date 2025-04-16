
FROM eclipse-temurin:17-jdk-jammy

# Install dependencies: bash, curl, unzip, Maven, and Gradle
ENV GRADLE_VERSION=8.4 \
    MAVEN_VERSION=3.9.4 \
    GRADLE_HOME=/opt/gradle \
    PATH=/opt/gradle/bin:/opt/maven/bin:$PATH \
    MAVEN_HOME=/opt/maven

RUN apt-get update && \
    apt-get install -y bash curl unzip && \
    # Install Maven
    curl -sLO https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.zip && \
    unzip apache-maven-${MAVEN_VERSION}-bin.zip && \
    mv apache-maven-${MAVEN_VERSION} /opt/maven && \
    rm apache-maven-${MAVEN_VERSION}-bin.zip && \
    # Install Gradle
    curl -sLO https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip && \
    unzip gradle-${GRADLE_VERSION}-bin.zip && \
    mkdir /opt/gradle && \
    mv gradle-${GRADLE_VERSION}/* /opt/gradle/ && \
    rm gradle-${GRADLE_VERSION}-bin.zip && \
    # Clean up
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Set the working directory inside the container
WORKDIR /app

# Step 1: Copy and build the actus-core project using Maven
COPY actus-core /app/actus-core
WORKDIR /app/actus-core
RUN mvn clean install
RUN rm -rf src

# Step 2: Copy and build the risksrv3 project using Gradle
COPY . /app/actus-service
WORKDIR /app/actus-service
RUN gradle clean build

# Expose port 8083 as default
EXPOSE 8083

# Default command, can be adjusted as needed
CMD ["gradle", "bootRun"]