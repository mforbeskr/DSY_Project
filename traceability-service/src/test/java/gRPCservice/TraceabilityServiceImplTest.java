package gRPCservice;

import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.TraceabilityRepository;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TraceabilityServiceImplTest
{
    private FakeRepository repository;
    private Server server;
    private ManagedChannel channel;
    private TraceabilityServiceGrpc.TraceabilityServiceBlockingStub stub;

    @BeforeEach
    void setUp() throws Exception
    {
        repository = new FakeRepository();

        String serverName = InProcessServerBuilder.generateName();

        server = InProcessServerBuilder.forName(serverName)
                .directExecutor()
                .addService(new TraceabilityServiceImpl(repository))
                .build()
                .start();

        channel = InProcessChannelBuilder.forName(serverName)
                .directExecutor()
                .build();

        stub = TraceabilityServiceGrpc.newBlockingStub(channel);
    }

    @AfterEach
    void tearDown()
    {
        channel.shutdownNow();
        server.shutdownNow();
    }

    @Test
    void getAnimalsForProduct_returnsRegistrationNumbers()
    {
        repository.animalsByProduct = Map.of(1, List.of("DK-001", "DK-002"));

        AnimalsResponse response = stub.getAnimalsForProduct(
                ProductRequest.newBuilder().setProductId(1).build());

        assertEquals(List.of("DK-001", "DK-002"), response.getRegistrationNumbersList());
    }

    @Test
    void getAnimalsForProduct_passesRequestedProductIdToRepository()
    {
        stub.getAnimalsForProduct(
                ProductRequest.newBuilder().setProductId(42).build());

        assertEquals(42, repository.lastProductId);
    }

    @Test
    void getAnimalsForProduct_returnsEmptyResponseWhenNoAnimalsFound()
    {
        AnimalsResponse response = stub.getAnimalsForProduct(
                ProductRequest.newBuilder().setProductId(999).build());

        assertTrue(response.getRegistrationNumbersList().isEmpty());
    }

    @Test
    void getProductsForAnimal_returnsProductIds()
    {
        repository.productsByAnimal = Map.of(7, List.of(10, 11, 12));

        ProductsResponse response = stub.getProductsForAnimal(
                AnimalRequest.newBuilder().setAnimalId(7).build());

        assertEquals(List.of(10, 11, 12), response.getProductIdsList());
    }

    @Test
    void getProductsForAnimal_passesRequestedAnimalIdToRepository()
    {
        stub.getProductsForAnimal(
                AnimalRequest.newBuilder().setAnimalId(42).build());

        assertEquals(42, repository.lastAnimalId);
    }

    @Test
    void getProductsForAnimal_returnsEmptyResponseWhenNoProductsFound()
    {
        ProductsResponse response = stub.getProductsForAnimal(
                AnimalRequest.newBuilder().setAnimalId(999).build());

        assertTrue(response.getProductIdsList().isEmpty());
    }

    private static class FakeRepository implements TraceabilityRepository
    {
        Map<Integer, List<String>> animalsByProduct = Map.of();
        Map<Integer, List<Integer>> productsByAnimal = Map.of();
        int lastProductId = -1;
        int lastAnimalId = -1;

        @Override
        public List<String> getRegistrationNumbersForProduct(int productId)
        {
            lastProductId = productId;
            return animalsByProduct.getOrDefault(productId, List.of());
        }

        @Override
        public List<Integer> getProductIdsForAnimal(int animalId)
        {
            lastAnimalId = animalId;
            return productsByAnimal.getOrDefault(animalId, List.of());
        }
    }
}
