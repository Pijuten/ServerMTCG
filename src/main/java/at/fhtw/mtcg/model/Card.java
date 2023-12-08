package at.fhtw.mtcg.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter @AllArgsConstructor
public class Card {
    @JsonAlias({"Id"})
    private String id;
    @JsonAlias({"Name"})
    private String name;
    @JsonAlias({"Damage"})
    private double damage;
    private boolean deck;
    private String username;
    private int packageid;

    public Card(){}
    public Card(String id, String name, float damage) {

        this.id = id;
        this.name = name;
        this.damage = damage;
    }

     public void setDeck(boolean deck) {
         this.deck = deck;
     }
     public boolean getDeck() {
       return this.deck;
    }
 }