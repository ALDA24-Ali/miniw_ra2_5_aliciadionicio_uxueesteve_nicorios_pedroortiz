# Fase 2 - RA3: Sockets (UDP y Multicast)
## Diagrama UML - Sesión 13 de enero

### Arquitectura General

```
┌─────────────────────────────────────────────────────┐
│                  RED DE COMUNICACION                 │
│                 (UDP / Multicast)                    │
└──────────────┬──────────────────────────────┬────────┘
               │                              │
        ┌──────▼──────┐              ┌───────▼──────┐
        │   FITA 3    │              │   FITA 4     │
        │  UDP punto  │              │   MULTICAST  │
        │  a punto    │              │ (un servidor,│
        │             │              │  múltiples   │
        │             │              │   clientes)  │
        └──────┬──────┘              └───────┬──────┘
               │                             │
        ┌──────┴──────┐              ┌──────┴─────────┐
        │             │              │                │
   ┌────▼─────┐  ┌───▼──────┐   ┌───▼────────┐  ┌──▼────────┐
   │ClientUDP │  │ServidorUDP   │ServidorMulti   ClientMulti
   │          │  │          │   │cast         │  │cast (N)    │
   │Puerto: X │  │Puerto: Y │   │Puerto: 7000 │  │Puerto: 7000│
   │IP: local │  │IP: local │   │IP: 230.x.x │  │IP: 230.x.x │
   └──────────┘  └──────────┘   │Grupo:privado   └────────────┘
                                 └─────────────┘
```

### FITA 3: UDP (Cliente-Servidor bidireccional)

#### Clases principales:

```
┌─────────────────────────────┐
│    ClientUDP                │
├─────────────────────────────┤
│ - puerto: int               │
│ - ipServidor: String        │
│ - socket: DatagramSocket    │
├─────────────────────────────┤
│ + main(args: String[])      │
│ + enviarMensaje(String)     │
│ + recibirMensaje(): String  │
└─────────────────────────────┘
         │
         │ conecta con
         │
         ▼
┌──────────────────────────────┐
│    ServidorUDP               │
├──────────────────────────────┤
│ - puerto: int                │
│ - socket: DatagramSocket     │
├──────────────────────────────┤
│ + main(args: String[])       │
│ + recibirMensaje()           │
│ + enviarMensaje(respuesta)   │
└──────────────────────────────┘
```

**Flujo UDP:**
```
ClientUDP                          ServidorUDP
   │                                  │
   │──────── DatagramPacket ─────────▶│
   │                                  │
   │                         [Procesa]│
   │                                  │
   │◀────── DatagramPacket ───────────│
   │                                  │
```

---

### FITA 4: MULTICAST (Servidor a múltiples clientes)

#### Clases principales:

```
┌──────────────────────────────┐
│    ServidorMulticast         │
├──────────────────────────────┤
│ - grupo: String = "230.0.0.1"│
│ - puerto: int = 7000         │
│ - socket: MulticastSocket    │
├──────────────────────────────┤
│ + main(args: String[])       │
│ + enviarMensajeGrupo()       │
│ + run() [bucle infinito]     │
└──────────────────────────────┘
         │
         │ envía a (230.0.0.1:7000)
         │
    ┌────┴─────────────────────┐
    │                           │
    ▼                           ▼
┌──────────────────┐    ┌──────────────────┐
│ ClientMulticast1 │    │ ClientMulticast2 │
├──────────────────┤    ├──────────────────┤
│ - grupo: String  │    │ - grupo: String  │
│ - socket: Multi  │    │ - socket: Multi  │
├──────────────────┤    ├──────────────────┤
│ + joinGroup()    │    │ + joinGroup()    │
│ + receive()      │    │ + receive()      │
└──────────────────┘    └──────────────────┘
       ▲                       ▲
       │                       │
       └───── ... N clientes──┘
```

**Flujo Multicast:**
```
ServidorMulticast (Envía cada 3 segundos)
   │
   ├──┬──────────────────────────────┬─────┐
   │  │                              │     │
   ▼  ▼                              ▼     ▼
Client1  Client2                  Client3  Client...N
  ✓        ✓                         ✓      ✓
```
---
## Características de implementación:

### FITA 3 - UDP
-  Protocolo sin conexión
-  Datagramas independientes
-  Comunicación bidireccional
-  Puerto dinámico (cliente) / Fijo (servidor)
-  IP: localhost (127.0.0.1)

### FITA 4 - MULTICAST
-  Rango multicast: 224.0.0.0 - 239.255.255.255
-  Grupo: 230.0.0.1 (privado)
-  Puerto: 7000
-  Servidor envía continuamente
-  Múltiples clientes pueden recibir simultáneamente
-  Los clientes se unen al grupo (joinGroup)
-  Comunicación unidireccional (servidor → clientes)

---

## Pruebas recomendadas (Tester):

```bash
# Terminal 1: Iniciar servidor multicast
java -cp target/classes ra3.ProvaFase2.Fita4.ServidorMulticast

# Terminal 2, 3, 4, etc.: Iniciar múltiples clientes
java -cp target/classes ra3.ProvaFase2.Fita4.ClientMulticast
java -cp target/classes ra3.ProvaFase2.Fita4.ClientMulticast
java -cp target/classes ra3.ProvaFase2.Fita4.ClientMulticast
```

**Resultado esperado:** Todos los clientes reciben los mismos mensajes del servidor.

---

## Consideraciones de Seguridad:

1. Validación de direcciones multicast
2. Manejo de excepciones (IOException, SocketException)
3. Cierre de sockets con try-with-resources
4. Sincronización en caso de acceso concurrente
5. Considerar encriptación si es necesario (DTLS para UDP)
6. Control de firewall en puertos 7000 y rango UDP

---

**Sesión del 13 de enero de 2026**
- Backend & Security:  Implementación completada
- Tester: Pruebas con múltiples clientes
- Documentador: Diagrama UML completado
