package Classes;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {

    private String filePath;
    private FileReader fileReader;
    private FileWriter fileWriter;

    public FileManager(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() { return filePath; }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getTextFromFile() {
        if (filePath == "") {
            System.out.println("Ошибка чтения: не указан путь к файлу!");
            return null;
        }
        try {
            fileReader = new FileReader(filePath);
            String line = "";
            int i;
            while ((i=fileReader.read()) != -1)
            {
                line = line + (char) i;
            }
            closeFile();
            return line;
        }
        catch (Exception ex) {
            System.out.println("Ошибка при чтении файла по пути "+this.filePath);
            System.out.println(ex.getMessage().toString());
            return null;
        }

    }

    public void saveInFile(String savedText) {
        if (filePath == "") {
            System.out.println("Ошибка записи: не указан путь к файлу!");
            return;
        }
        try {
            fileWriter = new FileWriter(filePath);
            fileWriter.write(savedText);
            this.closeFile();
        }
        catch (IOException ex) {
            System.out.println("Ошибка при записи в файл по пути "+this.filePath);
            return;
        }

    }


    private void closeFile() {
        if (fileReader != null || fileWriter != null) {
            try {
                if (fileReader != null)
                    fileReader.close();
                if (fileWriter != null)
                    fileWriter.close();
            }
            catch (IOException ex) {
                System.out.println("Ошибка при закрытии файла по пути "+this.filePath);
                return;
            }
        }
    }

}
