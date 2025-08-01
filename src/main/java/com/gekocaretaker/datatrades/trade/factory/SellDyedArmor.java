package com.gekocaretaker.datatrades.trade.factory;

import com.gekocaretaker.datatrades.trade.TradeOfferFactory;
import com.gekocaretaker.datatrades.trade.TradeOfferFactoryType;
import com.gekocaretaker.datatrades.trade.TradeOfferFactoryTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.entity.Entity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record SellDyedArmor(Item sell, int price, int maxUses, int experience, int level) implements TradeOfferFactory {
    public static final MapCodec<SellDyedArmor> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
                Registries.ITEM.getCodec().fieldOf("sell").forGetter(SellDyedArmor::sell),
                Codec.INT.optionalFieldOf("price", 1).forGetter(SellDyedArmor::price),
                Codec.INT.optionalFieldOf("max_uses", 12).forGetter(SellDyedArmor::maxUses),
                Codec.INT.optionalFieldOf("experience", 1).forGetter(SellDyedArmor::experience),
                Codec.INT.optionalFieldOf("level", 1).forGetter(SellDyedArmor::level)
        ).apply(instance, SellDyedArmor::new);
    });

    @Override
    public TradeOfferFactoryType<?> getType() {
        return TradeOfferFactoryTypes.SELL_DYED_ARMOR;
    }

    @Nullable
    @Override
    public TradeOffer create(Entity entity, Random random) {
        TradedItem tradedItem = new TradedItem(Items.EMERALD, this.price);
        ItemStack itemStack = new ItemStack(this.sell);
        if (itemStack.isIn(ItemTags.DYEABLE)) {
            List<DyeItem> list = Lists.newArrayList();
            list.add(getDye(random));
            if (random.nextFloat() > 0.7F) {
                list.add(getDye(random));
            }
            if (random.nextFloat() > 0.8F) {
                list.add(getDye(random));
            }
            itemStack = DyedColorComponent.setColor(itemStack, list);
        }
        return new TradeOffer(tradedItem, itemStack, this.maxUses, this.experience, 0.2F);
    }

    private static DyeItem getDye(Random random) {
        return DyeItem.byColor(DyeColor.byIndex(random.nextInt(16)));
    }
}
