CREATE TABLE orders (
                        id BIGSERIAL PRIMARY KEY,
                        item_id VARCHAR(255) NOT NULL,
                        quantity INT NOT NULL,
                        order_date TIMESTAMP NOT NULL,
                        status VARCHAR(255) NOT NULL
);