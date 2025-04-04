package ru.sauvest.market.service;

import ru.sauvest.market.dto.BondDto;
import ru.sauvest.market.dto.ShareDto;

import java.util.List;

public interface InstrumentService {
	
	ShareDto findShareByFigi(String ssoToken, String figi);
	
	ShareDto findShareByTicker(String ssoToken, String ticker);

	List<ShareDto> findAllShares(String ssoToken);

	BondDto findBondByFigi(String ssoToken, String figi);

	List<BondDto> findAllBonds(String ssoToken);
	
}
