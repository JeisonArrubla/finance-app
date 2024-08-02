INSERT INTO finance_db.roles (name) VALUES ('ROLE_ADMIN');
INSERT INTO finance_db.roles (name) VALUES ('ROLE_USER');

INSERT INTO finance_db.users (enabled, id, username, password) VALUES (1, 1, "user", "$2a$10$C1.XbN79T/O9D6F18tLREOgw.PSl3u7cGa7mBoObb2NAJByCXs49C");
INSERT INTO finance_db.users (enabled, id, username, password) VALUES (1, 2, "admin", "$2a$10$C1.XbN79T/O9D6F18tLREOgw.PSl3u7cGa7mBoObb2NAJByCXs49C");

INSERT INTO finance_db.users_roles (role_id, user_id) VALUES (2, 1);
INSERT INTO finance_db.users_roles (role_id, user_id) VALUES (2, 2);
INSERT INTO finance_db.users_roles (role_id, user_id) VALUES (1, 2);

INSERT INTO finance_db.accounts (balance, user_id, description) VALUES (0.00, 1, "Cuenta bancaria");
INSERT INTO finance_db.accounts (balance, user_id, description) VALUES (0.00, 1, "Efectivo");
INSERT INTO finance_db.accounts (balance, user_id, description) VALUES (0.00, 2, "Cuenta de ahorros");