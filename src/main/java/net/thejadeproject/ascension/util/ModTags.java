package net.thejadeproject.ascension.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.thejadeproject.ascension.AscensionCraft;

public class ModTags {


    public static class Items {

        public static final TagKey<Item> FIRE = createTag("herbs/elements");
        public static final TagKey<Item> MEDICINAL = createTag("herbs/medicinal");



        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, name));
        }
    }
}
