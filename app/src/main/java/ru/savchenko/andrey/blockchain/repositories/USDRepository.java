package ru.savchenko.andrey.blockchain.repositories;

import java.util.Date;

import ru.savchenko.andrey.blockchain.base.BaseRepository;
import ru.savchenko.andrey.blockchain.entities.USD;

/**
 * Created by Andrey on 12.10.2017.
 */

public class USDRepository {
    public void writeIdDb(USD usd){
        BaseRepository baseRepository = new BaseRepository<>(USD.class);
        usd.setId(baseRepository.getMaxIdPlusOne());
        usd.setDate(new Date());
        baseRepository.addItem(usd);
    }
}
