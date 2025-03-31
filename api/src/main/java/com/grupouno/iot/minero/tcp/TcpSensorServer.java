package com.grupouno.iot.minero.tcp;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * TCP Server component that listens for incoming sensor data via TCP socket.
 * Only enabled when `tcp.listener.enabled=true` is set in application.properties.
 */
@Component
@ConditionalOnProperty(name = "tcp.listener.enabled", havingValue = "true")
public class TcpSensorServer {

    @Value("${tcp.listener.port:6000}")
    private int tcpPort;

    private final SensorDataTCPProcessor processor;

    public TcpSensorServer(SensorDataTCPProcessor processor) {
        this.processor = processor;
    }

    @PostConstruct
    public void startTcpListener() {
        Thread listenerThread = new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(tcpPort)) {
                System.out.println("✅ TCP Listener running on port " + tcpPort);
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    // Process each incoming connection in a separate thread
                    new Thread(new TcpSensorListener(clientSocket, processor)).start();
                }
            } catch (Exception e) {
                System.err.println("❌ Error starting TCP server on port " + tcpPort + ": " + e.getMessage());
                e.printStackTrace();
            }
        });

        listenerThread.setName("TcpSensorListenerThread");
        listenerThread.setDaemon(true); // Let app exit cleanly if only daemon threads remain
        listenerThread.start();
    }
}
