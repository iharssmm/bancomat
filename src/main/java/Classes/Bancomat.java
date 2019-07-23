package Classes;
import java.io.*;
import java.sql.Array;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;

import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Bancomat implements BancomatInterface{

    Card card = null;

    Integer bancomatAmount = 0;

    BancomatFileManager fileManager = null;

    public Integer tryesCount = 0; // КОЛИЧЕСТВО ПОПЫТОК

    public Integer cardCorrectLen = 0;

    Integer topUpLimit = 0;

    public Bancomat(Integer tryesCount, Integer cardCorrectLen, Integer topUpLimit) {
        this.tryesCount = tryesCount;
        this.cardCorrectLen = cardCorrectLen;
        this.topUpLimit = topUpLimit;
    }

    private void showMessage(String msg) {
        System.out.flush();
        System.out.println(msg);
        try {
            System.in.skip(System.in.read());
            //System.in.read();
        } catch (IOException ex) {
            System.out.println("Произошла ошибка ввода...");
            return;
        }

    }

    private String redirectToScreen(String screenMsg) {
        System.out.flush();
        System.out.println(screenMsg);
        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine();
        return userInput;
    }

    private Integer redirectToScreenWithNumber(String screenMsg) {
        boolean tryToEnter = true;
        Integer value = 0;
            while (tryToEnter)
            {
                try {
                    value = Integer.parseInt(redirectToScreen(screenMsg));
                    tryToEnter = false;
                }
                catch (NumberFormatException ex) {
                    showMessage("Ошибка! Необходимо ввести число!");
                    continue;
                }
            }
        return value;
    }

    private boolean isCorrectParagraph(String p) {
        if (p.equals("1") || p.equals("2") || p.equals("3") || p.equals("4")) {
            return true;
        } else {
            return false;
        }
    }

    public void runBancomat(String fileCards, String fileBancomat) {
        if (fileCards == "" || fileCards == null || fileBancomat == "" || fileBancomat == null) {
            showMessage("Для начала работы необходимо указать пути к файлам информации!");
            return;
        } else {
            fileManager = new BancomatFileManager(fileCards);
            this.card = fileManager.getCardFromFile();
            if (card == null) {
                showMessage("Ошибка в чтении данных карты из файла!");
                return;
            }
            fileManager.setFilePath(fileBancomat);
            this.bancomatAmount = fileManager.getAmountFromFile();
            if (bancomatAmount == null) {
                showMessage("Ошибка в чтении данных банкомата из файла!");
                return;
            }
            if (card.isBlocked()) {
                float daysOfBlocking = card.daysOfBlocking();
                if (daysOfBlocking < 1f) {
                    showMessage("Извините, ваша карта заблокирована!");
                    return;
                } else {
                    card.unblockCard();
                    showMessage("Ваша карта была заблокирована, но срок блокировки прошел! Будьте внимательны и не доверяйте свою карту посторонним лицам!");
                    saveInfo(fileCards, fileBancomat);
                }
            }
            String numbCardUserInput = redirectToScreen("Введите номер карты");
            if (!(card.isCorrectNum(numbCardUserInput) && numbCardUserInput.length() == cardCorrectLen)) {
                showMessage("Ошибка, вы ввели некорректный номер карты!");
                while (!(card.isCorrectNum(numbCardUserInput) && numbCardUserInput.length() == cardCorrectLen)) {
                    numbCardUserInput = redirectToScreen("Введите номер карты, будьте внимательней!");
                    if (!(card.isCorrectNum(numbCardUserInput) && numbCardUserInput.length() == cardCorrectLen)) {
                        showMessage("Ошибка, вы ввели некорректный номер карты!");
                    }
                }
            }
            String pinCardUserInput = redirectToScreen("Введите PIN карты");
            if (!card.isCorrectPin(pinCardUserInput)) {
                while (!(card.isCorrectPin(pinCardUserInput)) && this.tryesCount > 0) {
                    showMessage("Некорректный PIN!");
                    this.tryesCount--;
                    Integer tryesLabel = tryesCount + 1;
                    pinCardUserInput = redirectToScreen("Введите PIN карты. Осталось попыток: "+tryesLabel.toString()+". Будьте внимательны!");
                }
                if (this.tryesCount == 0) {
                    showMessage("Простите, нам придется заблокировать вашу карту!");
                    card.blockCard();
                    saveInfo(fileCards, fileBancomat);
                    return;
                }
            }
            String numberOfAct = redirectToScreen("Выберите действие:\n1. Пополнить баланс\n2. Получить наличные\n3. Посмотреть состояние счета\n4. Выход");
            if (!isCorrectParagraph(numberOfAct)) {
                while (!isCorrectParagraph(numberOfAct)) {
                    showMessage("Вы ввели некорректный пункт!");
                    numberOfAct = redirectToScreen("Выберите действие:\n1. Пополнить баланс\n2. Получить наличные\n3. Посмотреть состояние счета\n4. Выход");
                }
            }
            switch (numberOfAct.charAt(0)) {
                case '1': {
                    Integer value = redirectToScreenWithNumber("Введите сумму пополнения...");
                    if (value > topUpLimit) {
                        while (value > topUpLimit) {
                            showMessage("Ошибка! Введенное число превышает лимит в "+topUpLimit.toString());
                            value = redirectToScreenWithNumber("Введите сумму пополнения...");
                        }
                    }
                    card.topUpTheBalance(value);
                    showMessage("Операция по пополнению баланса прошла успешно!");
                    saveInfo(fileCards, fileBancomat);
                    return;
                }
                case '2' : {
                    Integer value = redirectToScreenWithNumber("Введите сумму, которую хотите снять...");
                    if (value > bancomatAmount || card.errorWithdraw(value)) {
                        while (value > bancomatAmount || card.errorWithdraw(value)) {
                            showMessage("Ошибка! Введенное число превышает средства, которыми располагает банкомат, либо на вашей карте недостаточно средств!");
                            value = redirectToScreenWithNumber("Введите сумму, которую хотите снять...");
                        }
                    }
                    card.withdrawMoney(value);
                    this.bancomatAmount -= value;
                    showMessage("Операция по снятию прошла успешно!");
                    saveInfo(fileCards, fileBancomat);
                    return;
                }
                case '3' : {
                    showMessage("Ваш баланс составляет "+card.getCardAmount().toString());
                    return;
                }
                case '4' : {
                    showMessage("Хорошего дня!");
                    return;
                }
            }
        }
    }


    public void saveInfo(String fileCards, String fileBancomat) {
        if (fileManager == null) {
            fileManager = new BancomatFileManager(fileCards);
        }
        if (fileManager.getFilePath() != fileCards) {
            fileManager.setFilePath(fileCards);
        }
        fileManager.saveInFile(card);
        fileManager.setFilePath(fileBancomat);
        fileManager.saveInFile(bancomatAmount);
    }

}
