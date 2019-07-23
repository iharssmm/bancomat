
package Classes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.*;

public class Card {

    private String codeOfCard;
    private String pinOfCard;
    private Integer cardAmount;
    private Date dateOfBlocking;

    public Card(String codeOfCard, String pinOfCard, Integer cardAmount, Date dateOfBlocking) {
        this.codeOfCard = codeOfCard;
        this.pinOfCard = pinOfCard;
        this.cardAmount = cardAmount;
        this.dateOfBlocking = dateOfBlocking;
    }

    public Integer getCardAmount() {
        return this.cardAmount;
    }

    public String getCodeOfCard() {
        return this.codeOfCard;
    }

    public String getPinOfCard() {
        return this.pinOfCard;
    }

    public boolean isCorrectNum(String verifiableNum) { return verifiableNum.equals(this.codeOfCard); }

    public boolean isCorrectPin(String verifiableCode) {
        return verifiableCode.equals(this.pinOfCard);
    }

    public void blockCard() {
        this.dateOfBlocking = new Date();
    }

    public boolean isBlocked() { return this.dateOfBlocking != null; }

    public float daysOfBlocking() {
            long difference = (new Date()).getTime() - dateOfBlocking.getTime();
            float daysBetween = (difference / (1000*60*60*24));
            return daysBetween;
    }

    public void unblockCard() {
        this.dateOfBlocking = null;
    }

    public boolean errorWithdraw(Integer sum)
    {
        return this.cardAmount < sum;
    }

    public void topUpTheBalance(Integer sum) {
        if (this.dateOfBlocking == null)
        {
            cardAmount += sum;
        }
    }

    public void withdrawMoney(Integer sum) {
        if (this.dateOfBlocking == null)
        {
            this.cardAmount -= sum;
        }
    }

    public String toString() {
        String strDateOfBlocking = "null";
        if (dateOfBlocking != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            strDateOfBlocking = format.format(this.dateOfBlocking);
        }
        return this.codeOfCard + " " + this.pinOfCard + " " + this.cardAmount.toString() + " " + strDateOfBlocking;
    }

}
