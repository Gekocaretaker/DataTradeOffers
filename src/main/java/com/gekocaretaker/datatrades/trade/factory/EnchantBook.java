package com.gekocaretaker.datatrades.trade.factory;

import com.gekocaretaker.datatrades.trade.TradeOfferFactory;
import com.gekocaretaker.datatrades.trade.TradeOfferFactoryType;
import com.gekocaretaker.datatrades.trade.TradeOfferFactoryTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record EnchantBook(int experience, int minLevel, int maxLevel, TagKey<Enchantment> possibleEnchantments,
                          int level) implements TradeOfferFactory {
    public static final MapCodec<EnchantBook> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
                Codec.INT.fieldOf("experience").forGetter(EnchantBook::experience),
                Codec.INT.optionalFieldOf("min_enchant_level", 0).forGetter(EnchantBook::minLevel),
                Codec.INT.optionalFieldOf("max_enchant_level", Integer.MAX_VALUE).forGetter(EnchantBook::maxLevel),
                TagKey.codec(RegistryKeys.ENCHANTMENT).fieldOf("enchantments").forGetter(EnchantBook::possibleEnchantments),
                Codec.INT.optionalFieldOf("level", 1).forGetter(EnchantBook::level)
        ).apply(instance, EnchantBook::new);
    });

    @Override
    public TradeOfferFactoryType<?> getType() {
        return TradeOfferFactoryTypes.ENCHANT_BOOK;
    }

    @Nullable
    @Override
    public TradeOffer create(Entity entity, Random random) {
        Optional<RegistryEntry<Enchantment>> optional = entity.getWorld().getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getRandomEntry(this.possibleEnchantments, random);
        int price;
        ItemStack itemStack;
        if (optional.isPresent()) {
            RegistryEntry<Enchantment> registryEntry = optional.get();
            Enchantment enchantment = registryEntry.value();
            int neededMin = Math.max(enchantment.getMinLevel(), this.minLevel);
            int neededMax = Math.min(enchantment.getMaxLevel(), this.maxLevel);
            int enchantLevel = MathHelper.nextInt(random, neededMin, neededMax);
            itemStack = EnchantmentHelper.getEnchantedBookWith(new EnchantmentLevelEntry(registryEntry, enchantLevel));
            price = 2 + random.nextInt(5 + enchantLevel * 10) + 3 * enchantLevel;
            if (registryEntry.isIn(EnchantmentTags.DOUBLE_TRADE_PRICE)) {
                price *= 2;
            }

            if (price > 64) {
                price = 64;
            }
        } else {
            price = 1;
            itemStack = new ItemStack(Items.BOOK);
        }

        return new TradeOffer(new TradedItem(Items.EMERALD, price), Optional.of(new TradedItem(Items.BOOK)), itemStack, 12, this.experience, 0.2F);
    }
}
