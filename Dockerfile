FROM mcr.microsoft.com/playwright/java:v1.50.0-noble

# Install dependencies
RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y maven

# Copy the project
Copy . /app

# Set the working directory
WORKDIR /app

# Build the project
RUN mvn clean install

# Run the tests
CMD ["mvn", "clean", "test"]
