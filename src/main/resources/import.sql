//Base de datos inicial
INSERT INTO rol (nombre) VALUES ('ROLE_ADMIN');
INSERT INTO rol (nombre) VALUES ('ROLE_VOLUNTARIO');
INSERT INTO rol (nombre) VALUES ('ROLE_DONANTE');

INSERT INTO usuario (numerodocumento, nombredocumento, nombre, apellidopaterno, apellidomaterno, celular, correo, password, fecharegistro, idrol) VALUES ('87654321', 'DNI', 'Marco', 'Del Pozo', 'Rossi', '888888888', 'marco@email.com', '$2a$12$xTrWRVqMbG905Slsmo4wb.J6Tl3JYLM3Agp8wAveSXIO8omegfkTq', NOW(), 1);

INSERT INTO usuario (numerodocumento, nombredocumento, nombre, apellidopaterno, apellidomaterno, celular, correo, password, fecharegistro, idrol) VALUES ('12345678', 'DNI', 'Juan', 'Perez', 'Lopez', '999999999', 'juan@email.com', '$2a$12$fwy1GDh.YYwYXnZXa4gIfuQIQuwkuY8u7e5isB/1HsykxnNaxyKa2', NOW(), 2);

INSERT INTO proyectos (nombreproyecto, descripcion, montoobjetivo, montorecaudado, fechainicio, fechafin, nombreorganización, escuelabeneficiada, cupoMaximo, imagen) VALUES ('Biblioteca Escolar', 'Construcción y equipamiento de una biblioteca para la escuela local.', 15000.00, 1200.50, '2025-01-10', '2025-06-30', 'Fundacion Cultura Viva', 'Escuela Primaria N°12', 200, 'https://otramirada.pe/sites/default/files/https%3A/otramirada.pe/sites/default/files/filefield_paths/educ.rural_.jpeg/educ.rural_.jpeg');

INSERT INTO proyectos (nombreproyecto, descripcion, montoobjetivo, montorecaudado, fechainicio, fechafin, nombreorganización, escuelabeneficiada, cupoMaximo, imagen) VALUES ('Huerto Comunitario', 'Proyecto para crear un huerto educativo y sostenible.', 5000.00, 800.00, '2025-03-01', '2025-09-01', 'Red Verde', 'Escuela Secundaria Nº3', 50, 'https://gobernanzadelatierra.org.pe/wp-content/uploads/2017/07/rural-450x400.png');

INSERT INTO proyectos (nombreproyecto, descripcion, montoobjetivo, montorecaudado, fechainicio, fechafin, nombreorganización, escuelabeneficiada, cupoMaximo, imagen) VALUES ('Taller de Robótica', 'Compra de kits y formación para estudiantes de robótica.', 12000.00, 4500.00, '2025-02-15', '2025-11-15', 'Tech4Kids', 'Colegio Técnico Central', 30, 'https://idehpucp.pucp.edu.pe/revista-memoria/wp-content/uploads/2025/04/000585250w1.webp');

INSERT INTO proyectos (nombreproyecto, descripcion, montoobjetivo, montorecaudado, fechainicio, fechafin, nombreorganización, escuelabeneficiada, cupoMaximo, imagen) VALUES ('Renovación de Cancha', 'Mejoras y seguridad en la cancha deportiva escolar.', 8000.00, 300.00, '2025-04-01', '2025-10-01', 'Deportes Para Todos', 'Escuela Deportiva Local', 150, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSs0xpQFLdM94zhEw9X6UaIg8ODqSPKem2aFQ&s');

INSERT INTO proyectos (nombreproyecto, descripcion, montoobjetivo, montorecaudado, fechainicio, fechafin, nombreorganización, escuelabeneficiada, cupoMaximo, imagen) VALUES ('Programa de Becas', 'Fondo para becas escolares y materiales didácticos.', 20000.00, 5600.75, '2025-01-20', '2026-01-20', 'Manos Solidarias', 'Varias escuelas municipales', 20, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ0lCeNJQ5XkmbVzj0HZdBJyYG5kNvydtaiPA&s');

