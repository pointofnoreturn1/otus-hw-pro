package ru.otus.controller;

import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.service.ClientService;

@Controller
@RequestMapping("/clients")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAllAttributes(Map.of(
                "clients", clientService.findAll(),
                "client", new Client(),
                "address", new Address(),
                "phone", new Phone()));

        return "clients";
    }

    @PostMapping
    public String save(
            @ModelAttribute("client") Client client,
            @ModelAttribute("address") Address address,
            @ModelAttribute("phone") Phone phone) {
        client.setAddress(address);
        client.setPhones(Set.of(phone));
        clientService.saveClient(client);

        return "redirect:/clients";
    }
}
