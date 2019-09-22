# Stacksize
Modify the maximum stack sizes of materials.
### Compatibility
Tested for Spigot 1.14.4.
### Usage
- To view an item's material name and its current maximum stack size hold it in your hand and execute the command `stacksize inspect`.
- Execute `stacksize view <material>` to view the current maximum stack size of any material.
- To modify a material's maximum stack size, execute `stacksize modify <material> <stacksize>`. It is __not recommended__ to set
a stack size outside of the range from 1 to 64.
- The modified stack size is also added to the configuration, `config.yml` in the plugin's directory, so that the
modified stack sizes will remain the next time the server is started. You can edit the configuration manually,
but changes to the configuration will not have any effect until the plugin is reloaded.
### Commands
| Command | Description | Permission |
| ------- | ----------- | ---------- |
| `stacksize inspect` | View material and maximum stack size of the held item. | `stacksize.view` |
| `stacksize view <material>` | View maximum stack size of a particular material. | `stacksize.view` |
| `stacksize modify <material> <stacksize>` | Set the maximum stack size of a particular material. | `stacksize.modify` |
### Permissions
| Permission | Description | Default | Children |
| ---------- | ----------- | ------- | -------- |
| `stacksize` | All Stacksize permissions. | Operator | `stacksize.view`, `stacksize.modify` |
| `stacksize.view` | Permission to use the `stacksize view` and `stacksize inspect` commands. | All |  |
| `stacksize.modify` | Permission to use `stacksize modify` command. | Operator | `stacksize.view` |
### Installation
Place the Stacksize jar file in the Bukkit server's plugins directory.
### Building
 - Have Java installed.
 - Execute Spigot BuildTools once. This places the latest Bukkit in your local Maven repository.
 
You can then execute `./gradlew build` in the project root directory to build.