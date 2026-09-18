import io.grpc.Server;
import io.grpc.ServerBuilder;
import gRPCservice.TraceabilityServiceImpl;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        Server server =
                ServerBuilder.forPort(9090)
                        .addService(new TraceabilityServiceImpl())
                        .build();

        server.start();

        System.out.println("gRPC Server started on port 9090");

        server.awaitTermination();
    }
}