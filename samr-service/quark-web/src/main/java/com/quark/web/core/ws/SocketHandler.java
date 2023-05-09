package com.quark.web.core.ws;

import com.google.gson.Gson;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SocketHandler extends TextWebSocketHandler {

    List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    List<StockQuote> tickers = new ArrayList<>();
    Random rnd = new Random();
    Gson gson = new Gson();

    public SocketHandler() {
        Runnable r = () -> streamPrices();
        tickers.add(new StockQuote("AAPL", 1000));
        tickers.add(new StockQuote("MSFT", 500));
        tickers.add(new StockQuote("AMZN", 2000));
        tickers.add(new StockQuote("GOOG", 2500));
        tickers.add(new StockQuote("FACB", 200));
        tickers.add(new StockQuote("LINK", 100));

        new Thread(r).start();
    }

    private void streamPrices() {

        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (sessions.isEmpty()) {
                //log.info("No Sessions : {}", hashCode());
            } else {
                //log.info("Sending prices : {}", hashCode());
                tickers.forEach(t -> t.setPrice(t.getInitialPrice() + rnd.nextDouble() * t.getPrice() * 0.05));
                for (WebSocketSession s : sessions) {
                    try {
                        if (s.isOpen()) {
                            s.sendMessage(new TextMessage(gson.toJson(tickers)));
                        }
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws InterruptedException, IOException {

        for (WebSocketSession webSocketSession : sessions) {
            Map value = new Gson().fromJson(message.getPayload(), Map.class);
            webSocketSession.sendMessage(new TextMessage("Hello " + value.get("name") + " !"));
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Added web socket session : {}. Total websocket connections : {}", session.toString(), sessions.size());
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("Connection closed by {}:{}", session.getRemoteAddress().getHostString(), session.getRemoteAddress().getPort());
        super.afterConnectionClosed(session, status);
        sessions.remove(session);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable throwable) throws Exception {
        log.error("error occured at sender " + session, throwable);
    }
}