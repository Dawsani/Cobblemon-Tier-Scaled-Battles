# Cobblemon: Tier-Scaled Battles
This is an addon mod for Cobblemon that adds a new "Tier-Scaled" option to the PvP battle level options. In a Tier-Scaled battle, each Pokemon's level is set based on it's competative tier on Pokemon Showdown. Generally, weaker Pokemon will be higher level while stronger Pokemon will be lower level. This allows for a much more diverse pool of battle-viable Pokemon

# Tiering Information
Each Pokemon's tier is based on the tier provided by Pokemon Showdown in the National Dex format. Each Pokemon's level in a tier-scaled battle directly corresponds to their tier. Below is a list of each tier and it's corresponding level. The level assigned to each pokemon in a tier is based on Pokemon Showdown's assignment as well. 

| Tier | Level |
| ---- | ----- |
| AG (Anything Goes) | 71 |
| Uber | 73 |
| OU (Over-Used) | 75 |
| UU (Under-Used) | 77 |
| UUBL (Under-Used Ban List) | 76 |
| RU (Rarely Used) | 79 |
| RUBL (Rarely Used Ban List) | 76 |
| NFE (Not Fully Evolved) | 84 |

## Forms
Some species of Pokemon have different tiers for different forms. For example, normal Lucario is RU, while Mega Lucario is Uber. The mod determines if a Pokemon will be tiered by it's Mega form based on whether or not it is holding a Mega Stone (Any Mega Stone for now). And for Pokemon with multiple Mega Evolutions, it determines which Mega form to tier it by based on the type of Mega Stone the Pokemon is holding (X, Y or Z Mega Stones). So to complete the Lucario example:
- Lucario holding no Mega Stone will be RU tier and level 79.
- Lucario holding the Lucarionite will be Uber tier and level 73.
- Lucario holding the Lucarionite Z will be OU tier and level 75.

> [!NOTE]
> All Mega Evolution forms from Pokemon Legends: Z-A are tiered at OU for now.

# Bugs
Cobblemon and Pokemon alike have over 1,000 different species, many with different forms that sometimes have different tiers. This mod has not been tested with each and every one of those species. It is not unlikely that you may encounter a Pokemon being set to level 1 in a tier-scaled battle. This is becuase level 1 is the default level if no tier is found for the species or if no level has been assigned to it's tier.

Please report any bugs you find to me over GitHub, Modrinth, CurseForge or email at [dawson@dawsonmatthews.com](mailto:dawson@dawsonmatthews.com).

# Compatability
For the Mega Forms, this mod was only made in mind with the Mega Showdown mod and Navas ZA Mega. If you want this mod to work with other Mega evolution mods let me know and I'll get back to you.
