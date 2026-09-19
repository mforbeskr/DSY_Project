package gRPCservice;

import io.grpc.stub.StreamObserver;
import repository.TraceabilityRepository;
import repository.TraceabilityRepositoryImpl;

import java.util.List;

public class TraceabilityServiceImpl extends TraceabilityServiceGrpc.TraceabilityServiceImplBase
{
    private final TraceabilityRepository repository;

    public TraceabilityServiceImpl()
    {
        repository = new TraceabilityRepositoryImpl();
    }

    @Override
    public void getAnimalsForProduct(
            ProductRequest request,
            StreamObserver<AnimalsResponse> responseObserver)
    {
        List<String> registrationNumbers =
                repository.getRegistrationNumbersForProduct(request.getProductId());

        AnimalsResponse response =
                AnimalsResponse.newBuilder()
                        .addAllRegistrationNumbers(registrationNumbers)
                        .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getProductsForAnimal(
            AnimalRequest request,
            StreamObserver<ProductsResponse> responseObserver)
    {
        List<Integer> productIds =
                repository.getProductIdsForAnimal(request.getAnimalId());

        ProductsResponse response =
                ProductsResponse.newBuilder()
                        .addAllProductIds(productIds)
                        .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}