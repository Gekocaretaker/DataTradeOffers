package com.gekocaretaker.datatrades.registry;

import com.gekocaretaker.datatrades.trade.ProfessionManager;
import com.gekocaretaker.datatrades.trade.TradeOfferFactory;
import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;

import java.util.*;

public class ModEventLoader {
    private final static Map<RegistryKey<VillagerProfession>, Int2ObjectMap<TradeOffers.Factory[]>> BUILTIN_PROFESSION_TO_LEVELED_TRADE = new HashMap<>(TradeOffers.PROFESSION_TO_LEVELED_TRADE);

    public static void init() {
        ServerLifecycleEvents.SERVER_STARTED.register(ModEventLoader::registerTrades);
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((minecraftServer, lifecycledResourceManager, b) -> registerTrades(minecraftServer));
    }

    public static void registerTrades(MinecraftServer minecraftServer) {
        Registry<TradeOfferFactory> tradeOfferFactories = minecraftServer.getRegistryManager().getOrThrow(ModRegistryKeys.TRADE_OFFERS);
        Registry<ProfessionManager> professionManagers = minecraftServer.getRegistryManager().getOrThrow(ModRegistryKeys.PROFESSIONS);
        List<RegistryKey<VillagerProfession>> existingTrades = new ArrayList<>();
        professionManagers.forEach(professionManager -> {
            if (professionManager != null) {
                Int2ObjectMap<TradeOffers.Factory[]> currentFactory;
                if (existingTrades.contains(professionManager.profession())) {
                    currentFactory = TradeOffers.PROFESSION_TO_LEVELED_TRADE.get(professionManager.profession());
                } else if (professionManager.useBuiltin()) {
                    currentFactory = BUILTIN_PROFESSION_TO_LEVELED_TRADE.get(professionManager.profession());
                } else {
                    currentFactory = new Int2ObjectOpenHashMap<>(ImmutableMap.of(
                            1, new TradeOffers.Factory[0],
                            2, new TradeOffers.Factory[0],
                            3, new TradeOffers.Factory[0],
                            4, new TradeOffers.Factory[0],
                            5, new TradeOffers.Factory[0]
                    ));
                }

                if (!existingTrades.contains(professionManager.profession())) {
                    existingTrades.add(professionManager.profession());
                }

                List<List<TradeOffers.Factory>> levels = new ArrayList<>() {{
                    add(new ArrayList<>(Arrays.asList(currentFactory.get(1))));
                    add(new ArrayList<>(Arrays.asList(currentFactory.get(2))));
                    add(new ArrayList<>(Arrays.asList(currentFactory.get(3))));
                    add(new ArrayList<>(Arrays.asList(currentFactory.get(4))));
                    add(new ArrayList<>(Arrays.asList(currentFactory.get(5))));
                }};

                tradeOfferFactories.getIds().stream().filter(identifier1 -> {
                    return professionManager.allow().contains(RegistryKey.of(ModRegistryKeys.TRADE_OFFERS, identifier1))
                            && !professionManager.deny().contains(RegistryKey.of(ModRegistryKeys.TRADE_OFFERS, identifier1));
                }).forEach(identifier1 -> {
                    TradeOfferFactory tradeOfferFactory = tradeOfferFactories.get(identifier1);
                    if (tradeOfferFactory != null) {
                        levels.get(tradeOfferFactory.level() - 1).add(tradeOfferFactory);
                    }
                });

                TradeOffers.PROFESSION_TO_LEVELED_TRADE.replace(professionManager.profession(), new Int2ObjectOpenHashMap<>(ImmutableMap.of(
                        1, levels.get(0).toArray(TradeOffers.Factory[]::new),
                        2, levels.get(1).toArray(TradeOffers.Factory[]::new),
                        3, levels.get(2).toArray(TradeOffers.Factory[]::new),
                        4, levels.get(3).toArray(TradeOffers.Factory[]::new),
                        5, levels.get(4).toArray(TradeOffers.Factory[]::new)
                )));
            }
        });
    }

    private ModEventLoader() {}
}
