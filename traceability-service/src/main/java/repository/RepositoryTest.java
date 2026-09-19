package repository;

public class RepositoryTest
{
    public static void main(String[] args)
    {
        TraceabilityRepository repo =
                new TraceabilityRepositoryImpl();

        System.out.println(
                repo.getRegistrationNumbersForProduct(1));

        System.out.println(
                repo.getProductIdsForAnimal(1));
    }
}
