INSERT INTO rol (nombre) VALUES ('ROLE_ADMIN');
INSERT INTO rol (nombre) VALUES ('ROLE_VOLUNTARIO');

INSERT INTO usuario (numerodocumento, nombredocumento, nombre, apellidopaterno, apellidomaterno, celular, correo, password, fecharegistro, idrol) VALUES ('87654321', 'DNI', 'Marco', 'Del Pozo', 'Rossi', '888888888', 'marco@email.com', '$2a$12$xTrWRVqMbG905Slsmo4wb.J6Tl3JYLM3Agp8wAveSXIO8omegfkTq', NOW(), 1);

INSERT INTO usuario (numerodocumento, nombredocumento, nombre, apellidopaterno, apellidomaterno, celular, correo, password, fecharegistro, idrol) VALUES ('12345678', 'DNI', 'Juan', 'Perez', 'Lopez', '999999999', 'juan@email.com', '$2a$12$fwy1GDh.YYwYXnZXa4gIfuQIQuwkuY8u7e5isB/1HsykxnNaxyKa2', NOW(), 2);
