package com.br.phdev.conexao;

import com.br.phdev.Controlador;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor extends Thread{
    
    private final int PORTA = 12345;
    
    private static ServerSocket server;
    private InputStream in;
    private InputStreamReader inr;
    private BufferedReader bfr;
    private Controlador controlador;
    
    public Servidor(Controlador controlador){        
        this.controlador = controlador;
        
        Socket con = iniciar();
        
        try{
            in = con.getInputStream();
            inr = new InputStreamReader(in);
            bfr = new BufferedReader(inr);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    
    private Socket iniciar(){
        
        try{
            server = new ServerSocket(PORTA);
            while(true){
                Socket con = server.accept();
                System.out.println("Conectado");
                return con;
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return iniciar();
    }
    
    @Override
    public void run(){
        String msg = "";
        try{
            while(!(msg.equals("sair"))){
                msg = bfr.readLine();
                System.out.println("msg: " + msg);
                //if ((int)msg == 65535)
                   // break;
                controlador.receberMensagem(msg);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        finally{
            try{
                System.out.println("Fechando servidor");
                controlador.parar();
                server.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    
}
