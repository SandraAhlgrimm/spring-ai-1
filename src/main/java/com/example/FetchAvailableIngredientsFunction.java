package com.example;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

@Description("Fetches ingredients that are available at home")
@Service("fetchAvailableIngredients")
public class FetchAvailableIngredientsFunction implements Function<FetchAvailableIngredientsFunction.Request, FetchAvailableIngredientsFunction.Response> {

    private static final Logger log = LoggerFactory.getLogger(FetchAvailableIngredientsFunction.class);

    private final List<String> alwaysAvailableIngredients;
    private final List<String> availableIngredientsInFridge;

    public FetchAvailableIngredientsFunction(@Value("${app.always-available-ingredients}") List<String> alwaysAvailableIngredients,
                                             @Value("${app.available-ingredients-in-fridge}") List<String> availableIngredientsInFridge) {
        this.alwaysAvailableIngredients = alwaysAvailableIngredients;
        this.availableIngredientsInFridge = availableIngredientsInFridge;
    }

    @Override
    public Response apply(Request request) {
        log.info("Fetching available ingredients function called by LLM");
        var availableIngredients = Stream.concat(alwaysAvailableIngredients.stream(), availableIngredientsInFridge.stream()).toList();
        return new Response(availableIngredients);
    }

    public record Request(
            List<String> recipeIngredients) {}

    public record Response(List<String> availableIngredients) {}
}
