import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

/**
 * Creates a traffic report using Java Streams and parallel processing.
 */
public class TrafficAnalytics {

    private TrafficAnalytics() {
        // Utility class: do not create instances.
    }

    public static String generateReport(List<Car> vehicles) {
        Map<String, DoubleSummaryStatistics> statistics =
                vehicles.parallelStream()
                        .collect(Collectors.groupingBy(
                                TrafficAnalytics::getDirection,
                                TreeMap::new,
                                Collectors.summarizingDouble(Car::getSpeed)
                        ));

        StringBuilder report = new StringBuilder();
        report.append("\n========== TRAFFIC ANALYTICS ==========\n");
        report.append("Processing mode: parallelStream\n");
        report.append("ForkJoinPool parallelism: ")
                .append(ForkJoinPool.getCommonPoolParallelism())
                .append("\n");

        if (statistics.isEmpty()) {
            report.append("No vehicles currently on the road.\n");
            return report.toString();
        }

        statistics.forEach((direction, stats) -> {
            report.append("\nDirection: ").append(direction);
            report.append("\nCars: ").append(stats.getCount());
            report.append("\nMinimum speed: ")
                    .append(String.format(Locale.ROOT, "%.1f", stats.getMin()));
            report.append("\nMaximum speed: ")
                    .append(String.format(Locale.ROOT, "%.1f", stats.getMax()));
            report.append("\nAverage speed: ")
                    .append(String.format(Locale.ROOT, "%.1f", stats.getAverage()));
            report.append("\n---------------------------------------\n");
        });

        return report.toString();
    }

    private static String getDirection(Car car) {
        double angle = car.getDirectionDegrees();
        angle = ((angle % 360) + 360) % 360;

        if (isNear(angle, 0) || isNear(angle, 360)) {
            return "East";
        }
        if (isNear(angle, 90)) {
            return "South";
        }
        if (isNear(angle, 180)) {
            return "West";
        }
        if (isNear(angle, 270)) {
            return "North";
        }
        return "Other / Turning";
    }

    private static boolean isNear(double actual, double expected) {
        return Math.abs(actual - expected) < 5;
    }
}
