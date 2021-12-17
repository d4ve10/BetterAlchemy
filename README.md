# BetterAlchemy Minecraft Plugin
This Plugin adds additional potions with more complex ingredients
![Potions](potions.png?raw=true)
---
## Creating a new potion
1. Place a water cauldron above a heat source (either fire or campfire)
2. Throw the needed ingredients into the cauldron
3. Right-click the cauldron with a stick to create the potion
4. Extract the potions with empty bottles

## Potions

| Potion             	             | Ingredients                           	                                                                                                                                                                                                                                                                                                  | Effect                                                                                                                                                                          | Corrupted Effect                                                         |
|----------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------|
| Potion of Corruption             | [Wither Rose][44] <br> [Pufferfish][45] <br> [Red Mushroom][46]                                                                                                                                                                                                                                                                          | Inverts every effect the player has. For example, if a player has the swiftness effect, he now gets the slowness effect. <br> These custom potions also have a corrupted effect |                                                                          |
| Potion of Drowning 	             | [Lily Pad][1] <br> [Kelp][2] <br> [Turtle Egg][3] <br> [Big Dripleaf][4]	                                                                                                                                                                                                                                                                | The player gets the drowning effect and needs Water Breathing to not get drowned                                                                                                | The player gets Water Breathing                                          |
| Potion of Glowing   	            | [Glow Ink Sac][5] <br> [Glow Berries][6] <br> [Sunflower][7] <br> [Glow Lichen][8]                                                                                                                                                                                                                                                       | Gives the player the glowing effect                                                                                                                                             | Gives the player the Invisibility effect                                 |
| Potion of Spectator 	            | [Brain Coral][9] <br> [Sea Pickle][10] <br> [Spore Blossom][11]        	                                                                                                                                                                                                                                                                 | Gives the player Spectator Mode for a brief moment                                                                                                                              | Gives the player Adventure Mode                                          |
| Potion of Warping   	            | [Ender Pearl][12] <br> [Warped Fungus][13] <br> [Nether Sprouts][14] <br> [Warped Roots][15] <br> [Eye of Ender][16] <br> [Twisting Vines][17] <br> [Chorus Fruit][18] <br> [Popped Chorus Fruit][19]      	                                                                                                                             | Teleports the player randomly in a range of ~20 blocks when he is getting hit by a projectile or takes damage                                                                   | Gives the player slowness                                                |
| Potion of Firestorm              | [Crimson Roots][20] <br> [Crimson Fungus][21] <br> [Weeping Vines][22] <br> [Fire Coral][23]                                                                                                                                                                                                                                             | Creates fire around the player                                                                                                                                                  | Creates water splash potions around the player which can extinguish fire |
| Potion of Friendship	            | [Honeycomb][24] <br> [Honey Bottle][25] <br> [Dandelion][26] <br> [Blue Orchid][27] <br> [Allium][28] <br> [Red Tulip][29] <br> [Oxeye Daisy][30] <br> [Cornflower][31] <br> [Sugar Cane][32] <br> [Peony][33] <br> [Sunflower][34] <br> [Tube Coral][35] <br> [Horn Coral][36] <br> [Bubble Coral][37] <br> [Lilac][38]               	 | Mobs are friendly towards the player and won't attack him                                                                                                                       | All Mobs in a range of ~100 blocks are now targeting the player          |
| Potion of Poison <br> Resistance | [Slime Ball][39] <br> [Blue Orchid][40] <br> [Fermented Spider Eye][41] <br> [Poisonous Potato][42] <br> [Zombie Head][43]        	                                                                                                                                                                                                      | The player is immune against poison                                                                                                                                             | Gives the player poison                                                  |

[1]: https://minecraft.fandom.com/wiki/Lily_Pad
[2]: https://minecraft.fandom.com/wiki/Kelp
[3]: https://minecraft.fandom.com/wiki/Turtle_Egg
[4]: https://minecraft.fandom.com/wiki/Big_Dripleaf
[5]: https://minecraft.fandom.com/wiki/Glow_Ink_Sac
[6]: https://minecraft.fandom.com/wiki/Glow_Berries
[7]: https://minecraft.fandom.com/wiki/Sunflower
[8]: https://minecraft.fandom.com/wiki/Glow_Lichen
[9]: https://minecraft.fandom.com/wiki/Brain_Coral
[10]: https://minecraft.fandom.com/wiki/Sea_Pickle
[11]: https://minecraft.fandom.com/wiki/Spore_Blossom
[12]: https://minecraft.fandom.com/wiki/Ender_Pearl
[13]: https://minecraft.fandom.com/wiki/Warped_Fungus
[14]: https://minecraft.fandom.com/wiki/Nether_Sprouts
[15]: https://minecraft.fandom.com/wiki/Warped_Roots
[16]: https://minecraft.fandom.com/wiki/Eye_of_Ender
[17]: https://minecraft.fandom.com/wiki/Twisting_Vines
[18]: https://minecraft.fandom.com/wiki/Chorus_Fruit
[19]: https://minecraft.fandom.com/wiki/Popped_Chorus_Fruit
[20]: https://minecraft.fandom.com/wiki/Crimson_Roots
[21]: https://minecraft.fandom.com/wiki/Crimson_Fungus
[22]: https://minecraft.fandom.com/wiki/Weeping_Vines
[23]: https://minecraft.fandom.com/wiki/Fire_Coral
[24]: https://minecraft.fandom.com/wiki/Honeycomb
[25]: https://minecraft.fandom.com/wiki/Honey_Bottle
[26]: https://minecraft.fandom.com/wiki/Dandelion
[27]: https://minecraft.fandom.com/wiki/Blue_Orchid
[28]: https://minecraft.fandom.com/wiki/Allium
[29]: https://minecraft.fandom.com/wiki/Red_Tulip
[30]: https://minecraft.fandom.com/wiki/Oxeye_Daisy
[31]: https://minecraft.fandom.com/wiki/Cornflower
[32]: https://minecraft.fandom.com/wiki/Sugar_Cane
[33]: https://minecraft.fandom.com/wiki/Peony
[34]: https://minecraft.fandom.com/wiki/Sunflower
[35]: https://minecraft.fandom.com/wiki/Tube_Coral
[36]: https://minecraft.fandom.com/wiki/Horn_Coral
[37]: https://minecraft.fandom.com/wiki/Bubble_Coral
[38]: https://minecraft.fandom.com/wiki/Lilac
[39]: https://minecraft.fandom.com/wiki/Slime_Ball
[40]: https://minecraft.fandom.com/wiki/Blue_Orchid
[41]: https://minecraft.fandom.com/wiki/Fermented_Spider_Eye
[42]: https://minecraft.fandom.com/wiki/Poisonous_Potato
[43]: https://minecraft.fandom.com/wiki/Zombie_Head
[44]: https://minecraft.fandom.com/wiki/Wither_Rose
[45]: https://minecraft.fandom.com/wiki/Pufferfish
[46]: https://minecraft.fandom.com/wiki/Red_Mushroom
