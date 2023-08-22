package com.mds.java.sample.code.stream;

import com.mds.java.sample.code.domain.Actor;
import com.mds.java.sample.code.domain.Movie;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.PropertySource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.maxBy;

@Slf4j
public class StreamApiExamples {

    public static void main(String[] args) {

        List<Movie> movies = Stream.of(
                createMovieInfo("The Godfather", 1972, "English", "USA", Set.of("Marlon Brando", "Al Pacino", "James Caan")),
                createMovieInfo("The Dark Night", 2008, "Spanish", "Spain", Set.of("Christian Bale", "Heath Ledger", "Aaron Eckhart")),
                createMovieInfo("The Godfather Part II", 1974, "Italian", "Italy", Set.of("Al Pacino", "Robert De Niro", "Robert Duvall"))
        ).toList();

        List<Actor> actors = Stream.of(
                        getActorDetails("Al Pacino", getDateOfBirth("1976-08-18"), "Italy", 300000),
                        getActorDetails("Christian Bale", getDateOfBirth("1976-08-18"), "UK", 1700000),
                        getActorDetails("Marlon Brando", getDateOfBirth("1923-01-28"), "USA", 100000),
                        getActorDetails("Robert De Niro", getDateOfBirth("1944-12-31"), "Italy", 250000),
                        getActorDetails("Heath Ledger", getDateOfBirth("1972-05-21"), "USA", 500000),
                        getActorDetails("Aaron Eckhart", getDateOfBirth("1984-05-16"), "USA", 150000))
                .toList();

        //map example
        log.debug("1. map() example");
        List<Set<String>> cast = movies.stream()
                .map(Movie::getCast)
                .toList();
        log.info("Actor names per movie: {}", cast);

        //flatmap() example
        List<String> castList = movies.stream().flatMap(movie -> movie.getCast().stream()).toList();
        log.info("All actor names: {}", castList);

        //flatmap() with toSet example
        Set<String> individualCasts = movies.stream().flatMap(movie -> movie.getCast().stream()).collect(Collectors.toSet());
        log.info("Distinct actor names: {}", individualCasts);

        //Counting in how many films each actor featured
        Map<String, Long> movieCount = movies.stream()
                .flatMap(movie -> movie.getCast().stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        log.info("Number of movies by each actor: {}", movieCount);

        //Finding who has acted in most number of movies
        String actorWithMostFilms = Collections.max(movieCount.entrySet(), Map.Entry.comparingByValue()).getKey();
        log.info("{} has acted in most number of movies", actorWithMostFilms);

        //Finding who has acted in most number of movies with their movie count: Method 1
        Map.Entry<String, Long> max = Collections.max(movieCount.entrySet(), Comparator.comparingInt(movie -> movie.getValue().intValue()));
        log.info("Option 1: {} has most number of movies to their name", max.toString());

        //Finding who has acted in most number of movies with their movie count: Method 2
        Map.Entry<String, Long> maxNumberOfFilms = Collections.max(movieCount.entrySet(), Map.Entry.comparingByValue());
        log.info("Option 2: {} has most number of movies to their name", maxNumberOfFilms.toString());

        //Find out highest paid actor from each country
        Map<String, Optional<Actor>> highestEarningActors = actors.stream()
                .collect(Collectors.groupingBy(Actor::getCountryOfBirth,
                        Collectors.reducing(BinaryOperator.maxBy(Comparator.comparing(Actor::getRemuneration)))));
        log.info("Option 1: Highest earning actors by country: {}", highestEarningActors);

        Map<String, Actor> highestEarningActorMap = actors.stream()
                .collect(Collectors.groupingBy(Actor::getCountryOfBirth,
                        Collectors.collectingAndThen(maxBy(Comparator.comparing(Actor::getRemuneration)), Optional::get)));
        log.info("Option 2: Highest earning actors by country: {}", highestEarningActorMap);

    }


    private static LocalDate getDateOfBirth(String dateOfBirth) {
        return LocalDate.parse(dateOfBirth, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    private static Actor getActorDetails(String name, LocalDate dateOfBirth, String countryOfBirth, long remuneration) {
        return Actor.builder()
                .name(name)
                .dateOfBirth(dateOfBirth)
                .countryOfBirth(countryOfBirth)
                .remuneration(remuneration)
                .build();
    }

    private static Movie createMovieInfo(String name, int year, String language, String country, Set<String> cast) {
        return Movie.builder()
                .name(name)
                .year(year)
                .language(language)
                .country(country)
                .cast(cast)
                .build();
    }


}
