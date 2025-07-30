package com.gekocaretaker.datatrades.trade;

import com.gekocaretaker.datatrades.DataTrades;
import com.gekocaretaker.datatrades.registry.ModRegistries;
import com.gekocaretaker.datatrades.trade.factory.*;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.registry.Registry;
import net.minecraft.server.MinecraftServer;

public class TradeOfferFactoryTypes {
    public static final TradeOfferFactoryType<TypedWrapper> TYPED_WRAPPER = register("typed_wrapper", new TradeOfferFactoryType<>(TypedWrapper.CODEC));
    public static final TradeOfferFactoryType<SellItem> SELL_ITEM = register("sell_item", new TradeOfferFactoryType<>(SellItem.CODEC));
    public static final TradeOfferFactoryType<BuyItem> BUY_ITEM = register("buy_item", new TradeOfferFactoryType<>(BuyItem.CODEC));
    public static final TradeOfferFactoryType<EnchantBook> ENCHANT_BOOK = register("enchant_book", new TradeOfferFactoryType<>(EnchantBook.CODEC));
    public static final TradeOfferFactoryType<SellSuspiciousStew> SELL_SUSPICIOUS_STEW = register("sell_suspicious_stew", new TradeOfferFactoryType<>(SellSuspiciousStew.CODEC));
    public static final TradeOfferFactoryType<ProcessItem> PROCESS_ITEM = register("process_item", new TradeOfferFactoryType<>(ProcessItem.CODEC));
    public static final TradeOfferFactoryType<SellEnchantedTool> SELL_ENCHANTED_TOOL = register("sell_enchanted_tool", new TradeOfferFactoryType<>(SellEnchantedTool.CODEC));
    public static final TradeOfferFactoryType<TypeAwareBuyForOneEmerald> TYPE_AWARE_BUY_FOR_ONE_EMERALD = register("type_aware_for_one_emerald", new TradeOfferFactoryType<>(TypeAwareBuyForOneEmerald.CODEC));
    public static final TradeOfferFactoryType<SellPotionHoldingItem> SELL_POTION_HOLDING_ITEM = register("sell_potion_holding_item", new TradeOfferFactoryType<>(SellPotionHoldingItem.CODEC));
    public static final TradeOfferFactoryType<SellMap> SELL_MAP = register("sell_map", new TradeOfferFactoryType<>(SellMap.CODEC));
    public static final TradeOfferFactoryType<SellDyedArmor> SELL_DYED_ARMOR = register("sell_dyed_armor", new TradeOfferFactoryType<>(SellDyedArmor.CODEC));
    public static final TradeOfferFactoryType<Empty> EMPTY = register("empty", new TradeOfferFactoryType<>(Empty.CODEC));

    private static <T extends TradeOfferFactory> TradeOfferFactoryType<T> register(String id, TradeOfferFactoryType<T> factoryType) {
        return Registry.register(ModRegistries.TRADE_OFFER_FACTORY_TYPE, DataTrades.id(id), factoryType);
    }

    public static void init() {
        ServerLifecycleEvents.SERVER_STARTED.register(TradeOfferFactoryTypes::registerTrades);
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((minecraftServer, lifecycledResourceManager, b) -> registerTrades(minecraftServer));
    }

    public static void registerTrades(MinecraftServer minecraftServer) {}

    private TradeOfferFactoryTypes() {}
}
