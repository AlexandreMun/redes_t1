import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Client {
    int aquecedor(int temp,int tempMax)  {
        do {
            temp= temp + 1;
            System.out.println("Temperatura: " + temp); 
        } while (temp<tempMax);
        return (temp);
    }
    int resfriador(int temp, int tempMin)  {
        do {
           temp = temp - 1;
        System.out.println("Temperatura: " + temp);       
        }while (temp>tempMin);
        return (temp);
    }
    int co2(int co2Atual, int co2Max)  {
        do {
            co2Atual = co2Atual + 1;
            System.out.println("CO2: " + co2Atual);        
         }while (co2Atual<co2Max);
         return (co2Atual);
    }
    int menosCo2(int co2Atual){
        for(int i=0;i<2;i++) {
            Random rand = new Random();
            int aleatorio = rand.nextInt(10);
            co2Atual = co2Atual - aleatorio;
            System.out.println("CO2: " + co2Atual);    
        }
        return (co2Atual);
    }
    int irrigacao(int irriAtual, int irriMax)  {
        do {
            irriAtual = irriAtual + 1;
            System.out.println("IRRIGACAO: " + irriAtual);          
         }while (irriAtual<irriMax);
         return (irriAtual);
    }
    int secar(int irriAtual){
        for(int i=0;i<2;i++) {
            Random rand = new Random();
            int aleatorio = rand.nextInt(10);
            irriAtual = irriAtual - aleatorio;
            System.out.println("IRRIGACAO: " + irriAtual); 
        }
        return (irriAtual);
    }
    

    
    /*
    * Sensores:
    * o Temperatura interna;
    * o Umidade do solo;
    * o NÃ­vel de CO2
    */
    public static void main(String[] args) throws UnknownHostException, IOException, EOFException {
        Scanner scanner = new Scanner(System.in);

        //Sensor temperatura
        System.out.println("Digite a temperatura maxima da estufa");
        int tempMax = scanner.nextInt();
        System.out.println("Digite a temperatura minima da estufa");
        int tempMin = scanner.nextInt();
        int tempAtual = 10;

        //irrigacao
        System.out.println("Digite a irrigacao maxima da estufa");
        int irriMax = scanner.nextInt();
        int irriAtual = 10;

        //Oxigenação
        System.out.println("Digite a oxigenacao maxima da estufa");
        int co2Max = scanner.nextInt();
        int co2Atual = 20;

        Client client = new Client();
        Timer timer = new Timer();
        TimerTask Temperatura = new TimerTask() {
            @Override
            public void run() { 
                    try {
                        
                        Socket socket;
                        socket = new Socket("localhost", 3000);
                        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                        // Temperatura
                        if (tempAtual<tempMax) {
                            output.writeInt(client.aquecedor(tempAtual,tempMax));
                        }
                        else{
                            output.writeInt(client.resfriador(tempAtual,tempMin));
                        }

                        //irrigacao
                        if (irriAtual < irriMax) {
                            output.writeInt(client.irrigacao(irriAtual,irriMax));
                        }
                        else {
                           output.writeInt(client.secar(irriAtual));
                        }

                        //CO2
                        if (co2Atual < co2Max) {
                            output.writeInt(client.co2(co2Atual,co2Max));
                        }
                        else {
                           output.writeInt(client.menosCo2(co2Atual));
                        }
                        output.flush();
                        socket.close();
                    } catch (Exception e) {
                        //TODO: handle exception
                    }
                }
        };
        timer.scheduleAtFixedRate(Temperatura, 0, 1000);
        
    }
}