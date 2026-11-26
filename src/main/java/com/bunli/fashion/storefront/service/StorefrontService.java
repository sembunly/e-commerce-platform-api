package com.bunli.fashion.storefront.service;

import com.bunli.fashion.storefront.dto.BannerResponse;
import com.bunli.fashion.storefront.dto.CategoryResponse;
import com.bunli.fashion.storefront.dto.CollectionResponse;
import com.bunli.fashion.storefront.dto.HomeResponse;
import com.bunli.fashion.storefront.dto.MediaResponse;
import com.bunli.fashion.storefront.dto.NavigationItemResponse;
import com.bunli.fashion.storefront.dto.ProductPageResponse;
import com.bunli.fashion.storefront.dto.ProductSectionResponse;
import com.bunli.fashion.storefront.dto.ProductSummaryResponse;
import com.bunli.fashion.storefront.dto.ProductDetailResponse;
import com.bunli.fashion.storefront.dto.ProductImageResponse;
import com.bunli.fashion.storefront.dto.VariantResponse;
import com.bunli.fashion.storefront.dto.ProductFiltersResponse;
import com.bunli.fashion.storefront.dto.FilterOptionResponse;
import com.bunli.fashion.storefront.dto.StoreResponse;
import com.bunli.fashion.storefront.dto.ArticlePageResponse;
import com.bunli.fashion.storefront.dto.ArticleSummaryResponse;
import com.bunli.fashion.storefront.dto.ArticleDetailResponse;
import com.bunli.fashion.storefront.entity.Article;
import com.bunli.fashion.storefront.entity.Store;
import com.bunli.fashion.storefront.entity.Banner;
import com.bunli.fashion.storefront.entity.Category;
import com.bunli.fashion.storefront.entity.Collection;
import com.bunli.fashion.storefront.entity.Media;
import com.bunli.fashion.storefront.entity.NavigationItem;
import com.bunli.fashion.storefront.entity.Product;
import com.bunli.fashion.storefront.entity.ProductImage;
import com.bunli.fashion.storefront.repository.BannerRepository;
import com.bunli.fashion.storefront.repository.CategoryRepository;
import com.bunli.fashion.storefront.repository.CollectionRepository;
import com.bunli.fashion.storefront.repository.MediaRepository;
import com.bunli.fashion.storefront.repository.NavigationItemRepository;
import com.bunli.fashion.storefront.repository.ProductImageRepository;
import com.bunli.fashion.storefront.repository.ProductRepository;
import com.bunli.fashion.storefront.repository.ProductVariantRepository;
import com.bunli.fashion.storefront.repository.SizeRepository;
import com.bunli.fashion.storefront.repository.ColorRepository;
import com.bunli.fashion.storefront.repository.StoreRepository;
import com.bunli.fashion.storefront.repository.ArticleRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
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
        private static final int HERO_THRESHOLD = 100;

        private final CategoryRepository categoryRepository;
        private final CollectionRepository collectionRepository;
        private final NavigationItemRepository navigationItemRepository;
        private final MediaRepository mediaRepository;
        private final BannerRepository bannerRepository;
        private final ProductRepository productRepository;
        private final ProductImageRepository productImageRepository;
        private final ProductVariantRepository productVariantRepository;
        private final SizeRepository sizeRepository;
        private final ColorRepository colorRepository;
        private final StoreRepository storeRepository;
        private final ArticleRepository articleRepository;

        /** e.g. {@code http://localhost:9000} (no trailing slash) */
        @Value("${minio.public-endpoint}")
        private String minioEndpoint;

        @Value("${minio.bucket}")
        private String minioBucket;

        public StorefrontService(CategoryRepository categoryRepository,
                        CollectionRepository collectionRepository,
                        NavigationItemRepository navigationItemRepository,
                        MediaRepository mediaRepository,
                        BannerRepository bannerRepository,
                        ProductRepository productRepository,
                        ProductImageRepository productImageRepository,
                        ProductVariantRepository productVariantRepository,
                        SizeRepository sizeRepository,
                        ColorRepository colorRepository,
                        StoreRepository storeRepository,
                        ArticleRepository articleRepository) {
                this.categoryRepository = categoryRepository;
                this.collectionRepository = collectionRepository;
                this.navigationItemRepository = navigationItemRepository;
                this.mediaRepository = mediaRepository;
                this.bannerRepository = bannerRepository;
                this.productRepository = productRepository;
                this.productImageRepository = productImageRepository;
                this.productVariantRepository = productVariantRepository;
                this.sizeRepository = sizeRepository;
                this.colorRepository = colorRepository;
                this.storeRepository = storeRepository;
                this.articleRepository = articleRepository;
        }

        // ─────────────────────────── Navigation ───────────────────────────────
        public List<NavigationItemResponse> navigation() {
                List<NavigationItem> items = navigationItemRepository.findByActiveOrderBySortOrderAscNameAsc(ACTIVE);
                Map<Long, Category> catById = categoryById();
                Map<Long, Collection> colById = collectionById();
                return navigationTree(items, null, catById, colById);
        }

        public NavigationItemResponse navigationBySlug(String slug) {
                List<NavigationItem> items = navigationItemRepository.findByActiveOrderBySortOrderAscNameAsc(ACTIVE);
                NavigationItem item = items.stream()
                                .filter(v -> v.getSlug().equals(slug))
                                .findFirst()
                                .orElseThrow(() -> notFound("Navigation item not found: " + slug));
                Map<Long, Category> catById = categoryById();
                Map<Long, Collection> colById = collectionById();
                Map<Long, List<NavigationItem>> childrenMap = groupByParent(items);
                return toNavResponse(item, childrenMap, catById, colById);
        }

        // ─────────────────────────── Categories ───────────────────────────────

        public List<CategoryResponse> categories() {
                return categoryTree(categoryRepository.findByActiveOrderBySortOrderAscNameAsc(ACTIVE), null);
        }

        public CategoryResponse categoryBySlug(String slug) {
                List<Category> all = categoryRepository.findByActiveOrderBySortOrderAscNameAsc(ACTIVE);
                Category category = all.stream()
                                .filter(c -> c.getSlug().equals(slug))
                                .findFirst()
                                .orElseThrow(() -> notFound("Category not found: " + slug));
                Map<Long, List<Category>> childrenMap = groupCatByParent(all);
                return categoryResponse(category, categoryChildren(category, childrenMap));
        }

        public List<CategoryResponse> categoryChildren(String slug) {
                Category category = categoryRepository.findBySlugAndActive(slug, ACTIVE)
                                .orElseThrow(() -> notFound("Category not found: " + slug));
                return categoryRepository
                                .findByParentIdAndActiveOrderBySortOrderAscNameAsc(category.getId(), ACTIVE)
                                .stream()
                                .map(c -> categoryResponse(c, List.of()))
                                .toList();
        }

        // ─────────────────────────── Collections ──────────────────────────────
        public List<CollectionResponse> collections() {
                return collectionRepository.findByActiveOrderByNameAsc(ACTIVE)
                                .stream().map(this::collectionResponse).toList();
        }

        public CollectionResponse collectionBySlug(String slug) {
                return collectionResponse(collectionRepository.findBySlugAndActive(slug, ACTIVE)
                                .orElseThrow(() -> notFound("Collection not found: " + slug)));
        }

        // ─────────────────────────── Banners ──────────────────────────────────
        public List<BannerResponse> banners() {
                return bannerRepository.findVisible(ACTIVE, LocalDateTime.now())
                                .stream()
                                .map(this::bannerResponse)
                                .flatMap(Optional::stream)
                                .toList();
        }

        // ─────────────────────────── Homepage ─────────────────────────────────
        public HomeResponse home() {
                List<BannerResponse> allBanners = banners();

                // Split banners into hero (sort_order < 100) and promotional strip (>= 100)
                List<BannerResponse> heroBanners = allBanners.stream()
                                .filter(b -> b.sortOrder() < HERO_THRESHOLD).toList();
                List<BannerResponse> promoBanners = allBanners.stream()
                                .filter(b -> b.sortOrder() >= HERO_THRESHOLD).toList();

                List<CollectionResponse> featured = collectionRepository.findByActiveOrderByNameAsc(ACTIVE)
                                .stream().map(this::collectionResponse).toList();

                List<ProductSectionResponse> sections = List.of(
                                productSection("new-arrivals", "New Arrivals", "/api/v1/products/new-arrivals",
                                                productRepository.findByActiveOrderByCreatedAtDesc(ACTIVE,
                                                                PageRequest.of(0, 8))),
                                productSection("featured", "Featured", "/api/v1/products/featured",
                                                productRepository.findByActiveAndFeaturedOrderByCreatedAtDesc(ACTIVE,
                                                                ACTIVE, PageRequest.of(0, 8))),
                                productSection("best-sellers", "Best Sellers", "/api/v1/products/best-sellers",
                                                productRepository.findBestSellers(PageRequest.of(0, 8))),
                                productSection("sale", "Sale", "/api/v1/products/sale",
                                                productRepository.findSaleProducts(PageRequest.of(0, 8))));

                return new HomeResponse(navigation(), heroBanners, promoBanners, categories(), featured, sections);
        }

        // ─────────────────────────── Product Sections ─────────────────────────
        public ProductPageResponse newArrivals(int page, int size) {
                return toPageResponse(productRepository.findByActiveOrderByCreatedAtDesc(ACTIVE, pageable(page, size)));
        }

        public ProductPageResponse featured(int page, int size) {
                return toPageResponse(productRepository.findByActiveAndFeaturedOrderByCreatedAtDesc(ACTIVE, ACTIVE,
                                pageable(page, size)));
        }

        public ProductPageResponse bestSellers(int page, int size) {
                return toPageResponse(productRepository.findBestSellers(pageable(page, size)));
        }

        public ProductPageResponse sale(int page, int size) {
                return toPageResponse(productRepository.findSaleProducts(pageable(page, size)));
        }

        // ───────────────────────────── Catalog ──────────────────────────────
        public ProductPageResponse catalog(String keyword, String gender, String category, String collection,
                        String size, String color, BigDecimal minPrice, BigDecimal maxPrice, Boolean inStock,
                        String sort, int page, int sizePerPage) {
                if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                        "minPrice cannot be greater than maxPrice");
                }
                return toPageResponse(productRepository.findCatalog(normalize(keyword), normalize(gender),
                                normalize(category),
                                normalize(collection), normalize(size), normalize(color), minPrice, maxPrice, inStock,
                                sortKey(sort),
                                pageable(page, sizePerPage)));
        }

        public ProductDetailResponse productBySlug(String slug) {
                Product product = activeProduct(slug);
                List<ProductImageResponse> images = productImages(List.of(product.getId()))
                                .getOrDefault(product.getId(), List.of());
                List<VariantResponse> variants = productVariantRepository.findActiveViewsByProductId(product.getId())
                                .stream()
                                .map(this::variantResponse).toList();
                List<CategoryResponse> productCategories = categoryRepository.findActiveByProductId(product.getId())
                                .stream()
                                .map(c -> categoryResponse(c, List.of())).toList();
                List<CollectionResponse> productCollections = collectionRepository
                                .findActiveByProductId(product.getId()).stream()
                                .map(this::collectionResponse).toList();
                return new ProductDetailResponse(product.getId(), product.getName(), product.getSlug(),
                                product.getDescription(),
                                product.getBasePrice(), product.getGender(), product.getMaterial(),
                                product.isFeatured(), productCategories,
                                productCollections, images, variants, product.getCreatedAt());
        }

        public List<VariantResponse> productVariants(String slug) {
                return productVariantRepository.findActiveViewsByProductId(activeProduct(slug).getId()).stream()
                                .map(this::variantResponse).toList();
        }

        public ProductPageResponse relatedProducts(String slug, int page, int size) {
                return toPageResponse(productRepository.findRelated(activeProduct(slug).getId(), pageable(page, size)));
        }

        public ProductFiltersResponse filters() {
                return new ProductFiltersResponse(categories(), collections(),
                                sizeRepository.findByActiveOrderBySortOrderAscNameAsc(ACTIVE).stream()
                                                .map(s -> new FilterOptionResponse(s.getId(), s.getName(), s.getName(),
                                                                null))
                                                .toList(),
                                colorRepository.findByActiveOrderByNameAsc(ACTIVE).stream()
                                                .map(c -> new FilterOptionResponse(c.getId(), c.getName(), c.getName(),
                                                                c.getHexCode()))
                                                .toList(),
                                productRepository.findCatalogMinPrice().orElse(null),
                                productRepository.findCatalogMaxPrice().orElse(null));
        }

        public ProductPageResponse categoryProducts(String slug, String keyword, String gender, String size,
                        String color,
                        BigDecimal minPrice, BigDecimal maxPrice, Boolean inStock, String sort, int page,
                        int sizePerPage) {
                categoryRepository.findBySlugAndActive(slug, ACTIVE)
                                .orElseThrow(() -> notFound("Category not found: " + slug));
                return catalog(keyword, gender, slug, null, size, color, minPrice, maxPrice, inStock, sort, page,
                                sizePerPage);
        }

        public ProductPageResponse collectionProducts(String slug, String keyword, String gender, String size,
                        String color,
                        BigDecimal minPrice, BigDecimal maxPrice, Boolean inStock, String sort, int page,
                        int sizePerPage) {
                collectionRepository.findBySlugAndActive(slug, ACTIVE)
                                .orElseThrow(() -> notFound("Collection not found: " + slug));
                return catalog(keyword, gender, null, slug, size, color, minPrice, maxPrice, inStock, sort, page,
                                sizePerPage);
        }

        public List<StoreResponse> stores() {
                return storeRepository.findByActiveOrderByNameAsc(ACTIVE).stream().map(this::storeResponse).toList();
        }

        public StoreResponse store(Long id) {
                return storeResponse(storeRepository.findByIdAndActive(id, ACTIVE)
                                .orElseThrow(() -> notFound("Store not found: " + id)));
        }

        public ArticlePageResponse articles(int page, int size) {
                Page<Article> articles = articleRepository
                                .findByActiveAndPublishedAtLessThanEqualOrderByPublishedAtDesc(ACTIVE,
                                                LocalDateTime.now(), pageable(page, size));
                return new ArticlePageResponse(articles.getContent().stream().map(this::articleSummary).toList(),
                                articles.getNumber(),
                                articles.getSize(), articles.getTotalElements(), articles.getTotalPages());
        }

        public ArticleDetailResponse articleBySlug(String slug) {
                Article article = articleRepository
                                .findBySlugAndActiveAndPublishedAtLessThanEqual(slug, ACTIVE, LocalDateTime.now())
                                .orElseThrow(() -> notFound("Article not found: " + slug));
                return new ArticleDetailResponse(article.getId(), article.getName(), article.getSlug(),
                                article.getContent(),
                                media(article.getMediaId()), article.getAuthorId(), article.getPublishedAt());
        }

        // ─────────────────────────── Private helpers ──────────────────────────
        private Map<Long, Category> categoryById() {
                return categoryRepository.findByActiveOrderBySortOrderAscNameAsc(ACTIVE)
                                .stream().collect(Collectors.toMap(Category::getId, c -> c));
        }

        private Map<Long, Collection> collectionById() {
                return collectionRepository.findByActiveOrderByNameAsc(ACTIVE)
                                .stream().collect(Collectors.toMap(Collection::getId, c -> c));
        }

        private Map<Long, List<NavigationItem>> groupByParent(List<NavigationItem> items) {
                return items.stream()
                                .filter(i -> i.getParentId() != null)
                                .collect(Collectors.groupingBy(NavigationItem::getParentId));
        }

        private Map<Long, List<Category>> groupCatByParent(List<Category> categories) {
                return categories.stream()
                                .filter(c -> c.getParentId() != null)
                                .collect(Collectors.groupingBy(Category::getParentId));
        }

        // ── Navigation tree ───────────────────────────────────────────────────
        private List<NavigationItemResponse> navigationTree(List<NavigationItem> items, Long parentId,
                        Map<Long, Category> catById,
                        Map<Long, Collection> colById) {
                Map<Long, List<NavigationItem>> childrenMap = groupByParent(items);
                return items.stream()
                                .filter(i -> Objects.equals(i.getParentId(), parentId))
                                .map(i -> toNavResponse(i, childrenMap, catById, colById))
                                .toList();
        }

        private NavigationItemResponse toNavResponse(NavigationItem item,
                        Map<Long, List<NavigationItem>> childrenMap,
                        Map<Long, Category> catById,
                        Map<Long, Collection> colById) {
                Category cat = item.getCategoryId() != null ? catById.get(item.getCategoryId()) : null;
                Collection col = item.getCollectionId() != null ? colById.get(item.getCollectionId()) : null;

                String categorySlug = cat != null ? cat.getSlug() : null;
                String collectionSlug = col != null ? col.getSlug() : null;

                // Cover image: prefer collection image, fall back to category image
                MediaResponse image = null;
                if (col != null && col.getImageId() != null) {
                        image = media(col.getImageId());
                } else if (cat != null && cat.getImageId() != null) {
                        image = media(cat.getImageId());
                }

                List<NavigationItemResponse> children = childrenMap
                                .getOrDefault(item.getId(), List.of())
                                .stream()
                                .map(child -> toNavResponse(child, childrenMap, catById, colById))
                                .toList();

                return new NavigationItemResponse(
                                item.getId(), item.getName(), item.getSlug(),
                                item.getCategoryId(), categorySlug,
                                item.getCollectionId(), collectionSlug,
                                item.getUrl(), item.getSortOrder(), image, children);
        }

        // ── Category tree ─────────────────────────────────────────────────────
        private List<CategoryResponse> categoryTree(List<Category> categories, Long parentId) {
                Map<Long, List<Category>> childrenMap = groupCatByParent(categories);
                return categories.stream()
                                .filter(c -> Objects.equals(c.getParentId(), parentId))
                                .map(c -> categoryResponse(c, categoryChildren(c, childrenMap)))
                                .toList();
        }

        private List<CategoryResponse> categoryChildren(Category category, Map<Long, List<Category>> childrenMap) {
                return childrenMap.getOrDefault(category.getId(), List.of())
                                .stream()
                                .map(child -> categoryResponse(child, categoryChildren(child, childrenMap)))
                                .toList();
        }

        private CategoryResponse categoryResponse(Category category, List<CategoryResponse> children) {
                return new CategoryResponse(
                                category.getId(), category.getParentId(), category.getName(), category.getSlug(),
                                category.getDescription(), media(category.getImageId()), category.getSortOrder(),
                                children);
        }

        // ── Collection mapper ─────────────────────────────────────────────────
        private CollectionResponse collectionResponse(Collection collection) {
                MediaResponse img = media(collection.getImageId());
                return new CollectionResponse(
                                collection.getId(), collection.getName(), collection.getSlug(),
                                collection.getDescription(), img, img != null ? img.url() : null);
        }

        // ── Banner mapper ─────────────────────────────────────────────────────
        private Optional<BannerResponse> bannerResponse(Banner banner) {
                MediaResponse img = media(banner.getMediaId());
                if (img == null)
                        return Optional.empty();
                return Optional.of(new BannerResponse(
                                banner.getId(), banner.getTitle(), img, img.url(),
                                banner.getTargetUrl(), banner.getSortOrder(),
                                banner.getStartsAt(), banner.getEndsAt()));
        }

        // ── Product section helper ────────────────────────────────────────────
        private ProductSectionResponse productSection(String key, String title, String viewAllUrl,
                        Page<Product> page) {
                return new ProductSectionResponse(key, title, viewAllUrl, productSummaries(page.getContent()));
        }

        // ── Page response mapper ──────────────────────────────────────────────
        private ProductPageResponse toPageResponse(Page<Product> page) {
                return new ProductPageResponse(
                                productSummaries(page.getContent()),
                                page.getNumber(), page.getSize(),
                                page.getTotalElements(), page.getTotalPages(),
                                page.hasNext(), page.hasPrevious());
        }

        // ── Product summary mapper ────────────────────────────────────────────
        private List<ProductSummaryResponse> productSummaries(List<Product> products) {
                if (products.isEmpty())
                        return List.of();
                List<Long> productIds = products.stream().map(Product::getId).toList();
                Map<Long, ProductVariantRepository.CardPriceView> prices = productVariantRepository
                                .findCardPricesByProductIds(productIds).stream()
                                .collect(Collectors.toMap(ProductVariantRepository.CardPriceView::getProductId,
                                                value -> value));
                Map<Long, List<ProductImageResponse>> images = productImages(productIds);
                return products.stream().map(product -> productSummary(product, prices.get(product.getId()),
                                images.getOrDefault(product.getId(), List.of()).stream().findFirst()
                                                .map(ProductImageResponse::media).orElse(null)))
                                .toList();
        }

        private ProductSummaryResponse productSummary(Product product, ProductVariantRepository.CardPriceView priceView,
                        MediaResponse image) {
                BigDecimal variantPrice = priceView == null || priceView.getPrice() == null ? product.getBasePrice()
                                : priceView.getPrice();
                BigDecimal salePrice = priceView == null ? null : priceView.getSalePrice();
                boolean onSale = salePrice != null;

                return new ProductSummaryResponse(
                                product.getId(), product.getName(), product.getSlug(), product.getDescription(),
                                product.getBasePrice(), variantPrice, salePrice, onSale,
                                product.getGender(), product.getMaterial(), product.isFeatured(),
                                image, image != null ? image.url() : null,
                                product.getCreatedAt());
        }

        // ── Media resolver ────────────────────────────────────────────────────
        private MediaResponse media(Long id) {
                if (id == null)
                        return null;
                return mediaRepository.findByIdAndActive(id, ACTIVE)
                                .map(m -> new MediaResponse(
                                                m.getId(), m.getBucket(), m.getObjectKey(),
                                                m.getContentType(), m.getSizeBytes(),
                                                buildUrl(m.getBucket(), m.getObjectKey())))
                                .orElse(null);
        }

        private Product activeProduct(String slug) {
                return productRepository.findBySlugAndActive(slug, ACTIVE)
                                .orElseThrow(() -> notFound("Product not found: " + slug));
        }

        private VariantResponse variantResponse(ProductVariantRepository.VariantView value) {
                return new VariantResponse(value.getId(), value.getSku(), value.getPrice(), value.getSalePrice(),
                                value.getStockQuantity(), value.getReservedQuantity(),
                                Math.max(0, value.getStockQuantity() - value.getReservedQuantity()), value.getSizeId(),
                                value.getSizeName(),
                                value.getColorId(), value.getColorName(), value.getColorHexCode());
        }

        private Map<Long, List<ProductImageResponse>> productImages(List<Long> productIds) {
                if (productIds.isEmpty())
                        return Map.of();
                List<ProductImage> images = productImageRepository
                                .findByProductIdInAndActiveOrderByProductIdAscSortOrderAscIdAsc(productIds, ACTIVE);
                if (images.isEmpty())
                        return Map.of();
                Map<Long, MediaResponse> mediaById = mediaRepository.findByIdInAndActive(
                                images.stream().map(ProductImage::getMediaId).distinct().toList(), ACTIVE).stream()
                                .collect(Collectors.toMap(Media::getId,
                                                m -> new MediaResponse(m.getId(), m.getBucket(), m.getObjectKey(),
                                                                m.getContentType(), m.getSizeBytes(),
                                                                buildUrl(m.getBucket(), m.getObjectKey()))));
                return images.stream().filter(image -> mediaById.containsKey(image.getMediaId()))
                                .collect(Collectors.groupingBy(
                                                ProductImage::getProductId,
                                                Collectors.mapping(image -> new ProductImageResponse(image.getId(),
                                                                mediaById.get(image.getMediaId()), image.getColorId(),
                                                                image.getAltText(), image.getSortOrder()),
                                                                Collectors.toList())));
        }

        private StoreResponse storeResponse(Store store) {
                return new StoreResponse(store.getId(), store.getName(), store.getAddress(), store.getPhone(),
                                store.getOpeningHours(), store.getLatitude(), store.getLongitude());
        }

        private ArticleSummaryResponse articleSummary(Article article) {
                return new ArticleSummaryResponse(article.getId(), article.getName(), article.getSlug(),
                                media(article.getMediaId()),
                                article.getPublishedAt());
        }

        private String normalize(String value) {
                return value == null || value.isBlank() ? null : value.trim();
        }

        private String sortKey(String sort) {
                return switch (normalize(sort) == null ? "newest" : normalize(sort).toLowerCase()) {
                        case "price_asc", "priceasc" -> "price_asc";
                        case "price_desc", "pricedesc" -> "price_desc";
                        case "name_asc", "nameasc" -> "name_asc";
                        case "name_desc", "namedesc" -> "name_desc";
                        default -> "newest";
                };
        }

        /**
         * Constructs the public MinIO/CDN URL for an object.
         * Pattern: {@code <minioEndpoint>/<bucket>/<objectKey>}
         */
        private String buildUrl(String bucket, String objectKey) {
                return minioEndpoint.stripTrailing() + "/" + bucket + "/" + objectKey;
        }

        private PageRequest pageable(int page, int size) {
                return PageRequest.of(Math.max(0, page), Math.min(Math.max(1, size), 50));
        }

        private ResponseStatusException notFound(String message) {
                return new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        }
}
