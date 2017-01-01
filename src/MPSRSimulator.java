import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class MPSRSimulator {

	public static void main(String[] args) throws InterruptedException {

		Thread backend = new Thread(new Runnable() {

			@Override
			public void run() {
				ServerSocket listener = null;
		        try {
		        	listener = new ServerSocket(38010);
		            while (true) {
		                Socket socket = listener.accept();
		                try {
		                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		                    out.println(new Date().toString());
		                } finally {
		                    socket.close();
		                }
		            }
		        }
		        catch (Exception e) {
		        	e.printStackTrace();
				}
		        finally {
		            try {
						listener.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
		        }
		    }
			
		});

		Thread tcc = new Thread(new Runnable() {

			@Override
			public void run() {

			}
		});
		
		Thread tccStatus = new Thread(new Runnable() {

			@Override
			public void run() {

			}
		});
		
		backend.start();
		tcc.start();
		tccStatus.start();
		System.err.println("Main thread suspended....");
		Thread.currentThread().wait(); 

	}
}
