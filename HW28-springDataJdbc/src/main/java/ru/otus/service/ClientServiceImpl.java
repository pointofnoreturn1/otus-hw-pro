package ru.otus.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import ru.otus.model.Client;
import ru.otus.repository.ClientRepository;
import ru.otus.sessionmanager.TransactionManager;

@Service
public class ClientServiceImpl implements ClientService {
    private final TransactionManager transactionManager;
    private final ClientRepository repository;

    public ClientServiceImpl(TransactionManager transactionManager, ClientRepository repository) {
        this.transactionManager = transactionManager;
        this.repository = repository;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(() -> repository.save(client));
    }

    @Override
    public Optional<Client> getClient(long id) {
        return repository.findById(id);
    }

    @Override
    public List<Client> findAll() {
        return repository.findAll();
    }
}
