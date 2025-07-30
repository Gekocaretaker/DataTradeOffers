package com.gekocaretaker.datatrades;

import com.gekocaretaker.datatrades.registry.ModEventLoader;
import com.gekocaretaker.datatrades.registry.ModRegistries;
import com.gekocaretaker.datatrades.registry.ModRegistryKeys;
import com.gekocaretaker.datatrades.trade.TradeOfferFactoryTypes;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class DataTrades implements ModInitializer {
    public static final String ID = "datatrades";

    @Override
    public void onInitialize() {
        ModRegistryKeys.init();
        ModRegistries.init();
        TradeOfferFactoryTypes.init();
        ModEventLoader.init();
    }

    public static Identifier id(String path) {
        return Identifier.of(ID, path);
    }
}
