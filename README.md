# KAIMUX Community Server Plugins

KAIMUX Community Server Plugins are modular extensions designed to enhance the functionality and gameplay of the KAIMUX Community Minecraft server. They introduce unique features, mechanics, and tools that enrich the player experience, ranging from custom commands and mini-games to advanced administrative utilities. These plugins are built using Java, leveraging the Bukkit/Spigot/Paper API, and are shared openly for anyone interested in contributing or using them.

## Coding

When developing plugins for the KAIMUX Community Server, contributors are free to use any IDE of their choice, such as IntelliJ IDEA, Eclipse, or VS Code. The chosen IDE does not affect the development process as long as standard Java coding practices are followed.

Key guidelines for coding KAIMUX plugins include:

### Main Class Structure
* All new plugins must have a primary class named Main.
* If the plugin provides an API, it should be defined as a public static inner class named PluginNameAPI within the Main class.

```java
public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static class PluginNameAPI {
        // API methods here
    }
}
```

### Java Coding Practices
* Follow consistent and clean code formatting.
* Use meaningful variable and method names.
* Document your code with comments to enhance readability and maintainability.
* Handle exceptions and edge cases to ensure robust and reliable plugins.

### Compatibility
Ensure your plugin adheres to the Bukkit/Spigot/Paper API standards for compatibility with the server setup.

By adhering to these guidelines, developers can contribute high-quality plugins that seamlessly integrate with the KAIMUX Community Server and improve the player experience.

## Building Locally with Gradle

To build KAIMUX plugins locally, developers must use Gradle and meet the following requirements:

### Prerequisites

#### Java Version
* Java 21 or later is required to build the plugins. Ensure your environment is properly configured with the correct version.

#### Gradle
* Install Gradle on your system, or use the Gradle Wrapper (`./gradlew` or `gradlew.bat` for Windows) included in the project repository.

#### Project Structure
* The KAIMUX plugins project is a multi-module Gradle project, meaning it consists of multiple submodules, each representing an individual plugin.

### Building Plugins

#### Build All Plugins Together
To build all plugins at once, execute the following command from the root directory:

```
./gradlew build
```

This will compile and package all plugins into their respective JAR files, located in the `build/libs/` directory of each module.

#### Build a Specific Plugin
To build an individual plugin, navigate to the plugin's module directory and run:

```
./gradlew build
```

Alternatively, you can specify the module directly from the root directory:

```
./gradlew :plugin-module-name:build
```

#### Customizing Build Paths
If you need to adjust output paths or other build configurations, you can modify the `build.gradle` files in the root project or the specific plugin modules.

## Local Testing

You can set up a local testing environment by running a Minecraft server (e.g., Paper or Spigot) and placing the compiled JAR files into the server's `plugins` directory.

We recommend testing plugins on a local server environment to verify functionality before using them on the community server.

## Versioning

All plugins should follow semantic versioning to maintain consistency and clarity. The version number should be formatted as:

**MAJOR.MINOR.PATCH**

* MAJOR: Incremented for significant changes, breaking backward compatibility (e.g., removing features, altering APIs).
* MINOR: Incremented for backward-compatible feature additions or improvements.
* PATCH: Incremented for bug fixes and small changes that do not affect functionality or introduce new features.

Example:

* `1.0.0` – Initial release.
* `1.1.0` – Added a new feature.
* `1.1.1` – Fixed a minor bug in the existing feature.

Please update version numbers accordingly when making changes.

## Licensing

This repository is open to the public and contributions are welcome under the terms of the [MIT License](LICENSE).

Feel free to fork, modify, and submit pull requests to improve or extend the plugins for the KAIMUX Community Server.

By contributing, you agree to license your contributions under the MIT License.
