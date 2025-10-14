package net.thejadeproject.ascension.util;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.thejadeproject.ascension.AscensionCraft;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ModTags {


    public static class Items {

        public static final TagKey<Item> MEDICINAL = createTag("herbs/medicinal");
        public static final TagKey<Item> HUMAN = createTag("herbs/human");
        public static final TagKey<Item> BLADE = createTag("blade");
        public static final TagKey<Item> SPEAR = createTag("spear");
        public static final TagKey<Item> SPEAR_ENCHANTABLE = createTag("spear_enchantable");


        public static final TagKey<Item> BLACK_IRON_RAW = createCommonTag("raw_materials/black_iron");
        public static final TagKey<Item> INGOTS_BLACK_IRON = createCommonTag("ingots/black_iron");
        public static final TagKey<Item> NUGGETS_BLACK_IRON = createCommonTag("nuggets/black_iron");



        public static HashMap<String,TagKey<Item>> daoItemTags = new HashMap<>();

        //attributes/elements

        public static void  createDaoTag(String name){
            daoItemTags.put("ascension:"+name,createTag(name));
        }


        private static TagKey<Item> createCommonTag(String path) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", path));
        }

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, name));
        }
    }

    public static class Blocks {
        public static final TagKey<Block> DESTRUCTIBLE_BLOCKS = createTag("blocks_destruction");
        public static final TagKey<Block> INCORRECT_FOR_JADE_TOOL = createTag("incorrect_for_jade_tool");

        public static final TagKey<Block> STORAGE_BLOCKS_BLACK_IRON = createCommonTag("storage_blocks/black_iron");



        private static TagKey<Block> createCommonTag(String path) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath("c", path));
        }

        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, name));
        }
    }
}
