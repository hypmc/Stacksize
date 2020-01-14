# Stacksize
Modify the maximum stack sizes of items.
### Compatibility
Tested for Spigot 1.15.1.
### Usage
- To view an item's material name and its current maximum stack size hold it in your hand and execute the command `stacksize inspect`.
- Execute `stacksize view <material>` to view the current maximum stack size of any item.
- To modify an item's maximum stack size, execute `stacksize modify <material> <stacksize>`. It is __not recommended__, and probably will not work, to set
a stack size outside of the range from 1 to 64.
- The modified stack size is also added to the configuration, `config.yml` in the plugin's directory, so that the
modified stack sizes will remain the next time the server is started.
- Modifications to the configuration while the plugin is running will be detected and the configuration will be reloaded
immediately.

### Commands
| Command | Description | Permission |
| ------- | ----------- | ---------- |
| `stacksize inspect` | View the material and maximum stack size of the held item. | `stacksize.view` |
| `stacksize view <material>` | View the maximum stack size of a particular item. | `stacksize.view` |
| `stacksize modify <material> <stacksize>` | Set the maximum stack size of an item and add it to the configuration. | `stacksize.modify` |
| `stacksize reset <material>` | Reset the maximum stack size of an item back to its Vanilla size and remove it from the configuration. | `stacksize.modify` |
### Permissions
| Permission | Description | Default | Children |
| ---------- | ----------- | ------- | -------- |
| `stacksize` | All Stacksize permissions. | Operator | `stacksize.view`, `stacksize.modify` |
| `stacksize.view` | Permission to use the `stacksize view` and `stacksize inspect` commands. | All |  |
| `stacksize.modify` | Permission to use the `stacksize modify` and `stacksize reset` commands. | Operator | `stacksize.view` |
### Configuration:
Updates to the configuration while the plugin is running will be detected and the configuration will be reloaded.
Default configuration: https://github.com/Torm/Stacksize/blob/master/src/main/resources/config.yml
##### config.yml
| Attribute | Description | Example |
| ---------- | ----------- | ------- |
| `version` | The configuration version. Do not change. | `version: 2` |
| `required` | If the server requires the plugin to run. If true the server is shut down if the plugin encounters an error. | `required: false` |
| `log` | A selection of events that will be logged.<ul><li>`STACK_SIZE_MODIFIED` - Log when a material's maximum stack size is modified.</li><li>`CONFIGURATION_MODIFIED` - Log when the configuration is modified or is loaded/reloaded.</li></ul> | <pre><code>log:<br />  - STACK_SIZE_MODIFIED <br />  - CONFIGURATION_MODIFIED</code></pre> |
| `stackSizes` | The maximum stack sizes of the given items. See a list of materials at https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html. Materials that are not items (which cannot be stored in an inventory) are ignored. __It is only recommended to use values from 1 to 64.__ | <pre><code>stackSizes:<br />  POTION: 4<br />  COOKED_BEEF: 16<br />  MINECART: 4</code></pre> |
### Installation
Place the Stacksize jar file in the Bukkit server's plugins directory.
### Building
 - Have Java installed.
 - Execute Spigot BuildTools once. This places Spigot in the local Maven repository.
 
You can then use the Gradle wrapper by executing `./gradlew build` in the project root directory to build.