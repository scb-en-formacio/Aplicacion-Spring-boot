INSERT INTO usuario (id, email, nombre, apellidos, contrasenya, username) 
VALUES (default, 'admin@admin.com', 'admin', 'administrador', '$2a$04$n6WIRDQlIByVFi.5rtQwEOTAzpzLPzIIG/O6quaxRKY2LlIHG8uty',
 'admin') ON CONFLICT (username)
DO nothing;

INSERT INTO role (id, descripcion, nombre) select maximo, 'ROLE_ADMIN', 'ADMIN' FROM role,
(select MAX(id)+1 maximo FROM role) m
WHERE NOT EXISTS (SELECT nombre FROM role WHERE nombre = 'ADMIN') LIMIT 1;

INSERT INTO role (id, descripcion, nombre) select maximo, 'ROLE_USUARIO', 'USUARIO' FROM role,
(select MAX(id)+1 maximo FROM role) m
WHERE NOT EXISTS (SELECT nombre FROM role WHERE nombre = 'USUARIO') LIMIT 1;

INSERT INTO role (id, descripcion, nombre) select maximo, 'ROLE_SUPERVISOR', 'SUPERVISOR' FROM role,
(select MAX(id)+1 maximo FROM role) m
WHERE NOT EXISTS (SELECT nombre FROM role WHERE nombre = 'SUPERVISOR') LIMIT 1;

INSERT INTO usuario_roles (usuario_id, role_id) 
select idu, idr FROM (select id idu FROM usuario where username='admin') a,
( SELECT id idr from role where role.nombre= 'ADMIN' ) b ON conflict
(usuario_id, role_id) DO NOTHING;