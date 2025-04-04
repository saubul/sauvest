package ru.sauvest.market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sauvest.market.dto.BondDto;
import ru.sauvest.market.dto.ShareDto;
import ru.sauvest.market.service.InstrumentService;
import ru.tinkoff.piapi.contract.v1.Bond;
import ru.tinkoff.piapi.contract.v1.InstrumentStatus;
import ru.tinkoff.piapi.contract.v1.Share;
import ru.tinkoff.piapi.core.InstrumentsService;
import ru.tinkoff.piapi.core.InvestApi;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstrumentServiceImpl implements InstrumentService {
	
	@Override
	public ShareDto findShareByFigi(String ssoToken, String figi) {
		InvestApi investApi = InvestApiUtilService.getInvestApi(ssoToken);
		InstrumentsService instrumentsService = investApi.getInstrumentsService();
		Share share = instrumentsService.getShareByFigiSync(figi);
        return this.convertShare(share);
	}
	
	@Override
	public ShareDto findShareByTicker(String ssoToken, String ticker) {
		InvestApi investApi = InvestApiUtilService.getInvestApi(ssoToken);
		InstrumentsService instrumentsService = investApi.getInstrumentsService();
		Share share = instrumentsService.getShareByTickerSync(ticker, "SPBXM");
        return this.convertShare(share);
	}

	@Override
	public List<ShareDto> findAllShares(String ssoToken) {
		InvestApi investApi = InvestApiUtilService.getInvestApi(ssoToken);
		InstrumentsService instrumentsService = investApi.getInstrumentsService();
		List<Share> shares = instrumentsService.getSharesSync(InstrumentStatus.INSTRUMENT_STATUS_BASE);
        return shares.stream().map(this::convertShare).toList();
	}

	@Override	
	public BondDto findBondByFigi(String ssoToken, String figi) {
		InvestApi investApi = InvestApiUtilService.getInvestApi(ssoToken);
		InstrumentsService instrumentsService = investApi.getInstrumentsService();
		Bond bond = instrumentsService.getBondByFigiSync(figi);

        return this.convertBond(bond);
	}

	@Override
	public List<BondDto> findAllBonds(String ssoToken) {
		InvestApi investApi = InvestApiUtilService.getInvestApi(ssoToken);
		InstrumentsService instrumentsService = investApi.getInstrumentsService();
		List<Bond> bonds = instrumentsService.getBondsSync(InstrumentStatus.INSTRUMENT_STATUS_BASE);
        return bonds.stream().map(this::convertBond).toList();
	}
	
	private ShareDto convertShare(Share share) {
		return ShareDto.builder()
				  .instrumentType(share.getShareType().toString())
				  .name(share.getName())
				  .figi(share.getFigi())
				  .ticker(share.getTicker())
				  .classCode(share.getClassCode())
				  .isin(share.getIsin())
				  .build();
	}
	
	private BondDto convertBond(Bond bond) {
		return BondDto.builder()
				.instrumentType("BOND")
				.name(bond.getName())
				.figi(bond.getFigi())
				.ticker(bond.getTicker())
				.classCode(bond.getClassCode())
				.isin(bond.getIsin())
				.build();
	}
	
}
