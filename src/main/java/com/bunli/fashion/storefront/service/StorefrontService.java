package com.bunli.fashion.storefront.service;

import com.bunli.fashion.storefront.dto.*;
import com.bunli.fashion.storefront.entity.*;
import com.bunli.fashion.storefront.repository.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class StorefrontService {
    private static final byte ACTIVE = 1;
    private final CategoryRepository categoryRepository;
    private final CollectionRepository collectionRepository;
    private final NavigationItemRepository navigationItemRepository;
    private final MediaRepository mediaRepository;
    private final BannerRepository bannerRepository;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

    public StorefrontService(CategoryRepository categoryRepository, CollectionRepository collectionRepository,
            NavigationItemRepository navigationItemRepository, MediaRepository mediaRepository,
            BannerRepository bannerRepository, ProductRepository productRepository,
            ProductImageRepository productImageRepository) {
        this.categoryRepository = categoryRepository;
        this.collectionRepository = collectionRepository;
        this.navigationItemRepository = navigationItemRepository;
        this.mediaRepository = mediaRepository;
        this.bannerRepository = bannerRepository;
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
    }

    public List<NavigationItemResponse> navigation() {
        return navigationTree(navigationItemRepository.findByActiveOrderBySortOrderAscNameAsc(ACTIVE), null);
    }

    public NavigationItemResponse navigationBySlug(String slug) {
        List<NavigationItem> items = navigationItemRepository.findByActiveOrderBySortOrderAscNameAsc(ACTIVE);
        NavigationItem item = items.stream().filter(value -> value.getSlug().equals(slug)).findFirst()
                .orElseThrow(() -> notFound("Navigation item not found"));
        Map<Long, List<NavigationItem>> children = items.stream().filter(value -> value.getParentId() != null)
                .collect(Collectors.groupingBy(NavigationItem::getParentId));
        return new NavigationItemResponse(item.getId(), item.getName(), item.getSlug(), item.getCategoryId(),
                item.getCollectionId(), item.getUrl(), item.getSortOrder(), navigationChildren(item, children));
    }

    public List<CategoryResponse> categories() {
        return categoryTree(categoryRepository.findByActiveOrderBySortOrderAscNameAsc(ACTIVE), null);
    }

    public CategoryResponse categoryBySlug(String slug) {
        List<Category> categories = categoryRepository.findByActiveOrderBySortOrderAscNameAsc(ACTIVE);
        Category category = categories.stream().filter(value -> value.getSlug().equals(slug)).findFirst()
                .orElseThrow(() -> notFound("Category not found"));
        Map<Long, List<Category>> children = categories.stream().filter(value -> value.getParentId() != null)
                .collect(Collectors.groupingBy(Category::getParentId));
        return categoryResponse(category, categoryChildren(category, children));
    }

    public List<CategoryResponse> categoryChildren(String slug) {
        Category category = categoryRepository.findBySlugAndActive(slug, ACTIVE)
                .orElseThrow(() -> notFound("Category not found"));
        return categoryRepository.findByParentIdAndActiveOrderBySortOrderAscNameAsc(category.getId(), ACTIVE).stream()
                .map(value -> categoryResponse(value, List.of())).toList();
    }

    public List<CollectionResponse> collections() {
        return collectionRepository.findByActiveOrderByNameAsc(ACTIVE).stream().map(this::collectionResponse).toList();
    }

    public CollectionResponse collectionBySlug(String slug) {
        return collectionResponse(collectionRepository.findBySlugAndActive(slug, ACTIVE)
                .orElseThrow(() -> notFound("Collection not found")));
    }

    public List<BannerResponse> banners() {
        return bannerRepository.findVisible(ACTIVE, LocalDateTime.now()).stream()
                .map(this::bannerResponse).flatMap(Optional::stream).toList();
    }

    public HomeResponse home() {
        return new HomeResponse(navigation(), banners(), categories(), collections(), List.of(
                new ProductSectionResponse("new-arrivals", "New arrivals", products(productRepository.findByActiveOrderByCreatedAtDesc(ACTIVE, PageRequest.of(0, 8))).items()),
                new ProductSectionResponse("featured", "Featured", products(productRepository.findByActiveAndFeaturedOrderByCreatedAtDesc(ACTIVE, ACTIVE, PageRequest.of(0, 8))).items()),
                new ProductSectionResponse("best-sellers", "Best sellers", products(productRepository.findBestSellers(PageRequest.of(0, 8))).items()),
                new ProductSectionResponse("sale", "Sale", products(productRepository.findSaleProducts(PageRequest.of(0, 8))).items())));
    }

    public ProductPageResponse newArrivals(int page, int size) {
        return products(productRepository.findByActiveOrderByCreatedAtDesc(ACTIVE, pageable(page, size)));
    }

    public ProductPageResponse featured(int page, int size) {
        return products(productRepository.findByActiveAndFeaturedOrderByCreatedAtDesc(ACTIVE, ACTIVE, pageable(page, size)));
    }

    public ProductPageResponse bestSellers(int page, int size) {
        return products(productRepository.findBestSellers(pageable(page, size)));
    }

    public ProductPageResponse sale(int page, int size) {
        return products(productRepository.findSaleProducts(pageable(page, size)));
    }

    private List<NavigationItemResponse> navigationTree(List<NavigationItem> items, Long parentId) {
        Map<Long, List<NavigationItem>> children = items.stream().filter(item -> item.getParentId() != null)
                .collect(Collectors.groupingBy(NavigationItem::getParentId));
        return items.stream().filter(item -> Objects.equals(item.getParentId(), parentId))
                .map(item -> new NavigationItemResponse(item.getId(), item.getName(), item.getSlug(), item.getCategoryId(),
                        item.getCollectionId(), item.getUrl(), item.getSortOrder(), navigationChildren(item, children)))
                .toList();
    }

    private List<NavigationItemResponse> navigationChildren(NavigationItem item, Map<Long, List<NavigationItem>> children) {
        return children.getOrDefault(item.getId(), List.of()).stream()
                .map(child -> new NavigationItemResponse(child.getId(), child.getName(), child.getSlug(), child.getCategoryId(),
                        child.getCollectionId(), child.getUrl(), child.getSortOrder(), navigationChildren(child, children)))
                .toList();
    }

    private List<CategoryResponse> categoryTree(List<Category> categories, Long parentId) {
        Map<Long, List<Category>> children = categories.stream().filter(category -> category.getParentId() != null)
                .collect(Collectors.groupingBy(Category::getParentId));
        return categories.stream().filter(category -> Objects.equals(category.getParentId(), parentId))
                .map(category -> categoryResponse(category, categoryChildren(category, children))).toList();
    }

    private List<CategoryResponse> categoryChildren(Category category, Map<Long, List<Category>> children) {
        return children.getOrDefault(category.getId(), List.of()).stream()
                .map(child -> categoryResponse(child, categoryChildren(child, children))).toList();
    }

    private CategoryResponse categoryResponse(Category category, List<CategoryResponse> children) {
        return new CategoryResponse(category.getId(), category.getParentId(), category.getName(), category.getSlug(),
                category.getDescription(), media(category.getImageId()), category.getSortOrder(), children);
    }

    private CollectionResponse collectionResponse(com.bunli.fashion.storefront.entity.Collection collection) {
        return new CollectionResponse(collection.getId(), collection.getName(), collection.getSlug(), collection.getDescription(),
                media(collection.getImageId()));
    }

    private Optional<BannerResponse> bannerResponse(Banner banner) {
        MediaResponse media = media(banner.getMediaId());
        return media == null ? Optional.empty() : Optional.of(new BannerResponse(banner.getId(), banner.getTitle(), media,
                banner.getTargetUrl(), banner.getSortOrder(), banner.getStartsAt(), banner.getEndsAt()));
    }

    private ProductPageResponse products(Page<Product> products) {
        return new ProductPageResponse(products.getContent().stream().map(this::productResponse).toList(), products.getNumber(),
                products.getSize(), products.getTotalElements(), products.getTotalPages());
    }

    private ProductSummaryResponse productResponse(Product product) {
        MediaResponse image = productImageRepository.findFirstByProductIdAndActiveOrderBySortOrderAscIdAsc(product.getId(), ACTIVE)
                .map(ProductImage::getMediaId).map(this::media).orElse(null);
        return new ProductSummaryResponse(product.getId(), product.getName(), product.getSlug(), product.getDescription(),
                product.getBasePrice(), product.getGender(), product.getMaterial(), product.isFeatured(), image, product.getCreatedAt());
    }

    private MediaResponse media(Long id) {
        if (id == null) return null;
        return mediaRepository.findByIdAndActive(id, ACTIVE)
                .map(value -> new MediaResponse(value.getId(), value.getBucket(), value.getObjectKey(), value.getContentType(), value.getSizeBytes()))
                .orElse(null);
    }

    private PageRequest pageable(int page, int size) {
        return PageRequest.of(Math.max(0, page), Math.min(Math.max(1, size), 50));
    }

    private ResponseStatusException notFound(String message) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, message);
    }
}
