/**
Singleton:

Clase Administrador: Esta clase utiliza el patrón Singleton para asegurar que solo exista una instancia de administrador en la aplicación. Esto es crítico para gestionar de manera centralizada todas las operaciones administrativas sin duplicar instancias.

Proxy:

Clase LoginProxy: Utiliza el patrón Proxy para controlar el acceso al servicio de autenticación real (RealLogginService). Este patrón permite añadir lógica adicional como controles de acceso o registro de intentos de inicio de sesión sin modificar la clase original.

Factory Method:

Método de creación de cupones: El método crearCupon en la clase Unieventos utiliza el patrón Factory Method para generar cupones con un código y porcentaje de descuento predefinidos. Esto simplifica la creación y asegura la consistencia de los objetos creados.

Observer:

Notificación por correo: Aunque no implementado explícitamente como un patrón Observer, el mecanismo de enviar correos electrónicos para notificaciones de verificación y descuentos actúa como un patrón Observer donde Unieventos actúa como el sujeto que notifica a los observadores (clientes) sobre eventos específicos (registro, primera compra).

MVC (Model-View-Controller):

Estructura de la aplicación: La separación entre las clases de interfaz de usuario (como RegisterPane, LoginPane, PostLoginPage, AdminMainPane) y la lógica de negocio (clases como Unieventos, Cliente, Evento) sigue el patrón MVC. Esto organiza la aplicación en modelos, vistas y controladores, facilitando la gestión y mantenimiento del código.
 */