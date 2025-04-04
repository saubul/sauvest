package ru.sauvest.social.grpc.client;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ru.sauvest.grpc.market.InstrumentServiceGrpc;
import ru.sauvest.grpc.market.InstrumentServiceOuterClass;
import ru.sauvest.social.dto.InstrumentDto;

import java.util.List;

@Service
public class InstrumentClient {

    @GrpcClient("sauvest-market")
    private InstrumentServiceGrpc.InstrumentServiceBlockingStub instrumentServiceBlockingStub;

    public List<InstrumentDto> getAllInstruments(String ssoToken) {
        return instrumentServiceBlockingStub.getAllInstruments(InstrumentServiceOuterClass.SsoTokenRequest
                        .newBuilder()
                        .setSsoToken(ssoToken)
                        .build()).getInstrumentsList()
                .stream()
                .map(instrumentGrpc ->
                        InstrumentDto.builder()
                                .name(instrumentGrpc.getName())
                                .figi(instrumentGrpc.getFigi())
                                .ticker(instrumentGrpc.getTicker())
                                .isin(instrumentGrpc.getIsin())
                                .classCode(instrumentGrpc.getClassCode())
                                .instrumentType(instrumentGrpc.getInstrumentType())
                                .build()
                )
                .toList();
    }

}
