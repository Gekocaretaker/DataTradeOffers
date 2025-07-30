package com.gekocaretaker.datatrades.trade;

import com.gekocaretaker.datatrades.registry.ModRegistryKeys;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.village.VillagerProfession;

import java.util.List;

public record ProfessionManager(List<RegistryKey<TradeOfferFactory>> allow, List<RegistryKey<TradeOfferFactory>> deny,
                                boolean useBuiltin, RegistryKey<VillagerProfession> profession) {
    public static final Codec<ProfessionManager> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
                RegistryKey.createCodec(ModRegistryKeys.TRADE_OFFERS).listOf().optionalFieldOf("allow", List.of())
                        .forGetter(ProfessionManager::allow),
                RegistryKey.createCodec(ModRegistryKeys.TRADE_OFFERS).listOf().optionalFieldOf("deny", List.of())
                        .forGetter(ProfessionManager::deny),
                Codec.BOOL.fieldOf("use_builtin_trades").forGetter(ProfessionManager::useBuiltin),
                RegistryKey.createCodec(RegistryKeys.VILLAGER_PROFESSION).fieldOf("profession").forGetter(ProfessionManager::profession)
        ).apply(instance, ProfessionManager::new);
    });
}
