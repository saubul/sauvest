package ru.otus.auth.grpc.client;

import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ru.otus.grpc.UserServiceGrpc;

@Service
@RequiredArgsConstructor
public class UserServiceGrpcClient {

    @GrpcClient("social-service")
    private UserServiceGrpc.UserServiceBlockingStub stub;

}
