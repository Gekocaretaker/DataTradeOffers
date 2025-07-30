package com.gekocaretaker.datatrades.registry;

import com.gekocaretaker.datatrades.trade.ProfessionManager;
import com.gekocaretaker.datatrades.trade.TradeOfferFactory;
import com.gekocaretaker.datatrades.trade.TradeOfferFactoryType;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.registry.Registry;

public class ModRegistries {
    public static final Registry<TradeOfferFactoryType<?>> TRADE_OFFER_FACTORY_TYPE =
            FabricRegistryBuilder.createSimple(ModRegistryKeys.TRADE_OFFER_FACTORY_TYPE)
                    .attribute(RegistryAttribute.SYNCED).buildAndRegister();

    public static void init() {
        DynamicRegistries.registerSynced(ModRegistryKeys.TRADE_OFFERS, TradeOfferFactory.CODEC);
        DynamicRegistries.registerSynced(ModRegistryKeys.PROFESSIONS, ProfessionManager.CODEC);
    }

    private ModRegistries() {}
}
