# KAIMUX Plugins

KAIMUX Plugins are modular extensions designed to enhance the functionality and gameplay of the KAIMUX Network Minecraft server. They introduce unique features, mechanics, and tools that enrich the player experience, ranging from custom commands and mini-games to advanced administrative utilities. These plugins are built using Java, leveraging the Bukkit/Spigot/Paper API, and are tailored specifically to meet the needs of the KAIMUX community.

## Coding

When developing plugins for the KAIMUX Network, coders have the freedom to use any IDE of their choice, such as IntelliJ IDEA, Eclipse, or VS Code. The chosen IDE does not affect the development process as long as standard Java coding practices are followed.

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
Ensure your plugin adheres to the Bukkit/Spigot/Paper API standards for compatibility with the KAIMUX server setup.
By adhering to these guidelines, developers can contribute high-quality plugins that seamlessly integrate with the KAIMUX Network and improve the player experience.

## Building Locally with Gradle
To build KAIMUX plugins locally, developers must use Gradle and meet the following requirements:

### Prerequisites
#### Java Version

* Java 21 or later is required to build the plugins. Ensure your environment is properly configured with the correct version.

#### Gradle

* Install Gradle on your system, or use the Gradle Wrapper (./gradlew or gradlew.bat for Windows) included in the project repository.

#### Project Structure
* The KAIMUX plugins project is a multi-module Gradle project, meaning it consists of multiple submodules, each representing an individual plugin.

#### Building Plugins
##### Build All Plugins Together

To build all plugins at once, execute the following command from the root directory:
```bash
./gradlew build
```
This will compile and package all plugins into their respective JAR files, located in the build/libs/ directory of each module.

##### Build a Specific Plugin

To build an individual plugin, navigate to the plugin's module directory and run:
```bash
./gradlew build
```
Alternatively, you can specify the module directly from the root directory:
```bash
./gradlew :plugin-module-name:build
```

#### Customizing Build Paths

If you need to adjust output paths or other build configurations, you can modify the build.gradle files in the root project or the specific plugin modules.

## Deployment

### Stage network deployment
Deployment to the stage server occurs automatically whenever a new commit or merge is pushed to the **main branch**. A Continuous Integration (CI) pipeline is triggered, and upon a successful build in GitHub, all plugins are copied to their respective stage servers as defined in the **target_paths** file within **each plugin's directory**. This process ensures that the latest changes are quickly available for testing and validation in a staging environment.

### Live network deployment
Deployment to the live server is triggered with each new GitHub release. The current state of the **main branch** is built during the release process, and after a successful build, the plugins are copied to the live project servers as defined in the **target_paths** file within **each plugin's directory**. This workflow ensures that only stable and production-ready changes are deployed to the live environment, providing a seamless and reliable experience for players.

## Local testing

### Setup environment
You can set up the local environment by following these steps:

1. Run the Gradle task `stage-build`. This build will create a clone of the "server" directory into the "stage" folder, which will be used for local testing and development.
2. Run the Gradle task `stage-download`. This will download the latest builds of Velocity and Paper to their respective directories in the Stage folder.

This setup only needs to be done once. After the initial setup, use the `build` and `download` tasks periodically to update the environment.

Some useful commands:
- `gradle task clean` will delete the Stage folder.

### Plugin testing
To test a plugin, run the `stage-deploy` task in that plugin. This will deploy the plugin and all of its dependencies to their respective servers.

In the root directory, run the following command:
```bash
stage.bat <server>
```
This will start the Velocity, Lobby, and the defined server instances on your local machine.

## Versioning
All plugins deployed to the KAIMUX Network must follow semantic versioning to maintain consistency and clarity. The version number should be formatted as:

**MAJOR.MINOR.PATCH**

* MAJOR: Incremented for significant changes, breaking backwards compatibility (e.g., removing features, altering APIs).
* MINOR: Incremented for backward-compatible feature additions or improvements.
* PATCH: Incremented for bug fixes and small changes that do not affect functionality or introduce new features.

Example:

* `1.0.0` – Initial release.
* `1.1.0` – Added a new feature.
* `1.1.1` – Fixed a minor bug in the existing feature.

Version numbers should be updated before every live deployment.

## Licensing
All KAIMUX plugins are the exclusive property of the KAIMUX Network. These plugins are protected under proprietary rights, and the following restrictions apply:

### Ownership

* The source code, compiled plugins, and any derivative works are owned solely by the KAIMUX Network.

### Distribution

* Plugins may not be distributed, shared, or sold by anyone other than the owner of the KAIMUX Network.
* Redistribution, in any form (modified or unmodified), without explicit written permission from the KAIMUX Network owner, is strictly prohibited.

### Resale

* Reselling KAIMUX plugins, either as standalone software or as part of a package, is forbidden.

### Usage Rights

* Plugins are intended solely for use on KAIMUX Network servers. Any use outside of the KAIMUX Network requires express written approval from the network owner.

By contributing to or using the KAIMUX plugins, developers and users agree to abide by these licensing terms. Any violation of these terms may result in legal action.
