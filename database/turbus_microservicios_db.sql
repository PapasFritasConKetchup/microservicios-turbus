USE turbus_microservicios_db;

INSERT IGNORE INTO pasajeros 
(id, rut, nombre, apellido, email, telefono, fecha_nacimiento, fecha_registro, activo) 
VALUES
(1, '12345678-9', 'Juan', 'Pérez', 'juan.perez@gmail.com', '987654321', '1998-05-10', CURDATE(), TRUE),
(2, '98765432-1', 'María', 'González', 'maria.gonzalez@gmail.com', '912345678', '1995-09-22', CURDATE(), TRUE),
(3, '11111111-1', 'Carlos', 'Muñoz', 'carlos.munoz@gmail.com', '923456789', '2000-01-15', CURDATE(), TRUE),
(4, '22222222-2', 'Camila', 'Rojas', 'camila.rojas@gmail.com', '934567890', '1999-11-03', CURDATE(), TRUE),
(5, '13579246-8', 'Ignacio', 'Díaz', 'ignacio.diaz@gmail.com', '945678123', '2001-04-18', CURDATE(), TRUE);

INSERT IGNORE INTO trabajadores 
(id, rut, nombre, apellido, email, cargo, telefono, activo) 
VALUES
(1, '33333333-3', 'Pedro', 'Soto', 'pedro.soto@turbus.cl', 'CONDUCTOR', '945678901', TRUE),
(2, '44444444-4', 'Ana', 'López', 'ana.lopez@turbus.cl', 'BOLETERIA', '956789012', TRUE),
(3, '55555555-5', 'Diego', 'Castro', 'diego.castro@turbus.cl', 'OPERACIONES', '967890123', TRUE),
(4, '66666666-6', 'Felipe', 'Morales', 'felipe.morales@turbus.cl', 'CONDUCTOR', '978901234', TRUE);

INSERT IGNORE INTO rutas 
(id, ciudad_origen, ciudad_destino, distancia_km, duracion_estimada, precio_base, activa) 
VALUES
(1, 'Santiago', 'Viña del Mar', 120.50, '2 horas', 6500, TRUE),
(2, 'Santiago', 'Temuco', 675.00, '8 horas', 18500, TRUE),
(3, 'Santiago', 'Concepción', 500.00, '6 horas', 15000, TRUE),
(4, 'Santiago', 'La Serena', 470.50, '6 horas', 14000, TRUE);

INSERT IGNORE INTO buses 
(id, patente, capacidad, tipo_bus, estado) 
VALUES
(1, 'ABCD12', 10, 'CLASICO', 'DISPONIBLE'),
(2, 'EFGH34', 10, 'SEMI CAMA', 'DISPONIBLE'),
(3, 'IJKL56', 10, 'SALON CAMA', 'MANTENCION'),
(4, 'MNOP78', 10, 'SEMI CAMA', 'DISPONIBLE');

INSERT IGNORE INTO asientos 
(id, bus_id, numero_asiento, tipo_asiento, disponible) 
VALUES
(1, 1, 1, 'VENTANA', TRUE),
(2, 1, 2, 'PASILLO', TRUE),
(3, 1, 3, 'VENTANA', TRUE),
(4, 1, 4, 'PASILLO', TRUE),
(5, 1, 5, 'VENTANA', TRUE),
(6, 1, 6, 'PASILLO', TRUE),
(7, 1, 7, 'VENTANA', TRUE),
(8, 1, 8, 'PASILLO', TRUE),
(9, 1, 9, 'VENTANA', TRUE),
(10, 1, 10, 'PASILLO', TRUE),

(11, 2, 1, 'VENTANA', TRUE),
(12, 2, 2, 'PASILLO', TRUE),
(13, 2, 3, 'VENTANA', TRUE),
(14, 2, 4, 'PASILLO', TRUE),
(15, 2, 5, 'VENTANA', TRUE),
(16, 2, 6, 'PASILLO', TRUE),
(17, 2, 7, 'VENTANA', TRUE),
(18, 2, 8, 'PASILLO', TRUE),
(19, 2, 9, 'VENTANA', TRUE),
(20, 2, 10, 'PASILLO', TRUE);

INSERT IGNORE INTO descuentos 
(id, codigo, descripcion, porcentaje, activo) 
VALUES
(1, 'ESTUDIANTE10', 'Descuento para estudiantes', 10.00, TRUE),
(2, 'PROMO20', 'Promoción especial Turbus', 20.00, TRUE),
(3, 'ADULTO5', 'Descuento adulto mayor', 5.00, TRUE);

INSERT IGNORE INTO viajes 
(id, ruta_id, bus_id, trabajador_id, fecha_salida, hora_salida, precio, estado) 
VALUES
(1, 1, 1, 1, '2026-05-20', '08:30:00', 7500, 'PROGRAMADO'),
(2, 2, 2, 4, '2026-05-21', '22:00:00', 20000, 'PROGRAMADO'),
(3, 3, 1, 1, '2026-05-22', '10:00:00', 16000, 'PROGRAMADO');

INSERT IGNORE INTO reservas
(id, pasajero_id, viaje_id, asiento_id, descuento_id, fecha_reserva, estado)
VALUES
(1, 1, 1, 1, 1, NOW(), 'CONFIRMADA'),
(2, 2, 1, 2, NULL, NOW(), 'CONFIRMADA'),
(3, 3, 2, 11, 2, NOW(), 'CONFIRMADA'),
(4, 4, 3, 3, NULL, NOW(), 'PENDIENTE');

UPDATE asientos SET disponible = FALSE WHERE id IN (1, 2, 11);

