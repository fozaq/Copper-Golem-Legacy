# Copper Golem Chest Compatibility

## Overview

The Copper Golem uses a flexible system for chest interactions:
- **Source Chests**: Only **Copper Chests**
- **Target Chests**: **Tag-based expandable** - defines which chests the golem can place items into

## Tags
### `golem_target_chests`
Defines which chests the golem can place items into.

**File**: `src/main/resources/data/coppergolemlegacy/tags/blocks/golem_target_chests.json`

```json
{
  "values": [
    "minecraft:chest",
    "minecraft:trapped_chest",
    "minecraft:barrel",
    "ironchest:iron_chest",
    "ironchest:gold_chest",
    "ironchest:diamond_chest",
    "ironchest:copper_chest",
    "ironchest:crystal_chest",
    "ironchest:obsidian_chest",
    "ironchest:dirt_chest",
    "ironchest:trapped_iron_chest",
    "ironchest:trapped_gold_chest",
    "ironchest:trapped_diamond_chest",
    "ironchest:trapped_copper_chest",
    "ironchest:trapped_crystal_chest",
    "ironchest:trapped_obsidian_chest",
    "ironchest:trapped_dirt_chest"
  ]
}
```

**Default**: 
- Minecraft Vanilla Chests (normal & trapped)
- Minecraft Barrel
- IronChest Mod (all 7 chest types + trapped variants)

## Adding New Mods

### Method 1: Individual Blocks
Add individual block IDs directly:

```json
{
  "values": [
    "minecraft:chest",
    "minecraft:trapped_chest",
    "minecraft:barrel",
    "#ironchest:iron_chests",
    "othermod:special_chest",
    "othermod:large_chest"
  ]
}
```

### Method 2: Using Mod Tags
If another mod already defines tags for their chests, use those:

```json
{
  "values": [
    "minecraft:chest",
    "minecraft:trapped_chest",
    "#ironchest:iron_chests",
    "#othermod:chests"
  ]
}
```

### Method 3: Datapack
Players can also add chests via datapack without modifying the mod:

1. Create a datapack in `saves/<worldname>/datapacks/my_chest_compatibility/`
2. Add: `data/coppergolemlegacy/tags/blocks/golem_target_chests.json`
3. Content:
```json
{
  "replace": false,
  "values": [
    "my_mod:my_chest"
  ]
}
```

**Important**: `"replace": false` so default values aren't overwritten!

## Examples for Other Mods

### Storage Drawers
```json
"#storagedrawers:drawers"
```

### Sophisticated Storage
```json
"#sophisticatedstorage:barrels",
"#sophisticatedstorage:chests"
```

### Applied Energistics 2
```json
"ae2:chest"
```

## Developer Notes

- Target tag is defined in `ModTags.java` (`GOLEM_TARGET_CHESTS`)
- The AI logic uses the tag in `CopperGolemAi.java` (line ~111)
- Target is expandable via tag
- Implementation is performance-optimized through tag caching

## Behavior

1. The golem searches within a radius of 32 blocks (horizontal) and 8 blocks (vertical)
2. It takes items **only** from Copper Chests (hardcoded, not changeable)
3. It places items in **all** Target Chests (expandable via tag: Vanilla Chests, Barrels, IronChest, etc.)
4. Automatically supports all oxidation levels of Copper Chests
5. Works with trapped/regular variants
6. Plays correct sounds for each container type (Chest/Barrel/Copper Chest)
