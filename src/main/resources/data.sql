INSERT INTO usuario (email, nombre, apellidos, contrasenya, username) 
VALUES ('admin@admin.com', 'admin', 'administrador', '$2a$04$n6WIRDQlIByVFi.5rtQwEOTAzpzLPzIIG/O6quaxRKY2LlIHG8uty', 'admin');

INSERT INTO role (descripcion, nombre) VALUES ('ROLE_ADMIN', 'ADMIN');
INSERT INTO role (descripcion, nombre) VALUES ('ROLE_USUARIO', 'USUARIO');
INSERT INTO role (descripcion, nombre) VALUES ('ROLE_SUPERVISOR', 'SUPERVISOR');

INSERT INTO usuario_roles (usuario_id, role_id) VALUES ('1', '1');
