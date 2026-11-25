CREATE TABLE inventory_movements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    variant_id BIGINT NOT NULL,
    order_id BIGINT NULL,
    created_by BIGINT NULL,
    movement_type VARCHAR(30) NOT NULL CHECK (movement_type IN ('IN','OUT','ADJUST','RETURN')),
    quantity_change INT NOT NULL CHECK (quantity_change <> 0),
    note VARCHAR(255),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

