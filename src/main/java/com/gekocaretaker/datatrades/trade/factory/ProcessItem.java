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

public record ProcessItem(TradedItem toBeProcessed, int price, ItemStack processed, int maxUses, int experience, float multiplier,
                          Optional<RegistryKey<EnchantmentProvider>> enchantmentProviderKey, int level) implements TradeOfferFactory {
    public static final MapCodec<ProcessItem> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
                TradedItem.CODEC.fieldOf("to_be_processed").forGetter(ProcessItem::toBeProcessed),
                Codec.INT.fieldOf("price").forGetter(ProcessItem::price),
                ItemStack.CODEC.fieldOf("processed").forGetter(ProcessItem::processed),
                Codec.INT.fieldOf("max_uses").forGetter(ProcessItem::maxUses),
                Codec.INT.fieldOf("experience").forGetter(ProcessItem::experience),
                Codec.FLOAT.fieldOf("multiplier").forGetter(ProcessItem::multiplier),
                RegistryKey.createCodec(RegistryKeys.ENCHANTMENT_PROVIDER).optionalFieldOf("enchantment_provider").forGetter(ProcessItem::enchantmentProviderKey),
                Codec.INT.fieldOf("level").forGetter(ProcessItem::level)
        ).apply(instance, ProcessItem::new);
    });

    @Override
    public TradeOfferFactoryType<?> getType() {
        return TradeOfferFactoryTypes.PROCESS_ITEM;
    }

    @Nullable
    @Override
    public TradeOffer create(Entity entity, Random random) {
        ItemStack itemStack = this.processed.copy();
        World world = entity.getWorld();
        this.enchantmentProviderKey.ifPresent(key -> {
            EnchantmentHelper.applyEnchantmentProvider(itemStack, world.getRegistryManager(), key, world.getLocalDifficulty(entity.getBlockPos()), random);
        });
        return new TradeOffer(new TradedItem(Items.EMERALD, this.price), Optional.of(this.toBeProcessed), itemStack, 0, this.maxUses, this.experience, this.multiplier);
    }
}
