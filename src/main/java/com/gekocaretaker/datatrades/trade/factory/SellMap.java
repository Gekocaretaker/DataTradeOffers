package com.gekocaretaker.datatrades.trade.factory;

import com.gekocaretaker.datatrades.trade.TradeOfferFactory;
import com.gekocaretaker.datatrades.trade.TradeOfferFactoryType;
import com.gekocaretaker.datatrades.trade.TradeOfferFactoryTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapDecorationType;
import net.minecraft.item.map.MapState;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.Structure;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record SellMap(int price, TagKey<Structure> structure, String nameKey, RegistryEntry<MapDecorationType> decoration, int maxUses, int experience, int level) implements TradeOfferFactory {
    public static final MapCodec<SellMap> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
                Codec.INT.fieldOf("price").forGetter(SellMap::price),
                TagKey.codec(RegistryKeys.STRUCTURE).fieldOf("structure").forGetter(SellMap::structure),
                Codec.STRING.fieldOf("name_key").forGetter(SellMap::nameKey),
                MapDecorationType.CODEC.fieldOf("decoration").forGetter(SellMap::decoration),
                Codec.INT.fieldOf("max_uses").forGetter(SellMap::maxUses),
                Codec.INT.fieldOf("experience").forGetter(SellMap::experience),
                Codec.INT.fieldOf("level").forGetter(SellMap::level)
        ).apply(instance, SellMap::new);
    });

    @Override
    public TradeOfferFactoryType<?> getType() {
        return TradeOfferFactoryTypes.SELL_MAP;
    }

    @Nullable
    @Override
    public TradeOffer create(Entity entity, Random random) {
        World world = entity.getWorld();
        if (world instanceof ServerWorld serverWorld) {
            BlockPos blockPos = serverWorld.locateStructure(this.structure, entity.getBlockPos(), 100, true);
            if (blockPos != null) {
                ItemStack itemStack = FilledMapItem.createMap(serverWorld, blockPos.getX(), blockPos.getZ(), (byte) 2, true, true);
                MapState.addDecorationsNbt(itemStack, blockPos, "+", this.decoration);
                itemStack.set(DataComponentTypes.ITEM_NAME, Text.translatable(this.nameKey));
                return new TradeOffer(new TradedItem(Items.EMERALD, this.price), Optional.of(new TradedItem(Items.COMPASS)), itemStack, this.maxUses, this.experience, 0.2F);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
