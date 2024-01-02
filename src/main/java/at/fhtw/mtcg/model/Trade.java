package at.fhtw.mtcg.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Trade {
    @JsonAlias({"Id"})
    private String tradeId;
    @JsonAlias({"CardToTrade"})
    private String cardId;
    @JsonAlias({"Type"})
    private String type;
    @JsonAlias({"MinimumDamage"})
    private double minDamage;
    private String username;

    public Trade(){}
    public Trade(String tradeId,String cardId,String type,double minDamage){
        this.tradeId = tradeId;
        this.cardId = cardId;
        this.type = type;
        this.minDamage = minDamage;
    }

}
