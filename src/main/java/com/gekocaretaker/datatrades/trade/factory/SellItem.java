package com.gekocaretaker.datatrades.trade.factory;

import com.gekocaretaker.datatrades.trade.TradeOfferFactory;
import com.gekocaretaker.datatrades.trade.TradeOfferFactoryType;
import com.gekocaretaker.datatrades.trade.TradeOfferFactoryTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.provider.EnchantmentProvider;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record SellItem(ItemStack sell, int price, int maxUses, int experience, float multiplier, Optional<RegistryKey<EnchantmentProvider>> enchantmentProviderKey, int level) implements TradeOfferFactory {
    public static final MapCodec<SellItem> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
                ItemStack.CODEC.fieldOf("sell").forGetter(SellItem::sell),
                Codec.INT.optionalFieldOf("price", 1).forGetter(SellItem::price),
                Codec.INT.optionalFieldOf("max_uses", 12).forGetter(SellItem::maxUses),
                Codec.INT.optionalFieldOf("experience", 1).forGetter(SellItem::experience),
                Codec.FLOAT.optionalFieldOf("multiplier", 0.05F).forGetter(SellItem::multiplier),
                RegistryKey.createCodec(RegistryKeys.ENCHANTMENT_PROVIDER).optionalFieldOf("enchantment_provider").forGetter(SellItem::enchantmentProviderKey),
                Codec.INT.optionalFieldOf("level", 1).forGetter(SellItem::level)
        ).apply(instance, SellItem::new);
    });

    @Override
    public TradeOfferFactoryType<?> getType() {
        return TradeOfferFactoryTypes.SELL_ITEM;
    }

    @Nullable
    @Override
    public TradeOffer create(Entity entity, Random random) {
        ItemStack itemStack = this.sell.copy();
        World world = entity.getWorld();
        this.enchantmentProviderKey.ifPresent((key) -> {
            EnchantmentHelper.applyEnchantmentProvider(itemStack, world.getRegistryManager(), key, world.getLocalDifficulty(entity.getBlockPos()), random);
        });
        return new TradeOffer(new TradedItem(Items.EMERALD, this.price), itemStack, this.maxUses, this.experience, this.multiplier);
    }
}
