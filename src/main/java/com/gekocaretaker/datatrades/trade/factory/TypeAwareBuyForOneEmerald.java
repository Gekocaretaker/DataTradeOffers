package com.gekocaretaker.datatrades.trade.factory;

import com.gekocaretaker.datatrades.trade.TradeOfferFactory;
import com.gekocaretaker.datatrades.trade.TradeOfferFactoryType;
import com.gekocaretaker.datatrades.trade.TradeOfferFactoryTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;
import net.minecraft.village.VillagerDataContainer;
import net.minecraft.village.VillagerType;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public record TypeAwareBuyForOneEmerald(Map<RegistryKey<VillagerType>, Item> map, int count, int maxUses, int experience, int level) implements TradeOfferFactory {
    public static final Codec<Map<RegistryKey<VillagerType>, Item>> VILLAGER_TYPE_TO_ITEM_MAP_CODEC = Codec.unboundedMap(
            RegistryKey.createCodec(RegistryKeys.VILLAGER_TYPE), Registries.ITEM.getCodec()
    );
    public static final MapCodec<TypeAwareBuyForOneEmerald> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
                VILLAGER_TYPE_TO_ITEM_MAP_CODEC.fieldOf("items_by_type").forGetter(TypeAwareBuyForOneEmerald::map),
                Codec.INT.fieldOf("count").forGetter(TypeAwareBuyForOneEmerald::count),
                Codec.INT.fieldOf("max_uses").forGetter(TypeAwareBuyForOneEmerald::maxUses),
                Codec.INT.fieldOf("experience").forGetter(TypeAwareBuyForOneEmerald::experience),
                Codec.INT.fieldOf("level").forGetter(TypeAwareBuyForOneEmerald::level)
        ).apply(instance, TypeAwareBuyForOneEmerald::new);
    });

    @Override
    public TradeOfferFactoryType<?> getType() {
        return TradeOfferFactoryTypes.TYPE_AWARE_BUY_FOR_ONE_EMERALD;
    }

    @Nullable
    @Override
    public TradeOffer create(Entity entity, Random random) {
        if (entity instanceof VillagerDataContainer villagerDataContainer) {
            RegistryKey<VillagerType> registryKey = villagerDataContainer.getVillagerData().type().getKey().orElse(null);
            if (registryKey == null) {
                return null;
            } else {
                TradedItem tradedItem = new TradedItem(this.map.get(registryKey), this.count);
                return new TradeOffer(tradedItem, new ItemStack(Items.EMERALD), this.maxUses, this.experience, 0.05F);
            }
        } else {
            return null;
        }
    }
}
