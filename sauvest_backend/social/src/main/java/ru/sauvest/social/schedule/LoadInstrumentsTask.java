package ru.sauvest.social.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.sauvest.social.grpc.client.InstrumentClient;
import ru.sauvest.social.mapper.InstrumentMapper;
import ru.sauvest.social.service.InstrumentService;

// TODO Сделать получш. Нужен ли синхрон?
@Component
@RequiredArgsConstructor
public class LoadInstrumentsTask {

    // TODO
    @Value("${sso-token}")
    private String ssoToken;

    private final InstrumentClient instrumentClient;

    private final InstrumentService instrumentService;

    private final InstrumentMapper instrumentMapper;

    @Scheduled(fixedRate = 1000 * 60 * 60 * 24)
    public void loadInstruments() {
        instrumentService.saveAll(instrumentClient.getAllInstruments(ssoToken));
    }

}
