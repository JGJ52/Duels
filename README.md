# Duels

![Time](https://hackatime-badge.hackclub.com/U0922GMGGTU/Duels?label=Time+I+spent+on+this)

**Dependencies**:
- WorldEdit
- WorldGuard
---
**Admin commands**:
- **Arena creation**: Select the arena you want to create with the worldedit //wand. Type //copy to copy it, and then type /arena create <name>. This will open up a menu containing two settings: How far the players should spawn from the center of the arena (center is where you typed //copy) and how long the countdown should be. You can set these by right and left clicking them. After you're done, click the save button and your arena is created! (This also saves the worldguard regions in the selected arena)
- **Kit creation**: Get the items of your kit to you inventory, then type /kit create <name>. This will open up a menu containing 3 settings. You can set the first two by left and right clicking, and the third by left clicking it, then clicking all the arenas you want the kit to possibly be in (it selects an arena randomly from these when the duel starts)
- **Kit loading**: /kit load <name> gives you the content of the kit
- **Kit deleting:**: /kit delete <name> deletes the kit
---
- **Player commands**:
- **/editkit <kit>**: Gives you the kit's default ordering, you can reorder the kit and it will save it. When you duel somebody, you get the saved kit
- **/duel <player>**: Opens a gui to select witch kit you want to duel the player, if you click a kit then it sends the request
- **/forfeit**: Gives this round to the other player
- **/leave**: Gives the entire duel to the other player
- **/queue**: Opens a menu where you can queue to a gamemode, when 2 players are in the queue it starts a duel between them
- **/acceptduel <player>**: Accepts the duel request of the player
---
**Command permissions**:\
duels.command.\<command>\
if the command has subcommands e.g.: kit, arena then you will need duels.command.kit.create too to run it
---