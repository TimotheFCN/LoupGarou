package fr.timothefcn.mc.loupgarou.classes;

import fr.timothefcn.mc.loupgarou.events.LGCustomItemChangeEvent;
import fr.timothefcn.mc.loupgarou.roles.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.StringJoiner;

public class LGCustomItems {
    static HashMap<Class<? extends Role>, HashMap<String, Material>> mappings = new HashMap<Class<? extends Role>, HashMap<String, Material>>();

    static {
        JSONParser parser = new JSONParser();
        try {
            JSONObject mappings = (JSONObject) parser.parse("{\"PetiteFille\":{\"\":\"WET_SPONGE\",\"infecte\":\"ORANGE_BED\",\"infecte_mort\":\"ACACIA_WOOD\",\"infecte_maire\":\"SPRUCE_TRAPDOOR\",\"vampire-infecte\":\"BLAST_FURNACE\",\"maire\":\"POPPY\",\"mort\":\"STONE_SLAB\",\"maire_mort\":\"OAK_PLANKS\",\"mort_vampire-infecte\":\"GREEN_SHULKER_BOX\",\"maire_vampire-infecte\":\"SPRUCE_WOOD\",\"infecte_maire_mort\":\"DARK_OAK_PRESSURE_PLATE\",\"maire_mort_vampire-infecte\":\"LIGHT_BLUE_CONCRETE\"},\"LoupGarou\":{\"\":\"DARK_OAK_TRAPDOOR\",\"infecte\":\"CORNFLOWER\",\"infecte_mort\":\"GREEN_WOOL\",\"infecte_maire\":\"RED_BANNER\",\"vampire-infecte\":\"ACACIA_DOOR\",\"maire\":\"PRISMARINE_WALL\",\"maire_mort\":\"BLACK_STAINED_GLASS\",\"mort\":\"DEAD_BRAIN_CORAL\",\"mort_vampire-infecte\":\"COMPARATOR\",\"maire_vampire-infecte\":\"STONE_BRICK_SLAB\",\"infecte_maire_mort\":\"MAGMA_BLOCK\",\"maire_mort_vampire-infecte\":\"JUNGLE_TRAPDOOR\"},\"Corbeau\":{\"infecte_mort\":\"PINK_CONCRETE_POWDER\",\"infecte\":\"TRIDENT\",\"\":\"GRAY_BED\",\"infecte_maire\":\"OAK_SIGN\",\"vampire-infecte\":\"STRIPPED_DARK_OAK_LOG\",\"maire\":\"YELLOW_GLAZED_TERRACOTTA\",\"maire_mort\":\"LECTERN\",\"mort\":\"MELON\",\"mort_vampire-infecte\":\"BARREL\",\"maire_vampire-infecte\":\"LIGHT_GRAY_DYE\",\"infecte_maire_mort\":\"REDSTONE_LAMP\",\"maire_mort_vampire-infecte\":\"BELL\"},\"LoupGarouBlanc\":{\"infecte_mort\":\"LIME_STAINED_GLASS_PANE\",\"infecte\":\"TORCH\",\"\":\"ACACIA_FENCE_GATE\",\"infecte_maire\":\"SPRUCE_LOG\",\"vampire-infecte\":\"GREEN_CONCRETE_POWDER\",\"maire\":\"DEAD_BUBBLE_CORAL_BLOCK\",\"mort\":\"MUSHROOM_STEM\",\"maire_mort\":\"HORN_CORAL_FAN\",\"mort_vampire-infecte\":\"ORANGE_CARPET\",\"maire_vampire-infecte\":\"MAGENTA_BANNER\",\"infecte_maire_mort\":\"FLOWER_BANNER_PATTERN\",\"maire_mort_vampire-infecte\":\"GRASS_PATH\"},\"Assassin\":{\"infecte\":\"WITHER_ROSE\",\"\":\"ENCHANTED_BOOK\",\"infecte_mort\":\"DARK_OAK_SIGN\",\"infecte_maire\":\"DEAD_TUBE_CORAL_WALL_FAN\",\"vampire-infecte\":\"PURPUR_SLAB\",\"maire\":\"DEAD_TUBE_CORAL_BLOCK\",\"maire_mort\":\"BIRCH_FENCE_GATE\",\"mort\":\"REDSTONE_TORCH\",\"mort_vampire-infecte\":\"ACACIA_SLAB\",\"maire_vampire-infecte\":\"DEAD_BUSH\",\"infecte_maire_mort\":\"INFESTED_MOSSY_STONE_BRICKS\",\"maire_mort_vampire-infecte\":\"BLACK_SHULKER_BOX\"},\"Voyante\":{\"infecte_mort\":\"FLINT\",\"\":\"DRAGON_HEAD\",\"infecte\":\"FIRE_CORAL_BLOCK\",\"infecte_maire\":\"BRAIN_CORAL_WALL_FAN\",\"vampire-infecte\":\"LIGHT_BLUE_WOOL\",\"maire\":\"DARK_OAK_BOAT\",\"maire_mort\":\"NETHERRACK\",\"mort\":\"BLACK_STAINED_GLASS_PANE\",\"mort_vampire-infecte\":\"WHEAT_SEEDS\",\"maire_vampire-infecte\":\"GRAY_GLAZED_TERRACOTTA\",\"infecte_maire_mort\":\"LIGHT_BLUE_SHULKER_BOX\",\"maire_mort_vampire-infecte\":\"TURTLE_HELMET\"},\"Dictateur\":{\"\":\"STRIPPED_DARK_OAK_WOOD\",\"infecte_mort\":\"LAPIS_BLOCK\",\"infecte\":\"GREEN_CARPET\",\"infecte_maire\":\"OAK_DOOR\",\"vampire-infecte\":\"STONE_BRICK_WALL\",\"maire\":\"COCOA_BEANS\",\"maire_mort\":\"GRANITE_SLAB\",\"mort\":\"STICKY_PISTON\",\"mort_vampire-infecte\":\"GRAY_CARPET\",\"maire_vampire-infecte\":\"ACACIA_BOAT\",\"infecte_maire_mort\":\"JUNGLE_LEAVES\",\"maire_mort_vampire-infecte\":\"RED_MUSHROOM\"},\"LoupGarouNoir\":{\"infecte_mort\":\"WHITE_STAINED_GLASS_PANE\",\"\":\"YELLOW_BED\",\"infecte\":\"LIGHT_BLUE_BANNER\",\"infecte_maire\":\"SPRUCE_PLANKS\",\"vampire-infecte\":\"ORANGE_SHULKER_BOX\",\"maire\":\"STRIPPED_OAK_LOG\",\"maire_mort\":\"MOSSY_COBBLESTONE_SLAB\",\"mort\":\"DEAD_FIRE_CORAL_WALL_FAN\",\"mort_vampire-infecte\":\"BIRCH_SIGN\",\"maire_vampire-infecte\":\"BEETROOT_SEEDS\",\"infecte_maire_mort\":\"BEACON\",\"maire_mort_vampire-infecte\":\"CLAY\"},\"ChaperonRouge\":{\"\":\"YELLOW_CONCRETE_POWDER\",\"infecte\":\"LIME_BANNER\",\"infecte_mort\":\"DEAD_BUBBLE_CORAL\",\"infecte_maire\":\"GRAY_SHULKER_BOX\",\"vampire-infecte\":\"WITHER_SKELETON_SKULL\",\"maire\":\"PODZOL\",\"mort\":\"GRAY_BANNER\",\"maire_mort\":\"LIGHT_BLUE_DYE\",\"mort_vampire-infecte\":\"INK_SAC\",\"maire_vampire-infecte\":\"PURPLE_CARPET\",\"infecte_maire_mort\":\"WHITE_CONCRETE_POWDER\",\"maire_mort_vampire-infecte\":\"LAPIS_LAZULI\"},\"EnfantSauvage\":{\"infecte\":\"CARTOGRAPHY_TABLE\",\"\":\"LIGHT_GRAY_STAINED_GLASS_PANE\",\"infecte_mort\":\"YELLOW_WOOL\",\"infecte_maire\":\"POLISHED_DIORITE\",\"vampire-infecte\":\"IRON_HORSE_ARMOR\",\"maire\":\"TUBE_CORAL\",\"mort\":\"STRING\",\"maire_mort\":\"LEATHER_HORSE_ARMOR\",\"mort_vampire-infecte\":\"DARK_OAK_FENCE\",\"maire_vampire-infecte\":\"SPRUCE_DOOR\",\"infecte_maire_mort\":\"YELLOW_TERRACOTTA\",\"maire_mort_vampire-infecte\":\"BAMBOO\"},\"Faucheur\":{\"infecte_mort\":\"MAGENTA_CARPET\",\"infecte\":\"GRAY_STAINED_GLASS\",\"\":\"LIME_GLAZED_TERRACOTTA\",\"infecte_maire\":\"STRIPPED_OAK_WOOD\",\"vampire-infecte\":\"PINK_CONCRETE\",\"maire\":\"FERN\",\"maire_mort\":\"CHORUS_PLANT\",\"mort\":\"ORANGE_TULIP\",\"mort_vampire-infecte\":\"MAGENTA_GLAZED_TERRACOTTA\",\"maire_vampire-infecte\":\"NETHER_BRICK_WALL\",\"infecte_maire_mort\":\"DARK_OAK_FENCE_GATE\",\"maire_mort_vampire-infecte\":\"STRIPPED_BIRCH_LOG\"},\"Cupidon\":{\"\":\"SPRUCE_SLAB\",\"infecte_mort\":\"BONE_MEAL\",\"infecte\":\"MAGENTA_WOOL\",\"infecte_maire\":\"POPPED_CHORUS_FRUIT\",\"vampire-infecte\":\"LAVA_BUCKET\",\"maire\":\"GOLDEN_HELMET\",\"maire_mort\":\"GRASS\",\"mort\":\"STONE_PICKAXE\",\"mort_vampire-infecte\":\"LIGHT_GRAY_CARPET\",\"maire_vampire-infecte\":\"DAMAGED_ANVIL\",\"infecte_maire_mort\":\"COBBLESTONE\",\"maire_mort_vampire-infecte\":\"JUNGLE_FENCE_GATE\"},\"EnfantSauvageLG\":{\"\":\"SAND\",\"infecte_mort\":\"END_CRYSTAL\",\"infecte\":\"CYAN_WOOL\",\"infecte_maire\":\"WOODEN_AXE\",\"vampire-infecte\":\"GRAY_TERRACOTTA\",\"maire\":\"LANTERN\",\"maire_mort\":\"HEAVY_WEIGHTED_PRESSURE_PLATE\",\"mort\":\"WHITE_SHULKER_BOX\",\"mort_vampire-infecte\":\"BUBBLE_CORAL_FAN\",\"maire_vampire-infecte\":\"BIRCH_PRESSURE_PLATE\",\"infecte_maire_mort\":\"PINK_CARPET\",\"maire_mort_vampire-infecte\":\"BLAZE_POWDER\"},\"Chasseur\":{\"infecte_mort\":\"PINK_STAINED_GLASS\",\"infecte\":\"PRISMARINE_SLAB\",\"\":\"SEAGRASS\",\"infecte_maire\":\"WOODEN_SHOVEL\",\"vampire-infecte\":\"GRINDSTONE\",\"maire\":\"YELLOW_STAINED_GLASS_PANE\",\"maire_mort\":\"SUNFLOWER\",\"mort\":\"STICK\",\"mort_vampire-infecte\":\"YELLOW_BANNER\",\"maire_vampire-infecte\":\"SPONGE\",\"infecte_maire_mort\":\"HOPPER_MINECART\",\"maire_mort_vampire-infecte\":\"CYAN_CONCRETE\"},\"Bouffon\":{\"infecte\":\"BIRCH_PLANKS\",\"\":\"ACACIA_SAPLING\",\"infecte_mort\":\"COBBLESTONE_SLAB\",\"infecte_maire\":\"COBBLESTONE_WALL\",\"vampire-infecte\":\"RABBIT_HIDE\",\"maire\":\"SUGAR_CANE\",\"maire_mort\":\"NETHER_BRICK_FENCE\",\"mort\":\"DRIED_KELP_BLOCK\",\"mort_vampire-infecte\":\"END_STONE_BRICK_SLAB\",\"maire_vampire-infecte\":\"RED_CONCRETE_POWDER\",\"infecte_maire_mort\":\"BUBBLE_CORAL\",\"maire_mort_vampire-infecte\":\"IRON_ORE\"},\"Detective\":{\"\":\"CROSSBOW\",\"infecte_mort\":\"RED_STAINED_GLASS\",\"infecte\":\"CYAN_GLAZED_TERRACOTTA\",\"infecte_maire\":\"DIAMOND_AXE\",\"vampire-infecte\":\"SPRUCE_LEAVES\",\"maire\":\"BLACK_TERRACOTTA\",\"mort\":\"YELLOW_CARPET\",\"maire_mort\":\"DIORITE\",\"mort_vampire-infecte\":\"ZOMBIE_HEAD\",\"maire_vampire-infecte\":\"ENDER_EYE\",\"infecte_maire_mort\":\"PINK_SHULKER_BOX\",\"maire_mort_vampire-infecte\":\"DEAD_FIRE_CORAL_BLOCK\"},\"GrandMechantLoup\":{\"infecte_mort\":\"SEA_LANTERN\",\"\":\"OBSERVER\",\"infecte\":\"DRAGON_BREATH\",\"infecte_maire\":\"MAGENTA_TERRACOTTA\",\"vampire-infecte\":\"MOSSY_STONE_BRICK_WALL\",\"maire\":\"DROPPER\",\"maire_mort\":\"PRISMARINE_SHARD\",\"mort\":\"PINK_STAINED_GLASS_PANE\",\"mort_vampire-infecte\":\"SMOKER\",\"maire_vampire-infecte\":\"COAL_ORE\",\"infecte_maire_mort\":\"ORANGE_WOOL\",\"maire_mort_vampire-infecte\":\"FIRE_CORAL_FAN\"},\"Survivant\":{\"\":\"BROWN_DYE\",\"infecte_mort\":\"CYAN_SHULKER_BOX\",\"infecte\":\"END_STONE_BRICKS\",\"infecte_maire\":\"MILK_BUCKET\",\"vampire-infecte\":\"JUNGLE_LOG\",\"maire\":\"CAKE\",\"mort\":\"GLOBE_BANNER_PATTERN\",\"maire_mort\":\"RED_MUSHROOM_BLOCK\",\"mort_vampire-infecte\":\"FIRE_CORAL_WALL_FAN\",\"maire_vampire-infecte\":\"BLACK_CONCRETE\",\"infecte_maire_mort\":\"LILY_PAD\",\"maire_mort_vampire-infecte\":\"MINECART\"},\"ChienLoupLG\":{\"\":\"RED_NETHER_BRICK_SLAB\",\"infecte\":\"PURPLE_STAINED_GLASS\",\"infecte_mort\":\"LIME_CONCRETE\",\"infecte_maire\":\"PURPLE_WOOL\",\"vampire-infecte\":\"SOUL_SAND\",\"maire\":\"ACACIA_SIGN\",\"mort\":\"PURPLE_GLAZED_TERRACOTTA\",\"maire_mort\":\"GRAY_CONCRETE_POWDER\",\"mort_vampire-infecte\":\"IRON_DOOR\",\"maire_vampire-infecte\":\"JUNGLE_DOOR\",\"infecte_maire_mort\":\"DARK_OAK_DOOR\",\"maire_mort_vampire-infecte\":\"JUNGLE_BOAT\"},\"Garde\":{\"\":\"RED_SANDSTONE\",\"infecte\":\"LIGHT_GRAY_BED\",\"infecte_mort\":\"WOODEN_PICKAXE\",\"infecte_maire\":\"YELLOW_DYE\",\"vampire-infecte\":\"FARMLAND\",\"maire\":\"EXPERIENCE_BOTTLE\",\"maire_mort\":\"GLASS_PANE\",\"mort\":\"HORN_CORAL_BLOCK\",\"mort_vampire-infecte\":\"DEAD_HORN_CORAL_BLOCK\",\"maire_vampire-infecte\":\"LIME_DYE\",\"infecte_maire_mort\":\"VINE\",\"maire_mort_vampire-infecte\":\"TNT_MINECART\"},\"Villageois\":{\"\":\"SPRUCE_FENCE\",\"infecte_mort\":\"IRON_INGOT\",\"infecte\":\"DEAD_HORN_CORAL_FAN\",\"infecte_maire\":\"PURPLE_CONCRETE\",\"vampire-infecte\":\"DARK_OAK_PLANKS\",\"maire\":\"END_ROD\",\"maire_mort\":\"BLUE_ICE\",\"mort\":\"MAGENTA_STAINED_GLASS\",\"mort_vampire-infecte\":\"ENDER_PEARL\",\"maire_vampire-infecte\":\"COMMAND_BLOCK\",\"infecte_maire_mort\":\"TERRACOTTA\",\"maire_mort_vampire-infecte\":\"ACACIA_BUTTON\"},\"Ange\":{\"\":\"CAMPFIRE\",\"infecte\":\"BROWN_BED\",\"infecte_mort\":\"WHITE_TERRACOTTA\",\"infecte_maire\":\"GOLDEN_PICKAXE\",\"vampire-infecte\":\"PISTON\",\"maire\":\"BLUE_GLAZED_TERRACOTTA\",\"maire_mort\":\"GREEN_TERRACOTTA\",\"mort\":\"GOLDEN_BOOTS\",\"mort_vampire-infecte\":\"JUNGLE_FENCE\",\"maire_vampire-infecte\":\"SLIME_BALL\",\"infecte_maire_mort\":\"DEAD_BUBBLE_CORAL_FAN\",\"maire_mort_vampire-infecte\":\"LIME_TERRACOTTA\"},\"ChienLoup\":{\"\":\"RED_SANDSTONE_SLAB\",\"infecte_mort\":\"MAGENTA_DYE\",\"infecte\":\"BIRCH_BUTTON\",\"infecte_maire\":\"PINK_BANNER\",\"vampire-infecte\":\"LIGHT_BLUE_STAINED_GLASS_PANE\",\"maire\":\"GREEN_BED\",\"mort\":\"DIRT\",\"maire_mort\":\"POLISHED_GRANITE_SLAB\",\"mort_vampire-infecte\":\"LIGHT_GRAY_STAINED_GLASS\",\"maire_vampire-infecte\":\"STRIPPED_JUNGLE_LOG\",\"infecte_maire_mort\":\"PURPLE_STAINED_GLASS_PANE\",\"maire_mort_vampire-infecte\":\"LEATHER\"},\"Medium\":{\"infecte_mort\":\"BROWN_MUSHROOM_BLOCK\",\"infecte\":\"ORANGE_CONCRETE\",\"\":\"GREEN_STAINED_GLASS\",\"infecte_maire\":\"BROWN_STAINED_GLASS_PANE\",\"vampire-infecte\":\"COAL\",\"maire\":\"CHARCOAL\",\"maire_mort\":\"BRICK_SLAB\",\"mort\":\"ENCHANTING_TABLE\",\"mort_vampire-infecte\":\"CYAN_CARPET\",\"maire_vampire-infecte\":\"DARK_OAK_SAPLING\",\"infecte_maire_mort\":\"SALMON_BUCKET\",\"maire_mort_vampire-infecte\":\"NETHER_BRICKS\"},\"Pyromane\":{\"\":\"RED_WOOL\",\"infecte_mort\":\"CYAN_DYE\",\"infecte\":\"INFESTED_COBBLESTONE\",\"infecte_maire\":\"BIRCH_FENCE\",\"vampire-infecte\":\"RED_BED\",\"maire\":\"DEAD_HORN_CORAL_WALL_FAN\",\"mort\":\"GLASS_BOTTLE\",\"maire_mort\":\"OAK_TRAPDOOR\",\"mort_vampire-infecte\":\"COBWEB\",\"maire_vampire-infecte\":\"POLISHED_GRANITE\",\"infecte_maire_mort\":\"JUNGLE_SLAB\",\"maire_mort_vampire-infecte\":\"NETHER_STAR\"},\"Pirate\":{\"infecte\":\"BEDROCK\",\"\":\"JUNGLE_SAPLING\",\"infecte_mort\":\"YELLOW_CONCRETE\",\"infecte_maire\":\"POLISHED_DIORITE_SLAB\",\"vampire-infecte\":\"RED_TULIP\",\"maire\":\"NETHER_BRICK_SLAB\",\"maire_mort\":\"ANDESITE\",\"mort\":\"BLUE_CONCRETE\",\"mort_vampire-infecte\":\"ORANGE_TERRACOTTA\",\"maire_vampire-infecte\":\"STRIPPED_ACACIA_LOG\",\"infecte_maire_mort\":\"GLASS\",\"maire_mort_vampire-infecte\":\"LIME_BED\"},\"Pretre\":{\"\":\"DARK_OAK_SLAB\",\"infecte_mort\":\"DARK_OAK_LEAVES\",\"infecte\":\"CONDUIT\",\"infecte_maire\":\"OXEYE_DAISY\",\"vampire-infecte\":\"SPRUCE_SIGN\",\"maire\":\"HOPPER\",\"maire_mort\":\"SLIME_BLOCK\",\"mort\":\"MOSSY_COBBLESTONE\",\"mort_vampire-infecte\":\"RED_GLAZED_TERRACOTTA\",\"maire_vampire-infecte\":\"SANDSTONE_SLAB\",\"infecte_maire_mort\":\"LIGHT_BLUE_CARPET\",\"maire_mort_vampire-infecte\":\"CYAN_STAINED_GLASS_PANE\"},\"Sorciere\":{\"\":\"MAP\",\"infecte_mort\":\"MOJANG_BANNER_PATTERN\",\"infecte\":\"CYAN_BED\",\"infecte_maire\":\"IRON_HELMET\",\"vampire-infecte\":\"CYAN_TERRACOTTA\",\"maire\":\"PURPLE_TERRACOTTA\",\"mort\":\"PUFFERFISH_BUCKET\",\"maire_mort\":\"SCUTE\",\"mort_vampire-infecte\":\"STRIPPED_SPRUCE_LOG\",\"maire_vampire-infecte\":\"BLACK_BED\",\"infecte_maire_mort\":\"DIAMOND_HOE\",\"maire_mort_vampire-infecte\":\"BIRCH_SLAB\"}}");
            for (Object key : mappings.keySet()) {
                HashMap<String, Material> map = new HashMap<String, Material>();
                JSONObject array = (JSONObject) mappings.get(key);
                for (Object key2 : array.keySet())
                    map.put((String) key2, Material.valueOf((String) array.get(key2)));
                try {
                    LGCustomItems.mappings.put((Class<? extends Role>) Class.forName("fr.timothefcn.mc.loupgarou.roles.R" + key), map);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Material getItem(Role role) {
        return mappings.get(role.getClass()).get("");
    }

    //TODO: Résoudre problème avec les icons
    public static Material getItem(LGPlayer player, ArrayList<String> constraints) {
        Bukkit.getPluginManager().callEvent(new LGCustomItemChangeEvent(player.getGame(), player, constraints));

        Collections.sort(constraints);
        HashMap<String, Material> mapps = mappings.get(player.getRole().getClass());
        if (mapps == null)
            return Material.AIR;//Lors du développement de rôles.
        StringJoiner sj = new StringJoiner("_");
        for (String s : constraints)
            sj.add(s);
        return mapps.get(sj.toString());
    }

    public static Material getItem(LGPlayer player) {
        return getItem(player, new ArrayList<String>());
    }

    public static void updateItem(LGPlayer lgp) {
        lgp.getPlayer().getInventory().setItemInOffHand(new ItemStack(getItem(lgp)));
        lgp.getPlayer().updateInventory();
    }

    public static void updateItem(LGPlayer lgp, ArrayList<String> constraints) {
        lgp.getPlayer().getInventory().setItemInOffHand(new ItemStack(getItem(lgp, constraints)));
        lgp.getPlayer().updateInventory();
    }

    @RequiredArgsConstructor
    public static enum LGCustomItemsConstraints {
        INFECTED("infecte"),
        MAYOR("maire"),
        DEAD("mort");
        @Getter
        private final String name;
    }

}