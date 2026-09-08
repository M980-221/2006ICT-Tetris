package com.pg2.tetris;

import com.google.gson.Gson;
import javafx.application.Platform;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/** Exact client for the supplied TetrisServer.jar JSON request/response protocol. */
public final class ExternalPlayerClient implements AutoCloseable {
    public record GameState(int width,int height,int[][] cells,int[][] currentShape,int[][] nextShape){}
    public record Move(int opX,int opRotate){}
    private final Gson gson=new Gson(); private final Consumer<Move> moveConsumer; private final Consumer<Boolean> connectionConsumer;
    private final BlockingQueue<GameState> requests=new LinkedBlockingQueue<>(1); private final AtomicBoolean running=new AtomicBoolean(); private Thread worker; private volatile Socket socket;
    public ExternalPlayerClient(Consumer<Move> moves,Consumer<Boolean> connection){moveConsumer=moves;connectionConsumer=connection;}
    public void start(){if(!running.compareAndSet(false,true))return;worker=new Thread(this::loop,"external-player");worker.setDaemon(true);worker.start();}
    public void requestMove(GameState state){requests.clear();requests.offer(state);}
    private void loop(){GameState pending=null;while(running.get()){
        try{if(pending==null)pending=requests.poll(250,TimeUnit.MILLISECONDS);if(pending==null)continue;
            try(Socket s=new Socket()){socket=s;s.connect(new InetSocketAddress(InetAddress.getLoopbackAddress(),3000),1000);s.setSoTimeout(2500);
                try(PrintWriter out=new PrintWriter(new OutputStreamWriter(s.getOutputStream(),StandardCharsets.UTF_8),true);BufferedReader in=new BufferedReader(new InputStreamReader(s.getInputStream(),StandardCharsets.UTF_8))){out.println(gson.toJson(pending));String response=in.readLine();if(response==null)throw new EOFException("Server returned no move");Move move=gson.fromJson(response,Move.class);if(move==null)throw new IOException("Invalid server response");status(true);Platform.runLater(()->moveConsumer.accept(move));pending=null;}}
        }catch(InterruptedException e){Thread.currentThread().interrupt();break;}catch(IOException|RuntimeException e){status(false);try{Thread.sleep(1000);}catch(InterruptedException interrupted){Thread.currentThread().interrupt();break;}}finally{socket=null;}}
    }
    private void status(boolean value){Platform.runLater(()->connectionConsumer.accept(value));}
    @Override public void close(){running.set(false);requests.clear();try{if(socket!=null)socket.close();}catch(IOException ignored){}if(worker!=null)worker.interrupt();}
}
