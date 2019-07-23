package Classes;

public class MainClass {
    public static void main(String[] args) {
        Bancomat bancomat = new Bancomat(3, 16, 1000000);
        bancomat.runBancomat("Cards.txt", "BancomatInfo.txt");
    }
}
