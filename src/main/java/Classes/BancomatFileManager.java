package Classes;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BancomatFileManager extends FileManager {

    public BancomatFileManager(String filePath) {
        super(filePath);
    }

    public Card getCardFromFile()
    {
        String textFile = super.getTextFromFile();
        if (textFile == null) {
            return null;
        } else {
            textFile = textFile.replaceAll("\\s+", " ");
            String[] paramsList = null;
            paramsList = textFile.split(" ");
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                return new Card(paramsList[0], paramsList[1], Integer.parseInt(paramsList[2]), format.parse(paramsList[3]));
            } catch (ParseException ex) {
                return new Card(paramsList[0], paramsList[1], Integer.parseInt(paramsList[2]), null);
            }
        }
    }

    public Integer getAmountFromFile() {
        String amount = super.getTextFromFile();
        return Integer.parseInt(amount);
    }

    public void saveInFile(Card card) {
        String cardString = card.toString();
        super.saveInFile(cardString);
    }


    public void saveInFile(Integer bancomatAmount) {
        String amount = bancomatAmount.toString();
        super.saveInFile(amount);
    }


}
