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
                Codec.INT.fieldOf("sell_count").forGetter(SellPotionHoldingItem::sellCount),
                Codec.INT.fieldOf("price").forGetter(SellPotionHoldingItem::price),
                Codec.INT.fieldOf("map_uses").forGetter(SellPotionHoldingItem::maxUses),
                Codec.INT.fieldOf("experience").forGetter(SellPotionHoldingItem::experience),
                Registries.ITEM.getCodec().fieldOf("second_buy").forGetter(SellPotionHoldingItem::secondBuy),
                Codec.INT.fieldOf("second_count").forGetter(SellPotionHoldingItem::secondCount),
                Codec.FLOAT.fieldOf("multiplier").forGetter(SellPotionHoldingItem::multiplier),
                Codec.INT.fieldOf("level").forGetter(SellPotionHoldingItem::level)
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
