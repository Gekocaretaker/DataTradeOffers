package com.gekocaretaker.datatrades.trade.factory;

import com.gekocaretaker.datatrades.trade.TradeOfferFactory;
import com.gekocaretaker.datatrades.trade.TradeOfferFactoryType;
import com.gekocaretaker.datatrades.trade.TradeOfferFactoryTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record SellEnchantedTool(ItemStack tool, int basePrice, int maxUses, int experience, float multiplier, int level) implements TradeOfferFactory {
    public static final MapCodec<SellEnchantedTool> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
                ItemStack.UNCOUNTED_CODEC.fieldOf("tool").forGetter(SellEnchantedTool::tool),
                Codec.INT.optionalFieldOf("base_price", 1).forGetter(SellEnchantedTool::basePrice),
                Codec.INT.optionalFieldOf("max_uses", 12).forGetter(SellEnchantedTool::maxUses),
                Codec.INT.optionalFieldOf("experience", 1).forGetter(SellEnchantedTool::experience),
                Codec.FLOAT.optionalFieldOf("multiplier", 0.05F).forGetter(SellEnchantedTool::multiplier),
                Codec.INT.optionalFieldOf("level", 1).forGetter(SellEnchantedTool::level)
        ).apply(instance, SellEnchantedTool::new);
    });

    @Override
    public TradeOfferFactoryType<?> getType() {
        return TradeOfferFactoryTypes.SELL_ENCHANTED_TOOL;
    }

    @Nullable
    @Override
    public TradeOffer create(Entity entity, Random random) {
        int usedLevel = 5 + random.nextInt(15);
        DynamicRegistryManager dynamicRegistryManager = entity.getWorld().getRegistryManager();
        Optional<RegistryEntryList.Named<Enchantment>> optional = dynamicRegistryManager.getOrThrow(RegistryKeys.ENCHANTMENT).getOptional(EnchantmentTags.ON_TRADED_EQUIPMENT);
        ItemStack itemStack = EnchantmentHelper.enchant(random, new ItemStack(this.tool.getItem()), usedLevel, dynamicRegistryManager, optional);
        int price = Math.min(this.basePrice + usedLevel, 64);
        TradedItem tradedItem = new TradedItem(Items.EMERALD, price);
        return new TradeOffer(tradedItem, itemStack, this.maxUses, this.experience, this.multiplier);
    }
}
