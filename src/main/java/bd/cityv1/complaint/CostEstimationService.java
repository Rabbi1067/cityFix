package bd.cityv1.complaint;

import bd.cityv1.complaint.common.Priority;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CostEstimationService {

    private static final Map<String, Double> categoryBaseCost = Map.of(
            "Roads", 6000.0,
            "Sanitation", 2500.0,
            "Lighting", 3000.0,
            "Parks", 4000.0,
            "Water", 5000.0,
            "Other", 2000.0
    );

    private static final double defaultCategoryCost = 2000.0;

    private static final Map<Priority, Double> priorityMultiplier = Map.of(
            Priority.LOW, 0.7,
            Priority.MEDIUM, 1.0,
            Priority.HIGH, 1.4,
            Priority.CRITICAL, 2.0
    );

    private static final double defaultPriorityMultiplier = 1.0;

    public Double estimate(String category, Priority priority) {

        double baseCost = categoryBaseCost.getOrDefault(category, defaultCategoryCost);
        double multiplier = priorityMultiplier.getOrDefault(priority, defaultPriorityMultiplier);

        double estimatedCost = baseCost * multiplier;

        return Math.round(estimatedCost / 50.0) * 50.0;
    }
}
