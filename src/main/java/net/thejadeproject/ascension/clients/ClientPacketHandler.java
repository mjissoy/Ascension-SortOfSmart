package net.thejadeproject.ascension.clients;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.thejadeproject.ascension.menus.karmicledger.KarmicLedgerScreen;
import net.thejadeproject.ascension.network.clientBound.OpenKarmicLedgerScreen;

@OnlyIn(Dist.CLIENT)
public class ClientPacketHandler {
    public static void handleOpenKarmicLedgerScreen(OpenKarmicLedgerScreen packet) {
        Minecraft.getInstance().execute(() -> {
            Minecraft.getInstance().setScreen(new KarmicLedgerScreen(packet));
        });
    }
}
