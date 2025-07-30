package com.gekocaretaker.datatrades.trade.factory;

import com.gekocaretaker.datatrades.trade.TradeOfferFactory;
import com.gekocaretaker.datatrades.trade.TradeOfferFactoryType;
import com.gekocaretaker.datatrades.trade.TradeOfferFactoryTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import org.jetbrains.annotations.Nullable;

public record Empty(int level) implements TradeOfferFactory {
    public static final MapCodec<Empty> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
                Codec.INT.optionalFieldOf("level", 0).forGetter(Empty::level)
        ).apply(instance, Empty::new);
    });

    @Override
    public TradeOfferFactoryType<?> getType() {
        return TradeOfferFactoryTypes.EMPTY;
    }

    @Nullable
    @Override
    public TradeOffer create(Entity entity, Random random) {
        return null;
    }
}
