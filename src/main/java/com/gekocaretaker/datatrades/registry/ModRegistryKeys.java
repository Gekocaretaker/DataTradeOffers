package com.gekocaretaker.datatrades.registry;

import com.gekocaretaker.datatrades.DataTrades;
import com.gekocaretaker.datatrades.trade.ProfessionManager;
import com.gekocaretaker.datatrades.trade.TradeOfferFactory;
import com.gekocaretaker.datatrades.trade.TradeOfferFactoryType;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class ModRegistryKeys {
    public static final RegistryKey<Registry<TradeOfferFactoryType<?>>> TRADE_OFFER_FACTORY_TYPE = of("trade_offer_type");
    public static final RegistryKey<Registry<TradeOfferFactory>> TRADE_OFFERS = of("trade_offers");
    public static final RegistryKey<Registry<ProfessionManager>> PROFESSIONS = of("professions");

    private static <T> RegistryKey<Registry<T>> of(String id) {
        return RegistryKey.ofRegistry(DataTrades.id(id));
    }

    public static void init() {}

    private ModRegistryKeys() {}
}
