package com.teufelsturm.tt_downloaderand_kotlin.TT_DB_Helper;

// based on:
// http://stackoverflow.com/questions/10417442/client-server-file-transfer-from-android-to-pc-connected-via-socket
//  http://stackoverflow.com/questions/4687615/how-to-achieve-transfer-file-between-client-and-server-using-java-socket

import android.annotation.SuppressLint;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

@SuppressLint("DefaultLocale")
public class SendFileActivity {
	public static String sendFile() throws Exception {
		String strReturn = "";
		while (true) {
			String clientSentence;
			String capitalizedSentence = "";

			ServerSocket welcomeSocket = new ServerSocket(3248);
			Socket connectionSocket = welcomeSocket.accept();

			// Socket sock = welcomeSocket.accept();

			BufferedReader inFromClient = new BufferedReader(
					new InputStreamReader(connectionSocket.getInputStream()));

			DataOutputStream outToClient = new DataOutputStream(
					connectionSocket.getOutputStream());

			clientSentence = inFromClient.readLine();
			// System.out.println(clientSentence);
			if (clientSentence == "download") {
				File myFile = new File("C:\\Users\\cguo\\11.lsp");
				byte[] mybytearray = new byte[(int) myFile.length()];
				FileInputStream fis = new FileInputStream(myFile);
				BufferedInputStream bis = new BufferedInputStream(fis);
				bis.read(mybytearray, 0, mybytearray.length);
				OutputStream os = connectionSocket.getOutputStream();
				System.out.println("Sending...");
				os.write(mybytearray, 0, mybytearray.length);
				os.flush();
				bis.close();
				connectionSocket.close();
			}
			if (clientSentence.equals("set")) {
				outToClient.writeBytes("connection is ");
				System.out.println("running here");
				// welcomeSocket.close();
				// outToClient.writeBytes(capitalizedSentence);
			}

			capitalizedSentence = clientSentence.toUpperCase() + "\n";

			// if(!clientSentence.equals("quit"))
			outToClient.writeBytes(capitalizedSentence
					+ "enter the message or command: ");

			System.out.println("passed");
			// outToClient.writeBytes("enter the message or command: ");
			welcomeSocket.close();
			System.out.println("connection terminated");
			return strReturn;
		}		
	}
}