package ru.otus.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.model.Request;

@SuppressWarnings({"java:S1989"})
public class ClientsApiServlet extends HttpServlet {
    private final transient DBServiceClient dbServiceClient;
    private final transient Gson gson;

    public ClientsApiServlet(DBServiceClient dbServiceClient, Gson gson) {
        this.dbServiceClient = dbServiceClient;
        this.gson = gson;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var client = extractClient(request);
        var newClient = dbServiceClient.saveClient(client);

        response.setContentType("application/json;charset=UTF-8");

        var out = response.getOutputStream();
        out.print(gson.toJson(newClient.getId()));
    }

    private static Client extractClient(HttpServletRequest request) throws IOException {
        var mapper = new ObjectMapper();
        var clientRequest = mapper.readValue(request.getInputStream(), Request.class);

        var name = clientRequest.name();
        var street = clientRequest.address();
        var number = clientRequest.phone();
        var address = new Address(null, street);
        var phone = new Phone(null, number);

        return new Client(null, name, address, List.of(phone));
    }
}
