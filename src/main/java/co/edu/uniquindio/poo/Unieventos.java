package co.edu.uniquindio.poo;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import javax.mail.MessagingException;
import lombok.Getter;
import lombok.Setter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

@Getter
@Setter
public class Unieventos implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Cliente> clientes = new ArrayList<>();
    private List<Evento> eventos = new ArrayList<>();
    private List<String> codigosDescuento = new ArrayList<>(); // Lista de códigos de descuento
    private Usuario usuarioActual;
    private Administrador administrador;
    private transient EmailService emailService;

    /**
     * Constructor que inicializa el administrador y el servicio de correo electrónico.
     */
    public Unieventos() {
        administrador = Administrador.getInstance();
        emailService = new EmailService("uqunieventos@gmail.com", "ynsf wlno spjm vxad");
        cargarDatos();
    }

    /**
     * Registra un nuevo cliente y envía un código de verificación y un código de descuento por correo electrónico.
     *
     * @param identificacion Identificación del cliente.
     * @param nombre         Nombre del cliente.
     * @param telefono       Teléfono del cliente.
     * @param email          Email del cliente.
     * @param contrasena     Contraseña del cliente.
     */
    public void registrarCliente(String identificacion, String nombre, String telefono, String email, String contrasena) {
        if (buscarClientePorIdentificacion(identificacion) != null) {
            System.out.println("El cliente con identificación " + identificacion + " ya está registrado.");
            return;
        }
        Cliente cliente = new Cliente(identificacion, nombre, telefono, email, contrasena);
        clientes.add(cliente);
        System.out.println("Cliente registrado: " + nombre);

        // Enviar correo de verificación y código de descuento
        enviarCodigoVerificacion(email, cliente.getCodigoVerificacion());
        String codigoDescuento = CodeGenerator.generateCode();
        cliente.agregarCodigoDescuento(codigoDescuento);
        enviarCodigoDescuento(email, codigoDescuento);
    }

    /**
     * Envía un correo electrónico con el código de verificación.
     *
     * @param email  Email del destinatario.
     * @param codigo Código de verificación.
     */
    public void enviarCodigoVerificacion(String email, String codigo) {
        try {
            emailService.sendEmail(email, "Código de Verificación", "Su código de verificación es: " + codigo);
            System.out.println("Correo de verificación enviado a " + email);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Envía un correo electrónico con el código de descuento.
     *
     * @param email  Email del destinatario.
     * @param codigo Código de descuento.
     */
    public void enviarCodigoDescuento(String email, String codigo) {
        try {
            emailService.sendEmail(email, "Código de Descuento", "Su código de descuento es: " + codigo);
            System.out.println("Correo de descuento enviado a " + email);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Agrega un nuevo evento al sistema.
     *
     * @param evento Evento a agregar.
     */
    public void agregarEvento(Evento evento) {
        if (buscarEventoPorNombreCiudadFecha(evento.getNombre(), evento.getCiudad(), evento.getFecha()) != null) {
            System.out.println("El evento " + evento.getNombre() + " en " + evento.getCiudad() + " en la fecha " + evento.getFecha() + " ya está registrado.");
            return;
        }
        eventos.add(evento);
        System.out.println("Evento agregado: " + evento.getNombre() + " en " + evento.getCiudad());
    }

    /**
     * Lista los eventos en una ciudad específica.
     *
     * @param ciudad Ciudad donde buscar eventos.
     */
    public void listarEventosPorCiudad(String ciudad) {
        System.out.println("Eventos en " + ciudad + ":");
        for (Evento evento : eventos) {
            if (evento.getCiudad().equals(ciudad)) {
                System.out.println(evento.getNombre() + " - " + evento.getFecha());
            }
        }
    }

    /**
     * Crea una nueva cuenta de cliente.
     *
     * @param identificacion Identificación del cliente.
     * @param nombre         Nombre del cliente.
     * @param telefono       Teléfono del cliente.
     * @param email          Email del cliente.
     * @param contrasena     Contraseña del cliente.
     * @return true si la cuenta fue creada, false si la identificación ya está registrada.
     */
    public boolean crearCuenta(String identificacion, String nombre, String telefono, String email, String contrasena) {
        if (buscarClientePorIdentificacion(identificacion) != null) {
            System.out.println("El cliente con identificación " + identificacion + " ya está registrado.");
            return false;
        }
        registrarCliente(identificacion, nombre, telefono, email, contrasena);
        return true;
    }

    /**
     * Inicia sesión en el sistema.
     *
     * @param identificacion Identificación del usuario.
     * @param contrasena     Contraseña del usuario.
     * @return true si el login es exitoso, false en caso contrario.
     */
    public boolean login(String identificacion, String contrasena) {
        if (identificacion.equals(administrador.getEmail()) && contrasena.equals(administrador.getContrasena())) {
            usuarioActual = administrador;
            System.out.println("Login exitoso. Bienvenido, Administrador!");
            return true;
        }

        Cliente cliente = buscarClientePorIdentificacion(identificacion);
        if (cliente != null && cliente.getContrasena().equals(contrasena)) {
            if (!cliente.isVerificado()) {
                System.out.println("Debe verificar su cuenta ingresando el código de verificación.");
                return false;
            }
            usuarioActual = cliente;
            System.out.println("Login exitoso. Bienvenido, " + cliente.getNombre() + "!");
            return true;
        }

        System.out.println("Identificación o contraseña incorrecta.");
        return false;
    }

    /**
     * Cierra la sesión del usuario actual.
     */
    public void logout() {
        if (usuarioActual != null) {
            System.out.println("Hasta luego, " + usuarioActual.getNombre() + "!");
            usuarioActual = null;
        } else {
            System.out.println("No hay usuario logueado.");
        }
    }

    /**
     * Verifica el código de verificación de un cliente.
     *
     * @param identificacion Identificación del cliente.
     * @param codigo         Código de verificación.
     * @return true si el código es correcto, false en caso contrario.
     */
    public boolean verificarCodigo(String identificacion, String codigo) {
        Cliente cliente = buscarClientePorIdentificacion(identificacion);
        if (cliente != null && cliente.getCodigoVerificacion().equals(codigo)) {
            cliente.setVerificado(true);
            System.out.println("Cuenta verificada exitosamente.");
            return true;
        } else {
            System.out.println("Código de verificación incorrecto.");
            return false;
        }
    }

    /**
     * Busca un cliente por su identificación.
     *
     * @param identificacion Identificación del cliente.
     * @return El cliente si se encuentra, null en caso contrario.
     */
    public Cliente buscarClientePorIdentificacion(String identificacion) {
        for (Cliente cliente : clientes) {
            if (cliente.getIdentificacion().equals(identificacion)) {
                return cliente;
            }
        }
        return null;
    }

    /**
     * Busca un evento por su nombre, ciudad y fecha.
     *
     * @param nombre Nombre del evento.
     * @param ciudad Ciudad del evento.
     * @param fecha  Fecha del evento.
     * @return El evento si se encuentra, null en caso contrario.
     */
    private Evento buscarEventoPorNombreCiudadFecha(String nombre, String ciudad, LocalDate fecha) {
        for (Evento evento : eventos) {
            if (evento.getNombre().equals(nombre) && evento.getCiudad().equals(ciudad) && evento.getFecha().equals(fecha)) {
                return evento;
            }
        }
        return null;
    }

    /**
     * Inicia la aplicación con un menú interactivo.
     */
    public void iniciarAplicacion() {
        Scanner scanner = new Scanner(System.in);
        LoginService loginService = new LoginProxy(this);

        while (true) {
            System.out.println("1. Crear cuenta");
            System.out.println("2. Login");
            System.out.println("3. Logout");
            System.out.println("4. Verificar cuenta");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            if (opcion == 1) {
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
                if (crearCuenta(identificacion, nombre, telefono, email, contrasena)) {
                    System.out.println("Cuenta creada exitosamente. Verifique su correo electrónico para activar su cuenta y recibir su cupón de descuento.");
                } else {
                    System.out.println("No se pudo crear la cuenta. Identificación ya registrada.");
                }
            } else if (opcion == 2) {
                System.out.print("Identificación: ");
                String identificacion = scanner.nextLine();
                System.out.print("Contraseña: ");
                String contrasena = scanner.nextLine();
                if (loginService.login(identificacion, contrasena)) {
                    System.out.println("Acceso concedido. Puede usar el programa.");
                } else {
                    System.out.println("Identificación o contraseña incorrecta, o cuenta no verificada.");
                }
            } else if (opcion == 3) {
                logout();
            } else if (opcion == 4) {
                System.out.print("Identificación: ");
                String identificacion = scanner.nextLine();
                System.out.print("Código de verificación: ");
                String codigo = scanner.nextLine();
                if (verificarCodigo(identificacion, codigo)) {
                    System.out.println("Cuenta verificada exitosamente. Ahora puede iniciar sesión.");
                } else {
                    System.out.println("Código de verificación incorrecto.");
                }
            } else if (opcion == 5) {
                System.out.println("Saliendo del programa...");
                break;
            } else {
                System.out.println("Opción no válida.");
            }
        }

        scanner.close();
        guardarDatos(); // Guardar datos al salir
    }

    /**
     * Realiza la compra de boletos para un evento y genera una factura con un código QR.
     *
     * @param cliente         Cliente que realiza la compra.
     * @param evento          Evento para el cual se compran las boletas.
     * @param cantidadBoletas Cantidad de boletas compradas.
     */
    public void realizarCompra(Cliente cliente, Evento evento, Localidad localidad, int cantidadBoletas) {
        // Calcular el total antes de descuentos
        double totalAntesDescuentos = localidad.getPrecio() * cantidadBoletas;
        double totalCompra = totalAntesDescuentos;

        // Aplicar descuentos si los hay
        if (!cliente.getCodigosDescuento().isEmpty()) {
            totalCompra *= 0.9; // Aplicar descuento del 10%
            cliente.getCodigosDescuento().remove(0); // Suponiendo que se usa el primer código
        }

        // Generar la factura
        Factura factura = new Factura(totalCompra, totalAntesDescuentos, evento.getNombre(), cantidadBoletas);
        String facturaStr = factura.toString();

        // Generar el código QR de la factura
        String qrCodePath = "factura_" + cliente.getIdentificacion() + ".png";
        try {
            generarQRCode(facturaStr, qrCodePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Enviar la factura por correo
        try {
            emailService.sendEmailWithAttachment(cliente.getEmail(), "Factura de compra", 
                    "Gracias por tu compra. Adjunta encontrarás tu factura en formato QR.", qrCodePath);
            System.out.println("Correo con la factura enviado a " + cliente.getEmail());
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        // Actualizar entradas vendidas
        evento.venderEntradas(cantidadBoletas);

        // Verificar si es la primera compra
        if (!cliente.isHaRealizadoPrimeraCompra()) {
            System.out.println("Primera compra detectada para el cliente: " + cliente.getNombre());
            cliente.setHaRealizadoPrimeraCompra(true);
            enviarDescuentoPrimeraCompra(cliente);
        }

        // Guardar la compra y la factura
        guardarDatos();
    }

    /**
     * Envía un descuento por la primera compra del cliente.
     *
     * @param cliente Cliente que realiza su primera compra.
     */
    private void enviarDescuentoPrimeraCompra(Cliente cliente) {
        String codigoDescuento = CodeGenerator.generateCode();
        cliente.getCodigosDescuento().add(codigoDescuento);
        try {
            emailService.sendEmail(cliente.getEmail(), "Descuento por primera compra",
                    "¡Felicitaciones por tu primera compra! Aquí tienes un código de descuento del 10% para tu próxima compra: " + codigoDescuento);
            System.out.println("Correo de descuento por primera compra enviado a " + cliente.getEmail());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Genera un código QR a partir de un texto y lo guarda en un archivo.
     *
     * @param text     Texto a codificar en el QR.
     * @param filePath Ruta del archivo donde se guardará el QR.
     * @throws Exception Si ocurre un error durante la generación del QR.
     */
    public void generarQRCode(String text, String filePath) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200, hints);

        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

        System.out.println("Código QR generado: " + filePath);
    }

    /**
     * Agrega un código de descuento a la lista global de códigos.
     *
     * @param codigo Código de descuento a agregar.
     */
    public void agregarCodigoDescuentoGlobal(String codigo) {
        codigosDescuento.add(codigo);
    }

    /**
     * Valida un código de descuento y lo elimina de la lista si es válido.
     *
     * @param codigo Código de descuento a validar.
     * @return true si el código es válido, false en caso contrario.
     */
    public boolean validarCodigoDescuento(String codigo) {
        if (codigosDescuento.contains(codigo)) {
            codigosDescuento.remove(codigo); // Eliminar el código una vez utilizado
            return true;
        }
        return false;
    }

    /**
     * Lista los tres primeros eventos con más entradas vendidas.
     */
    public void listarTopEventos() {
        eventos.stream()
            .sorted(Comparator.comparingInt(Evento::getEntradasVendidas).reversed())
            .limit(3)
            .forEach(evento -> {
                System.out.println("Nombre: " + evento.getNombre());
                System.out.println("Entradas Vendidas: " + evento.getEntradasVendidas());
                System.out.println("Fecha: " + evento.getFecha());
                System.out.println("Ciudad: " + evento.getCiudad());
                System.out.println("Descripción: " + evento.getDescripcion());
                System.out.println();
            });
    }

    /**
     * Guarda los datos de la aplicación en un archivo.
     */
    public void guardarDatos() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("unieventos.dat"))) {
            out.writeObject(this);
            System.out.println("Datos guardados correctamente.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga los datos de la aplicación desde un archivo.
     */
    public void cargarDatos() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("unieventos.dat"))) {
            Unieventos uniEventos = (Unieventos) in.readObject();
            this.clientes = uniEventos.getClientes();
            this.eventos = uniEventos.getEventos();
            this.codigosDescuento = uniEventos.getCodigosDescuento(); // Cargar los códigos de descuento
            this.usuarioActual = null; // Asegurar que no se mantiene la sesión de usuario previa
            System.out.println("Datos cargados correctamente.");
        } catch (FileNotFoundException e) {
            System.out.println("No se encontró el archivo de datos. Se creará uno nuevo al guardar.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
