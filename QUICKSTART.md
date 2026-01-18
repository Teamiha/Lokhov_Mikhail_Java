# Quick Start

## Prerequisites: Java 11 Installation

**This project requires Java 11.** If you don't have Java 11 installed, follow these steps:

### Installing Java 11 on Mac

1. **Install Java 11 via Homebrew:**
   ```bash
   brew install openjdk@11
   ```

2. **Link Java 11:**
   ```bash
   sudo ln -sfn /opt/homebrew/opt/openjdk@11/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-11.jdk
   ```

3. **Set JAVA_HOME for current session (REQUIRED!):**
   ```bash
   export JAVA_HOME=$(/usr/libexec/java_home -v 11)
   ```

4. **Verify installation:**
   ```bash
   java -version
   mvn -version
   ```
   Both should show Java 11. If `mvn -version` shows Java 25, `JAVA_HOME` is not set correctly.

5. **Make it permanent (add to ~/.zshrc):**
   ```bash
   echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 11)' >> ~/.zshrc
   source ~/.zshrc
   ```

**⚠️ IMPORTANT:** You MUST set `JAVA_HOME` before running Maven commands, otherwise Maven will use the default Java (which might be Java 25) and compilation will fail!

**Note:** If you have multiple Java versions installed, you can switch between them using:
```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 11)  # For Java 11
export JAVA_HOME=$(/usr/libexec/java_home -v 25)  # For Java 25
```

## Running the Application on Mac

### Option 1: Using Maven Wrapper (Recommended) ⭐

**Before running, make sure JAVA_HOME is set to Java 11:**
```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 11)
```

Then run:
```bash
./mvnw spring-boot:run
```

**Note:** On the first run, Maven Wrapper will automatically download Maven (~10MB), which may take 1-2 minutes. Subsequent runs will be fast.

**If the command hangs:** This is normal on the first run - wait 1-2 minutes while Maven is being downloaded.

### Option 2: Installing Maven via Homebrew (Optional)

If you want to install Maven globally:

```bash
brew install maven
```

**Before running, make sure JAVA_HOME is set to Java 11:**
```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 11)
```

Then use:

```bash
mvn spring-boot:run
```

### Option 3: Running from Already Compiled Project

If the project has already been compiled, you can run it directly via Java:

```bash
# First build the JAR (if it doesn't exist)
./mvnw clean package -DskipTests

# Then run
java -jar target/vacation-calculator-1.0.0.jar
```

## Accessing the Application

After starting, the application will be available at:

- **Web Interface**: http://localhost:8080/
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API**: http://localhost:8080/calculate

## Stopping the Application

Press `Ctrl+C` in the terminal where the application is running.

## Troubleshooting

### Maven Wrapper Hangs on First Run

This is normal - it's downloading Maven. Wait 1-2 minutes. If the download doesn't complete:

1. Check your internet connection
2. Try installing Maven manually: `brew install maven` (if Homebrew is installed)
3. Then use `mvn` instead of `./mvnw`

### Port 8080 is Busy

Change the port in `src/main/resources/application.properties`:

```properties
server.port=8081
```

### Java Version Issues

**If you get compilation errors related to Lombok**, it's likely because you're using Java 25 instead of Java 11.

**Solution:**

1. **Make sure Java 11 is installed** (see Prerequisites section above)

2. **Set JAVA_HOME to Java 11 before running Maven:**
   ```bash
   export JAVA_HOME=$(/usr/libexec/java_home -v 11)
   mvn clean compile
   ```

3. **Verify you're using Java 11:**
   ```bash
   java -version
   mvn -version
   ```
   Both should show Java 11.

**If Java 11 is not installed**, follow the installation instructions in the Prerequisites section above.
