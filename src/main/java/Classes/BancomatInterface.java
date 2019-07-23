package Classes;

import java.util.ArrayList;

public interface BancomatInterface {

     Card card = null;
     BancomatFileManager fileManager = null;

     Integer bancomatAmount = 0;

     Integer tryesCount = 0;

     Integer cardCorrectLen = 0;

     Integer topUpLimit = 0;

     void runBancomat(String fileCards, String fileBancomat);

     void saveInfo(String fileCards, String fileBancomat);

}
