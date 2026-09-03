package bd.cityv1.home.dto;

public record ComplaintHomeDto(
        Long id,
        String title,
        String description,
        String category,
        String location,
        String status,
        String imagePath,
        String createdAt
) {
}
