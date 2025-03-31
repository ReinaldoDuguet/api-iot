package com.grupouno.iot.minero.tcp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class TcpSensorListener implements Runnable {

    private final Socket clientSocket;
    private final SensorDataTCPProcessor processor;

    public TcpSensorListener(Socket clientSocket, SensorDataTCPProcessor processor) {
        this.clientSocket = clientSocket;
        this.processor = processor;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()))) {

            String line;
            while ((line = reader.readLine()) != null) {
                processor.processJson(line);
            }

        } catch (Exception e) {
            System.err.println("⚠️ Error handling TCP client: " + e.getMessage());
        }
    }
}
