package ru.sauvest.auth.grpc.client;

import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ru.sauvest.grpc.UserServiceGrpc;

@Service
@RequiredArgsConstructor
public class UserServiceGrpcClient {

    @GrpcClient("social-service")
    private UserServiceGrpc.UserServiceBlockingStub stub;

}
