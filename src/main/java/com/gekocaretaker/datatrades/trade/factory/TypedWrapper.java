package com.gekocaretaker.datatrades.trade.factory;

import com.gekocaretaker.datatrades.trade.TradeOfferFactory;
import com.gekocaretaker.datatrades.trade.TradeOfferFactoryType;
import com.gekocaretaker.datatrades.trade.TradeOfferFactoryTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.VillagerDataContainer;
import net.minecraft.village.VillagerType;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public record TypedWrapper(Map<RegistryKey<VillagerType>, TradeOfferFactory> typeToFactory, int level) implements TradeOfferFactory {
    public static final Codec<Map<RegistryKey<VillagerType>, TradeOfferFactory>> VILLAGER_TYPE_TO_TRADE_OFFER_FACTORY_CODEC = Codec.unboundedMap(
            RegistryKey.createCodec(RegistryKeys.VILLAGER_TYPE), TradeOfferFactory.CODEC
    );
    public static final MapCodec<TypedWrapper> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
                VILLAGER_TYPE_TO_TRADE_OFFER_FACTORY_CODEC.fieldOf("trade_offers_by_villager_type").forGetter(TypedWrapper::typeToFactory),
                Codec.INT.fieldOf("level").forGetter(TypedWrapper::level)
        ).apply(instance, TypedWrapper::new);
    });

    @Override
    public TradeOfferFactoryType<?> getType() {
        return TradeOfferFactoryTypes.TYPED_WRAPPER;
    }

    @SafeVarargs
    public static TypedWrapper of(TradeOfferFactory factory, int level, RegistryKey<VillagerType>... types) {
        return new TypedWrapper(Arrays.stream(types).collect(Collectors.toMap(registryKey -> {
            return registryKey;
        }, registryKey -> {
            return factory;
        })), level);
    }

    @Nullable
    @Override
    public TradeOffer create(Entity entity, Random random) {
        if (entity instanceof VillagerDataContainer villagerDataContainer) {
            RegistryKey<VillagerType> registryKey = villagerDataContainer.getVillagerData().type().getKey().orElse(null);
            if (registryKey == null) {
                return null;
            } else {
                TradeOfferFactory factory = this.typeToFactory.get(registryKey);
                return factory == null ? null : factory.create(entity, random);
            }
        } else {
            return null;
        }
    }
}
