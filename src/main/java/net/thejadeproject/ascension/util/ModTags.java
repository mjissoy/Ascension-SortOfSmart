package net.thejadeproject.ascension.util;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.thejadeproject.ascension.AscensionCraft;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ModTags {


    public static class Items {

        public static final TagKey<Item> MEDICINAL = createTag("herbs/medicinal");
        public static final TagKey<Item> HUMAN = createTag("herbs/human");
        public static final TagKey<Item> ALCHEMY_FAILURE = createTag("herbs/alchemy_failure");
        public static final TagKey<Item> ALCHEMY_SUCCESS = createTag("herbs/alchemy_success");




        public static HashMap<String,TagKey<Item>> daoItemTags = new HashMap<>();

        //attributes/elements

        public static void  createDaoTag(String name){
            daoItemTags.put("ascension:"+name,createTag(name));
        }


        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, name));
        }
    }
}
