package ru.otus.dataprocessor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import ru.otus.model.Measurement;

public class ResourcesFileLoader implements Loader {
    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        try {
            return new ObjectMapper()
                    .readValue(new File(ClassLoader.getSystemResource(fileName).toURI()), new TypeReference<>() {});
        } catch (IOException | URISyntaxException e) {
            throw new FileProcessException(e);
        }
    }
}
