import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class MPSRSimulator {

	public static void main(String[] args) throws InterruptedException {

		Thread backend = new Thread(new Runnable() {
			String backendResponseSuccess = "ok";
			String backendIdle = "Idle";
			String backendPrepared = "parsed correctly";
			@Override
			public void run() {
				ServerSocket listener = null;
				try {
					System.err.println("Backend listening...");
					listener = new ServerSocket(38010);
					String status = "Idle";
					while (true) {
						Socket socket = listener.accept();
						System.err.println("Backend connected..");
						try {
							BufferedReader in =new BufferedReader( new InputStreamReader(socket.getInputStream()));
							String input = in.readLine();
							System.err.println("Got message:" + input);
							PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
							
							if(input.contains("query")) out.println("<response><mpsr_status>"+status+"</mpsr_status></response>");
							if(input.contains("prepare")) {
								status = "prepared";
								out.println("<reply>"+backendPrepared+"</reply>");
							}
							if(input.contains("start")) {
								status = "recording";
								out.println("<response>"+backendResponseSuccess+"</response>");
							}
							if(input.contains("stop")) {
								status = "Idle";
								out.println("<response>"+backendResponseSuccess+"</response>");
							}
							out.flush();
							System.err.println("response sent..");
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
				ServerSocket listener = null;
		        try {
					System.err.println("tcc listening...");
		        	 listener = new ServerSocket(38012);
		            while (true) {
		                Socket socket = listener.accept();
		                System.err.println("tcc connected...");
		                try {
		                    PrintWriter out =
		                        new PrintWriter(socket.getOutputStream(), true);
		                    out.println("ok");
		                } finally {
		                    socket.close();
		                }
		            }
		        }
		        catch(Exception e){
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

		Thread tccStatus = new Thread(new Runnable() {

			 final String s = "<?xml version='1.0' encoding='ISO-8859-1'?> "
		        		+" <tcc_status> "
		        		+"   <overview> "
		        		+"     <error_string>blah</error_string> "
		        		+"   </overview> "
		        		+"   <coordinates> "
		        		+"     <RA>12:45:31.35</RA> "
		        		+"     <Dec>-88:56:33.4</Dec> "
		        		+"     <HA>6:19:10.2</HA> "
		        		+"     <Glat>-0.453464155975</Glat> "
		        		+"     <Glon>5.28620911537</Glon> "
		        		+"     <Alt>0.636015534401</Alt> "
		        		+"     <Az>3.14418578148</Az> "
		        		+"     <NS>-0.935093133362</NS> "
		        		+"     <EW>0.0</EW> "
		        		+"     <LMST>13:11:29.55</LMST> "
		        		+"   </coordinates> "
		        		+"   <ns> "
		        		+"     <error>None</error> "
		        		+"     <east> "
		        		+"       <tilt>-0.935093133362</tilt> "
		        		+"       <count>10345</count> "
		        		+"       <driving>False</driving> "
		        		+"       <state>disabled</state> "
		        		+"       <on_target>True</on_target> "
		        		+"       <system_status>112</system_status> "
		        		+"     </east> "
		        		+"     <west> "
		        		+"       <tilt>-0.935093133362</tilt> "
		        		+"       <count>10562</count> "
		        		+"       <driving>False</driving> "
		        		+"       <state>slow</state> "
		        		+"       <on_target>True</on_target> "
		        		+"       <system_status>112</system_status> "
		        		+"     </west> "
		        		+"   </ns> "
		        		+"   <md> "
		        		+"     <error>None</error> "
		        		+"     <east> "
		        		+"       <tilt>0.0</tilt> "
		        		+"       <count>8388608</count> "
		        		+"       <driving>False</driving> "
		        		+"       <state>auto</state> "
		        		+"       <on_target>True</on_target> "
		        		+"       <system_status>0</system_status> "
		        		+"     </east> "
		        		+"     <west> "
		        		+"       <tilt>0.0</tilt> "
		        		+"       <count>8388608</count> "
		        		+"       <driving>False</driving> "
		        		+"       <state>auto</state> "
		        		+"       <on_target>True</on_target> "
		        		+"       <system_status>0</system_status> "
		        		+"     </west> "
		        		+"   </md> "
		        		+" </tcc_status> ";
			
			@Override 
			public void run() {
				ServerSocket listener = null;
		        try {
					System.err.println("tcc status listening...");
		        	 listener = new ServerSocket(38013);
		            while (true) {
		                Socket socket = listener.accept();
		                System.err.println("tcc status connected...");
		                try {
		                    PrintWriter out =
		                        new PrintWriter(socket.getOutputStream(), true);
		                    out.println(s);
		                } finally {
		                    socket.close();
		                }
		            }
		        }
		        catch(Exception e){
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

		backend.start();
		tcc.start();
		tccStatus.start();
		System.err.println("Main thread suspended....");
		backend.join();
		tcc.join();
		tccStatus.join();

	}
}
