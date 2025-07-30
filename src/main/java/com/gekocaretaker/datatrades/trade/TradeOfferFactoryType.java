package com.gekocaretaker.datatrades.trade;

import com.mojang.serialization.MapCodec;

public record TradeOfferFactoryType<T extends TradeOfferFactory>(MapCodec<T> codec) {
}
