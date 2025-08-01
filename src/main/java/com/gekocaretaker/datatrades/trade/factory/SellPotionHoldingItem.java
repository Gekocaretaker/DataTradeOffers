package com.gekocaretaker.datatrades.trade.factory;

import com.gekocaretaker.datatrades.trade.TradeOfferFactory;
import com.gekocaretaker.datatrades.trade.TradeOfferFactoryType;
import com.gekocaretaker.datatrades.trade.TradeOfferFactoryTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Util;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public record SellPotionHoldingItem(Item sell, int sellCount, int price, int maxUses, int experience, Item secondBuy, int secondCount, float multiplier, int level) implements TradeOfferFactory {
    public static final MapCodec<SellPotionHoldingItem> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
                Registries.ITEM.getCodec().fieldOf("sell").forGetter(SellPotionHoldingItem::sell),
                Codec.INT.optionalFieldOf("sell_count", 1).forGetter(SellPotionHoldingItem::sellCount),
                Codec.INT.optionalFieldOf("price", 1).forGetter(SellPotionHoldingItem::price),
                Codec.INT.optionalFieldOf("max_uses", 12).forGetter(SellPotionHoldingItem::maxUses),
                Codec.INT.optionalFieldOf("experience", 1).forGetter(SellPotionHoldingItem::experience),
                Registries.ITEM.getCodec().fieldOf("second_buy").forGetter(SellPotionHoldingItem::secondBuy),
                Codec.INT.optionalFieldOf("second_count", 1).forGetter(SellPotionHoldingItem::secondCount),
                Codec.FLOAT.optionalFieldOf("multiplier", 0.05F).forGetter(SellPotionHoldingItem::multiplier),
                Codec.INT.optionalFieldOf("level", 1).forGetter(SellPotionHoldingItem::level)
        ).apply(instance, SellPotionHoldingItem::new);
    });

    @Override
    public TradeOfferFactoryType<?> getType() {
        return TradeOfferFactoryTypes.SELL_POTION_HOLDING_ITEM;
    }

    @Nullable
    @Override
    public TradeOffer create(Entity entity, Random random) {
        TradedItem tradedItem = new TradedItem(Items.EMERALD, this.price);
        List<RegistryEntry<Potion>> list = Registries.POTION.streamEntries().filter((entry) -> {
            return !entry.value().getEffects().isEmpty() && entity.getWorld().getBrewingRecipeRegistry().isBrewable(entry);
        }).collect(Collectors.toList());
        RegistryEntry<Potion> registryEntry = Util.getRandom(list, random);
        ItemStack itemStack = new ItemStack(this.sell, this.sellCount);
        itemStack.set(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(registryEntry));
        return new TradeOffer(tradedItem, Optional.of(new TradedItem(this.secondBuy, this.secondCount)), itemStack, this.maxUses, this.experience, this.multiplier);
    }
}
