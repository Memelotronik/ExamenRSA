/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package copiarsa;

/**
 *
 * @author Angel
 */
import java.io.BufferedReader;
import java.util.*;
import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;

public class cRSA {
    //variables para el algoritmo
    int tamPrimo;
    BigInteger p, q, n;
    BigInteger phi;
    BigInteger e, d;
    
    //1234567890
    //metodo que se encarga de generar primos
    public void generarPrimos(){
        String tamP = VentanaServidor.tam.getText();
        System.out.println(tamP);
        tamPrimo = Integer.parseInt(tamP);
        p = new BigInteger(tamPrimo, 10, new Random());
        do q = new BigInteger(tamPrimo, 10, new Random());
            while(q.compareTo(p)==0);
        VentanaServidor.campoP.setText(p.toString());
        VentanaServidor.campoQ.setText(q.toString());
    }

    //generar las claves
    public void generarClaves(BigInteger p,BigInteger q){
        //n = p*q
        n = p.multiply(q);
        VentanaServidor.campoN.setText(n.toString());
        //phi = (p-1)*(q-1)
        phi = p.subtract(BigInteger.valueOf(1));
        phi = phi.multiply(q.subtract(BigInteger.valueOf(1)));
        VentanaServidor.campoPhi.setText(phi.toString());
        //calcular el primo relativo o coprimo e y menor que n

        do e = new BigInteger(2*tamPrimo, new Random());
            //calcular el mcd e
            while ((e.compareTo(phi)!=-1)||(e.gcd(phi).compareTo(BigInteger.valueOf(1))!=0));
        //calcular d
        d = e.modInverse(phi);
        VentanaServidor.campoE.setText(e.toString());
        VentanaServidor.campoD.setText(d.toString());
    }

    //cifrar

    public BigInteger[] encriptar(String mensaje, BigInteger e, BigInteger n)throws IOException{

        //variables
        mensaje = VentanaServidor.campoMensaje.getText();
        e = new BigInteger(VentanaServidor.campoE.getText());
        n = new BigInteger(VentanaServidor.campoN.getText());
        System.out.println(mensaje);
        System.out.println(e);
        System.out.println(n);
        int i;
        byte[] temp = new byte[1];
        byte[] digitos = mensaje.getBytes();
        BigInteger[] bigdigitos = new BigInteger[digitos.length];

        //primero es recorrer el tamaño de bigdigitos para asignarlos al temp
        for(i = 0; i<bigdigitos.length; i++){
            temp[0] = digitos[i];
            bigdigitos[i] = new BigInteger(temp);
        }

        //necesito un biginteger para el cifrado
        BigInteger[] cifrado = new BigInteger[bigdigitos.length];

        for(i = 0; i<bigdigitos.length; i++){
            //aplico el modulo para el cifrado
            cifrado[i] = bigdigitos[i].modPow(e, n);
        }
        FileWriter ficheroCifrado = new FileWriter("C:/Users/Angel/Cifrado.txt");
        ficheroCifrado.write(Arrays.toString(cifrado));
        ficheroCifrado.close();
        System.out.println("Se creo el fichero correctamente :)");
        VentanaServidor.campoMsjCifrado.setText(Arrays.toString(cifrado));
        return cifrado;
        
    }

    //descifrar
    public String descifrar(String readCifrado, BigInteger d, BigInteger n) throws IOException{
        d=new BigInteger(VentanaServidor.campoD.getText());
        n=new BigInteger(VentanaServidor.campoN.getText());
        FileReader leerCifrado= new FileReader("C:/Users/Angel/cifrado.txt");
        BufferedReader b = new BufferedReader(leerCifrado);
        readCifrado = b.readLine();
        String rdCifrado = readCifrado.substring(1, readCifrado.length()-1);
        String[] rCif = new String[rdCifrado.split(", ").length];
        rCif = rdCifrado.split(", ");
        BigInteger[] datCif = new BigInteger[rdCifrado.split(", ").length];
        for (int i = 0; i < rdCifrado.split(", ").length; i++) {
            String das;
            das = rCif[i];
            BigInteger newBig=new BigInteger(das);
            datCif[i] = newBig;
        }
        BigInteger[] cifrado = new BigInteger[rdCifrado.split(", ").length];
        int i;
        for (int j = 0; j < rdCifrado.split(", ").length; j++) {
            cifrado[j] = datCif[j];
        }
        BigInteger[] descifrado = new BigInteger[cifrado.length];

        //descifrar
        for(i = 0; i<descifrado.length; i++){
            //aplico el descifrado
            descifrado[i] = cifrado[i].modPow(d, n);
        }

        //lo tengo que colocar en un arreglo de caracteres para despues pasarlo a una cadena
        char[] charArray = new char[descifrado.length];

        for(i = 0; i <charArray.length; i++){
            charArray[i] =(char)(descifrado[i].intValue());
        }
        VentanaCliente.campoMsjDescifrado.setText(Arrays.toString(charArray));
        return (new String(charArray));
    }

    //enviar los datos

    public BigInteger damep(){
        return p;
    }

    public BigInteger dameq(){
        return q;
    }

    public BigInteger damen(){
        return n;
    }

    public BigInteger damephi(){
        return phi;
    }

    public BigInteger damee(){
        return e;
    }

    public BigInteger damed(){
        return d;
    }
}
