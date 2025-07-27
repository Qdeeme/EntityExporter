# How does it work?
## Use a command to print in chat or export to .json file all entities registered by the game.

Supports moded entities and moded entity_groups.
It's been created for all modpack/mod creators as a utility for quick checks of all entities registered.

Mod adds 8 commands into the game:

```
/entitylist groups - Prints in chat all the entity groups registered.
/entitylist all export - Export entities to JSON file
/entitylist all print - Print all entities in chat
/entitylist hostile export  - Export hostiles to JSON file
/entitylist hostile print - Print all hostile entities in chat
/entitylist creature export - Export all creatures to JSON file
/entitylist creature print - Print all creature entities in chat
/entitylist help - Prints all these commands.
```
By default, all the data are exported into the instance folder:
```
C:\Users\[user]\AppData\Roaming\Minecraft\[instance]\Entity_list
```
Following the pattern in alfabetical order:
```
[modid]:[entity]
```
By default, all the data are exported into the instance folder:
```
C:\Users\[user]\AppData\Roaming\Minecraft\[instance]\Entity_list
```

And each export command creates new .JSON file unless it already exists.

# SIMPLE, EASY, FAST

### Mod is available on Modrinth and CurseForge
### https://www.curseforge.com/minecraft/mc-mods/entityexporter
### https://modrinth.com/mod/entity-exporter




# DEPENDENCIES

Curret version:
1.20.1 Minecraft 

Fabric Loader:
=> 0.16.0

Fabric API:
=> 0.90.0+1.20.1



All tested, all working fine, but keep in mind, that some bugs may occur.
