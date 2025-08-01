package com.gekocaretaker.datatrades.trade.factory;

import com.gekocaretaker.datatrades.trade.TradeOfferFactory;
import com.gekocaretaker.datatrades.trade.TradeOfferFactoryType;
import com.gekocaretaker.datatrades.trade.TradeOfferFactoryTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.SuspiciousStewEffectsComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record SellSuspiciousStew(RegistryEntry<StatusEffect> effect, int duration, int experience, float multiplier, int level) implements TradeOfferFactory {
    public static final MapCodec<SellSuspiciousStew> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
                Registries.STATUS_EFFECT.getEntryCodec().fieldOf("effect").forGetter(SellSuspiciousStew::effect),
                Codec.INT.fieldOf("duration").forGetter(SellSuspiciousStew::duration),
                Codec.INT.optionalFieldOf("experience", 1).forGetter(SellSuspiciousStew::experience),
                Codec.FLOAT.optionalFieldOf("multiplier", 0.05F).forGetter(SellSuspiciousStew::multiplier),
                Codec.INT.optionalFieldOf("level", 1).forGetter(SellSuspiciousStew::level)
        ).apply(instance, SellSuspiciousStew::new);
    });

    @Override
    public TradeOfferFactoryType<?> getType() {
        return TradeOfferFactoryTypes.SELL_SUSPICIOUS_STEW;
    }

    @Nullable
    @Override
    public TradeOffer create(Entity entity, Random random) {
        ItemStack itemStack = new ItemStack(Items.SUSPICIOUS_STEW, 1);
        itemStack.set(DataComponentTypes.SUSPICIOUS_STEW_EFFECTS, new SuspiciousStewEffectsComponent(
                List.of(new SuspiciousStewEffectsComponent.StewEffect(this.effect, this.duration))));
        return new TradeOffer(new TradedItem(Items.EMERALD), itemStack, 12, this.experience, this.multiplier);
    }
}
