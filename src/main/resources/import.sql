/*Base de datos inicial*/
INSERT INTO rol (nombre) VALUES ('ROLE_ADMIN');
INSERT INTO rol (nombre) VALUES ('ROLE_VOLUNTARIO');
INSERT INTO rol (nombre) VALUES ('ROLE_DONANTE');


/*Usuarios*/
INSERT INTO usuario (numerodocumento, nombredocumento, nombre, apellidopaterno, apellidomaterno, celular, correo, password, fecharegistro, idrol) VALUES ('87654321', 'DNI', 'Marco', 'Del Pozo', 'Rossi', '888888888', 'marco@email.com', '$2a$12$xTrWRVqMbG905Slsmo4wb.J6Tl3JYLM3Agp8wAveSXIO8omegfkTq', NOW(), 1);

INSERT INTO usuario (numerodocumento, nombredocumento, nombre, apellidopaterno, apellidomaterno, celular, correo, password, fecharegistro, idrol) VALUES ('12345678', 'DNI', 'Juan', 'Perez', 'Lopez', '999999999', 'juan@email.com', '$2a$12$fwy1GDh.YYwYXnZXa4gIfuQIQuwkuY8u7e5isB/1HsykxnNaxyKa2', NOW(), 2);
/*usuario: Manuel | contraseña: Manuel23*/
INSERT INTO usuario (numerodocumento, nombredocumento, nombre, apellidopaterno, apellidomaterno, celular, correo, password, fecharegistro, idrol) VALUES ('12345678', 'DNI', 'Manuel', 'Velazco', 'Reyes', '943689000', 'manu@email.com', '$2a$12$ZdxNLquGsuaUqXwEZI4i2ehGsSx/0VmhAvPwCY/6gnnbtPZIO2bte', NOW(), 2);

/*usuario: Majo | contraseña: majo12345*/
INSERT INTO usuario (numerodocumento, nombredocumento, nombre, apellidopaterno, apellidomaterno, celular, correo, password, fecharegistro, idrol) VALUES ('87456321', 'DNI', 'Majo', 'Elorrieta', 'Carrion', '111111111', 'majo@email.com', '$2a$12$c/qVM.HTUva77Ku1Nic0g.BquWGyngxooSEq5nmrInlTByLSSQX.G', NOW(), 3);


/*Proyectos*/
INSERT INTO proyectos (nombreproyecto, descripcion, montoobjetivo, montorecaudado, fechainicio, fechafin, nombreorganizacion, escuelabeneficiada, cupoMaximo, imagen) VALUES ('Biblioteca Escolar', 'Construcción y equipamiento de una biblioteca para la escuela local.', 15000.00, 0, '2025-01-10', '2025-06-30', 'Fundacion Cultura Viva', 'Escuela Primaria N°12', 200, 'https://otramirada.pe/sites/default/files/https%3A/otramirada.pe/sites/default/files/filefield_paths/educ.rural_.jpeg/educ.rural_.jpeg');

INSERT INTO proyectos (nombreproyecto, descripcion, montoobjetivo, montorecaudado, fechainicio, fechafin, nombreorganizacion, escuelabeneficiada, cupoMaximo, imagen) VALUES ('Huerto Comunitario', 'Proyecto para crear un huerto educativo y sostenible.', 5000.00, 0, '2025-03-01', '2025-09-01', 'Red Verde', 'Escuela Secundaria Nº3', 50, 'https://gobernanzadelatierra.org.pe/wp-content/uploads/2017/07/rural-450x400.png');

INSERT INTO proyectos (nombreproyecto, descripcion, montoobjetivo, montorecaudado, fechainicio, fechafin, nombreorganizacion, escuelabeneficiada, cupoMaximo, imagen) VALUES ('Taller de Robótica', 'Compra de kits y formación para estudiantes de robótica.', 12000.00, 0, '2025-02-15', '2025-11-15', 'Tech4Kids', 'Colegio Técnico Central', 30, 'https://idehpucp.pucp.edu.pe/revista-memoria/wp-content/uploads/2025/04/000585250w1.webp');

INSERT INTO proyectos (nombreproyecto, descripcion, montoobjetivo, montorecaudado, fechainicio, fechafin, nombreorganizacion, escuelabeneficiada, cupoMaximo, imagen) VALUES ('Renovación de Cancha', 'Mejoras y seguridad en la cancha deportiva escolar.', 8000.00, 0, '2025-04-01', '2025-10-01', 'Deportes Para Todos', 'Escuela Deportiva Local', 150, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSs0xpQFLdM94zhEw9X6UaIg8ODqSPKem2aFQ&s');

INSERT INTO proyectos (nombreproyecto, descripcion, montoobjetivo, montorecaudado, fechainicio, fechafin, nombreorganizacion, escuelabeneficiada, cupoMaximo, imagen) VALUES ('Programa de Becas', 'Fondo para becas escolares y materiales didácticos.', 20000.00, 0, '2025-01-20', '2026-01-20', 'Manos Solidarias', 'Varias escuelas municipales', 20, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ0lCeNJQ5XkmbVzj0HZdBJyYG5kNvydtaiPA&s');

/*Comentarios*/
INSERT INTO comentario (comentario, estrella, idusuario) VALUES ('Excelente iniciativa, me encantó apoyar.', 4.5, (SELECT idusuario FROM usuario WHERE correo = 'marco@email.com'));

/*Notificacion*/
INSERT INTO notificacion (mensaje, tipo, fechaEnvio, leido, usuario_id) Values ('Bienvenido a la plataforma.', 'BIENVENIDA', '2025-11-01 10:00:00', FALSE, 1);

INSERT INTO notificacion (mensaje, tipo, fechaEnvio, leido, usuario_id) VALUES ('Tu cuenta ha sido actualizada.', 'SISTEMA', '2025-11-05 15:30:00', FALSE, 2);

INSERT INTO notificacion (mensaje, tipo, fechaEnvio, leido, usuario_id) VALUES ('Nuevo proyecto disponible para donar.', 'PROYECTO', '2025-11-10 09:15:00', FALSE, 3);



/*Inscripcion*/

/*Donacion*/


/* Pago*/

/*Universitarios*/
INSERT INTO universitario (codigoestudiante, idusuario) VALUES ('U2025001', 2);
INSERT INTO universitario (codigoestudiante, idusuario) VALUES ('U2025123', 3);

/*Wishlist*/
