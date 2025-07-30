package com.gekocaretaker.datatrades.trade;

import com.gekocaretaker.datatrades.registry.ModRegistries;
import com.mojang.serialization.Codec;
import net.minecraft.registry.RegistryKey;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;

public interface TradeOfferFactory extends TradeOffers.Factory {
    Codec<TradeOfferFactory> CODEC = ModRegistries.TRADE_OFFER_FACTORY_TYPE.getCodec()
            .dispatch("type", TradeOfferFactory::getType, TradeOfferFactoryType::codec);

    TradeOfferFactoryType<?> getType();

    int level();
}
