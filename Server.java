import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.Timer;

import javax.swing.*;

public class Server {
    static int a = 0;
    static int b = 0;
    static int c = 0;
    private ServerSocket socket;
    private ServerSocket serverSocket;

    // instanciar o objeto Servidor
    private void criarServidor(int ip) {
        try {
            serverSocket = new ServerSocket(ip);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    // instanciar o objeto Socket
    // Aguarda a conexão
    private Socket esperaConexao() throws IOException {
        Socket socket = serverSocket.accept();
        return socket;
    }

    private void fechaSocket(Socket socket) throws IOException {
        socket.close();
    }

    private void trataConexao(Socket socket) throws IOException {
        try {
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            //temperatura
            int SensorTemperatura = input.readInt();
            a = SensorTemperatura;
            System.out.println(SensorTemperatura);
            //Irrigação
            int sensorIrrigacao = input.readInt();
            b = sensorIrrigacao;
            //Oxigenação
            int sensorco2 = input.readInt();
            c = sensorco2;
            input.close();
            output.close();

        } catch (Exception IOException) {
            // TODO: handle exception
        } finally {
            fechaSocket(socket);
        }
    }

    public static void main(String[] args) throws IOException {
        JFrame tela = new JFrame();
        tela.setTitle("SERVIDOR");
        tela.setSize(500, 500);
        tela.setLocation(500, 300);
        tela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel painel = new JPanel();
        JLabel painelLabel = new JLabel("Sensor de temperatura: ");
        JLabel teste = new JLabel(" ");
        JLabel teste2 = new JLabel(" ");
        JLabel teste3 = new JLabel(" ");
        JLabel painelLabel2 = new JLabel("Sensor de umidade: ");
        JLabel painelLabel3 = new JLabel("Sensor de CO2: ");
        JButton temperaturaInterna = new JButton("Temperatura Interna");
        JButton umidadeSolo = new JButton("Umidade Solo");
        JButton CO2 = new JButton("NIVEL CO2");
        // VISIBLE
        tela.setVisible(true);
        tela.add(painel);
        painel.add(painelLabel);
        painel.add(teste);
        painel.add(painelLabel2);
        painel.add(teste2);
        painel.add(painelLabel3);
        painel.add(teste3);
        Timer timer = new Timer();
        TimerTask Temperatura = new TimerTask() {
            @Override
            public void run() {
                String text = String.format(" %d", a);
                teste.setText(text);
            }
        };
        timer.scheduleAtFixedRate(Temperatura, 0, 1000);

        Timer timer2 = new Timer();
        TimerTask Irrrigacao = new TimerTask() {
            @Override
            public void run() {
                String text = String.format(" %d", b);
                teste2.setText(text);
            }
        };
        timer.scheduleAtFixedRate(Irrrigacao, 0, 1000);

        Timer timer3 = new Timer();
        TimerTask co2 = new TimerTask() {
            @Override
            public void run() {
                String text = String.format(" %d", c);
                teste3.setText(text);
            }
        };
        timer.scheduleAtFixedRate(co2, 0, 1000);

        System.out.println("Servidor inicializado...");
        System.out.println("Aguardando Conexão...");
        Server server = new Server();
        server.criarServidor(3000);
        while (true) {
            Socket socket = server.esperaConexao();
            System.out.println("Cliente conectado...");
            server.trataConexao(socket);
        }
    }
}