# Stacksize
Modify the maximum stack sizes of materials.
### Compatibility
Tested for Spigot 1.14.4.
### Usage
- To view an item's material name and its current maximum stack size hold it in your hand and execute the command `stacksize inspect`.
- Execute `stacksize info <material>` to view the current maximum stack size of any material.
- To modify a material's maximum stack size, execute `stacksize set <material> <stacksize>`. It is __not recommended__ to set
a stack size outside of the range from 1 to 64.
- The modified stack size is also added to the configuration, `config.yml` in the plugin's directory, so that the
modified stack sizes will remain the next time the server is started. You can edit the configuration manually,
but changes to the configuration will not have any effect until the plugin is reloaded.
### Commands
| Command | Description | Permission |
| ------- | ----------- | ---------- |
| `stacksize inspect` | View material and maximum stack size of the held item. | `stacksize` |
| `stacksize info <material>` | View maximum stack size of a particular material. | `stacksize` |
| `stacksize set <material> <stacksize>` | Set the maximum stack size of a particular material. | `stacksize` |
### Permissions
| Permission | Description | Default |
| ---------- | ----------- | ------- |
| `stacksize` | Permission to use the `stacksize` command. | Operator |
### Build
 - Have Java installed.
 - Execute Spigot BuildTools once. This places the latest Bukkit in your local Maven repository.
 
You can then execute `./gradlew build` to build.