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

        public static final TagKey<Item> FIRE = createTag("herbs/elements/fire");
        public static final TagKey<Item> EARTH = createTag("herbs/elements/earth");
        public static final TagKey<Item> METAL = createTag("herbs/elements/metal");
        public static final TagKey<Item> MEDICINAL = createTag("herbs/medicinal");
        public static final TagKey<Item> HUMAN = createTag("herbs/human");
        public static final TagKey<Item> ALCHEMY_FAILURE = createTag("herbs/alchemy_failure");
        public static final TagKey<Item> ALCHEMY_SUCCESS = createTag("herbs/alchemy_success");



        public static HashMap<TagKey<Item>, Component> tagDisplayData = new HashMap<>();
        public static HashSet<TagKey<Item>> ASCENSION_ATTRIBUTES = new HashSet<>();


        //attributes/elements
        public static final TagKey<Item> SWORD_INTENT = createAscensionAttributeTag("sword_intent","§8[Sword Intent]");
        public static final TagKey<Item> FIST_INTENT = createAscensionAttributeTag("fist_intent","§8[Fist Intent]");
        public static final TagKey<Item> SPEAR_INTENT = createAscensionAttributeTag("spear_intent","§8[Spear Intent]");
        public static final TagKey<Item> BLADE_INTENT = createAscensionAttributeTag("blade_intent","§8[Blade Intent]");
        public static final TagKey<Item> BOW_INTENT = createAscensionAttributeTag("bow_intent","§8[Bow Intent]");

        private static TagKey<Item> createAscensionAttributeTag(String name,String displayName){
            TagKey<Item> tag = createTag(name);
            tagDisplayData.put(tag,Component.literal(displayName));
            ASCENSION_ATTRIBUTES.add(tag);
            return tag;
        }


        private static TagKey<Item> createAscensionAttributeTag(String name){
            TagKey<Item> tag = createTag(name);
            tagDisplayData.put(tag,Component.translatable("tag_name."+name));
            ASCENSION_ATTRIBUTES.add(tag);
            return tag;
        }

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, name));
        }
    }
}
