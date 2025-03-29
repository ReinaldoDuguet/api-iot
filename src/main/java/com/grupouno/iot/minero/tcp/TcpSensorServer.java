package com.grupouno.iot.minero.tcp;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.ServerSocket;
import java.net.Socket;

@Component
public class TcpSensorServer {

    @Value("${tcp.listener.enabled:true}")
    private boolean listenerEnabled;

    @Value("${tcp.listener.port:6000}")
    private int tcpPort;

    private final SensorDataTCPProcessor processor;

    public TcpSensorServer(SensorDataTCPProcessor processor) {
        this.processor = processor;
    }

    @PostConstruct
    public void startTcpListener() {
        if (!listenerEnabled) return;

        Thread listenerThread = new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(tcpPort)) {
                System.out.println("✅ TCP Listener running on port " + tcpPort);
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    new Thread(new TcpSensorListener(clientSocket, processor)).start();
                }
            } catch (Exception e) {
                System.err.println("❌ Error starting TCP server: " + e.getMessage());
            }
        });
        listenerThread.setDaemon(true);
        listenerThread.start();
    }
}
