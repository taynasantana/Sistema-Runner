import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.*;

/**
 * Implementação simplificada do assinador.jar conforme diretrizes do plano.
 * Java 21 – arquivo único para facilitar empacotamento.
 *
 * Funcionalidades:
 * - CLI: sign e validate
 * - Modo servidor HTTP: /sign e /validate
 * - Simulação de assinatura digital
 */
public class AssinadorApp {

    interface SignatureService {
        String sign(String document, String signer);
        boolean validate(String document, String signature);
    }

    static class FakeSignatureService implements SignatureService {

        @Override
        public String sign(String document, String signer) {
            try {
                String payload = document + ":" + signer + ":" + Instant.now();
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(payload.getBytes(StandardCharsets.UTF_8));
                return Base64.getEncoder().encodeToString(hash);
            } catch (Exception e) {
                throw new RuntimeException("Erro ao gerar assinatura", e);
            }
        }

        @Override
        public boolean validate(String document, String signature) {
            return signature != null && !signature.isBlank() && document != null;
        }
    }

    static SignatureService service = new FakeSignatureService();

    public static void main(String[] args) throws Exception {

        if (args.length == 0) {
            help();
            return;
        }

        switch (args[0]) {

            case "sign" -> {
                Map<String,String> params = parseArgs(args);
                validateRequired(params, "document", "signer");

                String signature = service.sign(
                        params.get("document"),
                        params.get("signer")
                );

                System.out.println("Assinatura criada com sucesso");
                System.out.println("signature=" + signature);
            }

            case "validate" -> {
                Map<String,String> params = parseArgs(args);
                validateRequired(params, "document", "signature");

                boolean result = service.validate(
                        params.get("document"),
                        params.get("signature")
                );

                System.out.println("valid=" + result);
            }

            case "server" -> {
                int port = 8080;
                if (args.length > 1) port = Integer.parseInt(args[1]);
                startServer(port);
            }

            default -> help();
        }
    }

    static void startServer(int port) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/sign", (HttpExchange exchange) -> {
            if (!exchange.getRequestMethod().equals("POST")) {
                send(exchange, 405, "Method Not Allowed");
                return;
            }

            Map<String,String> params = parseBody(exchange);
            try {
                validateRequired(params,"document","signer");

                String sig = service.sign(params.get("document"), params.get("signer"));

                send(exchange,200,"{\"signature\":\""+sig+"\"}");
            } catch (Exception e) {
                send(exchange,400,e.getMessage());
            }
        });

        server.createContext("/validate", (HttpExchange exchange) -> {
            if (!exchange.getRequestMethod().equals("POST")) {
                send(exchange,405,"Method Not Allowed");
                return;
            }

            Map<String,String> params = parseBody(exchange);

            try {
                validateRequired(params,"document","signature");

                boolean valid = service.validate(
                        params.get("document"),
                        params.get("signature")
                );

                send(exchange,200,"{\"valid\":"+valid+"}");

            } catch (Exception e) {
                send(exchange,400,e.getMessage());
            }
        });

        server.start();
        System.out.println("Assinador iniciado na porta " + port);
    }

    static Map<String,String> parseArgs(String[] args) {
        Map<String,String> map = new HashMap<>();

        for (String arg : args) {
            if (arg.contains("=")) {
                String[] p = arg.split("=",2);
                map.put(p[0],p[1]);
            }
        }

        return map;
    }

    static Map<String,String> parseBody(HttpExchange ex) throws IOException {

        String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

        Map<String,String> map = new HashMap<>();

        for (String part : body.split("&")) {
            String[] kv = part.split("=");
            if (kv.length==2) map.put(kv[0],kv[1]);
        }

        return map;
    }

    static void validateRequired(Map<String,String> map, String... keys) {
        for (String k : keys) {
            if (!map.containsKey(k) || map.get(k).isBlank()) {
                throw new IllegalArgumentException("Parametro obrigatorio ausente: " + k);
            }
        }
    }

    static void send(HttpExchange ex, int status, String response) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        ex.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(bytes);
        }
    }

    static void help() {
        System.out.println("""
Uso:

java -jar assinador.jar sign document=arquivo signer=nome

java -jar assinador.jar validate document=arquivo signature=abc

java -jar assinador.jar server 8080
""");
    }
}
