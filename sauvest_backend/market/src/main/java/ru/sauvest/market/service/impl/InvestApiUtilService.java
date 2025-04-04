package ru.sauvest.market.service.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import ru.tinkoff.piapi.core.InvestApi;

public final class InvestApiUtilService {

    private InvestApiUtilService() {
    }

    @Cacheable(cacheNames = "InvestAPIs")
    public static InvestApi getInvestApi(String ssoToken) {
        return InvestApi.create(ssoToken);
    }

    @CacheEvict(cacheNames = "InvestAPIs", key = "#ssoToken")
    public static void evictInvestApi(String ssoToken) {
    }

}
