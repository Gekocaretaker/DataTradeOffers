package com.gekocaretaker.datatrades.trade.factory;

import com.gekocaretaker.datatrades.trade.TradeOfferFactory;
import com.gekocaretaker.datatrades.trade.TradeOfferFactoryType;
import com.gekocaretaker.datatrades.trade.TradeOfferFactoryTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.predicate.component.ComponentMapPredicate;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;
import org.jetbrains.annotations.Nullable;

public record BuyItem(ItemStack item, int count, int maxUses, int experience, int price, int level) implements TradeOfferFactory {
    public static final MapCodec<BuyItem> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
                ItemStack.UNCOUNTED_CODEC.fieldOf("item").forGetter(BuyItem::item),
                Codec.INT.fieldOf("count").forGetter(BuyItem::count),
                Codec.INT.fieldOf("max_uses").forGetter(BuyItem::maxUses),
                Codec.INT.fieldOf("experience").forGetter(BuyItem::experience),
                Codec.INT.optionalFieldOf("price", 1).forGetter(BuyItem::price),
                Codec.INT.optionalFieldOf("level", 1).forGetter(BuyItem::level)
        ).apply(instance, BuyItem::new);
    });

    @Override
    public TradeOfferFactoryType<?> getType() {
        return TradeOfferFactoryTypes.BUY_ITEM;
    }

    @Nullable
    @Override
    public TradeOffer create(Entity entity, Random random) {
        return new TradeOffer(
                new TradedItem(this.item.getItem().getRegistryEntry(), this.count, ComponentMapPredicate.of(this.item.getComponents())),
                new ItemStack(Items.EMERALD, this.price), this.maxUses, this.experience, 0.05F);
    }
}
