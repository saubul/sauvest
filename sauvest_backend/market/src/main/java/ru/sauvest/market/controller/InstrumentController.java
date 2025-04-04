package ru.sauvest.market.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sauvest.market.dto.BondDto;
import ru.sauvest.market.dto.GetInstrumentInfoRequestDto;
import ru.sauvest.market.dto.ShareDto;
import ru.sauvest.market.dto.SsoTokenDto;
import ru.sauvest.market.service.InstrumentService;
import ru.sauvest.market.service.impl.InvestApiUtilService;

import java.util.List;

@RestController
@RequestMapping("/${sauvest.applicationName}/api/v1/instrument")
@RequiredArgsConstructor
public class InstrumentController {

    private final InstrumentService instrumentService;

    @PostMapping("/share")
    public HttpEntity<ShareDto> getShare(@RequestBody GetInstrumentInfoRequestDto requestDto) {
        ShareDto shareDto;
        String ssoToken = requestDto.getSsoToken();
        String field = requestDto.getField();
        if (field.length() == 4) {
            shareDto = instrumentService.findShareByTicker(ssoToken, field);
        } else {
            shareDto = instrumentService.findShareByFigi(ssoToken, field);
        }
        if (shareDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(shareDto);
    }

    @PostMapping("/shares")
    public HttpEntity<List<ShareDto>> getAllShares(@RequestBody SsoTokenDto ssoTokenDto) {
        return ResponseEntity.ok(instrumentService.findAllShares(ssoTokenDto.getSsoToken()));
    }

    @PostMapping("/bond")
    public HttpEntity<BondDto> getBond(@RequestBody GetInstrumentInfoRequestDto requestDto) {
        return ResponseEntity.ok(instrumentService.findBondByFigi(requestDto.getSsoToken(), requestDto.getField()));
    }

    @PostMapping("/bonds")
    public HttpEntity<List<BondDto>> getAllBonds(@RequestBody SsoTokenDto ssoTokenDto) {
        return ResponseEntity.ok(instrumentService.findAllBonds(ssoTokenDto.getSsoToken()));
    }

    @PostMapping("/investapi/eviction")
    public HttpEntity<Void> evictInvestApi(@RequestBody SsoTokenDto ssoTokenDto) {
        InvestApiUtilService.evictInvestApi(ssoTokenDto.getSsoToken());
        return ResponseEntity.ok().build();
    }

}
