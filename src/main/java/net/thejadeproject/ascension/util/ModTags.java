package net.thejadeproject.ascension.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.thejadeproject.ascension.AscensionCraft;

public class ModTags {


    public static class Items {

        public static final TagKey<Item> FIRE = createTag("herbs/elements/fire");
        public static final TagKey<Item> EARTH = createTag("herbs/elements/earth");
        public static final TagKey<Item> METAL = createTag("herbs/elements/metal");
        public static final TagKey<Item> MEDICINAL = createTag("herbs/medicinal");
        public static final TagKey<Item> HUMAN = createTag("herbs/human");
        public static final TagKey<Item> ALCHEMY_FAILURE = createTag("herbs/alchemy_failure");


        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, name));
        }
    }
}
