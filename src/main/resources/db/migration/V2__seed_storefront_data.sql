-- =====================================================
-- V2: demo seed data

INSERT IGNORE INTO media (object_key, bucket, content_type, size_bytes) VALUES
  -- Banner images
  ('banners/hero-women-fw24.jpg',          'fashion-media', 'image/jpeg', 204800),
  ('banners/hero-men-fw24.jpg',            'fashion-media', 'image/jpeg', 204800),
  ('banners/hero-sale.jpg',                'fashion-media', 'image/jpeg', 204800),
  ('banners/promo-new-arrivals.jpg',       'fashion-media', 'image/jpeg', 102400),
  ('banners/promo-accessories.jpg',        'fashion-media', 'image/jpeg', 102400),
  -- Collection images
  ('collections/fall-winter-2024.jpg',     'fashion-media', 'image/jpeg', 153600),
  ('collections/spring-summer-2025.jpg',   'fashion-media', 'image/jpeg', 153600),
  ('collections/avenzo-classics.jpg',      'fashion-media', 'image/jpeg', 153600),
  -- Category images
  ('categories/women-shoes.jpg',           'fashion-media', 'image/jpeg', 102400),
  ('categories/women-bags.jpg',            'fashion-media', 'image/jpeg', 102400),
  ('categories/women-accessories.jpg',     'fashion-media', 'image/jpeg', 102400),
  ('categories/men-shoes.jpg',             'fashion-media', 'image/jpeg', 102400),
  ('categories/men-bags.jpg',              'fashion-media', 'image/jpeg', 102400),
  ('categories/men-accessories.jpg',       'fashion-media', 'image/jpeg', 102400),
  -- Product hero images
  ('products/velvet-block-heel-pump.jpg',  'fashion-media', 'image/jpeg', 81920),
  ('products/quilted-chain-bag.jpg',       'fashion-media', 'image/jpeg', 81920),
  ('products/pearl-hoop-earrings.jpg',     'fashion-media', 'image/jpeg', 81920),
  ('products/oxford-brogue-shoe.jpg',      'fashion-media', 'image/jpeg', 81920),
  ('products/leather-tote-bag.jpg',        'fashion-media', 'image/jpeg', 81920),
  ('products/woven-crossbody-bag.jpg',     'fashion-media', 'image/jpeg', 81920),
  ('products/strappy-stiletto-sandal.jpg', 'fashion-media', 'image/jpeg', 81920),
  ('products/platform-loafer.jpg',         'fashion-media', 'image/jpeg', 81920),
  ('products/minimalist-sneaker.jpg',      'fashion-media', 'image/jpeg', 81920),
  ('products/mens-derby-loafer.jpg',       'fashion-media', 'image/jpeg', 81920),
  ('products/mens-leather-bifold.jpg',     'fashion-media', 'image/jpeg', 81920),
  ('products/mens-canvas-tote.jpg',        'fashion-media', 'image/jpeg', 81920);

-- ─────────────────────────── Collections ──────────────────────────────────

INSERT IGNORE INTO collections (name, slug, description, image_id) VALUES
  ('Fall Winter 2024',   'fall-winter-2024',   'The latest Fall/Winter 2024 collection from AVENZO.',
   (SELECT id FROM media WHERE object_key = 'collections/fall-winter-2024.jpg')),
  ('Spring Summer 2025', 'spring-summer-2025', 'Fresh looks for Spring/Summer 2025.',
   (SELECT id FROM media WHERE object_key = 'collections/spring-summer-2025.jpg')),
  ('AVENZO Classics',     'avenzo-classics',     'Timeless styles designed for the AVENZO wardrobe.',
   (SELECT id FROM media WHERE object_key = 'collections/avenzo-classics.jpg'));

-- ─────────────────────────── Categories ───────────────────────────────────
-- V1 already inserted: Men (slug=men), Women (slug=women)
-- and their Shoes/Bags children.
-- V2 adds: Accessories children + images + New Arrivals + Sale top-level cats.

-- Assign images to existing V1 categories
UPDATE categories c JOIN media m ON m.object_key = 'categories/women-shoes.jpg'
  SET c.image_id = m.id WHERE c.slug = 'women-shoes';
UPDATE categories c JOIN media m ON m.object_key = 'categories/women-bags.jpg'
  SET c.image_id = m.id WHERE c.slug = 'women-bags';
UPDATE categories c JOIN media m ON m.object_key = 'categories/men-shoes.jpg'
  SET c.image_id = m.id WHERE c.slug = 'men-shoes';
UPDATE categories c JOIN media m ON m.object_key = 'categories/men-bags.jpg'
  SET c.image_id = m.id WHERE c.slug = 'men-bags';

-- Add Accessories under Women and Men
INSERT INTO categories (parent_id, name, slug, description, image_id, sort_order)
SELECT c.id, 'Accessories', 'women-accessories', 'Women''s jewellery, belts, and small leather goods.', m.id, 3
FROM categories c JOIN media m ON m.object_key = 'categories/women-accessories.jpg'
WHERE c.slug = 'women'
UNION ALL
SELECT c.id, 'Accessories', 'men-accessories', 'Men''s belts, card holders, and small leather goods.', m.id, 3
FROM categories c JOIN media m ON m.object_key = 'categories/men-accessories.jpg'
WHERE c.slug = 'men';

-- Top-level "New Arrivals" and "Sale" landing categories
INSERT INTO categories (name, slug, description, sort_order) VALUES
  ('New Arrivals', 'new-arrivals', 'The latest drops across all categories.', 3),
  ('Sale',         'sale',         'Up to 50% off selected styles.',           4);

-- ─────────────────────────── Navigation items ─────────────────────────────
-- Top-level structure: Women | Men | New Arrivals | Collections | Sale
-- V1 already inserted top-level Women and Men nav items linked to their categories.
-- V2 fixes sort_order and adds: New Arrivals, Collections, Sale tabs
--     plus Accessories children and cross-category promo links.

-- Ensure correct sort order for V1 items
UPDATE navigation_items SET sort_order = 1 WHERE slug = 'women';
UPDATE navigation_items SET sort_order = 2 WHERE slug = 'men';

-- Add child nav items for Accessories (Women)
INSERT INTO navigation_items (parent_id, name, slug, category_id, sort_order)
SELECT n.id, 'Accessories', 'women-accessories-nav', c.id, 3
FROM navigation_items n
JOIN categories c ON c.slug = 'women-accessories'
WHERE n.slug = 'women';

-- Add child nav items for Accessories (Men)
INSERT INTO navigation_items (parent_id, name, slug, category_id, sort_order)
SELECT n.id, 'Accessories', 'men-accessories-nav', c.id, 3
FROM navigation_items n
JOIN categories c ON c.slug = 'men-accessories'
WHERE n.slug = 'men';

-- Top-level: New Arrivals
INSERT INTO navigation_items (name, slug, category_id, url, sort_order)
SELECT 'New Arrivals', 'new-arrivals-nav', c.id, '/new-arrivals', 3
FROM categories c WHERE c.slug = 'new-arrivals';

-- Top-level: Collections (links to /collections listing page)
INSERT INTO navigation_items (name, slug, url, sort_order) VALUES
  ('Collections', 'collections-nav', '/collections', 4);

-- Children of Collections: one entry per active collection
INSERT INTO navigation_items (parent_id, name, slug, collection_id, sort_order)
SELECT n.id, col.name, CONCAT('nav-col-', col.slug), col.id, col.id
FROM navigation_items n
CROSS JOIN collections col
WHERE n.slug = 'collections-nav' AND col.is_active = 1;

-- Top-level: Sale
INSERT INTO navigation_items (name, slug, category_id, url, sort_order)
SELECT 'Sale', 'sale-nav', c.id, '/sale', 5
FROM categories c WHERE c.slug = 'sale';

-- ─────────────────────────── Banners ──────────────────────────────────────
-- sort_order < 100  → hero banners  (full-width carousel)
-- sort_order >= 100 → promotional banners (secondary strip)

INSERT INTO banners (title, media_id, target_url, sort_order) VALUES
  ('Fall Winter 2024 — New Collection',
   (SELECT id FROM media WHERE object_key = 'banners/hero-women-fw24.jpg'),
   '/collections/fall-winter-2024', 1),
  ('Men''s Edit — Bold Essentials',
   (SELECT id FROM media WHERE object_key = 'banners/hero-men-fw24.jpg'),
   '/categories/men', 2),
  ('Sale — Up to 50% Off',
   (SELECT id FROM media WHERE object_key = 'banners/hero-sale.jpg'),
   '/sale', 3),
  -- Promotional banners
  ('New Arrivals — Just Dropped',
   (SELECT id FROM media WHERE object_key = 'banners/promo-new-arrivals.jpg'),
   '/new-arrivals', 100),
  ('Accessories — Complete the Look',
   (SELECT id FROM media WHERE object_key = 'banners/promo-accessories.jpg'),
   '/categories/women-accessories', 101);

-- ─────────────────────────── Colors & Sizes ───────────────────────────────
-- V1 already inserted: Black, Brown, sizes 40-42 + ONE SIZE.
-- V2 adds more colors and sizes.

INSERT INTO colors (name, hex_code) VALUES
  ('Nude',          '#E8C4A0'),
  ('Cream',         '#FFFDD0'),
  ('Navy',          '#001F5B'),
  ('Tan',           '#D2B48C'),
  ('White',         '#FFFFFF'),
  ('Gold',          '#FFD700'),
  ('Silver',        '#C0C0C0');

INSERT INTO sizes (name, sort_order) VALUES
  ('35', 35), ('36', 36), ('37', 37), ('38', 38), ('39', 39),
  ('43', 43), ('44', 44), ('45', 45);

-- ─────────────────────────── Products ─────────────────────────────────────

INSERT INTO products (name, slug, description, base_price, gender, material, is_featured) VALUES
  -- Women's Shoes
  ('Velvet Block Heel Pump',    'velvet-block-heel-pump',
   'Luxurious velvet upper with a comfortable block heel. A versatile evening staple.',
   129.00, 'WOMEN', 'Velvet', 1),
  ('Strappy Stiletto Sandal',   'strappy-stiletto-sandal',
   'Minimalist strappy sandal with a sculpted stiletto heel.',
   109.00, 'WOMEN', 'Patent Leather', 0),
  ('Platform Loafer',           'platform-loafer',
   'Chunky platform loafer with gold-tone hardware. Street-ready sophistication.',
   99.00,  'WOMEN', 'Full-grain Leather', 1),
  ('Minimalist Sneaker',        'minimalist-sneaker',
   'Clean-lined leather sneaker for effortless everyday style.',
   89.00,  'WOMEN', 'Leather', 0),

  -- Women's Bags
  ('Quilted Chain Bag',         'quilted-chain-bag',
   'Compact quilted flap bag with interlocking chain strap.',
   189.00, 'WOMEN', 'Lambskin Leather', 1),
  ('Woven Crossbody Bag',       'woven-crossbody-bag',
   'Artisanal woven leather crossbody with adjustable strap.',
   149.00, 'WOMEN', 'Woven Leather', 0),

  -- Women's Accessories
  ('Pearl Hoop Earrings',       'pearl-hoop-earrings',
   'Delicate freshwater pearl hoop earrings. Polished 18k gold-tone finish.',
   49.00,  'WOMEN', 'Gold-plated Brass', 0),

  -- Men's Shoes
  ('Oxford Brogue Shoe',        'oxford-brogue-shoe',
   'Full-brogue oxford in polished calfskin. A wardrobe cornerstone.',
   139.00, 'MEN', 'Calfskin Leather', 1),
  ('Classic Derby Loafer',      'classic-derby-loafer',
   'Refined penny loafer with leather lining and rubber sole.',
   119.00, 'MEN', 'Full-grain Leather', 0),
  ('Canvas Slip-on',            'canvas-slip-on',
   'Lightweight canvas slip-on sneaker with vulcanised rubber sole.',
   69.00,  'MEN', 'Canvas', 0),

  -- Men's Bags
  ('Leather Tote Bag',          'leather-tote-bag',
   'Spacious leather tote with zip top and interior pockets.',
   169.00, 'MEN', 'Pebbled Leather', 1),
  ('Men''s Canvas Tote',        'mens-canvas-tote',
   'Durable canvas tote with leather handles and contrast stitching.',
   79.00,  'MEN', 'Canvas & Leather', 0);

-- ─────────────────────────── Product → Categories ─────────────────────────

INSERT INTO product_categories (product_id, category_id)
SELECT p.id, c.id FROM products p CROSS JOIN categories c
WHERE (p.slug = 'velvet-block-heel-pump'  AND c.slug IN ('women-shoes', 'new-arrivals'))
   OR (p.slug = 'strappy-stiletto-sandal' AND c.slug = 'women-shoes')
   OR (p.slug = 'platform-loafer'         AND c.slug IN ('women-shoes', 'new-arrivals'))
   OR (p.slug = 'minimalist-sneaker'      AND c.slug = 'women-shoes')
   OR (p.slug = 'quilted-chain-bag'       AND c.slug IN ('women-bags', 'new-arrivals'))
   OR (p.slug = 'woven-crossbody-bag'     AND c.slug = 'women-bags')
   OR (p.slug = 'pearl-hoop-earrings'     AND c.slug = 'women-accessories')
   OR (p.slug = 'oxford-brogue-shoe'      AND c.slug IN ('men-shoes', 'new-arrivals'))
   OR (p.slug = 'classic-derby-loafer'    AND c.slug = 'men-shoes')
   OR (p.slug = 'canvas-slip-on'          AND c.slug IN ('men-shoes', 'sale'))
   OR (p.slug = 'leather-tote-bag'        AND c.slug = 'men-bags')
   OR (p.slug = 'mens-canvas-tote'        AND c.slug IN ('men-bags', 'sale'));

-- ─────────────────────────── Product → Collections ────────────────────────

INSERT INTO product_collections (product_id, collection_id)
SELECT p.id, col.id FROM products p CROSS JOIN collections col
WHERE (p.slug IN ('velvet-block-heel-pump','quilted-chain-bag','oxford-brogue-shoe','leather-tote-bag')
       AND col.slug = 'fall-winter-2024')
   OR (p.slug IN ('platform-loafer','minimalist-sneaker','woven-crossbody-bag','canvas-slip-on')
       AND col.slug = 'spring-summer-2025')
   OR (p.slug IN ('classic-derby-loafer','strappy-stiletto-sandal','mens-canvas-tote','pearl-hoop-earrings')
       AND col.slug = 'avenzo-classics');

-- ─────────────────────────── Product Variants ─────────────────────────────
-- Women's sizes: 35-40  |  Men's sizes: 40-45
-- Several products get a sale_price to populate /products/sale

-- Helper SETs for readability
SET @black   = (SELECT id FROM colors WHERE name = 'Black');
SET @brown   = (SELECT id FROM colors WHERE name = 'Brown');
SET @nude    = (SELECT id FROM colors WHERE name = 'Nude');
SET @cream   = (SELECT id FROM colors WHERE name = 'Cream');
SET @navy    = (SELECT id FROM colors WHERE name = 'Navy');
SET @tan     = (SELECT id FROM colors WHERE name = 'Tan');
SET @white   = (SELECT id FROM colors WHERE name = 'White');
SET @gold    = (SELECT id FROM colors WHERE name = 'Gold');
SET @silver  = (SELECT id FROM colors WHERE name = 'Silver');

SET @s35  = (SELECT id FROM sizes WHERE name = '35');
SET @s36  = (SELECT id FROM sizes WHERE name = '36');
SET @s37  = (SELECT id FROM sizes WHERE name = '37');
SET @s38  = (SELECT id FROM sizes WHERE name = '38');
SET @s39  = (SELECT id FROM sizes WHERE name = '39');
SET @s40  = (SELECT id FROM sizes WHERE name = '40');
SET @s41  = (SELECT id FROM sizes WHERE name = '41');
SET @s42  = (SELECT id FROM sizes WHERE name = '42');
SET @s43  = (SELECT id FROM sizes WHERE name = '43');
SET @s44  = (SELECT id FROM sizes WHERE name = '44');
SET @s45  = (SELECT id FROM sizes WHERE name = '45');
SET @os   = (SELECT id FROM sizes WHERE name = 'ONE SIZE');

-- Velvet Block Heel Pump — Black (sale!) + Nude
INSERT INTO product_variants (product_id, size_id, color_id, sku, price, sale_price, stock_quantity)
SELECT p.id, s.id, @black, CONCAT('VBHP-BK-', s.name), 129.00, 89.00, 10
FROM products p CROSS JOIN sizes s
WHERE p.slug = 'velvet-block-heel-pump' AND s.name IN ('36','37','38','39');

INSERT INTO product_variants (product_id, size_id, color_id, sku, price, stock_quantity)
SELECT p.id, s.id, @nude, CONCAT('VBHP-NU-', s.name), 129.00, 8
FROM products p CROSS JOIN sizes s
WHERE p.slug = 'velvet-block-heel-pump' AND s.name IN ('36','37','38','39');

-- Strappy Stiletto Sandal — Black + Nude
INSERT INTO product_variants (product_id, size_id, color_id, sku, price, stock_quantity)
SELECT p.id, s.id, @black, CONCAT('SSS-BK-', s.name), 109.00, 12
FROM products p CROSS JOIN sizes s
WHERE p.slug = 'strappy-stiletto-sandal' AND s.name IN ('36','37','38','39','40');

INSERT INTO product_variants (product_id, size_id, color_id, sku, price, sale_price, stock_quantity)
SELECT p.id, s.id, @nude, CONCAT('SSS-NU-', s.name), 109.00, 79.00, 6
FROM products p CROSS JOIN sizes s
WHERE p.slug = 'strappy-stiletto-sandal' AND s.name IN ('36','37','38');

-- Platform Loafer — Black + White
INSERT INTO product_variants (product_id, size_id, color_id, sku, price, stock_quantity)
SELECT p.id, s.id, @black, CONCAT('PL-BK-', s.name), 99.00, 15
FROM products p CROSS JOIN sizes s
WHERE p.slug = 'platform-loafer' AND s.name IN ('35','36','37','38','39','40');

INSERT INTO product_variants (product_id, size_id, color_id, sku, price, stock_quantity)
SELECT p.id, s.id, @white, CONCAT('PL-WH-', s.name), 99.00, 10
FROM products p CROSS JOIN sizes s
WHERE p.slug = 'platform-loafer' AND s.name IN ('36','37','38','39');

-- Minimalist Sneaker — White (sale!) + Black
INSERT INTO product_variants (product_id, size_id, color_id, sku, price, sale_price, stock_quantity)
SELECT p.id, s.id, @white, CONCAT('MS-WH-', s.name), 89.00, 69.00, 20
FROM products p CROSS JOIN sizes s
WHERE p.slug = 'minimalist-sneaker' AND s.name IN ('35','36','37','38','39','40');

INSERT INTO product_variants (product_id, size_id, color_id, sku, price, stock_quantity)
SELECT p.id, s.id, @black, CONCAT('MS-BK-', s.name), 89.00, 15
FROM products p CROSS JOIN sizes s
WHERE p.slug = 'minimalist-sneaker' AND s.name IN ('36','37','38','39');

-- Quilted Chain Bag — Black + Cream
INSERT INTO product_variants (product_id, size_id, color_id, sku, price, stock_quantity)
SELECT p.id, NULL, @black, CONCAT('QCB-BK-', p.id), 189.00, 8
FROM products p WHERE p.slug = 'quilted-chain-bag';

INSERT INTO product_variants (product_id, size_id, color_id, sku, price, stock_quantity)
SELECT p.id, NULL, @cream, CONCAT('QCB-CR-', p.id), 189.00, 5
FROM products p WHERE p.slug = 'quilted-chain-bag';

-- Woven Crossbody Bag — Tan (sale!) + Black
INSERT INTO product_variants (product_id, size_id, color_id, sku, price, sale_price, stock_quantity)
SELECT p.id, NULL, @tan, CONCAT('WCB-TN-', p.id), 149.00, 109.00, 7
FROM products p WHERE p.slug = 'woven-crossbody-bag';

INSERT INTO product_variants (product_id, size_id, color_id, sku, price, stock_quantity)
SELECT p.id, NULL, @black, CONCAT('WCB-BK-', p.id), 149.00, 6
FROM products p WHERE p.slug = 'woven-crossbody-bag';

-- Pearl Hoop Earrings — Gold + Silver (ONE SIZE)
INSERT INTO product_variants (product_id, size_id, color_id, sku, price, stock_quantity)
SELECT p.id, @os, @gold, CONCAT('PHE-GD-', p.id), 49.00, 25
FROM products p WHERE p.slug = 'pearl-hoop-earrings';

INSERT INTO product_variants (product_id, size_id, color_id, sku, price, stock_quantity)
SELECT p.id, @os, @silver, CONCAT('PHE-SV-', p.id), 49.00, 20
FROM products p WHERE p.slug = 'pearl-hoop-earrings';

-- Oxford Brogue Shoe — Black + Brown
INSERT INTO product_variants (product_id, size_id, color_id, sku, price, stock_quantity)
SELECT p.id, s.id, @black, CONCAT('OBS-BK-', s.name), 139.00, 10
FROM products p CROSS JOIN sizes s
WHERE p.slug = 'oxford-brogue-shoe' AND s.name IN ('40','41','42','43','44');

INSERT INTO product_variants (product_id, size_id, color_id, sku, price, stock_quantity)
SELECT p.id, s.id, @brown, CONCAT('OBS-BR-', s.name), 139.00, 8
FROM products p CROSS JOIN sizes s
WHERE p.slug = 'oxford-brogue-shoe' AND s.name IN ('40','41','42','43');

-- Classic Derby Loafer — Black + Tan (sale!)
INSERT INTO product_variants (product_id, size_id, color_id, sku, price, stock_quantity)
SELECT p.id, s.id, @black, CONCAT('CDL-BK-', s.name), 119.00, 12
FROM products p CROSS JOIN sizes s
WHERE p.slug = 'classic-derby-loafer' AND s.name IN ('40','41','42','43','44');

INSERT INTO product_variants (product_id, size_id, color_id, sku, price, sale_price, stock_quantity)
SELECT p.id, s.id, @tan, CONCAT('CDL-TN-', s.name), 119.00, 89.00, 5
FROM products p CROSS JOIN sizes s
WHERE p.slug = 'classic-derby-loafer' AND s.name IN ('41','42','43');

-- Canvas Slip-on — Navy (sale!) + White
INSERT INTO product_variants (product_id, size_id, color_id, sku, price, sale_price, stock_quantity)
SELECT p.id, s.id, @navy, CONCAT('CS-NV-', s.name), 69.00, 49.00, 18
FROM products p CROSS JOIN sizes s
WHERE p.slug = 'canvas-slip-on' AND s.name IN ('40','41','42','43','44','45');

INSERT INTO product_variants (product_id, size_id, color_id, sku, price, stock_quantity)
SELECT p.id, s.id, @white, CONCAT('CS-WH-', s.name), 69.00, 12
FROM products p CROSS JOIN sizes s
WHERE p.slug = 'canvas-slip-on' AND s.name IN ('41','42','43','44');

-- Leather Tote Bag — Black + Tan
INSERT INTO product_variants (product_id, size_id, color_id, sku, price, stock_quantity)
SELECT p.id, NULL, @black, CONCAT('LTB-BK-', p.id), 169.00, 6
FROM products p WHERE p.slug = 'leather-tote-bag';

INSERT INTO product_variants (product_id, size_id, color_id, sku, price, stock_quantity)
SELECT p.id, NULL, @tan, CONCAT('LTB-TN-', p.id), 169.00, 4
FROM products p WHERE p.slug = 'leather-tote-bag';

-- Men's Canvas Tote — Navy (sale!) + Black
INSERT INTO product_variants (product_id, size_id, color_id, sku, price, sale_price, stock_quantity)
SELECT p.id, NULL, @navy, CONCAT('MCT-NV-', p.id), 79.00, 59.00, 14
FROM products p WHERE p.slug = 'mens-canvas-tote';

INSERT INTO product_variants (product_id, size_id, color_id, sku, price, stock_quantity)
SELECT p.id, NULL, @black, CONCAT('MCT-BK-', p.id), 79.00, 10
FROM products p WHERE p.slug = 'mens-canvas-tote';

-- ─────────────────────────── Inventory opening stock ─────────────────────

INSERT INTO inventory_movements (variant_id, movement_type, quantity_change, note)
SELECT id, 'IN', stock_quantity, 'V2 demo opening stock'
FROM product_variants
WHERE sku LIKE 'VBHP-%' OR sku LIKE 'SSS-%'  OR sku LIKE 'PL-%'
   OR sku LIKE 'MS-%'   OR sku LIKE 'QCB-%'  OR sku LIKE 'WCB-%'
   OR sku LIKE 'PHE-%'  OR sku LIKE 'OBS-%'  OR sku LIKE 'CDL-%'
   OR sku LIKE 'CS-%'   OR sku LIKE 'LTB-%'  OR sku LIKE 'MCT-%';

-- ─────────────────────────── Product Images ───────────────────────────────

INSERT INTO product_images (product_id, media_id, sort_order, alt_text)
SELECT p.id, m.id, 0, p.name
FROM products p JOIN media m ON m.object_key = CONCAT('products/', p.slug, '.jpg')
WHERE p.slug IN (
  'velvet-block-heel-pump', 'quilted-chain-bag', 'pearl-hoop-earrings',
  'oxford-brogue-shoe', 'leather-tote-bag', 'woven-crossbody-bag',
  'strappy-stiletto-sandal', 'platform-loafer', 'minimalist-sneaker',
  'classic-derby-loafer', 'mens-canvas-tote'
);

-- Men's Derby Loafer image (product slug differs from media file)
INSERT INTO product_images (product_id, media_id, sort_order, alt_text)
SELECT p.id, m.id, 0, p.name
FROM products p JOIN media m ON m.object_key = 'products/mens-derby-loafer.jpg'
WHERE p.slug = 'classic-derby-loafer'
ON DUPLICATE KEY UPDATE sort_order = sort_order;  -- no-op if already inserted above

-- Men's Canvas Tote
INSERT INTO product_images (product_id, media_id, sort_order, alt_text)
SELECT p.id, m.id, 0, p.name
FROM products p JOIN media m ON m.object_key = 'products/mens-canvas-tote.jpg'
WHERE p.slug = 'mens-canvas-tote'
ON DUPLICATE KEY UPDATE sort_order = sort_order;

-- ─────────────────────────── Mark new products as featured ───────────────
-- Ensure at least 4 featured products for the homepage Featured section

UPDATE products SET is_featured = 1
WHERE slug IN ('velvet-block-heel-pump', 'quilted-chain-bag',
               'oxford-brogue-shoe',     'leather-tote-bag',
               'platform-loafer',        'classic-derby-loafer');
