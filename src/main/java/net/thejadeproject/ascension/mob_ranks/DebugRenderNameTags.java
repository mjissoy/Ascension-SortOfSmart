package net.thejadeproject.ascension.mob_ranks;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.data_attachments.ModAttachments;

@EventBusSubscriber(modid = AscensionCraft.MOD_ID, value = Dist.CLIENT)
public class DebugRenderNameTags {

    private static String formatRealm(String realmId) {
        String[] parts = realmId.split("_");
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < parts.length; i++) {
            if (parts[i].isEmpty()) continue;

            String word = parts[i];
            builder.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1));

            if (i < parts.length - 1) {
                builder.append(" ");
            }
        }

        return builder.toString();
    }


    @SubscribeEvent
    public static void onRenderNameTag(RenderNameTagEvent event) {
        if (!(event.getEntity() instanceof LivingEntity entity)) return;

        MobRankData data = entity.getData(ModAttachments.MOB_RANK);
        if (data == null || !data.isInitialized()) return;

        String realm = formatRealm(data.getRealmId());
        int stage = data.getStage();

        MobRankCategory category = MobRankResolver.resolveCategory(entity);

        Component original = event.getContent();

        Component combined = original.copy()
                .append(" ")
                .append(Component.literal("[" + category.name() + "]"))
                .append(" ")
                .append(Component.literal("[" + realm + " " + stage + "]"));

        event.setContent(combined);
    }

}
