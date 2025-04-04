package ru.sauvest.market.grpc.server;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.sauvest.grpc.market.InstrumentServiceGrpc;
import ru.sauvest.grpc.market.InstrumentServiceOuterClass;
import ru.sauvest.market.service.impl.InvestApiUtilService;
import ru.tinkoff.piapi.contract.v1.InstrumentStatus;
import ru.tinkoff.piapi.contract.v1.Share;
import ru.tinkoff.piapi.core.InstrumentsService;
import ru.tinkoff.piapi.core.InvestApi;

import java.util.List;

@GrpcService
public class InstrumentGrpcService extends InstrumentServiceGrpc.InstrumentServiceImplBase {

    @Override
    public void getAllInstruments(InstrumentServiceOuterClass.SsoTokenRequest request, StreamObserver<InstrumentServiceOuterClass.GetAllInstrumentsResponse> responseObserver) {
        InvestApi investApi = InvestApiUtilService.getInvestApi(request.getSsoToken());
        InstrumentsService instrumentsService = investApi.getInstrumentsService();
        List<Share> shares = instrumentsService.getSharesSync(InstrumentStatus.INSTRUMENT_STATUS_BASE);

        List<InstrumentServiceOuterClass.Instrument> shareInstrumentsGrpc = shares.stream()
                .map(share -> InstrumentServiceOuterClass.Instrument.newBuilder()
                        .setName(share.getName())
                        .setFigi(share.getFigi())
                        .setIsin(share.getIsin())
                        .setTicker(share.getTicker())
                        .setClassCode(share.getClassCode())
                        .setInstrumentType(share.getShareType().toString())
                        .build())
                .toList();

        responseObserver.onNext(
                InstrumentServiceOuterClass.GetAllInstrumentsResponse.newBuilder()
                        .addAllInstruments(shareInstrumentsGrpc)
                        .build()
        );
        responseObserver.onCompleted();

    }


}
