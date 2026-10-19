-- Inserción de roles
INSERT INTO rol (nombrerol) VALUES ('USER');
INSERT INTO rol (nombrerol) VALUES ('ADMIN');

-- Inserción de usuarios
INSERT INTO usuario (nombre ,apellidos, email, direccion, telefono, contrasena)
VALUES (
           'Jean Paul',
           'Quispe Salvador',
           'jean.paul@upc.edu.pe',
           'Av. Los desarrolladores 456',
           '999888777',
           '$2a$12$Mt0P5foXn.Z2lJp6XyojtOlipHDAAtsZBigi7fhLiG26hiwg41Pte'
       );
INSERT INTO usuario (nombre, apellidos, email, direccion, telefono, contrasena)
VALUES (
           'Alan Aaron',
           'Perez Saavedra',
           'alan.admin@upc.edu.pe',
           'Av. Los desarrolladores 456',
           '999888777',
           '$2a$12$Mt0P5foXn.Z2lJp6XyojtOlipHDAAtsZBigi7fhLiG26hiwg41Pte'
       );

-- Asociación usuario-rol
INSERT INTO usuariorol (idusuario, idrol) VALUES (1, 1);
INSERT INTO usuariorol (idusuario, idrol) VALUES (2, 2);

-- Insertar métodos de pago básicos
INSERT INTO metodopago (nombremetodo) VALUES
                                          ('Gratuito'),
                                          ('Visa'),
                                          ('Mastercard'),
                                          ('Yape');
