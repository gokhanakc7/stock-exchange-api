-- Insert Stock Exchanges
INSERT INTO stock_exchange (name, description, live_in_market) VALUES ('NASDAQ', 'National Association of Securities Dealers Automated Quotations', true);
INSERT INTO stock_exchange (name, description, live_in_market) VALUES ('NYSE', 'New York Stock Exchange', true);

-- Insert Stocks
INSERT INTO stock (name, description, current_price, last_update) VALUES ('AAPL', 'Apple Inc.', 150.00, '2024-07-03T00:00:00');
INSERT INTO stock (name, description, current_price, last_update) VALUES ('MSFT', 'Microsoft Corporation', 300.00, '2024-07-03T00:00:00');
INSERT INTO stock (name, description, current_price, last_update) VALUES ('GOOGL', 'Alphabet Inc.', 2800.00, '2024-07-03T00:00:00');
INSERT INTO stock (name, description, current_price, last_update) VALUES ('AMZN', 'Amazon.com Inc.', 3500.00, '2024-07-03T00:00:00');
INSERT INTO stock (name, description, current_price, last_update) VALUES ('TSLA', 'Tesla Inc.', 700.00, '2024-07-03T00:00:00');

INSERT INTO stock (name, description, current_price, last_update) VALUES ('FB', 'Meta Platforms, Inc.', 350.00, '2024-07-03T00:00:00');
INSERT INTO stock (name, description, current_price, last_update) VALUES ('NFLX', 'Netflix Inc.', 590.00, '2024-07-03T00:00:00');
INSERT INTO stock (name, description, current_price, last_update) VALUES ('NVDA', 'NVIDIA Corporation', 800.00, '2024-07-03T00:00:00');
INSERT INTO stock (name, description, current_price, last_update) VALUES ('ADBE', 'Adobe Inc.', 620.00, '2024-07-03T00:00:00');
INSERT INTO stock (name, description, current_price, last_update) VALUES ('ORCL', 'Oracle Corporation', 90.00, '2024-07-03T00:00:00');

-- Link Stocks to Stock Exchanges
INSERT INTO stock_exchange_stock (stock_exchange_id, stock_id) VALUES (1, 1);
INSERT INTO stock_exchange_stock (stock_exchange_id, stock_id) VALUES (1, 2);
INSERT INTO stock_exchange_stock (stock_exchange_id, stock_id) VALUES (1, 3);
INSERT INTO stock_exchange_stock (stock_exchange_id, stock_id) VALUES (1, 4);
INSERT INTO stock_exchange_stock (stock_exchange_id, stock_id) VALUES (1, 5);

INSERT INTO stock_exchange_stock (stock_exchange_id, stock_id) VALUES (2, 6);
INSERT INTO stock_exchange_stock (stock_exchange_id, stock_id) VALUES (2, 7);
INSERT INTO stock_exchange_stock (stock_exchange_id, stock_id) VALUES (2, 8);
INSERT INTO stock_exchange_stock (stock_exchange_id, stock_id) VALUES (2, 9);
INSERT INTO stock_exchange_stock (stock_exchange_id, stock_id) VALUES (2, 10);
