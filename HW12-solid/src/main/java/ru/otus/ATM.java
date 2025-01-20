package ru.otus;

import java.util.List;
import ru.otus.cassette.Cassette;

public record ATM(List<? extends Cassette> cassettes) {}
