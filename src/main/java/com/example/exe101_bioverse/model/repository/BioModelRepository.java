package com.example.exe101_bioverse.model.repository;

import com.example.exe101_bioverse.model.entity.BioModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BioModelRepository extends JpaRepository<BioModel, Long> {

    // ======================== Public Queries ========================

    /**
     * Lấy danh sách mô hình phổ biến (featured) cho trang chủ.
     * Chỉ lấy model active + featured, sắp xếp theo sortOrder ASC.
     */
    @Query("SELECT m FROM BioModel m WHERE m.isActive = true AND m.isFeatured = true ORDER BY m.sortOrder ASC, m.createdAt DESC")
    List<BioModel> findFeaturedModels();

    /**
     * Phân trang danh mục với bộ lọc linh hoạt: grade, category, subject, keyword.
     * Tìm kiếm keyword trong name, nameEn, description.
     */
    @Query("SELECT m FROM BioModel m WHERE m.isActive = true " +
           "AND (:grade IS NULL OR m.grade = :grade) " +
           "AND (:category IS NULL OR m.category = :category) " +
           "AND (:subject IS NULL OR m.subject = :subject) " +
           "AND (:keyword IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "     OR LOWER(m.nameEn) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "     OR LOWER(m.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<BioModel> findCatalogModels(
            @Param("grade") Integer grade,
            @Param("category") String category,
            @Param("subject") String subject,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    /**
     * Tìm model theo slug (URL-friendly identifier).
     */
    Optional<BioModel> findBySlugAndIsActiveTrue(String slug);

    /**
     * Tìm model active theo ID.
     */
    Optional<BioModel> findByIdAndIsActiveTrue(Long id);

    /**
     * Lấy danh sách các category riêng biệt (cho tab lọc trên UI).
     */
    @Query("SELECT DISTINCT m.category FROM BioModel m WHERE m.isActive = true AND m.category IS NOT NULL ORDER BY m.category")
    List<String> findDistinctCategories();

    // ======================== Admin Queries ========================

    /**
     * Admin: Tìm kiếm tất cả model (bao gồm inactive), lọc theo keyword và featured status.
     */
    @Query("SELECT m FROM BioModel m WHERE " +
           "(:keyword IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "     OR LOWER(m.nameEn) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:isFeatured IS NULL OR m.isFeatured = :isFeatured) " +
           "AND (:isActive IS NULL OR m.isActive = :isActive)")
    Page<BioModel> findAdminModels(
            @Param("keyword") String keyword,
            @Param("isFeatured") Boolean isFeatured,
            @Param("isActive") Boolean isActive,
            Pageable pageable
    );

    /**
     * Tăng lượt xem của model.
     */
    @Modifying
    @Query("UPDATE BioModel m SET m.viewsCount = m.viewsCount + 1 WHERE m.id = :id")
    void incrementViewCount(@Param("id") Long id);

    /**
     * Kiểm tra slug đã tồn tại chưa (trừ model hiện tại).
     */
    boolean existsBySlugAndIdNot(String slug, Long id);

    boolean existsBySlug(String slug);
}
