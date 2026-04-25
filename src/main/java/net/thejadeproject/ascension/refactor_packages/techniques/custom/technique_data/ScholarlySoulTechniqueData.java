package net.thejadeproject.ascension.refactor_packages.techniques.custom.technique_data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.refactor_packages.techniques.ITechniqueData;
import net.thejadeproject.ascension.refactor_packages.util.ByteBufUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ScholarlySoulTechniqueData implements ITechniqueData {

    private static final String UNLOCKED_CHAPTERS = "unlocked_chapters";

    public static final ResourceLocation INTRODUCTION =
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "scholarly_soul/introduction");

    public static final ResourceLocation RECTIFICATION_OF_NAMES =
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "scholarly_soul/rectification_of_names");

    public static final ResourceLocation GREAT_LEARNING =
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "scholarly_soul/great_learning");

    public static final ResourceLocation THOUSAND_COMMENTARIES =
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "scholarly_soul/thousand_commentaries");

    public static final ResourceLocation SAGE_MANDATE =
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "scholarly_soul/sage_mandate");

    private static final Map<ResourceLocation, Integer> CHAPTER_MAJOR_REALM_UNLOCKS = new HashMap<>();

    static {
        CHAPTER_MAJOR_REALM_UNLOCKS.put(INTRODUCTION, 1);
        CHAPTER_MAJOR_REALM_UNLOCKS.put(RECTIFICATION_OF_NAMES, 2);
        CHAPTER_MAJOR_REALM_UNLOCKS.put(GREAT_LEARNING, 3);
        CHAPTER_MAJOR_REALM_UNLOCKS.put(THOUSAND_COMMENTARIES, 4);
        CHAPTER_MAJOR_REALM_UNLOCKS.put(SAGE_MANDATE, 5);
    }

    private final Set<ResourceLocation> unlockedChapters = new HashSet<>();

    public ScholarlySoulTechniqueData() {
        unlockChapter(INTRODUCTION);
    }

    public ScholarlySoulTechniqueData(CompoundTag tag) {
        ListTag chapters = tag.getList(UNLOCKED_CHAPTERS, Tag.TAG_STRING);

        for (int i = 0; i < chapters.size(); i++) {
            unlockedChapters.add(ResourceLocation.parse(chapters.getString(i)));
        }

        if (unlockedChapters.isEmpty()) {
            unlockChapter(INTRODUCTION);
        }
    }

    public ScholarlySoulTechniqueData(RegistryFriendlyByteBuf buf) {
        int size = buf.readInt();

        for (int i = 0; i < size; i++) {
            unlockedChapters.add(ByteBufUtil.readResourceLocation(buf));
        }

        if (unlockedChapters.isEmpty()) {
            unlockChapter(INTRODUCTION);
        }
    }

    public boolean hasChapter(ResourceLocation chapter) {
        return unlockedChapters.contains(chapter);
    }

    public boolean unlockChapter(ResourceLocation chapter) {
        if (!CHAPTER_MAJOR_REALM_UNLOCKS.containsKey(chapter)) {
            return false;
        }

        return unlockedChapters.add(chapter);
    }

    public Set<ResourceLocation> getUnlockedChapters() {
        return Set.copyOf(unlockedChapters);
    }

    public int getMaxUnlockedMajorRealm() {
        int maxMajorRealm = 1;

        for (ResourceLocation chapter : unlockedChapters) {
            maxMajorRealm = Math.max(maxMajorRealm, getMajorRealmUnlockedByChapter(chapter));
        }

        return maxMajorRealm;
    }

    public boolean canCultivateMajorRealm(int majorRealm) {
        return majorRealm <= getMaxUnlockedMajorRealm();
    }

    public static int getMajorRealmUnlockedByChapter(ResourceLocation chapter) {
        return CHAPTER_MAJOR_REALM_UNLOCKS.getOrDefault(chapter, 1);
    }

    @Override
    public CompoundTag write() {
        CompoundTag tag = new CompoundTag();
        ListTag chapters = new ListTag();

        for (ResourceLocation chapter : unlockedChapters) {
            chapters.add(StringTag.valueOf(chapter.toString()));
        }

        tag.put(UNLOCKED_CHAPTERS, chapters);
        return tag;
    }

    @Override
    public void encode(RegistryFriendlyByteBuf buf) {
        buf.writeInt(unlockedChapters.size());

        for (ResourceLocation chapter : unlockedChapters) {
            ByteBufUtil.encodeString(buf, chapter.toString());
        }
    }
}