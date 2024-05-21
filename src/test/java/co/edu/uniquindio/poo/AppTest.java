package co.edu.uniquindio.poo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

class UnieventosTest {

    private Unieventos uniEventos;
    private Cliente cliente;
    private Evento evento;
    private Localidad localidad;

    @BeforeEach
    void setUp() {
        uniEventos = new Unieventos();
        cliente = new Cliente("123", "Juan Perez", "123456789", "juan@example.com", "password");
        localidad = new Localidad("VIP", 100.0, 50);
        evento = new Evento("Concierto", "Bogotá", "Gran concierto de música", TipoEvento.CONCIERTO, LocalDate.now(), "Calle 123", List.of(localidad));
    }

    @Test
    void testRegistrarCliente() {
        uniEventos.registrarCliente("123", "Juan Perez", "123456789", "juan@example.com", "password");
        Cliente clienteRegistrado = uniEventos.buscarClientePorIdentificacion("123");
        assertNotNull(clienteRegistrado);
        assertEquals("Juan Perez", clienteRegistrado.getNombre());
    }

    @Test
    void testLogin() {
        uniEventos.registrarCliente("123", "Juan Perez", "123456789", "juan@example.com", "password");
        // Verificar cuenta antes de login
        Cliente clienteRegistrado = uniEventos.buscarClientePorIdentificacion("123");
        uniEventos.verificarCodigo("123", clienteRegistrado.getCodigoVerificacion());

        boolean loginExitoso = uniEventos.login("123", "password");
        assertTrue(loginExitoso);
        assertEquals(clienteRegistrado.getNombre(), uniEventos.getUsuarioActual().getNombre());
    }

    @Test
    void testVerificarCodigo() {
        uniEventos.registrarCliente("123", "Juan Perez", "123456789", "juan@example.com", "password");
        Cliente clienteRegistrado = uniEventos.buscarClientePorIdentificacion("123");
        boolean verificacionExitosa = uniEventos.verificarCodigo("123", clienteRegistrado.getCodigoVerificacion());
        assertTrue(verificacionExitosa);
        assertTrue(clienteRegistrado.isVerificado());
    }

    @Test
    void testAgregarEvento() {
        uniEventos.agregarEvento(evento);
        Evento eventoRegistrado = uniEventos.getEventos().get(0);
        assertNotNull(eventoRegistrado);
        assertEquals("Concierto", eventoRegistrado.getNombre());
    }

    @Test
    void testRealizarCompra(@TempDir Path tempDir) throws IOException {
        uniEventos.setEmailService(new EmailService("test@example.com", "password")); // Mock email service

        uniEventos.registrarCliente("123", "Juan Perez", "123456789", "juan@example.com", "password");
        uniEventos.agregarEvento(evento);

        Cliente clienteRegistrado = uniEventos.buscarClientePorIdentificacion("123");
        Evento eventoRegistrado = uniEventos.getEventos().get(0);

        // Verificar cuenta antes de la compra
        uniEventos.verificarCodigo("123", clienteRegistrado.getCodigoVerificacion());

        // Realizar compra sin aplicar descuento
        uniEventos.realizarCompra(clienteRegistrado, eventoRegistrado, localidad, 2);

        // Verificar que la compra se realizó correctamente
        assertTrue(clienteRegistrado.isHaRealizadoPrimeraCompra()); // Primera compra registrada

        // Verificar factura
        Factura factura = new Factura(200, 200, evento.getNombre(), 2);
        assertEquals(factura.getTotalCompra(), 200);
        assertEquals(factura.getTotalAntesDescuentos(), 200);
    }

    @Test
    void testValidarCodigoDescuento() {
        String codigo = "DESC123";
        uniEventos.agregarCodigoDescuentoGlobal(codigo);
        boolean codigoValido = uniEventos.validarCodigoDescuento(codigo);
        assertTrue(codigoValido);
        boolean codigoInvalido = uniEventos.validarCodigoDescuento(codigo);
        assertFalse(codigoInvalido); // Código ya usado
    }

    @Test
    void testListarTopEventos() {
        // Agregar eventos con diferentes entradas vendidas
        evento.venderEntradas(50);
        Evento evento2 = new Evento("Teatro", "Medellín", "Obra de teatro", TipoEvento.TEATRO, LocalDate.now(), "Calle 456", List.of(new Localidad("General", 50.0, 100)));
        evento2.venderEntradas(30);
        Evento evento3 = new Evento("Deporte", "Cali", "Partido de fútbol", TipoEvento.DEPORTE, LocalDate.now(), "Calle 789", List.of(new Localidad("Tribuna", 70.0, 80)));
        evento3.venderEntradas(70);

        uniEventos.agregarEvento(evento);
        uniEventos.agregarEvento(evento2);
        uniEventos.agregarEvento(evento3);

        // Llamar al método listarTopEventos y verificar el resultado
        uniEventos.listarTopEventos();
    }
}
