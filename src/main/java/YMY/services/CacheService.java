package YMY.services;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    final CacheManager manager;
    public CacheService(CacheManager manager) {
        this.manager = manager;
    }

    public void cacheRefresh(String cacheName){
        manager.getCache(cacheName).clear();
    }

}
