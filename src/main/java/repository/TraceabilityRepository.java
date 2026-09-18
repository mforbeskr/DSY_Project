package repository;

import java.util.List;

public interface TraceabilityRepository
{
    List<String> getRegistrationNumbersForProduct(int productId);

    List<Integer> getProductIdsForAnimal(int animalId);
}

