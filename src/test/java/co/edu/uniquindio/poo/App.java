package co.edu.uniquindio.poo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Unieventos uniEventos = new Unieventos();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                System.out.println("Bienvenido a UniEventos");
                System.out.println("1. Crear cuenta");
                System.out.println("2. Iniciar sesión");
                System.out.println("3. Verificar cuenta");
                System.out.println("4. Salir");
                System.out.print("Seleccione una opción: ");
                int opcion = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea

                if (opcion == 1) {
                    System.out.println("Ingrese los siguientes datos para crear su cuenta:");
                    System.out.print("Identificación: ");
                    String identificacion = scanner.nextLine();
                    System.out.print("Nombre: ");
                    String nombre = scanner.nextLine();
                    System.out.print("Teléfono: ");
                    String telefono = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Contraseña: ");
                    String contrasena = scanner.nextLine();

                    uniEventos.registrarCliente(identificacion, nombre, telefono, email, contrasena);
                    System.out.println("Cuenta creada exitosamente. Verifique su correo electrónico para activar su cuenta y recibir su cupón de descuento.");
                } else if (opcion == 2) {
                    System.out.println("Ingrese sus credenciales para iniciar sesión:");
                    System.out.print("Identificación: ");
                    String identificacion = scanner.nextLine();
                    System.out.print("Contraseña: ");
                    String contrasena = scanner.nextLine();

                    if (uniEventos.login(identificacion, contrasena)) {
                        if (uniEventos.getUsuarioActual() instanceof Administrador) {
                            mostrarMenuAdministrador(uniEventos, scanner);
                        } else {
                            mostrarMenuCliente(uniEventos, scanner);
                        }
                    } else {
                        System.out.println("Identificación o contraseña incorrecta, o cuenta no verificada.");
                    }
                } else if (opcion == 3) {
                    System.out.print("Identificación: ");
                    String identificacion = scanner.nextLine();
                    System.out.print("Código de verificación: ");
                    String codigo = scanner.nextLine();
                    if (uniEventos.verificarCodigo(identificacion, codigo)) {
                        System.out.println("Cuenta verificada exitosamente. Ahora puede iniciar sesión.");
                    } else {
                        System.out.println("Código de verificación incorrecto.");
                    }
                } else if (opcion == 4) {
                    System.out.println("Saliendo del programa...");
                    break;
                } else {
                    System.out.println("Opción no válida.");
                }
            } catch (Exception e) {
                System.out.println("Dato incorrecto. Intente de nuevo.");
                scanner.nextLine(); // Consumir el salto de línea en caso de excepción
            }
        }

        scanner.close();
        uniEventos.guardarDatos(); // Guardar datos al salir
    }

    private static void mostrarMenuAdministrador(Unieventos uniEventos, Scanner scanner) {
        while (true) {
            try {
                System.out.println("Opciones de Administrador:");
                System.out.println("1. Crear evento");
                System.out.println("2. Listar eventos");
                System.out.println("3. Lista de eventos con más entradas vendidas");
                System.out.println("4. Salir");
                System.out.print("Seleccione una opción: ");
                int opcion = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea

                if (opcion == 1) {
                    System.out.println("Ingrese los siguientes datos para crear un evento:");
                    System.out.print("Nombre: ");
                    String nombre = scanner.nextLine();
                    System.out.print("Ciudad: ");
                    String ciudad = scanner.nextLine();
                    System.out.print("Descripción: ");
                    String descripcion = scanner.nextLine();
                    System.out.print("Tipo (CONCIERTO, TEATRO, DEPORTE, FESTIVAL, CULTURAL, EMPRESARIAL): ");
                    TipoEvento tipo = TipoEvento.valueOf(scanner.nextLine().toUpperCase());
                    System.out.print("Fecha (AAAA-MM-DD): ");
                    LocalDate fecha = LocalDate.parse(scanner.nextLine());
                    System.out.print("Dirección: ");
                    String direccion = scanner.nextLine();

                    List<Localidad> localidades = new ArrayList<>();
                    while (true) {
                        System.out.println("Ingrese los datos de la localidad:");
                        System.out.print("Nombre: ");
                        String nombreLocalidad = scanner.nextLine();
                        System.out.print("Capacidad: ");
                        int capacidad = scanner.nextInt();
                        System.out.print("Precio: ");
                        double precio = scanner.nextDouble();
                        scanner.nextLine(); // Consumir el salto de línea
                        localidades.add(new Localidad(nombreLocalidad, precio, capacidad));

                        System.out.print("¿Desea agregar otra localidad? (si/no): ");
                        if (scanner.nextLine().equalsIgnoreCase("no")) {
                            break;
                        }
                    }

                    Evento evento = new Evento(nombre, ciudad, descripcion, tipo, fecha, direccion, localidades);
                    uniEventos.agregarEvento(evento);
                } else if (opcion == 2) {
                    listarEventos(uniEventos);
                } else if (opcion == 3) {
                    System.out.println("Eventos con más entradas vendidas:");
                    uniEventos.listarTopEventos();
                } else if (opcion == 4) {
                    System.out.println("Saliendo del menú del administrador...");
                    break;
                } else {
                    System.out.println("Opción no válida.");
                }
            } catch (Exception e) {
                System.out.println("Dato incorrecto. Intente de nuevo.");
                scanner.nextLine(); // Consumir el salto de línea en caso de excepción
            }
        }
    }

    private static void mostrarMenuCliente(Unieventos uniEventos, Scanner scanner) {
        while (true) {
            try {
                System.out.println("Opciones:");
                System.out.println("1. Buscar evento por nombre");
                System.out.println("2. Buscar evento por tipo");
                System.out.println("3. Buscar evento por ciudad");
                System.out.println("4. Salir");
                System.out.print("Seleccione una opción: ");
                int opcionCliente = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea

                if (opcionCliente == 1) {
                    System.out.print("Ingrese el nombre del evento: ");
                    String nombreEvento = scanner.nextLine();
                    List<Evento> eventosEncontrados = buscarEventoPorNombre(uniEventos, nombreEvento);
                    if (!eventosEncontrados.isEmpty()) {
                        seleccionarEventoYComprar(uniEventos, eventosEncontrados, scanner);
                    }
                } else if (opcionCliente == 2) {
                    System.out.print("Ingrese el tipo de evento (CONCIERTO, TEATRO, DEPORTE, FESTIVAL, CULTURAL, EMPRESARIAL): ");
                    String tipoEvento = scanner.nextLine().toUpperCase();
                    List<Evento> eventosEncontrados = buscarEventoPorTipo(uniEventos, TipoEvento.valueOf(tipoEvento));
                    if (!eventosEncontrados.isEmpty()) {
                        seleccionarEventoYComprar(uniEventos, eventosEncontrados, scanner);
                    }
                } else if (opcionCliente == 3) {
                    System.out.print("Ingrese la ciudad: ");
                    String ciudad = scanner.nextLine();
                    List<Evento> eventosEncontrados = buscarEventoPorCiudad(uniEventos, ciudad);
                    if (!eventosEncontrados.isEmpty()) {
                        seleccionarEventoYComprar(uniEventos, eventosEncontrados, scanner);
                    }
                } else if (opcionCliente == 4) {
                    System.out.println("Saliendo del menú del cliente...");
                    break;
                } else {
                    System.out.println("Opción no válida.");
                }
            } catch (Exception e) {
                System.out.println("Dato incorrecto. Intente de nuevo.");
                scanner.nextLine(); // Consumir el salto de línea en caso de excepción
            }
        }
    }

    private static List<Evento> buscarEventoPorNombre(Unieventos uniEventos, String nombre) {
        List<Evento> eventosEncontrados = new ArrayList<>();
        for (Evento evento : uniEventos.getEventos()) {
            if (evento.getNombre().equalsIgnoreCase(nombre)) {
                imprimirDetallesEvento(evento);
                eventosEncontrados.add(evento);
            }
        }
        if (eventosEncontrados.isEmpty()) {
            System.out.println("No se encontró ningún evento con el nombre: " + nombre);
        }
        return eventosEncontrados;
    }

    private static List<Evento> buscarEventoPorTipo(Unieventos uniEventos, TipoEvento tipo) {
        List<Evento> eventosEncontrados = new ArrayList<>();
        for (Evento evento : uniEventos.getEventos()) {
            if (evento.getTipo() == tipo) {
                imprimirDetallesEvento(evento);
                eventosEncontrados.add(evento);
            }
        }
        if (eventosEncontrados.isEmpty()) {
            System.out.println("No se encontró ningún evento del tipo: " + tipo);
        }
        return eventosEncontrados;
    }

    private static List<Evento> buscarEventoPorCiudad(Unieventos uniEventos, String ciudad) {
        List<Evento> eventosEncontrados = new ArrayList<>();
        for (Evento evento : uniEventos.getEventos()) {
            if (evento.getCiudad().equalsIgnoreCase(ciudad)) {
                imprimirDetallesEvento(evento);
                eventosEncontrados.add(evento);
            }
        }
        if (eventosEncontrados.isEmpty()) {
            System.out.println("No se encontró ningún evento en la ciudad: " + ciudad);
        }
        return eventosEncontrados;
    }

    private static void seleccionarEventoYComprar(Unieventos uniEventos, List<Evento> eventosEncontrados, Scanner scanner) {
        try {
            System.out.println("Seleccione el número del evento para el que desea comprar entradas:");
            for (int i = 0; i < eventosEncontrados.size(); i++) {
                System.out.println((i + 1) + ". " + eventosEncontrados.get(i).getNombre());
            }
            int seleccion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            if (seleccion > 0 && seleccion <= eventosEncontrados.size()) {
                Evento eventoSeleccionado = eventosEncontrados.get(seleccion - 1);
                seleccionarLocalidadYComprar(uniEventos, eventoSeleccionado, scanner);
            } else {
                System.out.println("Selección no válida.");
            }
        } catch (Exception e) {
            System.out.println("Dato incorrecto. Intente de nuevo.");
            scanner.nextLine(); // Consumir el salto de línea en caso de excepción
        }
    }

    private static void seleccionarLocalidadYComprar(Unieventos uniEventos, Evento evento, Scanner scanner) {
        try {
            System.out.println("Seleccione la localidad para el evento " + evento.getNombre() + ":");
            List<Localidad> localidades = evento.getLocalidades();
            for (int i = 0; i < localidades.size(); i++) {
                System.out.println((i + 1) + ". " + localidades.get(i).getNombre() + " - Precio: " + localidades.get(i).getPrecio() + " - Capacidad: " + localidades.get(i).getCapacidadMaxima());
            }
            int seleccionLocalidad = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            if (seleccionLocalidad > 0 && seleccionLocalidad <= localidades.size()) {
                Localidad localidadSeleccionada = localidades.get(seleccionLocalidad - 1);
                menuCompraEntradas(uniEventos, evento, localidadSeleccionada, scanner);
            } else {
                System.out.println("Selección no válida.");
            }
        } catch (Exception e) {
            System.out.println("Dato incorrecto. Intente de nuevo.");
            scanner.nextLine(); // Consumir el salto de línea en caso de excepción
        }
    }

    private static void imprimirDetallesEvento(Evento evento) {
        System.out.println("Nombre: " + evento.getNombre());
        System.out.println("Ciudad: " + evento.getCiudad());
        System.out.println("Descripción: " + evento.getDescripcion());
        System.out.println("Tipo: " + evento.getTipo());
        System.out.println("Fecha: " + evento.getFecha());
        System.out.println("Dirección: " + evento.getDireccion());
        System.out.println("Localidades: ");
        for (Localidad localidad : evento.getLocalidades()) {
            System.out.println("  Nombre: " + localidad.getNombre());
            System.out.println("  Capacidad: " + localidad.getCapacidadMaxima());
            System.out.println("  Precio: " + localidad.getPrecio());
        }
        System.out.println();
    }

    private static void menuCompraEntradas(Unieventos uniEventos, Evento evento, Localidad localidad, Scanner scanner) {
        Cliente clienteActual = (Cliente) uniEventos.getUsuarioActual();
        System.out.println("¿Desea comprar entradas para la localidad " + localidad.getNombre() + " del evento " + evento.getNombre() + "? (si/no): ");
        if (scanner.nextLine().equalsIgnoreCase("si")) {
            try {
                System.out.println("Localidad: " + localidad.getNombre());
                System.out.println("Capacidad disponible: " + localidad.getCapacidadMaxima());
                System.out.println("Precio: " + localidad.getPrecio());
                System.out.print("¿Cuántas entradas desea comprar? ");
                int cantidad = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea

                if (cantidad <= localidad.getCapacidadMaxima()) {
                    double total = cantidad * localidad.getPrecio();
                    System.out.println("Total antes de aplicar descuentos: " + total);

                    // Aplicar códigos de descuento
                    while (true) {
                        System.out.print("¿Desea aplicar un código de descuento? (si/no): ");
                        if (scanner.nextLine().equalsIgnoreCase("si")) {
                            System.out.print("Ingrese el código de descuento: ");
                            String codigoDescuento = scanner.nextLine();
                            if (clienteActual.validarCodigoDescuento(codigoDescuento)) {
                                total *= 0.9;
                                clienteActual.eliminarCodigoDescuento(codigoDescuento);
                                System.out.println("Código de descuento aplicado. Total actualizado: " + total);
                            } else {
                                System.out.println("Código de descuento no válido.");
                            }
                        } else {
                            break;
                        }
                    }

                    System.out.println("Total a pagar: " + total);
                    System.out.print("¿Desea realizar la compra? (si/no): ");
                    if (scanner.nextLine().equalsIgnoreCase("si")) {
                        localidad.setCapacidadMaxima(localidad.getCapacidadMaxima() - cantidad);
                        System.out.println("Compra realizada para el evento: " + evento.getNombre() + " en la localidad: " + localidad.getNombre());
                        uniEventos.realizarCompra(clienteActual, evento, localidad, cantidad); // Realizar la compra y verificar primera compra
                        uniEventos.guardarDatos(); // Guardar datos de la compra
                    } else {
                        System.out.println("Compra cancelada.");
                    }
                } else {
                    System.out.println("No hay suficientes entradas disponibles en esta localidad.");
                }
            } catch (Exception e) {
                System.out.println("Dato incorrecto. Intente de nuevo.");
                scanner.nextLine(); // Consumir el salto de línea en caso de excepción
            }
        }
    }

    private static void listarEventos(Unieventos uniEventos) {
        System.out.println("Eventos guardados:");
        for (Evento evento : uniEventos.getEventos()) {
            imprimirDetallesEvento(evento);
        }
    }
}
